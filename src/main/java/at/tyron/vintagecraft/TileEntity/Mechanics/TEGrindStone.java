package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TEGrindStone extends TEMechanicalNetworkPowerNodeBase implements IUpdatePlayerListBox, IPitchAndVolumProvider {

	private boolean initialized;
	EnumRockType rocktype;
	public ResourceLocation rockTexture;
	
	public boolean refreshModel;

	
	public TEGrindStone() {
		setTreeType(EnumTree.OAK);
		setRockType(EnumRockType.ANDESITE);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		setRockType(EnumRockType.byId(compound.getInteger("rocktype")));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("rocktype", rocktype.getId());
	}
	
	
	@Override
	public boolean hasConnectorAt(EnumFacing facing) {
		return facing == EnumFacing.UP;
	}
	
    @Override
    public void update() {
		if (worldObj.isRemote && !initialized && isTurning()) {
			VintageCraft.proxy.playLoopingSound("vintagecraft:stone_grind", this);
			initialized = true;
		}
		

    	if (worldObj.isRemote) return;
    }
    
    
    

    
	private boolean isTurning() {
		return networkId != 0 && getNetwork(orientation) != null && getNetwork(orientation).getSpeed() > 0;
	}

	// Client side Network speed updates only happen every 2-3 seconds, so lets interpolate 
	// the pitch just like we to with the rotation angle
	float lastPitch = 0;
	float lastVolumne = 0;
	
	@Override
	public float getPitch() {
		if (getNetwork(orientation) == null) return 0;
		
		float pitch = Math.min(1f, Math.max(0f, (float) Math.sqrt(getNetwork(orientation).getSpeed() / 6)));
		float interpolatedPitch = (pitch - lastPitch) / 30f;
		
		return lastPitch += interpolatedPitch;
	}

	@Override
	public float getVolumne() {
		if (getNetwork(orientation) == null) return 0;
		
		if (getNetwork(orientation).getSpeed() < 0.01f) return 0;
		
		float volumne = (0.5f + getNetwork(orientation).getSpeed() / 100f) / 2;
		float interpolatedVolumne = (volumne - lastVolumne) / 30f;
		
		return lastVolumne += interpolatedVolumne;
	}

	@Override
	public boolean isDonePlaying(IPitchAndVolumProvider self) {
		return !(worldObj.getTileEntity(pos) instanceof TEGrindStone) || self != worldObj.getTileEntity(pos);
	}
	
	
	public void setRockType(EnumRockType rockType) {
		this.rocktype = rockType;
		
		rockTexture = new ResourceLocation(ModInfo.ModID, "textures/blocks/rock/" + rocktype.getName() + ".png");
	}
	
	public EnumRockType getRockType() {
		return rocktype;
	}

	@Override
	public float getTorque(MechanicalNetwork network) {
		return 0;
	}

	@Override
	public float getResistance(MechanicalNetwork network) {
		return 10f + 10f / Math.max(0.5f, network.getSpeed());
	}

	@Override
	public EnumFacing getOutputSideForNetworkPropagation() {
		return EnumFacing.UP;
	}
	
	@Override
	public void onDevicePlaced(World world, BlockPos pos, EnumFacing facing, EnumFacing ontoside) {
		orientation = EnumFacing.UP;
		super.onDevicePlaced(world, pos, facing, ontoside);
		updateWoodType();
		
	}
	
	public void onNeighborBlockChange() {
		updateWoodType();
	}

	private void updateWoodType() {
		TileEntity te = worldObj.getTileEntity(pos.up());
		if (te instanceof TEMechanicalNetworkDeviceBase) {
			setTreeType(((TEMechanicalNetworkDeviceBase)te).getTreeType());
		}
		
	}
	
	
	
	

}

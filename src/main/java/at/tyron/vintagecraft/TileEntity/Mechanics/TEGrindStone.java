package at.tyron.vintagecraft.TileEntity.Mechanics;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TEGrindStone extends TEMechanicalNetworkPowerNodeBase implements ITickable, IPitchAndVolumProvider {

	private boolean initialized;
	EnumRockType rocktype;

	
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
		return networkId != 0 && getNetwork(orientation).getSpeed() > 0;
	}

	@Override
	public float getPitch() {
		return Math.max(0.2f, getNetwork(orientation).getSpeed());
	}

	@Override
	public float getVolumne() {
		if (getNetwork(orientation).getSpeed() < 0.01f) return 0;
		
		return 0.5f + getNetwork(orientation).getSpeed() / 100f;
	}

	@Override
	public boolean isDonePlaying(IPitchAndVolumProvider self) {
		return !(worldObj.getTileEntity(pos) instanceof TEGrindStone) || self != worldObj.getTileEntity(pos);
	}
	
	
	public void setRockType(EnumRockType rockType) {
		this.rocktype = rockType;		
	}
	
	public EnumRockType EnumRockType() {
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
		return null;
	}

}

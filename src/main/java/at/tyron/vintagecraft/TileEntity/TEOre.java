package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockOreVC;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class TEOre extends TileEntity { /* extends NetworkTileEntity {*/
	private IExtendedBlockState state;
	
	EnumRockType rocktype = EnumRockType.ANDESITE;
	EnumMaterialDeposit oretype = EnumMaterialDeposit.LIGNITE;
	
	
	@Override
	public boolean canRenderBreaking() {
		return false;
	}
	
	

    public IExtendedBlockState getState() {
        if(state == null) {
        	state = (IExtendedBlockState)getBlockType().getDefaultState();
        	state = state.withProperty(BlockOreVC.properties[0], rocktype).withProperty(BlockOreVC.properties[1], oretype);
        }
        return state;
    }

    public void setState(IExtendedBlockState state) {
        this.state = state;
    }
	
	
	public EnumRockType getRockType() {
		return rocktype;
	}
	
	public EnumMaterialDeposit getOreType() {
		return oretype;
	}
	
	public TEOre setRockType(EnumRockType rocktype) {
		this.rocktype = rocktype;
		return this;
	}
	
	public TEOre setOreType(EnumMaterialDeposit oretype) {
		this.oretype = oretype;
		return this;
	}
	
	
	
	
	
	
	


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		setRockType(EnumRockType.byId(nbt.getInteger("rocktype")));
		setOreType(EnumMaterialDeposit.byId(nbt.getInteger("oretype")));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)  {
		super.writeToNBT(nbt);
		
		nbt.setInteger("rocktype", getRockType().id);
		nbt.setInteger("oretype", getOreType().id);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(pos, 1, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
		worldObj.markBlockForUpdate(pos);
	}

	
	
	
	
	

	
	
	
	/*@Override
	public void handleInitPacket(NBTTagCompound nbt) {
		setRockType(EnumRockType.byId(nbt.getInteger("rocktype")));
		setOreType(EnumMaterialDeposit.byId(nbt.getInteger("oretype")));
	}

	@Override
	public void handleDataPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}

	@Override
	public void createDataNBT(NBTTagCompound nbt) {
		System.out.println("create data nbt");
	}

	@Override
	public void createInitNBT(NBTTagCompound nbt) {
		nbt.setInteger("rocktype", getRockType().id);
		nbt.setInteger("oretype", getOreType().id);
	}
	*/
		

	
}
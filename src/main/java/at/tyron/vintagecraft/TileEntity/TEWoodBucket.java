package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.WorldProperties.EnumBucketContents;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TEWoodBucket extends NetworkTileEntity {
	public EnumBucketContents contents;
	public EnumTree treetype;
	public EnumFacing orientation;
	
	public boolean refreshModel = false;
	
	public TEWoodBucket() {
		contents = EnumBucketContents.EMPTY;
		treetype = EnumTree.ASH;
		orientation = EnumFacing.NORTH;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		contents = EnumBucketContents.values()[compound.getInteger("contents")];
		treetype = EnumTree.values()[compound.getInteger("treetype")];
		orientation = EnumFacing.values()[compound.getInteger("orientation")]; 
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("contents", contents.getId());
		compound.setInteger("treetype", treetype.getId());
		compound.setInteger("orientation", orientation.getIndex());
	}
	

}

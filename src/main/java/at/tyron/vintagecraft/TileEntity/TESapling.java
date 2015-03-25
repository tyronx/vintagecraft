package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.block.BlockFarmlandVC;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TESapling extends TileEntity {
	//private IExtendedBlockState state;

	public float size = 0.35f;
	private long growthEnd;
	public long lastBonemealTime = -1;
	public int fertilizersUsed = 0;
	
   /* public IExtendedBlockState getState() {
        if(state == null) {
        	state = (IExtendedBlockState)getBlockType().getDefaultState();
        	state = state.withProperty(BlockFarmlandVC.fertilityExact, size);
        }
        return state;
    }

    public void setState(IExtendedBlockState state) {
        this.state = state;
    }*/
	
	public TESapling() {
		size = 0.35f;
	}
	
	public boolean canApplyBonemeal(long worldtime) {
		return lastBonemealTime == -1 || worldtime - lastBonemealTime >= 24000;
	}
	
	public boolean applyBonemeal(long worldtime) {
		if (lastBonemealTime == -1 || worldtime - lastBonemealTime >= 24000) {
			growthEnd = Math.max(worldtime, growthEnd - 24000);
			lastBonemealTime = worldtime;
			return true;
		}
		return false;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		size = compound.getFloat("size");
		growthEnd = compound.getLong("growthEnd");
		fertilizersUsed = compound.getInteger("fertilizersUser");
		lastBonemealTime = compound.getLong("lastBonemealTime");
		super.readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setFloat("size", size);
		compound.setLong("growthEnd", growthEnd);
		compound.setInteger("fertilizersUser", fertilizersUsed);
		compound.setLong("lastBonemealTime", lastBonemealTime);
		super.writeToNBT(compound);
	}
    
    
    public float getSize() {
		return size;
	}
    
    public void setFertilized(int fertility) {
		this.size = fertility;
	}
    
    public boolean canUseFertilizer() {
    	return fertilizersUsed < 10;
    }
    
    public void incSize() {
   		this.size+=0.05f;
    	
    	fertilizersUsed++;
    }

    
    public void setGrowthEnd(long growth) {
    	this.growthEnd = growth;
    }
    
    public long getGrowthEnd(long worldtime, EnumTree tree) {
    	if (growthEnd == 0) {
    		updateGrowthEnd(worldtime, tree);
    	}
    	
    	return growthEnd;
    }
    
    public void updateGrowthEnd(long worldtime, EnumTree tree) {
    	if (growthEnd == 0) {
    		setGrowthEnd((long) (worldtime + 24000 * 8 * tree.growthspeed - 1));
    	}
    }
    
    
    @Override
    public Packet getDescriptionPacket() {
    	NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBT(tagCompound);
        return new S35PacketUpdateTileEntity(this.pos, 1, tagCompound);
    }
    
    
    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	readFromNBT(pkt.getNbtCompound());
    	super.onDataPacket(net, pkt);
    }
}

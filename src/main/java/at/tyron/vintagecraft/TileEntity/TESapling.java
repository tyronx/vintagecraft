package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Item.Terrafirma.ItemOreVC;
import at.tyron.vintagecraft.Item.Terrafirma.ItemPeatBrick;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TESapling extends TileEntity {
	public float size = 0.35f;
	private long growthEnd;
	public long lastBonemealTime = -1;
	public int peatsUsed = 0;
	public int sylvitesUsed = 0;
	


	
	public TESapling() {
		size = 0.35f;
	}
	
	
	public boolean tryPutItemStack(ItemStack itemstack) {
		// bone meal
		if (itemstack.getItem() == Items.dye && itemstack.getItemDamage() == 15) {
			boolean ok = canApplyBonemeal(worldObj.getWorldTime()) && applyBonemeal(worldObj.getWorldTime());
			if (ok) itemstack.stackSize--;
			return ok;
		}
		
		// peat or sylvite
		if (itemstack.getItem() instanceof ItemPeatBrick || (itemstack.getItem() instanceof ItemOreVC && ItemOreVC.getOreType(itemstack) == EnumOreType.SYLVITE_ROCKSALT)) {
			if (canUseFertilizer(itemstack)) {
				incSize(itemstack);
				itemstack.stackSize--;
				return true;
			}
			return false;
		}
		
		return false;
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
		peatsUsed = compound.getInteger("peatsUsed");
		sylvitesUsed = compound.getInteger("sylvitesUsed");
		lastBonemealTime = compound.getLong("lastBonemealTime");
		super.readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		compound.setFloat("size", size);
		compound.setLong("growthEnd", growthEnd);
		compound.setInteger("peatsUsed", peatsUsed);
		compound.setInteger("sylvitesUsed", sylvitesUsed);
		
		compound.setLong("lastBonemealTime", lastBonemealTime);
		super.writeToNBT(compound);
	}
    
    
    public float getSize() {
		return size;
	}
    
    public void setFertilized(int fertility) {
		this.size = fertility;
	}
    
    public boolean canUseFertilizer(ItemStack stack) {
    	if (stack.getItem() instanceof ItemPeatBrick) {
    		return peatsUsed < 10;
    	}
    	return sylvitesUsed < 5;
    }
    
    public void incSize(ItemStack stack) {
    	if (stack.getItem() instanceof ItemPeatBrick) {
    		this.size += 0.05f;
    		peatsUsed++;
    		return;
    	}
    	
   		this.size += 0.05f;
   		sylvitesUsed++;
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

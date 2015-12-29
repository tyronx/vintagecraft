package at.tyron.vintagecraft.TileEntity;

import java.util.Random;

import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class TEToolRack extends NetworkTileEntity implements IInventory {
	public ItemStack[] storage;
	public EnumTree woodtype;
	public EnumFacing facing;
	
	public TEToolRack() {
		storage = new ItemStack[4];
		woodtype = EnumTree.ACACIA;
		facing = EnumFacing.NORTH;
	}

	public void addContents(int index, ItemStack is) {
		if(storage[index] == null)
			storage[index] = is;
	}

	public void clearContents() {
		storage[0] = null;
		storage[1] = null;
		storage[2] = null;
		storage[3] = null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1, 1, 1));
		return bb;
	}


	public boolean canRenderBreaking() {
		return true;
	}
	
	
	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(storage[i] != null) {
			if(storage[i].stackSize <= j) {
				ItemStack itemstack = storage[i];
				storage[i] = null;
				return itemstack;
			}
			ItemStack itemstack1 = storage[i].splitStack(j);
			if(storage[i].stackSize == 0)
				storage[i] = null;
			return itemstack1;
		}
		else
		{
			return null;
		}
	}

	public void ejectContents() {
		float f3 = 0.05F;
		EntityItem entityitem;
		Random rand = new Random();
		float f = rand.nextFloat() * 0.8F + 0.1F;
		float f1 = rand.nextFloat() * 0.8F + 0.4F;
		float f2 = rand.nextFloat() * 0.8F + 0.1F;

		for (int i = 0; i < getSizeInventory(); i++) {
			if(storage[i]!= null) {
				entityitem = new EntityItem(worldObj, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, storage[i]);
				entityitem.motionX = (float)rand.nextGaussian() * f3;
				entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float)rand.nextGaussian() * f3;
				worldObj.spawnEntityInWorld(entityitem);
				storage[i] = null;
			}
		}
	}

	
	
	public void grabItem(int index, EnumFacing dir, EntityPlayer player) {
		if(storage[index] != null) {
			
			if (!player.inventory.addItemStackToInventory(storage[index])) {
				if (!getWorld().isRemote) {
					getWorld().spawnEntityInWorld(
						new EntityItem(getWorld(),
							getPos().getX() + 0.5f,
							getPos().getY() + 0.5f, 
							getPos().getZ() + 0.5f, 
							storage[index]
					));
				}
			}
			
			storage[index] = null;
		}
	}



	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)  {
		storage[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
			itemstack.stackSize = getInventoryStackLimit();
	}


	

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		facing = EnumFacing.getHorizontal(nbttagcompound.getInteger("facing"));
		
		woodtype = EnumTree.byId(nbttagcompound.getInteger("woodType"));
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		storage = new ItemStack[getSizeInventory()];
		for(int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < storage.length)
				storage[byte0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				storage[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);
		nbt.setInteger("woodType", woodtype == null ? 0 : woodtype.getId());
		
		nbt.setInteger("facing", facing.getHorizontalIndex());
	}
	
	
	
	
	
	


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}
	
	
	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int getSizeInventory() {
		return storage.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return storage[i];
	}

	@Override
	public ItemStack removeStackFromSlot(int var1) {
		return null;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void handleInitPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);

	}

	@Override
	public void handleDataPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}

	@Override
	public void createDataNBT(NBTTagCompound nbt) {
		writeToNBT(nbt);

	}

	@Override
	public void createInitNBT(NBTTagCompound nbt) {
		writeToNBT(nbt);
	}

	@Override
	public String getName() {
		return "toolrack";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Tool Rack");
	}

	@Override
	public void openInventory(EntityPlayer player) {
	
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	}


}
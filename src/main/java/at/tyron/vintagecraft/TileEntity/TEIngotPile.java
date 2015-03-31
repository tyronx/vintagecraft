package at.tyron.vintagecraft.TileEntity;


import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.block.BlockIngotPile;
import at.tyron.vintagecraft.item.ItemIngot;


public class TEIngotPile extends NetworkTileEntity implements IInventory {
	private ItemStack[] storage;

	public TEIngotPile() {
		storage = new ItemStack[1];
	}

	public void setItemStack(ItemStack copy) {
		storage[0] = copy;
	}

	public int getStackSize() {
		if (storage[0] == null) return 0;
		return storage[0].stackSize;
	}
	
	public EnumMetal getMetal() {
		EnumMetal metal = null;
		
		if (storage[0] != null) {
			metal = ItemIngot.getMetal(storage[0]); 
		}
		return metal == null ? EnumMetal.BISMUTH : metal;
	}

	
	
	public boolean tryTransferIngot(ItemStack stack) {
		TEIngotPile pile = getTopmostIngotPile();
		
		if (stack.getItem() != pile.storage[0].getItem() || ItemIngot.getMetal(stack) != getMetal()) return false;
		
		if (pile.storage[0].stackSize < ItemIngot.maxpilesize) {
			pile.storage[0].stackSize++;
			stack.stackSize--;
			
			getWorld().markBlockForUpdate(pile.getPos());
			
			return true;
		} else {
			return BlockIngotPile.tryCreatePile(stack, getWorld(), pile.getPos().up());
		}
	}

	

	public void tryGrabIngot(EntityPlayer player) {
		TEIngotPile pile = getTopmostIngotPile();
		
		if (pile.storage[0].stackSize > 0) {
			ItemStack ejectedstack = pile.storage[0].splitStack(1);
			
			if (!player.inventory.addItemStackToInventory(ejectedstack)) {
				if (!getWorld().isRemote) {
					getWorld().spawnEntityInWorld(
						new EntityItem(getWorld(),
							pile.getPos().getX(),
							pile.getPos().getY() + 1, 
							pile.getPos().getZ(), 
							ejectedstack
					));
				}
			}
		}
		
		if (pile.storage[0].stackSize <= 0) {
			getWorld().setBlockToAir(pile.getPos());
		}
		
		getWorld().markBlockForUpdate(pile.getPos());
	}
	

	
	public TEIngotPile getTopmostIngotPile() {
		BlockPos pos = getPos();
		TEIngotPile pile = this;
		
		while (getWorld().getBlockState(pos).getBlock() instanceof BlockIngotPile) {
			pile = (TEIngotPile)getWorld().getTileEntity(pos);
			pos = pos.up();
		}
		
		return pile;
	}
	
	
	



	@Override
	public ItemStack decrStackSize(int i, int j) {
		if(storage[i] != null) {
			if(storage[i].stackSize <= j) {
				ItemStack itemstack = storage[i];
				storage[i] = null;
				updateNeighbours();
				return itemstack;
			}
			ItemStack itemstack1 = storage[i].splitStack(j);
			if(storage[i].stackSize == 0) {
				storage[i] = null;
			}
			updateNeighbours();
			return itemstack1;
			
		} else {
			return null;
		}
	}

	public void ejectContents() {
		float f3 = 0.05F;
		EntityItem entityitem;
		Random rand = new Random();
		float f = rand.nextFloat() * 0.8F + 0.1F;
		float f1 = rand.nextFloat() * 2.0F + 0.4F;
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
		updateNeighbours();
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}


	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)  {
		storage[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
			itemstack.stackSize = getInventoryStackLimit();
	}

	public void updateNeighbours() {
		if(!worldObj.isAirBlock(pos.up()))
			worldObj.getBlockState(pos.up()).getBlock().onNeighborBlockChange(worldObj, pos.up(), worldObj.getBlockState(pos.up()), BlocksVC.ingotPile);
	}


	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
		storage = new ItemStack[getSizeInventory()];
		
		for(int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte idx = nbttagcompound1.getByte("Slot");
			if(idx >= 0 && idx < storage.length) {
				storage[idx] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
			}
		}
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				storage[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}	
		nbttagcompound.setTag("Items", nbttaglist);
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
	public void handleInitPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1, 1, 1));
		return bb;
	}
	

	
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public int getSizeInventory()
	{
		return storage.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return this.storage[i];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		return null;
	}

	
	@Override
	public String getName() {
		return "ingotpile";
	}
	@Override
	public boolean hasCustomName() {
		return false;
	}
	
	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Ingot Pile");
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
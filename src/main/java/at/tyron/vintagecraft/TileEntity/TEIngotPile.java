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
		TEIngotPile pile = getTopmostTEPile();
		
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

	
	

	public void grabIngot(EntityPlayer player) {
		TEIngotPile pile = getTopmostTEPile();
		
		if (pile.storage[0].stackSize > 0) {
			ItemStack ejectedstack = pile.storage[0].splitStack(1);
			
			if (!player.inventory.addItemStackToInventory(ejectedstack)) {
				getWorld().spawnEntityInWorld(
					new EntityItem(getWorld(),
						pile.getPos().getX(),
						pile.getPos().getY() + 1, 
						pile.getPos().getZ(), 
						ejectedstack
				));
			}
		}
		
		if (pile.storage[0].stackSize <= 0) {
			getWorld().setBlockToAir(pile.getPos());
		}
		
		getWorld().markBlockForUpdate(pile.getPos());
	}
	

	
	public TEIngotPile getTopmostTEPile() {
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
		if(storage[i] != null)
		{
			if(storage[i].stackSize <= j)
			{
				ItemStack itemstack = storage[i];
				storage[i] = null;
				updateNeighbours();
				return itemstack;
			}
			ItemStack itemstack1 = storage[i].splitStack(j);
			if(storage[i].stackSize == 0)
				storage[i] = null;
			updateNeighbours();
			return itemstack1;
		}
		else
			return null;
	}

	public void ejectContents() {
		float f3 = 0.05F;
		EntityItem entityitem;
		Random rand = new Random();
		float f = rand.nextFloat() * 0.8F + 0.1F;
		float f1 = rand.nextFloat() * 2.0F + 0.4F;
		float f2 = rand.nextFloat() * 0.8F + 0.1F;

		for (int i = 0; i < getSizeInventory(); i++)
		{
			if(storage[i]!= null)
			{
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
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/*@Override
	public String getInventoryName()
	{
		return "Ingot Pile";
	}*/

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

	public void injectContents(int index, int count)
	{
		if(storage[index] != null)
		{
			if(storage[index].stackSize > 0)
			{
				storage[index] = new ItemStack(storage[index].getItem(), storage[index].stackSize + count, storage[index].getItemDamage());
				worldObj.markBlockForUpdate(pos);
			}
		}
		updateNeighbours();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return false;
	}

	/*@Override
	public void openInventory()
	{
	}*/

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) 
	{
		storage[i] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
			itemstack.stackSize = getInventoryStackLimit();
	}

	public void updateNeighbours()
	{
		
		if(/*worldObj.blockExists(pos.up()) && */!worldObj.isAirBlock(pos.up()))
			worldObj.getBlockState(pos.up()).getBlock().onNeighborBlockChange(worldObj, pos.up(), worldObj.getBlockState(pos.up()), BlocksVC.ingotPile);
			//worldObj.getBlock(xCoord, yCoord+1, zCoord).onNeighborBlockChange(worldObj, xCoord, yCoord+1, zCoord, TFCBlocks.IngotPile);
	}

	/*@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}*/

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
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
		//storage[0] = ItemStack.loadItemStackFromNBT(nbt);
		readFromNBT(nbt);
	}

	@Override
	public void createDataNBT(NBTTagCompound nbt) {
		/*if(storage[0] != null) {
			storage[0].writeToNBT(nbt);
		}*/
		writeToNBT(nbt);
	}

	
	
	
	@Override
	public void createInitNBT(NBTTagCompound nbt) {
		writeToNBT(nbt);
		
		/*if(storage[0] != null) {
			ItemStack is = storage[0].copy();
			is.setTagCompound(null);
			is.writeToNBT(nbt);
		}*/
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
		
		//storage[0] = ItemStack.loadItemStackFromNBT(nbt);
		/*updateNeighbours();
		worldObj.markBlockForUpdate(pos);*/
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = new AxisAlignedBB(pos, pos.add(1, 1, 1)); //  AxisAlignedBB.fromBounds(xCoord, yCoord, zCoord, xCoord +1, yCoord + 1, zCoord + 1);
		return bb;
	}
	

	
	
	@Override
	public String getName() {
		return "ingotpile";
	}
	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Ingot Pile");
	}
	
	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


}
package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumStonePotUtilization;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TEStonePot extends TENoGUIInventory implements IUpdatePlayerListBox {
	public static int burnTimePerCoal = 900; 
	public static int maxCoalInPot = 5;
	
	public EnumRockType rocktype;
	public EnumStonePotUtilization utilization;
	

	// For utilization = FORGE
	public int burnTime = 0;
	public boolean burning = false;
	
	
	public TEStonePot() {
		storage = new ItemStack[getSizeInventory()];
		rocktype = EnumRockType.GRANITE;
	}
	
	public boolean tryIgnite() {
		if (utilization == EnumStonePotUtilization.FORGE && !burning && burnTime > 0) {
			burning = true;
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		
		return false;
	}
	
	
	@Override
    public void update() {
		
		if (utilization == EnumStonePotUtilization.FORGE) {
			if (burning) {
				burnTime--;
				
				if (worldObj.getWorldTime() % 60 == 0) {
					worldObj.markBlockForUpdate(pos);
				}
				
				if (burnTime <= 0) {
					burning = false;
					worldObj.markBlockForUpdate(pos);
				}
			}
			changeHeatableTemperature();
		}
	}
	
	
	
	public boolean canRenderBreaking() {
		return true;
	}
	
	
	
	public void changeHeatableTemperature() {
		if (getHeatableItemStack() != null) {
			int change = 4;
			if (burnTime > 0 && burnTime < burnTimePerCoal) change = 1;
			if (!burning) change = -4;

			
			IItemHeatable item = (IItemHeatable)getHeatableItemStack().getItem();
			int itemTemperature = item.getTemperatureM10(getHeatableItemStack()); 
			
			// Heat until max temperature of the item or until min temperature of 0 degree
			int forgetemp = Math.min(
				10 * item.heatableUntil(getHeatableItemStack()), 
				Math.max(0, itemTemperature + change)
			);
			
			// Forge can only max reach 1100 degrees
			forgetemp = Math.min(forgetemp, 11000);
			
			item.setTemperatureM10(getHeatableItemStack(), forgetemp, worldObj.getTotalWorldTime());
		}
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
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		burnTime = nbttagcompound.getInteger("burntime");
		burning = nbttagcompound.getBoolean("burning");
		rocktype = EnumRockType.byId(nbttagcompound.getInteger("rocktype"));
		
		
		int util = nbttagcompound.getInteger("utilization");
		if (util != -1) {
			utilization = EnumStonePotUtilization.values()[util];
		}
		
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("burntime", burnTime);
		nbttagcompound.setBoolean("burning", burning);
		nbttagcompound.setInteger("rocktype", rocktype.id);
		
		nbttagcompound.setInteger("utilization", utilization == null ? -1 : utilization.ordinal());
		
		super.writeToNBT(nbttagcompound);
	}
	
	
	@Override
	public int getSizeInventory() {
		return 2;
	}


	@Override
	public String getName() {
		return "Forge";
	}


	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Forge");
	}


	
	
	public int getFillHeight() {
		if (utilization == EnumStonePotUtilization.FORGE) {
			return 2 * (int) Math.ceil((burnTime * 1.0) / burnTimePerCoal);
		}
		return 0;
	}
	
	public int getHeatableQuantity() {
		if (utilization == EnumStonePotUtilization.FORGE) {
			if (storage[0].getItem() instanceof ItemIngot) {
				return storage[0].stackSize;
			} else {
				return 1;
			}
		}
		return 0;
	}
	
	
	public ItemStack getHeatableItemStack() {
		if (utilization == EnumStonePotUtilization.FORGE) {
			return storage[0];
		} else {
			return null;
		}
	}
	


	public boolean tryPutItemStack(ItemStack itemstack) {
		if (utilization == null || utilization == EnumStonePotUtilization.FORGE) {
		
			if (itemstack.getItem() instanceof IItemFuel && ((IItemFuel)itemstack.getItem()).isMetalWorkingFuel(itemstack)) {
				if (burnTime < burnTimePerCoal * maxCoalInPot) {
					itemstack.stackSize--;
					burnTime += burnTimePerCoal;
					worldObj.markBlockForUpdate(pos);
					utilization = EnumStonePotUtilization.FORGE;
					return true;
				}
			}
			
			if (suitableItemStackForHeating(itemstack)) {
				if (storage[0] == null) {
					storage[0] = itemstack.splitStack(1);
				} else {
					NBTTagCompound nbt = itemstack.getTagCompound();
					if (nbt == null) {
						nbt = new NBTTagCompound();
						nbt.setInteger("forgetemp", 0);
						itemstack.setTagCompound(nbt);
					}

					int newtemp = (storage[0].stackSize * storage[0].getTagCompound().getInteger("forgetemp") + nbt.getInteger("forgetemp")) / (storage[0].stackSize + 1);
					
					storage[0].stackSize++;
					itemstack.stackSize--;
					
					storage[0].getTagCompound().setInteger("forgetemp", newtemp);
				}
				
				utilization = EnumStonePotUtilization.FORGE;
				worldObj.markBlockForUpdate(pos);
				return true;
			}
			
		}
		
		return false;
	}
	
	
	public boolean tryGrabItemStack(EntityPlayer player) {
		if (storage[0] != null) {
			boolean merged = false;
			
			ItemStack dropped = storage[0].splitStack(1);
			
			
			if (utilization == EnumStonePotUtilization.FORGE) {
				dropped.getTagCompound().setLong("startcoolingat", worldObj.getTotalWorldTime() + 200);
				IItemHeatable heatable = (IItemHeatable)dropped.getItem();
				
				for (int i = 0; i < player.inventory.mainInventory.length; i++) {
					if (heatable.tryStackWith(worldObj, player.inventory.mainInventory[i], dropped)) {
						merged = true;
						break;
					} 
				}
				
			}
			
			
			
			if (!merged && !player.inventory.addItemStackToInventory(dropped)) {
				if (!getWorld().isRemote) {
					getWorld().spawnEntityInWorld(
						new EntityItem(getWorld(),
							getPos().getX() + 0.5f,
							getPos().getY() + 0.5f, 
							getPos().getZ() + 0.5f, 
							dropped
					));
				}
			}
			
			if (storage[0].stackSize <= 0) {
				storage[0] = null;
			}
			
			worldObj.markBlockForUpdate(pos);
			
			return true;
		}
		
		return false;
	}


	
	public boolean suitableItemStackForHeating(ItemStack itemstack) {
		ItemStack newwithouttemp = itemstack.copy();
		
		if (newwithouttemp.getTagCompound() != null) {
			newwithouttemp.getTagCompound().removeTag("forgetemp");
			newwithouttemp.getTagCompound().removeTag("lasttempupdate");
			newwithouttemp.getTagCompound().removeTag("startcoolingat");
		}
		
		ItemStack containedwithouttemp = null;
		if (storage[0] != null) {
			containedwithouttemp = storage[0].copy();
			if (containedwithouttemp.getTagCompound() != null) {
				containedwithouttemp.getTagCompound().removeTag("forgetemp");
				containedwithouttemp.getTagCompound().removeTag("lasttempupdate");
				containedwithouttemp.getTagCompound().removeTag("startcoolingat");
			}
		}
		
		return
			itemstack.getItem() instanceof IItemHeatable &&
			
			(
				storage[0] == null ||
				(containedwithouttemp.getItem() == newwithouttemp.getItem() && 
				 containedwithouttemp.getTagCompound().equals(newwithouttemp.getTagCompound()) && 
				 containedwithouttemp.stackSize < 4
				)
			)
		;
	}

	public EnumMetal getIngotMetal() {
		return ItemIngot.getMetal(storage[0]);
	}
	
}

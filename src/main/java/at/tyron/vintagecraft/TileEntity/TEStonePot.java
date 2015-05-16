package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemMetalPlate;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumStonePotUtilization;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class TEStonePot extends TENoGUIInventory implements IUpdatePlayerListBox {
	public static int burnTimePerCoal = 900; 
	
	public EnumRockType rocktype = EnumRockType.GRANITE;
	public EnumStonePotUtilization utilization;
	

	// Utilization = FORGE
	public int burnTime = 0;
	public boolean burning = false;
	
	
	public TEStonePot() {
		storage = new ItemStack[getSizeInventory()];
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
		//System.out.println(storage[0]);
		
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
	
	
	public void changeHeatableTemperature() {
		if (getHeatableItemStack() != null) {
			NBTTagCompound nbt = getHeatableItemStack().getTagCompound();
			if (nbt == null) nbt = new NBTTagCompound();
			
			int change = 6;
			if (burnTime > 0 && burnTime < burnTimePerCoal) change = 1;
			if (!burning) change = -4;

			
			IItemHeatable item = (IItemHeatable)getHeatableItemStack().getItem();
			
			int forgetemp = Math.min(
				10 * item.heatableUntil(getHeatableItemStack()), 
				Math.max(0, nbt.getInteger("forgetemp") + change)
			);
			
			
/*			if (getHeatableItemStack().getItem() instanceof ItemMetalPlate) {
				System.out.println(forgetemp / 10);
			}
	*/		
			nbt.setInteger("forgetemp", forgetemp);
			
			if (forgetemp <= 0) {
				nbt.removeTag("lasttempupdate");				
			} else {
				nbt.setLong("lasttempupdate", worldObj.getWorldTime());
			}

			//System.out.println(forgetemp);
			
			getHeatableItemStack().setTagCompound(nbt);
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
			return (int) Math.ceil((burnTime * 1.0) / burnTimePerCoal);
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
		
			if (itemstack.getItem() instanceof IItemFuel && ((IItemFuel)itemstack.getItem()).isForgeFuel(itemstack)) {
				if (burnTime < burnTimePerCoal * 10) {
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
				dropped.getTagCompound().setLong("startcoolingat", worldObj.getWorldTime() + 200);
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

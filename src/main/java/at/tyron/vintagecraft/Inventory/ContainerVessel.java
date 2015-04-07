package at.tyron.vintagecraft.Inventory;

import javax.management.RuntimeErrorException;

import at.tyron.vintagecraft.Interfaces.IFuel;
import at.tyron.vintagecraft.Interfaces.ISmeltable;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ContainerVessel extends Container {
	public InventoryPlayer playerInventory;
	public IInventory containerInv;

	
	public NBTTagCompound bagNbt; // When opened from item
	public TEVessel tevessel; // When opened from block
	
	
	public ContainerVessel(InventoryPlayer inventoryplayer, TEVessel tileentityvessel) {
		
		if (tileentityvessel == null) throw new RuntimeException("tileentityvessel cannot be null!");
		if (inventoryplayer == null) throw new RuntimeException("inventoryplayer cannot be null!");
		
		containerInv = tileentityvessel;
		tevessel = tileentityvessel;
		this.playerInventory = inventoryplayer;
		bagNbt = null;
		
		initSlots();
	}
	
	
	public ContainerVessel(InventoryPlayer playerInventory, NBTTagCompound bagNbt) {
		containerInv = new InventoryCrafting(this, 2, 2);
		this.playerInventory = playerInventory;
		this.bagNbt = bagNbt;
		tevessel =  null;
		
		initSlots();
		loadBagInventory();
	}
	
	
	void initSlots() {		
		this.addSlotToContainer(new SlotVessel(containerInv, 0, 71, 25));
		this.addSlotToContainer(new SlotVessel(containerInv, 1, 89, 25));
		this.addSlotToContainer(new SlotVessel(containerInv, 2, 71, 43));
		this.addSlotToContainer(new SlotVessel(containerInv, 3, 89, 43));
        
        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
        }        		
	}

	



	@Override
	public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer playerIn) {
		if (playerInventory.currentItem == slotId - 27 - 4 && tevessel == null) {
			return null;
		}
		
		ItemStack stack = super.slotClick(slotId, clickedButton, mode, playerIn);
		
		if (tevessel == null) {
			saveBagInventory(playerInventory.getCurrentItem());
		}
		return stack;
	}


	public void loadBagInventory() {
		if(bagNbt != null) {
			NBTTagList nbttaglist = bagNbt.getTagList("Items", 10);
			for(int i = 0; i < nbttaglist.tagCount(); i++) {
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				byte byte0 = nbttagcompound1.getByte("Slot");
				if(byte0 >= 0 && byte0 < 4) {
					ItemStack is = ItemStack.loadItemStackFromNBT(nbttagcompound1);
					if (is == null) continue;
					if(is.stackSize >= 1)
						this.containerInv.setInventorySlotContents(byte0, is);
					else
						this.containerInv.setInventorySlotContents(byte0, null);
				}
			}
		}
	}
	
	public void saveBagInventory(ItemStack is) {
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < containerInv.getSizeInventory(); i++) {
			if(containerInv.getStackInSlot(i) != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				containerInv.getStackInSlot(i).writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		if(is != null) {
			if(!is.hasTagCompound()) {
				is.setTagCompound(new NBTTagCompound());
			}
			is.getTagCompound().setTag("Items", nbttaglist);
		}
	}

	

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		return true;
	}



    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int clickedIndex) {
    	ItemStack returnedStack = null;
		Slot clickedSlot = (Slot)this.inventorySlots.get(clickedIndex);

		if (clickedSlot != null && clickedSlot.getHasStack()) {
			ItemStack clickedStack = clickedSlot.getStack();
			returnedStack = clickedStack.copy();

			if (clickedIndex < 4) {
				if (!this.mergeItemStack(clickedStack, 4, inventorySlots.size(), true))
					return null;
			}
			else if (clickedIndex >= 4 && clickedIndex < inventorySlots.size()) {
				if (SlotVessel.validItem(clickedStack) && !this.mergeItemStack(clickedStack, 0, 4, false))
					return null;
			}

			if (clickedStack.stackSize == 0)
				clickedSlot.putStack((ItemStack)null);
			else
				clickedSlot.onSlotChanged();

			if (clickedStack.stackSize == returnedStack.stackSize)
				return null;

			clickedSlot.onPickupFromSlot(player, clickedStack);
		}
		
		detectAndSendChanges();
		return returnedStack;
    }
}

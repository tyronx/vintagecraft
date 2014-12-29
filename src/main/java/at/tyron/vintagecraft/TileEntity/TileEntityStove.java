package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Inventory.ContainerStove;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.block.BlockStove;
import at.tyron.vintagecraft.interfaces.IFuel;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityStove extends TileEntityLockable implements IUpdatePlayerListBox, ISidedInventory {
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    
    // 0 = stuff to smelt
    // 1 = fuel 
    // 2 = output slot
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    
    private String furnaceCustomName;
    
    // What kind of furnace are we?
    public EnumFurnace furnace;
    // Current temperature of the furnace
    public int furnaceTemperature = 20;
    // Current temperature of the ore (Degree Celsius * deg
    public int oreTemperature = 20;
    // Maximum temperature that can be reached with the currently used fuel
    public int maxTemperature;
    // For how long the ore has been cooking
    public int oreCookingTime;
    // How much of the current fuel is consumed
    public int fuelBurnTime;
    // How much fuel is available
    public int maxFuelBurnTime;
    
    
    int counter;

    
	// Update once in counterMax() ticks
    public int counterMax() {
    	return 12;
    }
    
    // Resting temperature
    public int enviromentTemperature() {
    	return 20;
    }
    
    // seconds it requires to melt the ore once beyond melting point
    public int maxCookingTime() {
    	return 30;
    }
    
    
    
    
    public TileEntityStove() {
    	this.furnace = EnumFurnace.STOVE;
    	
    }
    
    public TileEntityStove(EnumFurnace furnace) {
		this.furnace = furnace;
	}

    

    

    
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    public ItemStack getStackInSlot(int index)
    {
        return this.furnaceItemStacks[index];
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (this.furnaceItemStacks[index] != null)
        {
            ItemStack itemstack;

            if (this.furnaceItemStacks[index].stackSize <= count)
            {
                itemstack = this.furnaceItemStacks[index];
                this.furnaceItemStacks[index] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.furnaceItemStacks[index].splitStack(count);

                if (this.furnaceItemStacks[index].stackSize == 0)
                {
                    this.furnaceItemStacks[index] = null;
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        if (this.furnaceItemStacks[index] != null) {
            ItemStack itemstack = this.furnaceItemStacks[index];
            this.furnaceItemStacks[index] = null;
            return itemstack;
        }
        else {
            return null;
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        boolean flag = stack != null && stack.isItemEqual(this.furnaceItemStacks[index]) && ItemStack.areItemStackTagsEqual(stack, this.furnaceItemStacks[index]);
        this.furnaceItemStacks[index] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }

        if (index == 0 && !flag) {
        	oreTemperature = enviromentTemperature();
        	oreCookingTime = 0;
            this.markDirty();
        }
    }

    public String getName()
    {
        return this.hasCustomName() ? this.furnaceCustomName : "container.stove";
    }

    public boolean hasCustomName()
    {
        return this.furnaceCustomName != null && this.furnaceCustomName.length() > 0;
    }

    public void setCustomInventoryName(String p_145951_1_) {
        this.furnaceCustomName = p_145951_1_;
    }



    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean isBurning() {
        return this.fuelBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory) {
        return inventory.getField(5) > 0;
    }
    

    
    public void update() {
    	if (worldObj.isRemote) return;
    	
    	counter = ++counter % counterMax();
    	int tick = counter / (counterMax()-1);
    	if (tick == 0) return;

    	
    	// Out of fuel
    	if (fuelBurnTime > 0) {
    		fuelBurnTime -= tick;
    		if (fuelBurnTime == 0) {
    			markDirty();
    			maxFuelBurnTime = 0;
    		}
    	}
    	
    	
    	// Furnace is burning: Heat furnace and ore
		if (isBurning()) {
   			furnaceTemperature = changeTemperature(furnaceTemperature, maxTemperature, tick); 			
		}
		
		heatOre();
		
		
		// Furnace is not burning and can burn: Ignite the fuel
		if (!isBurning() && canSmelt()) {
			igniteFuel();
		}
	
		
		// Furnace is not burning: Cool down furnace and ore also turn of fire
		if (!isBurning()) {
 			furnaceTemperature = changeTemperature(furnaceTemperature, enviromentTemperature(), tick);
 			
 			setStoveBurning(false);
 		}
    }
    
    
    

    
    
    public int changeTemperature(int fromTemp, int toTemp, int speed) {
    	return changeTemperature(fromTemp, toTemp, speed, true);
    }
    
    public int changeTemperature(int fromTemp, int toTemp, int speed, boolean condition) {
    	if (!condition) return fromTemp;
    	
    	int diff = Math.abs(fromTemp - toTemp);
    	
    	speed = speed + speed * (diff/50);
   
    	
    	if (diff < speed) {
    		return toTemp;
    	}
    	if (fromTemp > toTemp) {
    		speed = -speed;
    	}
    	if (fromTemp == toTemp) {
    		speed = 0;
    	}
    	
    	return fromTemp + speed;
    }
    
    
    
    public ItemStack fuelSlot() {
    	return furnaceItemStacks[1];
    }
    
    public ItemStack oreSlot() {
    	return furnaceItemStacks[0];
    }
    
    public ItemStack smeltedSlot() {
    	return furnaceItemStacks[2];
    }
    
    ItemStack oreItemSmelted() {
    	if (oreSlot() != null && oreSlot().getItem() instanceof ISmeltable) {
    		ISmeltable smeltable = (ISmeltable)oreSlot().getItem();
    		return smeltable.getSmelted(oreSlot());
    	}
    	return null;
    }

    int oreItemSmeltedRatio() {
    	if (oreSlot() != null && oreSlot().getItem() instanceof ISmeltable) {
    		ISmeltable smeltable = (ISmeltable)oreSlot().getItem();
    		return smeltable.getRaw2SmeltedRatio(oreSlot());
    	}
    	return 0;
    }
    
    int oreItemSmeltedMeltingPoint() {
    	if (oreSlot()!= null && oreSlot().getItem() instanceof ISmeltable) {
    		ISmeltable smeltable = (ISmeltable)oreSlot().getItem();
    		return smeltable.getMeltingPoint(oreSlot());
    	}
    	return 0;    	
    }
    
    
    ItemStack smeltedItem() {
    	return furnaceItemStacks[2];
    }

    
   


    private boolean canSmelt() {
    	//System.out.println(oreItemSmelted());
    	return
    			// Require smeltable tore
    			oreSlot() != null && oreItemSmelted() != null
    			// Require fuel
    			&& furnace.getProducedHeat(fuelSlot()) != 0
    			// Require enough fuel
    			&& fuelSlot().stackSize >= furnace.fuelUse
    			// Require enough ore
    			&& oreSlot().stackSize >= oreItemSmeltedRatio()
    			// Require empty output slot or stackable smelted
    			&& (smeltedSlot() == null || (smeltedSlot().isItemEqual(smeltedItem()) && smeltedSlot().stackSize + smeltedItem().stackSize < smeltedSlot().getMaxStackSize()))
    	;
    }

    
    public void heatOre() {
    	if (oreItemSmeltedMeltingPoint() > 0) {
    		oreTemperature = changeTemperature(oreTemperature, furnaceTemperature, 2);
    	} else {
    		oreTemperature = enviromentTemperature();
    	}
    	
		if (oreItemSmeltedMeltingPoint() > 0 && oreTemperature >= oreItemSmeltedMeltingPoint()) {
			oreCookingTime += MathHelper.clamp_int(3 * oreItemSmeltedMeltingPoint() / oreTemperature - 1, 1, 15);
		} else {
			if (oreCookingTime > 0) oreCookingTime--;
		}
		
		if (oreCookingTime > maxCookingTime()) {
			smeltItem();
		}
    }
    
    
    public void igniteFuel() {
    	maxFuelBurnTime = fuelBurnTime = furnace.getHeatDuration(fuelSlot());
		maxTemperature = furnace.getProducedHeat(fuelSlot());
		
		fuelSlot().stackSize -= furnace.fuelUse;
		
		if (fuelSlot().stackSize <= 0) {
			furnaceItemStacks[1] = null;
		}
		
		setStoveBurning(true);
    }
    
    public void setStoveBurning(boolean burning) {
    	boolean nowburning = worldObj.getBlockState(pos).getBlock() == BlocksVC.stove_lit;
    	
    	if (nowburning != burning) {
    		BlockStove.setState(burning, worldObj, pos);
    	}
    }
    
    
    public void smeltItem() {
    	if (
    		oreSlot() != null 
    		&& oreItemSmelted() != null
    		&& oreSlot().stackSize >= oreItemSmeltedRatio()
    		&& (smeltedSlot() == null || (smeltedSlot().isItemEqual(smeltedItem()) && smeltedSlot().stackSize + smeltedItem().stackSize < smeltedSlot().getMaxStackSize()))
    	) {
    		
            ItemStack itemstack = oreItemSmelted();

            if (smeltedItem() == null) {
                this.furnaceItemStacks[2] = itemstack.copy();
            }
            else if (smeltedItem().getItem() == itemstack.getItem()) {
            	smeltedItem().stackSize += itemstack.stackSize; 
            }
            
            oreSlot().stackSize -= oreItemSmeltedRatio();

            if (oreSlot().stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }
            
            oreTemperature = enviromentTemperature();
            oreCookingTime = 0;
        }
    }

    public int getItemBurnTime(ItemStack itemstack) {
       return furnace.getHeatDuration(itemstack);
    }

    public boolean isItemFuel(ItemStack itemstack) {
        return getItemBurnTime(itemstack) > 0;
    }

    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(this.pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    public void openInventory(EntityPlayer player) {}

    public void closeInventory(EntityPlayer player) {}

    public boolean isItemValidForSlot(int index, ItemStack stack) {
    	//System.out.println("check valid");
        return index == 2 ? false : (index != 1 ? true : isItemFuel(stack));
    }

    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? slotsBottom : (side == EnumFacing.UP ? slotsTop : slotsSides);
    }

    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    	//System.out.println("can insert");
        return isItemValidForSlot(index, itemStackIn);
    }

    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if (direction == EnumFacing.DOWN && index == 1) {
            Item item = stack.getItem();

            if (item != Items.water_bucket && item != Items.bucket) {
                return false;
            }
        }

        return true;
    }

    public String getGuiID()
    {
        return "minecraft:furnace";
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new ContainerStove(playerInventory, this);
    }
    
   
    public int getField(int id) {
        switch (id) {
        	case 0:
        		return furnace.id;
            case 1:
                return furnaceTemperature;
            case 2:
                return oreTemperature;
            case 3:
                return maxTemperature;
            case 4:
                return oreCookingTime;
            case 5:
                return fuelBurnTime;
            case 6:
            	return maxFuelBurnTime;
            default:
                return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
    	case 0:
    		furnace = EnumFurnace.byId(value);
            break;
        case 1:
            furnaceTemperature = value;
            break;
        case 2:
            oreTemperature = value;
            break;
        case 3:
            maxTemperature = value;
            break;
        case 4:
            oreCookingTime = value;
            break;
        case 5:
            fuelBurnTime = value;
            break;
        case 6:
        	maxFuelBurnTime = value;
        	break;
        default:
            break;          
        }
    }

    public int getFieldCount() {
        return 7;
    }

    public void clear() {
        for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
            this.furnaceItemStacks[i] = null;
        }
    }
    
    
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        furnace = EnumFurnace.byId(compound.getShort("furnace"));
        furnaceTemperature = compound.getShort("furnaceTemperature");
        oreTemperature = compound.getShort("oreTemperature");
        maxTemperature = compound.getShort("maxTemperature");
        oreCookingTime = compound.getShort("oreCookingTime");
        fuelBurnTime = compound.getShort("fuelBurnTime");

        
        if (compound.hasKey("CustomName", 8)) {
            this.furnaceCustomName = compound.getString("CustomName");
        }
    }

    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        

        compound.setShort("furnace", (short)this.furnace.id);
        compound.setShort("furnaceTemperature", (short)this.furnaceTemperature);
        compound.setShort("oreTemperature", (short)this.oreTemperature);
        compound.setShort("maxTemperature", (short)this.maxTemperature);
        compound.setShort("oreCookingTime", (short)this.oreCookingTime);
        compound.setShort("fuelBurnTime", (short)this.fuelBurnTime);
        
        
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i)
        {
            if (this.furnaceItemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        compound.setTag("Items", nbttaglist);

        if (this.hasCustomName())
        {
            compound.setString("CustomName", this.furnaceCustomName);
        }
    }
}
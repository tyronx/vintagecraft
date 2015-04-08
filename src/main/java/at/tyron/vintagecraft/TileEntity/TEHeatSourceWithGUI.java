package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Block.Utility.BlockStove;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import at.tyron.vintagecraft.Interfaces.IStrongHeatSource;
import at.tyron.vintagecraft.Inventory.ContainerStove;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
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

public class TEHeatSourceWithGUI extends TileEntityLockable implements IUpdatePlayerListBox, ISidedInventory {
    private static final int[] slotsTop = new int[] {0};
    private static final int[] slotsBottom = new int[] {2, 1};
    private static final int[] slotsSides = new int[] {1};
    
    // 0 = stuff to smelt
    // 1 = fuel 
    // 2 = output slot
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    
    private String furnaceCustomName;
    
    // What kind of furnace are we?
    public EnumStrongHeatSource furnace;
    // Current temperature of the furnace
    public int furnaceTemperature = 20;
    // Current temperature of the ore (Degree Celsius * deg
    public int oreTemperature = 20;
    // Maximum temperature that can be reached with the currently used fuel
    public int maxTemperature;
    // For how long the ore has been cooking
    public float oreCookingTime;
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
    
    
    
    
    public TEHeatSourceWithGUI() {
    	this.furnace = EnumStrongHeatSource.STOVE;
    	
    }
    
    public TEHeatSourceWithGUI(EnumStrongHeatSource furnace) {
		this.furnace = furnace;
	}

    
    public boolean tryPutItemStack(ItemStack itemstack) {
    	if (itemstack.getItem() instanceof IItemSmeltable && ((IItemSmeltable)itemstack.getItem()).getSmelted(itemstack) != null) {
    		if (furnaceItemStacks[0] == null) {
    			furnaceItemStacks[0] = itemstack.splitStack(1);
    			return true;
    		}
    		
    		if (ItemStack.areItemStackTagsEqual(itemstack, furnaceItemStacks[0]) && itemstack.isItemEqual(furnaceItemStacks[0])) {
    			furnaceItemStacks[0].stackSize++;
    			itemstack.stackSize--;
    			return true;
    		}
    	}

    	if (itemstack.getItem() instanceof IItemFuel && ((IItemFuel)itemstack.getItem()).getBurningHeat(itemstack) > 0) {
    		if (furnaceItemStacks[1] == null) {
    			furnaceItemStacks[1] = itemstack.splitStack(1);
    			return true;
    		}
    		
    		if (ItemStack.areItemStackTagsEqual(itemstack, furnaceItemStacks[1]) && itemstack.isItemEqual(furnaceItemStacks[1])) {
    			furnaceItemStacks[1].stackSize++;
    			itemstack.stackSize--;
    			return true;
    		}
    	}
    	
    	return false;
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
        return this.hasCustomName() ? this.furnaceCustomName : (furnace == EnumStrongHeatSource.STOVE ? "container.stove" : "container.firepit");
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
    

    @Override
    public void update() {
    	if (worldObj.isRemote) return;
    	
    	counter = ++counter % counterMax();
    	int tick = counter / (counterMax()-1);
    	if (tick == 0) return;

    	
    	// Burn up fuel
    	if (fuelBurnTime > 0) {
    		fuelBurnTime -= tick;
    		if (fuelBurnTime == 0) {
    			markDirty();
    			maxFuelBurnTime = 0;
    		}
    	}
    	
    	
    	// Furnace is burning: Heat furnace
		if (isBurning()) {
   			furnaceTemperature = changeTemperature(furnaceTemperature, maxTemperature, tick); 			
		}
		
		// Ore follows furnace temperature
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
    	if (oreSlot() != null && oreSlot().getItem() instanceof IItemSmeltable) {
    		IItemSmeltable smeltable = (IItemSmeltable)oreSlot().getItem();
    		return smeltable.getSmelted(oreSlot());
    	}
    	return null;
    }

    int oreItemSmeltedRatio() {
    	if (oreSlot() != null && oreSlot().getItem() instanceof IItemSmeltable) {
    		IItemSmeltable smeltable = (IItemSmeltable)oreSlot().getItem();
    		return smeltable.getRaw2SmeltedRatio(oreSlot());
    	}
    	return 0;
    }
    
    int oreItemSmeltedMeltingPoint() {
    	if (oreSlot()!= null && oreSlot().getItem() instanceof IItemSmeltable) {
    		IItemSmeltable smeltable = (IItemSmeltable)oreSlot().getItem();
    		return smeltable.getMeltingPoint(oreSlot());
    	}
    	return 0;    	
    }
    
    float oreItemSmeltingDurationModifier() {
    	if (oreSlot()!= null && oreSlot().getItem() instanceof IItemSmeltable) {
    		IItemSmeltable smeltable = (IItemSmeltable)oreSlot().getItem();
    		return smeltable.getSmeltingSpeedModifier(oreSlot());
    	}
    	return 1f;    	
    	
    }
    
    ItemStack smeltedItem() {
    	return furnaceItemStacks[2];
    }

    
   


    private boolean canSmelt() {
    	
    	boolean smeltableOre =
    			// Require smeltable ore
    			oreSlot() != null && oreItemSmelted() != null
    			// Require enough ore
    			&& oreSlot().stackSize >= oreItemSmeltedRatio();

    			
    	// Fire pits always burn up their fuel
    	if (furnace == EnumStrongHeatSource.FIREPIT) {
    		smeltableOre = smeltableOre || oreSlot() == null;
    	}
    	
    	
    	return
    			smeltableOre
    			// Require fuel
    			&& furnace.getProducedHeat(fuelSlot()) != 0
    			// Require enough fuel
    			&& fuelSlot().stackSize >= furnace.fueluse
    			// Require empty output slot or stackable smelted
    			&& (smeltedSlot() == null || (smeltedSlot().isItemEqual(smeltedItem()) && smeltedSlot().stackSize + smeltedItem().stackSize < smeltedSlot().getMaxStackSize()))
    	;
    }

    
    public void heatOre() {
    	// Adjust temperature of ore
    	if (oreItemSmeltedMeltingPoint() > 0) {
    		oreTemperature = changeTemperature(oreTemperature, furnaceTemperature, 2);
    	} else {
    		oreTemperature = enviromentTemperature();
    	}
    	
    	// Begin smelting when hot enough
		if (oreItemSmeltedMeltingPoint() > 0 && oreTemperature >= oreItemSmeltedMeltingPoint()) {
			float diff = (1f * oreTemperature / oreItemSmeltedMeltingPoint());
			oreCookingTime += MathHelper.clamp_int((int) (diff), 1, 30) * oreItemSmeltingDurationModifier();
		} else {
			if (oreCookingTime > 0) oreCookingTime--;
		}
		
		// Finished smelting? Turn to smelted item
		if (oreCookingTime > maxCookingTime()) {
			smeltItem();
		}
    }
    
    
    public void igniteFuel() {
    	maxFuelBurnTime = fuelBurnTime = furnace.getHeatDuration(fuelSlot());
		maxTemperature = furnace.getProducedHeat(fuelSlot());
		
		fuelSlot().stackSize -= furnace.fueluse;
		
		if (fuelSlot().stackSize <= 0) {
			furnaceItemStacks[1] = null;
		}
		
		setStoveBurning(true);
    }
    
    public void setStoveBurning(boolean burning) {
    	IStrongHeatSource block = (IStrongHeatSource) worldObj.getBlockState(pos).getBlock();
    	
    	if (block.isBurning() != burning) {
    		block.setState(burning, worldObj, pos);
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
                return (int) oreCookingTime;
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
    		furnace = EnumStrongHeatSource.byId(value);
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

        furnace = EnumStrongHeatSource.byId(compound.getShort("furnace"));
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
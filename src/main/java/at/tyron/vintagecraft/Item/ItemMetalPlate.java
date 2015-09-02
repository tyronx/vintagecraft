package at.tyron.vintagecraft.Item;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.BlockClass.MetalPlatingClassEntry;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Interfaces.IItemMetalTyped;
import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMetalPlate extends ItemBlockVC implements ISubtypeFromStackPovider, IItemSmithable, IItemHeatable, IItemMetalTyped {

	public ItemMetalPlate(Block block) {
		super(block);
        this.setHasSubtypes(true);
        setMaxStackSize(16);
	}
	
    
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
    	int tmp = getTemperatureM10(stack) / 60;
    	
    	if (renderPass == 1) {
    		int r = Math.min(255, Math.max(0, 128 + (int) (255*tmp/400f)));
        	int g = Math.max(0,  25 + (int) (255*tmp/800f));
        	int b = 25;
        	int a = Math.min(204, (int) (255*tmp/200f));

    		return
    			a << 24 |
    			r << 16 |
    			g << 8  |
    			b
    		;
    		
    	} else {
        	int g = Math.max(0, (int) (255*(1 - tmp/250f)));
        	int b = Math.max(0, (int) (255*(1 - tmp/200f)));
        	
    		return 0xffff0000 | (g << 8) | (b);    		
    	}
    }
    
    
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		int forgetemp = getTemperatureM10(itemstack) / 10;
		
		if (forgetemp > 0) {
			String workable = "";
			if (forgetemp >= getMetal(itemstack).getMinWorkableTemperature()) workable = "  | WORKABLE";
			tooltip.add("Temperature: " + forgetemp + " deg." + workable);
		}
		
		if (getAppliedTechniques(itemstack).length > 0) {
			tooltip.add("Has been worked");
		}
				
		updateTemperature(itemstack, playerIn.worldObj);
	}


	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getMetal(stack) == null) {
			return "item.metalplate.unknown";
		}
		return "item.metalplate." + getMetal(stack).getName() + (isOddlyShaped(stack) ? ".oddlyshaped" : "");
	}
	
	
	
	public static EnumMetal getMetal(ItemStack itemstack) {
		MetalPlatingClassEntry entry = (MetalPlatingClassEntry) BlocksVC.metalplate.getEntryFromItemStack(itemstack);
		if (entry == null) return null;
		return entry.metal;
	}

	
	
	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName();
	}


	
	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return 
			itemstack != null && comparison != null &&
			itemstack.getItem() == comparison.getItem() &&
			itemstack.stackSize == comparison.stackSize &&
			getMetal(itemstack) == getMetal(comparison) &&
			(!isOddlyShaped(itemstack) || forrecipe.wildcardMatch)
		;
	}

	@Override
	public int heatableUntil(ItemStack stack) {
		return getMetal(stack).getMaxWorkingTemperature();
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		updateTemperature(stack, worldIn);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}


	// Items taken from a forge have a grace timer when they start cooling and 
	// this timer will be shorter if the player already has taken an ingot that is not yet cooling, 
	// so that these may stack together 
	@Override
	public boolean canStackWith(World world, ItemStack self, ItemStack remote) {
		return 
			self != null &&
			remote != null &&
			self.getItem() == remote.getItem() &&
			getMetal(self) == getMetal(remote) &&
			world.getWorldTime() < self.getTagCompound().getLong("startcoolingat") &&
			world.getWorldTime() < remote.getTagCompound().getLong("startcoolingat") &&
			(Math.abs(self.getTagCompound().getInteger("forgetemp") - remote.getTagCompound().getInteger("forgetemp")) < 80)
		;
	}

	@Override
	public boolean tryStackWith(World world, ItemStack self, ItemStack remote) {
		if (canStackWith(world, self, remote)) {
			int quantityToStack = Math.min(self.getMaxStackSize(), self.stackSize + remote.stackSize) - self.stackSize; 
			int newtemp = (self.stackSize * self.getTagCompound().getInteger("forgetemp") + quantityToStack * remote.getTagCompound().getInteger("forgetemp")) / (self.stackSize + quantityToStack);
			
			self.stackSize += quantityToStack;
			remote.stackSize -= quantityToStack;
			
			self.getTagCompound().setInteger("forgetemp", newtemp);
			
			return true;
		}
		
		return false;
	}

	
	// Can only work ingots on same tier or higher anvil
	@Override
	public boolean workableOn(int anviltier, ItemStack itemstack, ItemStack itemstackoptional) {
		return
			getMetal(itemstack).tier <= anviltier + 1 &&
			getTemperatureM10(itemstack)/10 >= getMetal(itemstack).getMinWorkableTemperature();
	}

	

	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (world.isSideSolid(pos.offset(side.getOpposite()), side)) {
			String statename = getMetal(stack).getStateName().toLowerCase(Locale.ROOT) + "-" + side.getOpposite().getName().charAt(0);
			
			IBlockState state = BlocksVC.metalplate.getBlockStateFor(statename);
//			System.out.println(statename);
			
			if (state == null) return false;
			
	        if (!world.setBlockState(pos, state, 3)) return false;
	
	        state = world.getBlockState(pos);
	        if (state.getBlock() == this.block) {
	            this.block.onBlockPlacedBy(world, pos, state, player, stack);
	        }
	
	        return true;
		}
		
		return false;
    }
	
	
	// Useless extra code because Java is not able to inherit static methods... -.-
	@Override
	public ItemStack setItemMetal(ItemStack itemstack, EnumMetal metal) {
		return BlocksVC.metalplate.getItemStackFor(metal);
	}

	@Override
	public EnumMetal getItemMetal(ItemStack itemstack) {
		return getMetal(itemstack);
	}

}

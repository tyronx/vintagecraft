package at.tyron.vintagecraft.Item;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.AnvilRecipes;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Interfaces.ISmithable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;

public class ItemMetalPlate extends ItemVC implements ISubtypeFromStackPovider, ISmithable, IItemHeatable {

	public ItemMetalPlate() {
        this.setHasSubtypes(true);
        setCreativeTab(VintageCraft.resourcesTab);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMetal metal : EnumMetal.values()) {
    		if (!metal.hasArmor) continue;
    		
    		stack = new ItemStack(ItemsVC.metalplate);
    		setMetal(stack, metal);
    		subItems.add(stack);
    	}
    }
    
    
    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
    	int tmp = getTemperature(stack) / 6;
    	
    	//System.out.println(g + "/" + b);
    	
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
		int forgetemp = getTemperature(itemstack);
		
		if (forgetemp > 0) {
			String workable = "";
			if (forgetemp >= getMetal(itemstack).getMinWorkableTemperature()) workable = "  | WORKABLE";
			tooltip.add("Temperature: " + forgetemp + " deg." + workable);
		}
		
		if (getAppliedAnvilTechniques(itemstack).length > 0) {
			tooltip.add("Has been worked");
		}
				
		updateTemperature(itemstack, playerIn.worldObj);
	}


	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getMetal(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return super.getUnlocalizedName() + "." + getMetal(stack).getName() + (isOddlyShaped(stack) ? ".oddlyshaped" : "");
	}
	
	
	
	public static EnumMetal getMetal(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMetal.byId(itemstack.getTagCompound().getInteger("metal"));
		}
		return null;
	}

	
	
	
	public static ItemStack setMetal(ItemStack itemstack, EnumMetal metal) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		nbt.setInteger("metal", metal.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}

	public static ItemStack getItemStack(EnumMetal metal, int quantity) {
		return setMetal(new ItemStack(ItemsVC.metalplate, quantity), metal);
	}
	
	
	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName();
	}


	
	@Override
	public boolean isSmithingIngredient(ItemStack itemstack, ItemStack comparison, AnvilRecipes forrecipe) {
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
			getTemperature(itemstack) >= getMetal(itemstack).getMinWorkableTemperature();
	}

}

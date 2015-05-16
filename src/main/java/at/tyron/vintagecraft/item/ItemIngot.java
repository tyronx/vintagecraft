package at.tyron.vintagecraft.Item;

import java.util.List;

import scala.actors.threadpool.Arrays;
import at.tyron.vintagecraft.AnvilRecipes;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Utility.BlockIngotPile;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Interfaces.ISmithable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIngot extends ItemVC implements ISubtypeFromStackPovider, ISizedItem, ISmithable, IItemHeatable {
	public static final int maxstacksize = 64;
	public static final int maxpilesize = 64;
	
	public ItemIngot() {
        this.setHasSubtypes(true);
        setCreativeTab(VintageCraft.resourcesTab);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMetal metal : EnumMetal.values()) {
    		stack = new ItemStack(ItemsVC.metalingot);
    		setMetal(stack, metal);
    		subItems.add(stack);
    	}
    }
    
    

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getMetal(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return super.getUnlocalizedName() + "." + getMetal(stack).getName() + (isOddlyShaped(stack) ? ".oddlyshaped" : "");
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
		
		tooltip.add("Melting Point: " + getMetal(itemstack).meltingpoint + " deg.");
		
		updateTemperature(itemstack, playerIn.worldObj);
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
		return setMetal(new ItemStack(ItemsVC.metalingot, quantity), metal);
	}

	
	

	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName();
	}
	

	
	public boolean isPlaceable(ItemStack is) {
		return !isOddlyShaped(is) && getTemperature(is) < 500;
	}
	
	
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		updateTemperature(stack, worldIn);
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ)) return true;
		
		if (!entityplayer.isSneaking() || !isPlaceable(itemstack)) return false;
		
		boolean ingotPileAtPos = world.getBlockState(pos).getBlock() instanceof BlockIngotPile;
		
		if (!ingotPileAtPos) {
			return BlockIngotPile.tryCreatePile(itemstack, world, pos.offset(side));
		} else {
			BlocksVC.ingotPile.onBlockActivated(world, pos, world.getBlockState(pos), entityplayer, side, hitX, hitY, hitZ);
		}
		
		return false;
	}


	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.SMALL;
	}


	// Can only work ingots on same tier or higher anvil
	@Override
	public boolean workableOn(int anviltier, ItemStack itemstack, ItemStack itemstackoptional) {
		return
			getMetal(itemstack).tier <= anviltier + 1 &&
			getTemperature(itemstack) >= getMetal(itemstack).getMinWorkableTemperature();
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


	// Items taken from a forge have a grace timer when they start cooling and 
	// this timer will be shorter if the player already has taken an ingot that is not yet cooling, 
	// so that these may stack together 
	@Override
	public boolean canStackWith(World world, ItemStack self, ItemStack remote) {
		/*if (self != null && remote != null) {
			System.out.println(
				
				(self.getItem() == remote.getItem()) + " && " +
				(getMetal(self) == getMetal(remote)) + " && " +
				Math.abs(self.getTagCompound().getInteger("forgetemp") - remote.getTagCompound().getInteger("forgetemp"))
			);
		}*/
		
		
		return 
			self != null &&
			remote != null &&
			self.getItem() == remote.getItem() &&
			getMetal(self) == getMetal(remote) &&
			isOddlyShaped(self) == isOddlyShaped(remote) &&
/*			world.getWorldTime() < self.getTagCompound().getLong("startcoolingat") &&
			world.getWorldTime() < remote.getTagCompound().getLong("startcoolingat") &&*/
			(Math.abs(self.getTagCompound().getInteger("forgetemp") - remote.getTagCompound().getInteger("forgetemp")) < 800)
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

}



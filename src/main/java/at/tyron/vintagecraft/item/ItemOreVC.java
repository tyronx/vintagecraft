package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Organic.BlockSaplingVC;
import at.tyron.vintagecraft.Block.Utility.BlockFirepit;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemOreVC extends ItemVC implements ISubtypeFromStackPovider, IItemFuel, IItemSmeltable, ISizedItem {

	public ItemOreVC() {
        this.setHasSubtypes(true);
        setCreativeTab(VintageCraft.resourcesTab);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumOreType oretype : EnumOreType.values()) {
    		stack = new ItemStack(ItemsVC.ore);
    		setOreType(stack, oretype);
    		subItems.add(stack);
    	}
    }

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getOreType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return super.getUnlocalizedName() + "." + getOreType(stack).getName();
	}
	
	
	public static EnumOreType getOreType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null && itemstack.getTagCompound().hasKey("oretype")) {
			return EnumOreType.byId(itemstack.getTagCompound().getInteger("oretype"));
		}
		return null;
	}
	
	public static ItemStack getItemStackFor(EnumOreType oretype, int quantity) {
		return setOreType(new ItemStack(ItemsVC.ore, quantity), oretype);
		
	}
	
	public static ItemStack setOreType(ItemStack itemstack, EnumOreType oretype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("oretype", oretype.getId());
		itemstack.setTagCompound(nbt);
		return itemstack;
	}


	@Override
	public String getSubType(ItemStack stack) {
		return getOreType(stack).getName();
	}


	@Override
	public int getBurningHeat(ItemStack stack) {
		if (getOreType(stack) == EnumOreType.LIGNITE) {
			return 1100;
		}
		if (getOreType(stack) == EnumOreType.BITUMINOUSCOAL) {
			return 1200;
		}
		return 0;
	}


	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		if (getOreType(stack) == EnumOreType.LIGNITE) {
			return 2.2f;
		}
		if (getOreType(stack) == EnumOreType.BITUMINOUSCOAL) {
			return 2.4f;
		}
		return 0;
	}
	
	
	@Override
	public boolean isForgeFuel(ItemStack stack) {
		return getOreType(stack) == EnumOreType.BITUMINOUSCOAL || getOreType(stack) == EnumOreType.LIGNITE;
	}	


	@Override
	public ItemStack getSmelted(ItemStack ore) {
		switch (getOreType(ore)) {
			case LIMONITE: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.IRON);
			case NATIVEGOLD_QUARTZ: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.GOLD);
			case NATIVECOPPER: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.COPPER);
			case CASSITERITE: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.TIN);
			case NATIVESILVER_QUARTZ: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.SILVER);
			case PLATINUM: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.PLATINUM);
			case RHODIUM: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.RHODIUM);
			case IRIDIUM: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.IRIDIUM);
			case SPHALERITE: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.ZINC);
			case BISMUTHINITE: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.BISMUTH);
			
			default:
				break; 
		}
		return null;
	}


	@Override
	public int getRaw2SmeltedRatio(ItemStack ore) {
		switch (getOreType(ore)) {
			case LIMONITE:
			case NATIVECOPPER: return 4;
			case NATIVEGOLD_QUARTZ: return 8;
			case CASSITERITE: return 8;				// "even a rich cassiterite ore only has 5% tin" @ http://en.wikipedia.org/wiki/Smelting
			case NATIVESILVER_QUARTZ: return 4;
			case PLATINUM: return 4;
			case RHODIUM: return 4;
			case IRIDIUM: return 4;
			case SPHALERITE: return 4;
			case BISMUTHINITE: return 4;
			default: break;
		
		}
		return 0;
	}
	
	
	@Override
	public int getMeltingPoint(ItemStack ore) {
		switch (getOreType(ore)) {
			case CASSITERITE: return EnumMetal.TIN.meltingpoint;
			case LIMONITE: return EnumMetal.IRON.meltingpoint;
			case NATIVECOPPER: return EnumMetal.COPPER.meltingpoint;
			case NATIVEGOLD_QUARTZ: return EnumMetal.GOLD.meltingpoint;
			case NATIVESILVER_QUARTZ: return EnumMetal.SILVER.meltingpoint;
			case BISMUTHINITE: return EnumMetal.BISMUTH.meltingpoint;
			case IRIDIUM: return EnumMetal.IRIDIUM.meltingpoint;
			case PLATINUM: return EnumMetal.PLATINUM.meltingpoint;
			case RHODIUM: return EnumMetal.RHODIUM.meltingpoint;
			case SPHALERITE: return EnumMetal.ZINC.meltingpoint;
			default: break;
		}
		return 0;
	}
	
	
	@Override
	public float getSmeltingSpeedModifier(ItemStack raw) {
		return 0.25f * getRaw2SmeltedRatio(raw);
	}

	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		EnumStrongHeatSource.addItemStackInformation(itemstack, tooltip);
	}


	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.SMALL;
	}


	@Override
	public int smokeLevel(ItemStack stack) {
		return 80;
	}

}

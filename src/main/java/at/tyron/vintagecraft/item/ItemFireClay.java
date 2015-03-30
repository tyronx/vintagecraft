package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemFireClay extends ItemVC implements ISmeltable {
	boolean bakeable;
	
	public ItemFireClay(boolean bakeable) {
		this.bakeable = bakeable;
		setCreativeTab(CreativeTabs.tabMaterials);
	}

	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (bakeable) return new ItemStack(ItemsVC.fireclay_brick);
		return null;
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		if (bakeable) return 1;
		return 0;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		if (bakeable) return 1200;
		return 0;
	}

	@Override
	public float getSmeltingSpeedModifier(ItemStack raw) {
		return 1f;
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		if (getMeltingPoint(itemstack) > 0) {
			tooltip.add("Baking temperature: " + getMeltingPoint(itemstack) + " deg.");
		}
	}
	
	
	
}

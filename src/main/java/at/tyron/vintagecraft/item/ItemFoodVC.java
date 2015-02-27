package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.interfaces.ISmeltable;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodVC extends ItemFood implements ISmeltable, ISubtypeFromStackPovider {
	String internalname;
	
	public ItemFoodVC(int amount, boolean isWolfFood) {
		super(amount, isWolfFood);
	}
	
    public ItemFoodVC(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

	public Item register(String internalname) {
		setUnlocalizedName(internalname);
		GameRegistry.registerItem(this, internalname);
		VintageCraft.instance.proxy.addVariantName(this, ModInfo.ModID + ":food/" + internalname);
		this.internalname = internalname;
		return this;
	}
    
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (internalname.equals("porkchopRaw")) {
			return new ItemStack(ItemsVC.porkchopCooked);
		}
		if (internalname.equals("beefRaw")) {
			return new ItemStack(ItemsVC.beefCooked);
		}
		if (internalname.equals("chickenRaw")) {
			return new ItemStack(ItemsVC.chickenCooked);
		}
		return null;
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		return 1;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		return 100;
	}
	
	@Override
	public String getSubType(ItemStack stack) {
		return internalname;
	}

	
}

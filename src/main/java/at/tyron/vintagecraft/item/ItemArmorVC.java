package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemArmorVC extends ItemArmor implements ISubtypeFromStackPovider {
	// 																										5		Vanilla Leather = 1, 3, 2, 1
	public static ArmorMaterial LEATHERVC = EnumHelper.addArmorMaterial("LEATHERVC", "leather", 2, new int[]{2, 3, 2, 2}, 0);
	public static ArmorMaterial COPPERVC = EnumHelper.addArmorMaterial("COPPERVC", "copper", 3, new int[]{2, 3, 2, 2}, 0);
	public static ArmorMaterial TINBRONZEVC = EnumHelper.addArmorMaterial("TINBRONZEVC", "tinbronze", 4, new int[]{3, 4, 3, 3}, 0);
	public static ArmorMaterial BISMUTHBRONZEVC = EnumHelper.addArmorMaterial("BISMUTHBRONZEVC", "bismuthbronze", 5, new int[]{2, 4, 3, 2}, 0);
	
	
	public ItemArmorVC(ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		setCreativeTab(VintageCraft.toolsarmorTab);
	}

	
	public static String []armorTypes = new String[]{"helmet", "chestplate", "leggings", "boots"}; 

	@Override
	public String getSubType(ItemStack stack) {
		return getArmorMaterial().getName() + "_" + armorTypes[armorType];
	}
	
}

package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemMetalTyped;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class ItemArmorVC extends ItemArmor implements ISubtypeFromStackPovider, IItemMetalTyped {
	// 																										5		Vanilla Leather = 1, 3, 2, 1
	public static ArmorMaterial LEATHERVC = EnumHelper.addArmorMaterial("LEATHERVC", "leather", 2, new int[]{2, 3, 2, 2}, 0);
	public static ArmorMaterial COPPERVC = EnumHelper.addArmorMaterial("COPPERVC", "copper", 3, new int[]{2, 3, 2, 2}, 0);
	public static ArmorMaterial TINBRONZEVC = EnumHelper.addArmorMaterial("TINBRONZEVC", "tinbronze", 4, new int[]{3, 4, 3, 3}, 0);
	public static ArmorMaterial BISMUTHBRONZEVC = EnumHelper.addArmorMaterial("BISMUTHBRONZEVC", "bismuthbronze", 5, new int[]{2, 4, 3, 2}, 0);
	public static ArmorMaterial IRONVC = EnumHelper.addArmorMaterial("IRONVC", "iron", 5, new int[]{4, 5, 4, 4}, 0);
	public static ArmorMaterial STEELVC = EnumHelper.addArmorMaterial("STEELVC", "steel", 5, new int[]{5, 6, 5, 5}, 0);
	
	
	public ItemArmorVC(ArmorMaterial material, String name, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		setCreativeTab(VintageCraft.toolsarmorTab);
		
		int durability = 50;
		
		if (name.equals("copper")) durability = 80;
		if (name.equals("tinbronze")) durability = 160;
		if (name.equals("bismuthbronze")) durability = 184; 
		if (name.equals("iron")) durability = 320; 
		if (name.equals("steel")) durability = 512;
		
		
		this.setMaxDamage(durability);
	}

	
	public static String []armorTypes = new String[]{"helmet", "chestplate", "leggings", "boots"}; 

	@Override
	public String getSubType(ItemStack stack) {
		return getArmorMaterial().getName() + "_" + armorTypes[armorType];
	}

	
	// Useless extra code because Java is not able to inherit static methods... -.-
	@Override
	public ItemStack setItemMetal(ItemStack itemstack, EnumMetal metal) {
		return new ItemStack(ItemsVC.armor.get(metal.getName() + "_" + armorTypes[armorType]));
	}

	@Override
	public EnumMetal getItemMetal(ItemStack itemstack) {
		return EnumMetal.byString(getArmorMaterial().getName());
	}

}

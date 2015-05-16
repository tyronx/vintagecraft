package at.tyron.vintagecraft.Item;

import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.WorldProperties.EnumTool;

public class ItemToolHead extends ItemVC implements ISubtypeFromStackPovider {
	String material;
	EnumTool tooltype;
	
	public ItemToolHead(EnumTool tooltype, String material) {
		this.tooltype = tooltype;
		this.material = material;
		setCreativeTab(VintageCraft.toolsarmorTab);
		maxStackSize = 1;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return material + "_" + tooltype.getName();
	}

}

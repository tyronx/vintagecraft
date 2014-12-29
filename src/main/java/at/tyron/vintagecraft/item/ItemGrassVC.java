package at.tyron.vintagecraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumGrass;

public class ItemGrassVC extends ItemBlock {

	public ItemGrassVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getGrassType(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName() + "." + getGrassType(stack).getName();
	}
	

	
	public static EnumGrass getGrassType(ItemStack itemstack) {
		return EnumGrass.fromMeta(itemstack.getItemDamage());
	}
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	
}

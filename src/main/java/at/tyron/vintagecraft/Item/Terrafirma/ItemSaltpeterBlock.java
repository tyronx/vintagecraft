package at.tyron.vintagecraft.Item.Terrafirma;

import at.tyron.vintagecraft.Item.ItemBlockVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemModelMesher;



public class ItemSaltpeterBlock extends ItemBlockVC {

	public ItemSaltpeterBlock(Block block) {
		super(block);
		
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Terrafirma;
	}

	
	
	@Override
	public String getUnlocalizedName() {
		return "saltpeter";
	}
	
}

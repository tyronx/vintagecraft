package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class ItemToolSteel extends ItemToolVC {
	
	public ItemToolSteel(EnumTool tooltype, boolean diamondencrusted) {
		super(tooltype, diamondencrusted);
	}


	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case SAW:
				if (material == Material.wood) return 6.8f;
				break;
			
			case AXE: 
				if (material == Material.wood) return 7.15f; 
				break;
			
			case PICKAXE: 
				if (material == Material.rock) return 5.4f; 
				if (material == Material.ground) return 4f;
				if (material == Material.iron) return 2.7f;

				break;
			
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 7.15f;
				if (material == Material.sand) return 2.8f;
				break;
			
			
			case SWORD:
				if (material == Material.leaves) return 2.6f;
				break;
		
			default: break;
			
		}
		
		return 0.5f;
	}

	
	@Override
	public int getHarvestLevel() {
		return 5;
	}

	@Override
	public int getMaxUses() {
		if (tooltype == EnumTool.SHEARS) return 2534;
		return 1152;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 6f;
		}
		if (tooltype == EnumTool.AXE) {
			return 5.5f;
		}

		return 3.5f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "steel_" + tooltype.getName() + getVariant(stack);
	}
}

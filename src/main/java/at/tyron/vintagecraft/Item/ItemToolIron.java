package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class ItemToolIron extends ItemToolVC {
	
	public ItemToolIron(EnumTool tooltype, boolean diamondencrusted) {
		super(tooltype, diamondencrusted);
	}


	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case SAW:
				if (material == Material.wood) return 5.4f;
				break;
			
			case AXE: 
				if (material == Material.wood) return 5.5f; 
				break;
			
			case PICKAXE: 
				if (material == Material.rock) return 4f; 
				if (material == Material.ground) return 2.7f;
				if (material == Material.iron) return 2f;

				break;
			
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 5.5f;
				if (material == Material.sand) return 2f;
				break;
			
			
			case SWORD:
				if (material == Material.leaves) return 2.8f;
				break;
		
			default: break;
			
		}
		
		return 0.5f;
	}

	
	@Override
	public int getHarvestLevel() {
		return 4;
	}

	@Override
	public int getMaxUses() {
		if (tooltype == EnumTool.SHEARS) return 1584;
		return 720;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 5f;
		}
		if (tooltype == EnumTool.AXE) {
			return 4.5f;
		}

		return 2.5f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "iron_" + tooltype.getName() + getVariant(stack);
	}
	
    int quantityBonusBlockBreaks() {
    	if (tooltype == EnumTool.SHEARS) return 5;
    	if (tooltype == EnumTool.SICKLE) return 2;
    	return 0;
    }

}

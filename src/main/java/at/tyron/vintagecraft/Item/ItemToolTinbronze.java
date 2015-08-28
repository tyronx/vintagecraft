package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class ItemToolTinbronze extends ItemToolVC {

	public ItemToolTinbronze(EnumTool tooltype, boolean diamondencrusted) {
		super(tooltype, diamondencrusted);
	}


	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case SAW: 
				if (material == Material.wood) return 4.4f; 
				break;
			
			case AXE: 
				if (material == Material.wood) return 4.5f; 
				break;
			//case CHISEL:
			case PICKAXE: 
				if (material == Material.rock) return 3f; 
				if (material == Material.ground) return 2f;
				if (material == Material.iron) return 1f;

				break;
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 4.7f;
				if (material == Material.sand) return 2f;
				break;
			case SWORD:
				if (material == Material.leaves) return 2.7f;
				break;
		
			default: break;
			
		}
		
		return 0.5f;
	}

	
	@Override
	public int getHarvestLevel() {
		return 3;
	}

	@Override
	public int getMaxUses() {
		if (tooltype == EnumTool.SHEARS) return 880;
		return 400;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 4f;
		}
		if (tooltype == EnumTool.AXE) {
			return 3.5f;
		}

		return 2f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "tinbronze_" + tooltype.getName() + getVariant(stack);
	}


}

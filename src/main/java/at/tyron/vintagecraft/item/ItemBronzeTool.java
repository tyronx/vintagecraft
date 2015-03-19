package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class ItemBronzeTool extends ItemToolVC {

	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
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
		return 400;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 4f;
		}
		return 1f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "bronze_" + tooltype.getName();
	}


}

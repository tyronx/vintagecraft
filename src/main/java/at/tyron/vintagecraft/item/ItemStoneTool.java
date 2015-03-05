package at.tyron.vintagecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;

public class ItemStoneTool extends ItemToolVC {
	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case AXE: 
				if (material == Material.wood) return 3f; 
				break;
			//case CHISEL:
			case PICKAXE: 
				if (material == Material.rock) return 2f; 
				if (material == Material.ground) return 1f;  
				if (material == Material.iron) return 0.6f;
				break;
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 3f;  
				break;
			case SWORD:
				if (material == Material.leaves) return 2f;
				break;
		
			default: break;
			
		}
		
		return 0.5f;
	}

	
	@Override
	public int getHarvestLevel() {
		return 1;
	}

	@Override
	public int getMaxUses() {
		return 50;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 2f;
		}
		return 1f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "stone_" + tooltype.getName();
	}
	

}

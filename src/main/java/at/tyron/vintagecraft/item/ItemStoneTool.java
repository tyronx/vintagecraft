package at.tyron.vintagecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.interfaces.ItemToolVC;

public class ItemStoneTool extends ItemToolVC {
	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case AXE: if (material == Material.wood) return 3f; break;
			//case CHISEL:
			case PICKAXE: if (material == Material.rock) return 3f; if (material == Material.ground) return 2f;  break;
			case SHOVEL: if (material == Material.grass || material == Material.ground) return 3f;  break;
			case SWORD:  break;
		
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

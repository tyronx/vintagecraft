package at.tyron.vintagecraft.item;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.WorldProperties.EnumTool;

public class ItemToolBismuthBronze extends ItemToolTinBronze {

	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case AXE: 
				if (material == Material.wood) return 4.3f; 
				break;
			//case CHISEL:
			case PICKAXE: 
				if (material == Material.rock) return 2.8f; 
				if (material == Material.ground) return 1.8f;
				if (material == Material.iron) return 0.9f;

				break;
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 4.4f;  
				break;
			case SWORD:
				if (material == Material.leaves) return 2.6f;
				break;
		
			default: break;
			
		}
		
		return 0.5f;
	}
	
	@Override
	public int getMaxUses() {
		if (tooltype == EnumTool.SHEARS) return 920;
		return 460;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 3.5f;
		}
		return 1.5f;
	}


	@Override
	public String getSubType(ItemStack stack) {
		return "bismuthbronze_" + tooltype.getName();
	}

}

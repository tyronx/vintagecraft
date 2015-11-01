package at.tyron.vintagecraft.Item.Metalworking;

import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class ItemToolStone extends ItemToolVC {
	public ItemToolStone(EnumTool tooltype, boolean diamondencrusted) {
		super(tooltype, false);
	}


	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case AXE: 
				if (material == Material.wood) return 3f; 
				break;
			case PICKAXE: 
				if (material == Material.rock) return 2f; 
				if (material == Material.ground) return 1f;  
				if (material == Material.iron) return 0.6f;
				break;
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 3f;
				if (material == Material.sand) return 1f;
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
		if (tooltype == EnumTool.HOE) {
			return 30;
		}
		if (tooltype == EnumTool.HAMMER) {
			return 22;
		}
		return 45;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 2f;
		}
		if (tooltype == EnumTool.AXE) {
			return 1.5f;
		}
		return 1f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "stone_" + tooltype.getName();
	}
	

}

package at.tyron.vintagecraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;

public class ItemCopperTool extends ItemToolVC {
	@Override
	public float getEfficiencyOnMaterial(ItemStack itemstack, Material material) {
		switch (tooltype) {
			case AXE: 
				if (material == Material.wood) return 4f; 
				break;
			//case CHISEL:
			case PICKAXE: 
				if (material == Material.rock) return 2.5f; 
				if (material == Material.ground) return 1.5f;
				if (material == Material.iron) return 0.8f;

				break;
			case SHOVEL: 
				if (material == Material.grass || material == Material.ground) return 4f;  
				break;
			case SWORD:
				if (material == Material.leaves) return 2.5f;
				break;
		
			default: break;
			
		}
		
		return 0.5f;
	}

	
	@Override
	public int getHarvestLevel() {
		return 2;
	}

	@Override
	public int getMaxUses() {
		return 220;
	}

	@Override
	public float getDamageGainOnEntities() {
		if (tooltype == EnumTool.SWORD) {
			return 3f;
		}
		return 1f;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return "copper_" + tooltype.getName();
	}


}

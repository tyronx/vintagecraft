package at.tyron.vintagecraft.World;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemIngot;
import at.tyron.vintagecraft.item.ItemOre;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemsVC {
	public static Item stone;
	public static Item ore;
	
	
	public static Item ingot;
	
	public static void init() {
		initItems();
		initIngots();
	}
	
	
	static void initItems() {
		stone = new ItemStone().register("stone");
		
		for (EnumRockType rocktype : EnumRockType.values()) {
			ModelBakery.addVariantName(stone, ModInfo.ModID + ":stone/" + rocktype.getStateName());	
		}
		
		ore = new ItemOre().register("ore");
		for (EnumMaterialDeposit oretype : EnumMaterialDeposit.values()) {
			if (oretype.hasOre) {
				ModelBakery.addVariantName(ore, ModInfo.ModID + ":ore/" + oretype.getName());
			}
		}	
	}
	
	
	static void initIngots() {
		ingot = new ItemIngot().register("ingot");
		
		for (EnumMetal metal : EnumMetal.values()) {
			ModelBakery.addVariantName(ingot, ModInfo.ModID + ":ingot/" + metal.name().toLowerCase());
		}
		
		
		ItemStack copperIngot = new ItemStack(ingot);
		ItemIngot.setMetal(copperIngot, EnumMetal.COPPER);
		
		ItemStack nativeCopperOre = new ItemStack(ore);
		ItemOre.setOreType(nativeCopperOre, EnumMaterialDeposit.NATIVECOPPER);
		
		GameRegistry.addSmelting(nativeCopperOre, copperIngot, 0);
	}
	

	
}


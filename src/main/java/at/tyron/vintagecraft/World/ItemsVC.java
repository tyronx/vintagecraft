package at.tyron.vintagecraft.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.item.ItemBronzeTool;
import at.tyron.vintagecraft.item.ItemCopperTool;
import at.tyron.vintagecraft.item.ItemFoodVC;
import at.tyron.vintagecraft.item.ItemIngot;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemPeatBrick;
import at.tyron.vintagecraft.item.ItemStone;
import at.tyron.vintagecraft.item.ItemStoneTool;
import at.tyron.vintagecraft.item.ItemToolVC;
import at.tyron.vintagecraft.item.ItemVessel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemsVC {
	public static Item stone;
	public static Item ore;
	public static Item ingot;
	public static Item peatbrick;

	
	
	public static Item bronzeAxe;
	public static Item bronzePickaxe;
	public static Item bronzeShovel;
	public static Item bronzeSword;
	public static Item bronzeHoe;
	public static Item bronzeSaw;

	
	public static Item copperAxe;
	public static Item copperPickaxe;
	public static Item copperShovel;
	public static Item copperSword;
	public static Item copperHoe;
	public static Item copperSaw;

	
	public static Item stoneAxe;
	public static Item stonePickaxe;
	public static Item stoneShovel;
	public static Item stoneSword;
	public static Item stoneHoe;

	
	public static Item porkchopRaw;
	public static Item porkchopCooked;
	public static Item beefRaw;
	public static Item beefCooked;
	
	public static Item chickenRaw;
	public static Item chickenCooked;
	
	public static Item wheatSeeds;
	public static Item ceramicVessel;
	public static Item clayVessel;
	
	
	public static void init() {
		initItems();
		initIngots();
	}
	
	
	static void initItems() {
		stone = new ItemStone().register("stone");
		
		for (EnumRockType rocktype : EnumRockType.values()) {
			//ModelBakery.addVariantName(stone, ModInfo.ModID + ":stone/" + rocktype.getStateName());	
			VintageCraft.instance.proxy.addVariantName(stone, ModInfo.ModID + ":stone/" + rocktype.getStateName());
		}
		
		ore = new ItemOreVC().register("ore");
		for (EnumMaterialDeposit oretype : EnumMaterialDeposit.values()) {
			if (oretype.hasOre) {
				VintageCraft.instance.proxy.addVariantName(ore, ModInfo.ModID + ":ore/" + oretype.getName());
			}
		}
		
		peatbrick = new ItemPeatBrick().register("peatbrick");
		ceramicVessel = new ItemVessel(true).register("ceramicvessel");
		clayVessel = new ItemVessel(false).register("clayvessel");
		
		registerTools("bronze", ItemBronzeTool.class);
		registerTools("copper", ItemCopperTool.class);
		registerTools("stone", ItemStoneTool.class);
		
		porkchopRaw = new ItemFoodVC(3, 0.3f, true).register("porkchopRaw");
		porkchopCooked = new ItemFoodVC(8, 0.8f, true).register("porkchopCooked");
		beefRaw = new ItemFoodVC(3, 0.3f, true).register("beefRaw");
		beefCooked = new ItemFoodVC(8, 0.8f, true).register("beefCooked");
		chickenRaw = new ItemFoodVC(2, 0.3f, true).register("chickenRaw");
		chickenCooked = new ItemFoodVC(9, 0.6f, true).register("chickenCooked");
		
		wheatSeeds = new ItemSeeds(BlocksVC.wheatcrops, BlocksVC.farmland);
		register(wheatSeeds, "wheatseeds");
	}
	
	public static Item register(Item item, String internalname) {
		item.setUnlocalizedName(internalname);
		GameRegistry.registerItem(item, internalname);
		VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":" + internalname);
		return item;
	}
	
	
	static void registerTools(String metal, Class<? extends ItemToolVC> theclass) {
		
		
		for (EnumTool tool : EnumTool.values()) {
			if (metal.equals("stone") && !tool.canBeMadeFromStone) continue;
			
			String unlocalizedname = metal + "_" + tool.getName();
			String toolnamecode = metal + ucFirst(tool.getName());
			
			try {
				Field field = ItemsVC.class.getField(toolnamecode);
				field.set(ItemsVC.class, theclass.newInstance());
				ItemToolVC item = (ItemToolVC)field.get(null);
				
				item.tooltype = tool;
				
				item.setUnlocalizedName(unlocalizedname);
				GameRegistry.registerItem(item, unlocalizedname);
				
				VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":tool/" + unlocalizedname);
				
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
			
		}
		
	}
	
	static String ucFirst(String word) {
		word = word.toLowerCase();
		return Character.toString(word.charAt(0)).toUpperCase()+word.substring(1);
	}
	
	
	static void initIngots() {
		ingot = new ItemIngot().register("ingot");
		
		for (EnumMetal metal : EnumMetal.values()) {
			VintageCraft.instance.proxy.addVariantName(ingot, ModInfo.ModID + ":ingot/" + metal.name().toLowerCase());
		}
		
		
		ItemStack copperIngot = new ItemStack(ingot);
		ItemIngot.setMetal(copperIngot, EnumMetal.COPPER);
		
		ItemStack nativeCopperOre = new ItemStack(ore);
		ItemOreVC.setOreType(nativeCopperOre, EnumOreType.NATIVECOPPER);
		
		GameRegistry.addSmelting(nativeCopperOre, copperIngot, 0);
	}
	

	
}


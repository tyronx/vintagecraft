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
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.item.ItemArmorVC;
import at.tyron.vintagecraft.item.ItemFireClay;
import at.tyron.vintagecraft.item.ItemToolBismuthBronze;
import at.tyron.vintagecraft.item.ItemToolRack;
import at.tyron.vintagecraft.item.ItemToolTinBronze;
import at.tyron.vintagecraft.item.ItemToolCopper;
import at.tyron.vintagecraft.item.ItemFoodVC;
import at.tyron.vintagecraft.item.ItemIngot;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemPeatBrick;
import at.tyron.vintagecraft.item.ItemStone;
import at.tyron.vintagecraft.item.ItemToolStone;
import at.tyron.vintagecraft.item.ItemToolVC;
import at.tyron.vintagecraft.item.ItemVessel;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemsVC {
	public static Item stone;
	public static Item ore;
	public static Item ingot;
	public static Item peatbrick;
	public static Item fireclay_ball;
	public static Item fireclay_brick_raw;
	public static Item fireclay_brick;
	
	
	public static Item bismuthbronzeAxe;
	public static Item bismuthbronzePickaxe;
	public static Item bismuthbronzeShovel;
	public static Item bismuthbronzeSword;
	public static Item bismuthbronzeHoe;
	public static Item bismuthbronzeSaw;
	public static Item bismuthbronzeShears;
	
	public static Item bismuthbronzeHelmet;
	public static Item bismuthbronzeChestplate;
	public static Item bismuthbronzeLeggings;
	public static Item bismuthbronzeBoots;


	public static Item tinbronzeAxe;
	public static Item tinbronzePickaxe;
	public static Item tinbronzeShovel;
	public static Item tinbronzeSword;
	public static Item tinbronzeHoe;
	public static Item tinbronzeSaw;
	public static Item tinbronzeShears;

	public static Item tinbronzeHelmet;
	public static Item tinbronzeChestplate;
	public static Item tinbronzeLeggings;
	public static Item tinbronzeBoots;

	
	public static Item copperAxe;
	public static Item copperPickaxe;
	public static Item copperShovel;
	public static Item copperSword;
	public static Item copperHoe;
	public static Item copperSaw;
	public static Item copperShears;

	public static Item copperHelmet;
	public static Item copperChestplate;
	public static Item copperLeggings;
	public static Item copperBoots;
	
	
	public static Item stoneAxe;
	public static Item stonePickaxe;
	public static Item stoneShovel;
	public static Item stoneSword;
	public static Item stoneHoe;
	public static Item stoneShears;

	
	public static Item porkchopRaw;
	public static Item porkchopCooked;
	public static Item beefRaw;
	public static Item beefCooked;
	
	public static Item chickenRaw;
	public static Item chickenCooked;
	
	public static Item wheatSeeds;
	public static Item ceramicVessel;
	public static Item clayVessel;
	
	//public static Item toolrack;
	
	
	public static void init() {
		initItems();
		initIngots();
	}
	
	
	static void initItems() {
		fireclay_ball = new ItemFireClay(false).register("fireclay_ball");
		fireclay_brick_raw = new ItemFireClay(true).register("fireclay_brick_raw");
		fireclay_brick = new ItemFireClay(true).register("fireclay_brick");
		
		stone = new ItemStone().register("stone");
		for (EnumRockType rocktype : EnumRockType.values()) {
			VintageCraft.instance.proxy.addVariantName(stone, ModInfo.ModID + ":stone/" + rocktype.getStateName());
		}
		
		
	/*	toolrack = new ItemToolRack().register("toolrackitem");
		for (EnumTree treetype : EnumTree.values()) {
			VintageCraft.instance.proxy.addVariantName(toolrack, ModInfo.ModID + ":toolrackitem/" + treetype.getStateName());
		}*/
		
		
		
		ore = new ItemOreVC().register("ore");
		for (EnumMaterialDeposit oretype : EnumMaterialDeposit.values()) {
			if (oretype.dropsOre) {
				VintageCraft.instance.proxy.addVariantName(ore, ModInfo.ModID + ":ore/" + oretype.getName());
			}
		}
		
		peatbrick = new ItemPeatBrick().register("peatbrick");
		ceramicVessel = new ItemVessel(true).register("ceramicvessel");
		clayVessel = new ItemVessel(false).register("clayvessel");
		
		registerTools("bismuthbronze", ItemToolBismuthBronze.class);
		registerTools("tinbronze", ItemToolTinBronze.class);
		registerTools("copper", ItemToolCopper.class);
		registerTools("stone", ItemToolStone.class);
		
		registerArmor("copper", ItemArmorVC.class);
		registerArmor("tinbronze", ItemArmorVC.class);
		registerArmor("bismuthbronze", ItemArmorVC.class);
		
		
		porkchopRaw = new ItemFoodVC(3, 0.3f, true).register("porkchopRaw");
		porkchopCooked = new ItemFoodVC(8, 0.8f, true).register("porkchopCooked");
		beefRaw = new ItemFoodVC(3, 0.3f, true).register("beefRaw");
		beefCooked = new ItemFoodVC(8, 0.8f, true).register("beefCooked");
		chickenRaw = new ItemFoodVC(2, 0.3f, true).register("chickenRaw");
		chickenCooked = new ItemFoodVC(9, 0.6f, true).register("chickenCooked");
		
		wheatSeeds = new ItemSeeds(BlocksVC.wheatcrops, BlocksVC.farmland);
		register(wheatSeeds, "wheatseeds");
		
	//	toolrack = new ItemToolRack();
	//	register(toolrack, "toolrack");
	}
	
	public static Item register(Item item, String internalname) {
		item.setUnlocalizedName(internalname);
		GameRegistry.registerItem(item, internalname);
		VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":" + internalname);
		return item;
	}
	
	
	
	
	static void registerArmor(String metal, Class<? extends ItemArmorVC> theclass) {
		for (int i = 0; i < ItemArmorVC.armorTypes.length; i++) {
			String armorPiece = ItemArmorVC.armorTypes[i];
			String unlocalizedname = metal + "_" + armorPiece;
			String armornamecode = metal + ucFirst(armorPiece);
			
			try {
				Field field = ItemsVC.class.getField(armornamecode);
				
				ArmorMaterial armormat = (ArmorMaterial)ItemArmorVC.class.getField(metal.toUpperCase()+"VC").get(null);
				
				field.set(ItemsVC.class, theclass.getDeclaredConstructor(ArmorMaterial.class, int.class, int.class).newInstance(armormat, 0, i));
				ItemArmorVC item = (ItemArmorVC)field.get(null);

				item.setUnlocalizedName(unlocalizedname);
				GameRegistry.registerItem(item, unlocalizedname);
				VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":armor/" + unlocalizedname);
								
			} catch (Exception e) {
				System.out.println(e.toString());
				e.printStackTrace();
			}
			
		}
		
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


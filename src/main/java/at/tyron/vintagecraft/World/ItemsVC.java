package at.tyron.vintagecraft.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import at.tyron.vintagecraft.CreativeTabsVC;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Item.ItemArmorVC;
import at.tyron.vintagecraft.Item.ItemClayVessel;
import at.tyron.vintagecraft.Item.ItemFireClay;
import at.tyron.vintagecraft.Item.ItemFoodVC;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemPeatBrick;
import at.tyron.vintagecraft.Item.ItemSeedVC;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.Item.ItemToolBismuthBronze;
import at.tyron.vintagecraft.Item.ItemToolCopper;
import at.tyron.vintagecraft.Item.ItemToolIron;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.Item.ItemToolStone;
import at.tyron.vintagecraft.Item.ItemToolTinBronze;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
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
	
	
	public static Item ironAxe;
	public static Item ironPickaxe;
	public static Item ironShovel;
	public static Item ironSword;
	public static Item ironHoe;
	public static Item ironSaw;
	public static Item ironShears;

	
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

	
	public static void init() {
		initItems();
		initIngots();
		initTabIcons();
	}
	
	
	private static void initTabIcons() {
		VintageCraft.terrainTab.icon = Item.getItemFromBlock(BlocksVC.topsoil);
		VintageCraft.floraTab.icon = Item.getItemFromBlock(BlocksVC.flower.getBlockStateFor(EnumFlower.CATMINT).getBlock());
		VintageCraft.resourcesTab.icon = ItemsVC.stone;
		VintageCraft.craftedBlocksTab.icon = Item.getItemFromBlock(BlocksVC.fireclaybricks);
		VintageCraft.toolsarmorTab.icon = ItemsVC.copperSaw;
	}


	static void initItems() {
		fireclay_ball = new ItemFireClay(false).register("fireclay_ball");
		fireclay_brick_raw = new ItemFireClay(true).register("fireclay_brick_raw");
		fireclay_brick = new ItemFireClay(true).register("fireclay_brick");
		
		stone = new ItemStone().register("stone");
		for (EnumRockType rocktype : EnumRockType.values()) {
			VintageCraft.instance.proxy.addVariantName(stone, ModInfo.ModID + ":stone/" + rocktype.getStateName());
		}
		
		ore = new ItemOreVC().register("ore");
		for (EnumOreType oretype : EnumOreType.values()) {
			VintageCraft.instance.proxy.addVariantName(ore, ModInfo.ModID + ":ore/" + oretype.getName());
		}
		
		peatbrick = new ItemPeatBrick().register("peatbrick");
		
		registerTools("iron", ItemToolIron.class);
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
		
		wheatSeeds = new ItemSeedVC();
		register(wheatSeeds, "wheatseeds");
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
				
				ItemToolVC item = theclass.getDeclaredConstructor(EnumTool.class).newInstance(tool);
				
				field.set(ItemsVC.class, item);
			
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


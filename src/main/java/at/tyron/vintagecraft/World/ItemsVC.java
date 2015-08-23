package at.tyron.vintagecraft.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import at.tyron.vintagecraft.CreativeTabsVC;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Item.*;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.Terrain.*;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ItemsVC {
	public static Item stone;
	
	public static Item ore;
	public static Item metalingot;
	
	public static Item peatbrick;
	public static Item fireclay_ball;
	public static Item fireclay_brick_raw;
	public static Item fireclay_brick;
	
	
	public static HashMap<String, ItemToolVC> tools = new HashMap<String, ItemToolVC>();
	public static HashMap<String, ItemArmorVC> armor = new HashMap<String, ItemArmorVC>();
	public static HashMap<String, Item> toolheads = new HashMap<String, Item>();
	

	public static Item dryGrass;
	public static Item flaxFibers;
	public static Item flaxTwine;
	public static Item linenCloth;
	public static Item stitchedleather;
	public static Item firestarter;
	
	public static Item porkchopRaw;
	public static Item porkchopCooked;
	public static Item beefRaw;
	public static Item beefCooked;
	
	public static Item chickenRaw;
	public static Item chickenCooked;
	
	public static Item seeds;
	
	public static Item anvilbase;
	public static Item anvilsurface;
	public static Item ironTuyere;
	
	public static Item sail;

	//public static Item coke;
	

	
	public static void init() {
		initItems();
		initIngots();
		

		// Really messes with vanilla recipes, e.g. creates a vanilla iron sword with stick+2 copper ingots
		// Don't want to repair this right now
		initOreDictItems();
	}
	
	
	private static void initOreDictItems() {
		for (EnumMetal metal : EnumMetal.values()) {
			OreDictionary.registerOre("vcraft-ingot" + metal.getNameUcFirst(), ItemIngot.getItemStack(metal, 1));			
		}
		
		for (EnumOreType oretype : EnumOreType.values()) {
			OreDictionary.registerOre("vcraft-ore" + oretype.getNameUcFirst(), ItemOreVC.getItemStackFor(oretype, 1));
		}
		
		for (EnumRockType rocktype : EnumRockType.values()) {
			OreDictionary.registerOre("vcraft-stone" + rocktype.getNameUcFirst(), ItemStone.getItemStackFor(rocktype, 1));
			OreDictionary.registerOre("vcraft-stone-any", ItemStone.getItemStackFor(rocktype, 1));
		}
		
		
	}


	public static void initTabIcons() {
		VintageCraft.terrainTab.icon = Item.getItemFromBlock(BlocksVC.topsoil);
		VintageCraft.floraTab.icon = Item.getItemFromBlock(BlocksVC.flower.getBlockStateFor(EnumFlower.CATMINT).getBlock());
		VintageCraft.resourcesTab.icon = ItemsVC.stone;
		VintageCraft.craftedBlocksTab.icon = Item.getItemFromBlock(BlocksVC.fireclaybricks);
		VintageCraft.toolsarmorTab.icon = tools.get("copper_saw");
		VintageCraft.mechanicsTab.icon = Item.getItemFromBlock(BlocksVC.axle);
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
		
		registerAnvilParts();
		registerTools();
		registerToolHeads();
		registerArmor();
		
		
		
		porkchopRaw = new ItemFoodVC(3, 0.3f, true).register("porkchopRaw");
		porkchopCooked = new ItemFoodVC(8, 0.8f, true).register("porkchopCooked");
		beefRaw = new ItemFoodVC(3, 0.3f, true).register("beefRaw");
		beefCooked = new ItemFoodVC(8, 0.8f, true).register("beefCooked");
		chickenRaw = new ItemFoodVC(2, 0.3f, true).register("chickenRaw");
		chickenCooked = new ItemFoodVC(9, 0.6f, true).register("chickenCooked");
		
		seeds = new ItemSeedVC();
		register(seeds, "seeds");
		for (EnumCrop croptype : EnumCrop.values()) {
			VintageCraft.instance.proxy.addVariantName(stone, ModInfo.ModID + ":seeds/" + croptype.getStateName());
		}
		
		dryGrass = new ItemDryGrass();
		register(dryGrass, "drygrass");
		
		flaxFibers = new ItemCrafted();
		register(flaxFibers, "flaxfibers");
		flaxTwine = new ItemCrafted();
		register(flaxTwine, "flaxtwine");
		linenCloth = new ItemCrafted();
		register(linenCloth, "linencloth");

		stitchedleather = new ItemCrafted();
		register(stitchedleather, "stitchedleather");
		
		
		ironTuyere = new ItemCrafted();
		register(ironTuyere, "irontuyere");
		
		firestarter = new ItemFireStarter();
		register(firestarter, "firestarter");
		
		sail = new ItemSail();
		register(sail, "sail");
		
//		coke = new ItemCoke();
//		register(coke, "coke");
	}
	
	
	public static Item register(Item item, String internalname) {
		item.setUnlocalizedName(internalname);
		GameRegistry.registerItem(item, internalname);
		VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":" + internalname);
		return item;
	}
	
	
	
	
	static void registerAnvilParts() {
		anvilbase = new ItemAnvilPart(false).register("anvilbase");
		anvilsurface = new ItemAnvilPart(true).register("anvilsurface");
	
		for (EnumMetal metal : EnumMetal.values()) {
			if (metal.hasAnvil) {
				VintageCraft.instance.proxy.addVariantName(anvilbase, ModInfo.ModID + ":anvilbase/" + metal.name().toLowerCase(Locale.ROOT));
				VintageCraft.instance.proxy.addVariantName(anvilsurface, ModInfo.ModID + ":anvilsurface/" + metal.name().toLowerCase(Locale.ROOT));
			}
		}
	}
	
	
	
	
	static void registerArmor() {
		ArrayList<String> materials = new ArrayList<String>();
		
		for (int i = 0; i < ItemArmorVC.armorTypes.length; i++) {
			String armorpiece = ItemArmorVC.armorTypes[i];
			
			for (EnumMetal metal : EnumMetal.values()) {
				if (!metal.hasArmor) continue;
				
				String ucfirstmaterial = metal.getName().substring(0, 1).toUpperCase(Locale.ROOT) + metal.getName().substring(1);
				String unlocalizedname = metal.getName() + "_" + armorpiece;
				
				try {
					ArmorMaterial armormat = (ArmorMaterial)ItemArmorVC.class.getField(metal.getName().toUpperCase(Locale.ROOT)+"VC").get(null);
					
					ItemArmorVC item = ItemArmorVC.class.getDeclaredConstructor(ArmorMaterial.class, String.class, int.class, int.class).newInstance(armormat, metal.getName(), 0, i);
					item.setUnlocalizedName(unlocalizedname);
					GameRegistry.registerItem(item, unlocalizedname);
					
					VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":armor/" + unlocalizedname);
					
					armor.put(unlocalizedname, item);
					
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}		
			}
		}
	}
	
	static void registerTools() {
		ArrayList<String> materials = new ArrayList<String>();
		materials.add("stone");
		
		for (EnumMetal metal : EnumMetal.values()) {
			if (metal.hasTools) materials.add(metal.getName());
		}
		
		for (EnumTool tool : EnumTool.values()) {
			for (String material : materials) {
				if (material.equals("stone") && !tool.canBeMadeFromStone) continue;
				if (!tool.canBeMadeOf(material)) continue;
				
				String ucfirstmaterial = material.substring(0, 1).toUpperCase(Locale.ROOT) + material.substring(1);
				String unlocalizedname = material + "_" + tool.getName();
				
				try {
					Class<? extends ItemToolVC> toolclass = (Class<? extends ItemToolVC>) Class.forName("at.tyron.vintagecraft.Item.ItemTool" + ucfirstmaterial);
					ItemToolVC item = toolclass.getDeclaredConstructor(EnumTool.class).newInstance(tool);
					item.setUnlocalizedName(unlocalizedname);
					GameRegistry.registerItem(item, unlocalizedname);
					
					VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":tool/" + unlocalizedname);
					
					tools.put(unlocalizedname, item);
					
					
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}		
			}
		}
	}
	
	
	static void registerToolHeads() {
		ArrayList<String> materials = new ArrayList<String>();
		materials.add("stone");
		
		for (EnumMetal metal : EnumMetal.values()) {
			if (metal.hasTools) materials.add(metal.getName());
		}
		
		for (EnumTool tool : EnumTool.values()) {
			
			for (String material : materials) {
				if (material.equals("stone")) continue;
				if (!tool.canBeMadeOf(material)) continue;
				if (!tool.requiresWoodenHandle) continue;
				
				
				String ucfirstmaterial = material.substring(0, 1).toUpperCase(Locale.ROOT) + material.substring(1);
				String unlocalizedname = material + "_" + tool.getName()+"toolhead";
				
				try {
					Item item = new ItemToolHead(tool, material);
					item.setUnlocalizedName(unlocalizedname);
					GameRegistry.registerItem(item, unlocalizedname);
					
					VintageCraft.instance.proxy.addVariantName(item, ModInfo.ModID + ":toolhead/" + material + "_" + tool.getName());
					
					toolheads.put(unlocalizedname, item);
					
					
				} catch (Exception e) {
					System.out.println(e.toString());
					e.printStackTrace();
				}		
			}
		}
	}
	
	
	
	static String ucFirst(String word) {
		word = word.toLowerCase(Locale.ROOT);
		return Character.toString(word.charAt(0)).toUpperCase(Locale.ROOT)+word.substring(1);
	}
	
	
	static void initIngots() {
		metalingot = new ItemIngot().register("ingot");
		
		for (EnumMetal metal : EnumMetal.values()) {
			VintageCraft.instance.proxy.addVariantName(metalingot, ModInfo.ModID + ":ingot/" + metal.name().toLowerCase(Locale.ROOT));
		}
	}
	

	
	
	
	
	
	
	public static ItemToolVC[] getTools(String metal) {
		ArrayList<ItemToolVC> tools = new ArrayList<ItemToolVC>();
		
		for (EnumTool tool : EnumTool.values()) {
			ItemToolVC item = ItemsVC.tools.get(metal + "_" + tool.getName());
			if (item != null) {
				tools.add(item);
			}
			
		}
		
		return tools.toArray(new ItemToolVC[0]);
	}
	
	
	public static ItemArmorVC[] getArmorPieces(String metal) {
		return armor.values().toArray(new ItemArmorVC[0]);
	}
	
}


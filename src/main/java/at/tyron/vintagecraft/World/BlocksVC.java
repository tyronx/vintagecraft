package at.tyron.vintagecraft.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.*;
import at.tyron.vintagecraft.Block.Organic.BlockCropsVC;
import at.tyron.vintagecraft.Block.Organic.BlockDoubleWoodenSlab;
import at.tyron.vintagecraft.Block.Organic.BlockFarmlandVC;
import at.tyron.vintagecraft.Block.Organic.BlockFenceGateVC;
import at.tyron.vintagecraft.Block.Organic.BlockFenceVC;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesBranchy;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.Block.Organic.BlockPeat;
import at.tyron.vintagecraft.Block.Organic.BlockPlanksVC;
import at.tyron.vintagecraft.Block.Organic.BlockSaplingVC;
import at.tyron.vintagecraft.Block.Organic.BlockSingleWoodenSlab;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrass;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Block.Organic.BlockVineVC;
import at.tyron.vintagecraft.Block.Utility.BlockBloomeryBase;
import at.tyron.vintagecraft.Block.Utility.BlockBloomeryChimney;
import at.tyron.vintagecraft.Block.Utility.BlockCeramicVessel;
import at.tyron.vintagecraft.Block.Utility.BlockClayVessel;
import at.tyron.vintagecraft.Block.Utility.BlockFirepit;
import at.tyron.vintagecraft.Block.Utility.BlockIngotPile;
import at.tyron.vintagecraft.Block.Utility.BlockStove;
import at.tyron.vintagecraft.Block.Utility.BlockToolRack;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.FlowerClass;
import at.tyron.vintagecraft.BlockClass.OreClass;
import at.tyron.vintagecraft.BlockClass.RockClass;
import at.tyron.vintagecraft.BlockClass.SoilRockClass;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Client.VCraftModelLoader;
import at.tyron.vintagecraft.Client.Render.TESR.TESRIngotPile;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemBrick;
import at.tyron.vintagecraft.Item.ItemCeramicVessel;
import at.tyron.vintagecraft.Item.ItemClayVessel;
import at.tyron.vintagecraft.Item.ItemDoubleFlower;
import at.tyron.vintagecraft.Item.ItemFarmLand;
import at.tyron.vintagecraft.Item.ItemFlowerVC;
import at.tyron.vintagecraft.Item.ItemGrassVC;
import at.tyron.vintagecraft.Item.ItemLeaves;
import at.tyron.vintagecraft.Item.ItemLeavesBranchy;
import at.tyron.vintagecraft.Item.ItemLogVC;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemRock;
import at.tyron.vintagecraft.Item.ItemRockTyped;
import at.tyron.vintagecraft.Item.ItemSubsoil;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.Item.ItemTopSoil;
import at.tyron.vintagecraft.Item.ItemWoodtyped;
import at.tyron.vintagecraft.TileEntity.TEBloomery;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TESapling;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTallGrass;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public class BlocksVC {

	public static String raworeName = "rawore";

	public static Block stove;
	public static Block stove_lit;
	
	public static Block bloomerybase;
	public static Block bloomerychimney;
	
	public static Block fireclaybricks;
	
	//public static BlockVC[] flowers;
	//public static BlockVC[] doubleflowers;
	
	public static BlockVC tallgrass;
	public static BlockVC uppermantle;
	//public static BlockVC bedrock;
		
	public static BlockVC topsoil;
	public static BlockContainerVC farmland;
	public static Block wheatcrops;
	
	
	// Todo
	public static BlockVC charredtopsoil;  // Burned dirt when in contact with lava 
	public static BlockVC lichen; // Mossy stuff that grows on stones
	public static Block vine;
	
	
	
	public static BlockVC rawclay;
	public static BlockVC rawfireclay;
	public static BlockVC peat;
	public static BlockVC lignite;
	public static BlockVC coal;
	
	
	public static FlowerClass flower;
	public static FlowerClass doubleflower;
	
	public static TreeClass log;
	public static TreeClass leaves;
	public static TreeClass leavesbranchy;
	public static TreeClass planks;
	public static TreeClass fence;
	public static TreeClass fencegate;
	public static TreeClass stairs;
	public static TreeClass singleslab;
	public static TreeClass doubleslab;
	public static TreeClass sapling;
	public static Block toolrack;

	public static RockClass rock;
	public static RockClass cobblestone;
	public static RockClass regolith;
	public static SoilRockClass subsoil;
	public static RockClass sand;
	public static RockClass gravel;

	public static OreClass rawore;
	
	public static Block ingotPile;
	public static Block ceramicVessel;
	public static Block clayVessel;

	public static Block firepit_lit;
	public static Block firepit;

	
	public static TreeClass quartzglass;
	
	public static void init() {
		initBlocks();
		initHardness();
		initTileEntities();
	}
	

	public static void initBlocks() {
		firepit = new BlockFirepit(false);
		firepit.setHardness(1f);
		register(firepit, "firepit", ItemBlock.class);
		
		firepit_lit = new BlockFirepit(true);
		firepit_lit.setHardness(1F).setLightLevel(0.86f);
		register(firepit_lit, "firepit_lit", ItemBlock.class);
		
		stove = new BlockStove(false).setHardness(3F);
		stove_lit = new BlockStove(true).setHardness(3F);
		stove_lit.setLightLevel(0.86f);
		register(stove, "stove", ItemBlock.class);
		register(stove_lit, "stove_lit", ItemBlock.class);

		bloomerybase = new BlockBloomeryBase().setHardness(3.8f);
		register(bloomerybase, "bloomerybase", ItemBlock.class);

		bloomerychimney = new BlockBloomeryChimney().setHardness(2.5f);
		register(bloomerychimney, "bloomerychimney", ItemBlock.class);

		
		
		
		toolrack = new BlockToolRack().registerMultiState("toolrack", ItemToolRack.class, EnumTree.values()).setHardness(1.2f); 
		
		
		
		ingotPile = new BlockIngotPile();
		ingotPile.setHardness(1f);
		register(ingotPile, "ingotpile", null);
		
		
		ceramicVessel = new BlockCeramicVessel().setHardness(0.8f);
		register(ceramicVessel, "ceramicvessel2", ItemCeramicVessel.class);
		
		clayVessel = new BlockClayVessel().setHardness(0.8f);
		register(clayVessel, "clayvessel2", ItemClayVessel.class);

		
		farmland = new BlockFarmlandVC().registerMultiState("farmland", ItemFarmLand.class, EnumFertility.values());
		farmland.setHardness(2f).setStepSound(Block.soundTypeGravel);		
		
		
		topsoil = new BlockTopSoil().setHardness(2F).registerMultiState("topsoil", ItemTopSoil.class, EnumOrganicLayer.valuesWithFertilityForTopsoil()).setStepSound(Block.soundTypeGrass);
	
		tallgrass = new BlockTallGrass().registerMultiState("tallgrass", ItemGrassVC.class, EnumTallGrass.values()).setHardness(0.1f).setStepSound(Block.soundTypeGrass);
		
		flower = new FlowerClass();
		flower.init(false);
		
		doubleflower = new FlowerClass();
		doubleflower.init(true);
		
		log = new TreeClass("log", BlockLogVC.class, ItemLogVC.class, 3.5f, Block.soundTypeWood, "axe", 2);
		log.init();
		
		planks = new TreeClass("planks", BlockPlanksVC.class, ItemPlanksVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		planks.init();
		
		fence = new TreeClass("fence", BlockFenceVC.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		fence.init();
		
		fencegate = new TreeClass("fencegate", BlockFenceGateVC.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		fencegate.init();
		
		/*stairs = new TreeClass("stairs", BlockStairsVC.class, ItemWoodProductVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		stairs.init();
*/
		singleslab = new TreeClass("singleslab", BlockSingleWoodenSlab.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		singleslab.init();

		doubleslab = new TreeClass("doubleslab", BlockDoubleWoodenSlab.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		doubleslab.init();

		
		quartzglass = new TreeClass("quartzglass", BlockQuartzGlass.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		quartzglass.init();
		
		
		leaves = new TreeClass("leaves", BlockLeavesVC.class, ItemWoodtyped.class, 0.2f, Block.soundTypeGrass, null, 0);
		leaves.init();
		
		leavesbranchy = new TreeClass("leavesbranchy", BlockLeavesBranchy.class, ItemWoodtyped.class, 0.4f, Block.soundTypeGrass, "axe", 1);
		leavesbranchy.init();
		

		sapling = new TreeClass("sapling", BlockSaplingVC.class, ItemWoodtyped.class, 0.4f, Block.soundTypeGrass, null, 0);
		sapling.init();
		
		
		rawore = new OreClass();
		rawore.init();
		
		
		peat = new BlockPeat().setHardness(2F).registerMultiState("peat", ItemBlock.class, EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		rawclay = new BlockRawClay().setHardness(2F).registerSingleState("rawclay", ItemBlock.class).setStepSound(Block.soundTypeGrass);
		rawfireclay = new BlockRawFireClay().setHardness(2F).registerSingleState("rawfireclay", ItemBlock.class).setStepSound(Block.soundTypeGravel);
		fireclaybricks = new BlockFireBrick().setHardness(2F).registerSingleState("fireclaybricks", ItemBlock.class).setStepSound(Block.soundTypeStone);
		
		
		
		
		sand = new RockClass("sand", BlockSandVC.class, ItemRockTyped.class, 0.8f, Block.soundTypeSand, "shovel", 0);
		sand.init();
		
		gravel = new RockClass("gravel", BlockGravelVC.class, ItemRockTyped.class, 1f, Block.soundTypeGravel, "shovel", 0);
		gravel.init();
		
		subsoil = new SoilRockClass("subsoil", BlockSubSoil.class, ItemSubsoil.class, 1.5f, Block.soundTypeGravel, "shovel", 0);
		subsoil.init();
		
		regolith = new RockClass("regolith", BlockRegolith.class, ItemRockTyped.class, 2.5f, Block.soundTypeGravel, "shovel", 0);
		regolith.init();
		
		rock = new RockClass("rock", BlockRock.class, ItemRock.class, 1.8f, Block.soundTypeStone, "pickaxe", 0);
		rock.init();
		
		cobblestone = new RockClass("cobblestone", BlockCobblestone.class, ItemRockTyped.class, 1.5f, Block.soundTypeStone, "pickaxe", 0);
		cobblestone.init();
		
		uppermantle = new BlockUpperMantle().registerSingleState("uppermantle", ItemBlock.class).setBlockUnbreakable().setResistance(6000000.0F);
		

		wheatcrops = new BlockCropsVC().setHardness(0.2f);
		register(wheatcrops, "wheatcrops", ItemBlock.class);
		
		vine = new BlockVineVC().setHardness(0.2f).setStepSound(Block.soundTypeGrass);
		GameRegistry.registerBlock(vine, ItemBlock.class, "vine");
		vine.setUnlocalizedName("vine");
		VintageCraft.instance.proxy.registerItemBlockTextureVanilla(vine, "vine");
	}
	
	
	public static void initTileEntities() {
		GameRegistry.registerTileEntity(TEHeatSourceWithGUI.class, ModInfo.ModID + ":stove");
		GameRegistry.registerTileEntity(TEFarmland.class, ModInfo.ModID + ":farmlandte");
		GameRegistry.registerTileEntity(TESapling.class, ModInfo.ModID + ":saplingte");
		GameRegistry.registerTileEntity(TEIngotPile.class, ModInfo.ModID + ":ingotpile");
		GameRegistry.registerTileEntity(TEToolRack.class, ModInfo.ModID + ":toolrack");
		GameRegistry.registerTileEntity(TEVessel.class, ModInfo.ModID + ":ceramicvessel2");
		GameRegistry.registerTileEntity(TEBloomery.class, ModInfo.ModID + ":bloomery");
	}






	private static void initHardness() {
		topsoil.setHarvestLevel("shovel", 0);
		rawclay.setHarvestLevel("shovel", 0);
		rawfireclay.setHarvestLevel("shovel", 0);
		peat.setHarvestLevel("shovel", 1);
		bloomerybase.setHarvestLevel("pickaxe", 0);
		bloomerychimney.setHarvestLevel("pickaxe", 1);
	}
	

	
	
	
	public static void register(Block block, String blockclassname, Class<? extends ItemBlock> itemclass) {
		if (itemclass == null) {
			GameRegistry.registerBlock(block, blockclassname);
		} else {
			GameRegistry.registerBlock(block, itemclass, blockclassname);
		}
		
		block.setUnlocalizedName(blockclassname);
		
		VintageCraft.instance.proxy.registerItemBlockTexture(block, blockclassname);
	}


}

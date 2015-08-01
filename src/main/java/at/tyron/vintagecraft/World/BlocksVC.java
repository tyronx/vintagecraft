package at.tyron.vintagecraft.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.*;
import at.tyron.vintagecraft.Block.Mechanics.BlockAngledGearBox;
import at.tyron.vintagecraft.Block.Mechanics.BlockAxle;
import at.tyron.vintagecraft.Block.Mechanics.BlockBellows;
import at.tyron.vintagecraft.Block.Mechanics.BlockGrindstone;
import at.tyron.vintagecraft.Block.Mechanics.BlockWindMillRotor;
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
import at.tyron.vintagecraft.Block.Organic.BlockSpiderEgg;
import at.tyron.vintagecraft.Block.Organic.BlockStairsVC;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrass;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Block.Organic.BlockVineVC;
import at.tyron.vintagecraft.Block.Utility.BlockAnvilVC;
import at.tyron.vintagecraft.Block.Utility.BlockBloomeryBase;
import at.tyron.vintagecraft.Block.Utility.BlockBloomeryChimney;
import at.tyron.vintagecraft.Block.Utility.BlockCeramicVessel;
import at.tyron.vintagecraft.Block.Utility.BlockClayVessel;
import at.tyron.vintagecraft.Block.Utility.BlockFirepit;
import at.tyron.vintagecraft.Block.Utility.BlockStonePot;
import at.tyron.vintagecraft.Block.Utility.BlockIngotPile;
import at.tyron.vintagecraft.Block.Utility.BlockStoneAnvil;
import at.tyron.vintagecraft.Block.Utility.BlockStove;
import at.tyron.vintagecraft.Block.Utility.BlockToolRack;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.CoatingClass;
import at.tyron.vintagecraft.BlockClass.CropClass;
import at.tyron.vintagecraft.BlockClass.FlowerClass;
import at.tyron.vintagecraft.BlockClass.MetalCoatingClass;
import at.tyron.vintagecraft.BlockClass.OreClass;
import at.tyron.vintagecraft.BlockClass.RockClass;
import at.tyron.vintagecraft.BlockClass.SoilRockClass;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Client.VCraftModelLoader;
import at.tyron.vintagecraft.Client.Render.TESR.TESRIngotPile;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemAnvilVC;
import at.tyron.vintagecraft.Item.ItemBrick;
import at.tyron.vintagecraft.Item.ItemCeramicVessel;
import at.tyron.vintagecraft.Item.ItemClayVessel;
import at.tyron.vintagecraft.Item.ItemDoubleFlower;
import at.tyron.vintagecraft.Item.ItemFarmLand;
import at.tyron.vintagecraft.Item.ItemFlowerVC;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.Item.ItemTallGrassVC;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemLeaves;
import at.tyron.vintagecraft.Item.ItemLeavesBranchy;
import at.tyron.vintagecraft.Item.ItemLogVC;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemRock;
import at.tyron.vintagecraft.Item.ItemRockTyped;
import at.tyron.vintagecraft.Item.ItemSaltpeterBlock;
import at.tyron.vintagecraft.Item.ItemStonePot;
import at.tyron.vintagecraft.Item.ItemSubsoil;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.Item.ItemTopSoil;
import at.tyron.vintagecraft.Item.ItemWoodtyped;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalRock;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWooden;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWoodenOppositePlacement;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEBloomery;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TESapling;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.TileEntity.TileEntityForestSpiderSpawner;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTallGrass;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public class BlocksVC {
	public static String raworeName = "rawore";
	

	/* Terrain */
	public static BlockVC uppermantle;
	public static RockClass rock;
	public static RockClass cobblestone;
	public static RockClass regolith;
	public static SoilRockClass subsoil;
	public static RockClass sand;
	public static RockClass gravel;
	
	/* Resources */
	public static BlockVC rawclay;
	public static BlockVC rawfireclay;
	public static BlockVC peat;
	public static CoatingClass saltpeter;
	public static OreClass rawore;

	/* Organics */
	public static FlowerClass flower;
	public static FlowerClass doubleflower;
	public static TreeClass log;
	public static TreeClass leaves;
	public static TreeClass leavesbranchy;
	public static TreeClass sapling;	
	public static BlockVC tallgrass;		
	public static BlockVC topsoil;
	public static BlockContainerVC farmland;
	public static Block wheatcrops;			// Current crop block
	public static CropClass crops;			// Next gen crop block (unfinished)
	public static BlockContainer spiderEgg;	
	// Todo
	public static BlockVC charredtopsoil;  // Burned dirt when in contact with lava 
	public static BlockVC lichen; // Mossy stuff that grows on stones
	public static Block vine;
	

	
	/* Non-Organic Player created Blocks */
	public static RockClass stoneanvil;
	public static Block stove;
	public static Block stove_lit;
	public static Block bloomerybase;
	public static Block bloomerychimney;
	public static Block metalanvil;
	public static Block ceramicVessel;
	public static Block clayVessel;
	public static Block firepit_lit;
	public static Block firepit;
	public static Block stonepot;
	public static Block ingotPile;
	public static Block fireclaybricks;
	public static BlockContainerVC bellows;
	public static BlockContainerVC grindstone;
	public static MetalCoatingClass metalplate;
	
	
	/* Woodtyped Player created Blocks */
	public static Block toolrack;
	public static TreeClass quartzglass;
	public static TreeClass planks;
	public static TreeClass fence;
	public static TreeClass fencegate;
	public static TreeClass stairs;
	public static TreeClass singleslab;
	public static TreeClass doubleslab;
	public static BlockContainerVC axle;
	public static BlockContainerVC angledgearbox;
	public static BlockContainerVC windmillrotor;


	
	

	
	public static void init() {
		initBlocks();
		initBlockProperties();
	}
	

	public static void initBlocks() {
		
		/* Terrain */
		uppermantle = new BlockUpperMantle().registerSingleState("uppermantle", ItemBlock.class).setBlockUnbreakable().setResistance(6000000.0F);
		
		rock = new RockClass("rock", BlockRock.class, ItemRock.class, 1.8f, Block.soundTypeStone, "pickaxe", 0);
		rock.init();
		
		cobblestone = new RockClass("cobblestone", BlockCobblestone.class, ItemRockTyped.class, 1.5f, Block.soundTypeStone, "pickaxe", 0);
		cobblestone.init();

		regolith = new RockClass("regolith", BlockRegolith.class, ItemRockTyped.class, 2.5f, Block.soundTypeGravel, "shovel", 0);
		regolith.init();

		subsoil = new SoilRockClass("subsoil", BlockSubSoil.class, ItemSubsoil.class, 1.5f, Block.soundTypeGravel, "shovel", 0);
		subsoil.init();
		
		sand = new RockClass("sand", BlockSandVC.class, ItemRockTyped.class, 0.8f, Block.soundTypeSand, "shovel", 0);
		sand.init();
		
		gravel = new RockClass("gravel", BlockGravelVC.class, ItemRockTyped.class, 1f, Block.soundTypeGravel, "shovel", 0);
		gravel.init();
		
		
		/* Resources */
		rawore = new OreClass();
		rawore.init();
		
		saltpeter = new CoatingClass("saltpeter", BlockSaltpeter.class, ItemSaltpeterBlock.class, 0.8f, Block.soundTypeSand, "shovel", 0);
		saltpeter.init();
				
		peat = new BlockPeat().registerMultiState("peat", ItemBlock.class, EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		rawclay = new BlockRawClay().registerSingleState("rawclay", ItemBlock.class).setStepSound(Block.soundTypeGrass);
		rawfireclay = new BlockRawFireClay().registerSingleState("rawfireclay", ItemBlock.class).setStepSound(Block.soundTypeGravel);

		
		/* Organics */
		
		flower = new FlowerClass();
		flower.init(false);
		
		doubleflower = new FlowerClass();
		doubleflower.init(true);

		log = new TreeClass("log", BlockLogVC.class, ItemLogVC.class, 3.5f, Block.soundTypeWood, "axe", 2);
		log.init();

		leaves = new TreeClass("leaves", BlockLeavesVC.class, ItemWoodtyped.class, 0.2f, Block.soundTypeGrass, null, 0);
		leaves.init();
		
		leavesbranchy = new TreeClass("leavesbranchy", BlockLeavesBranchy.class, ItemWoodtyped.class, 0.4f, Block.soundTypeGrass, "axe", 1);
		leavesbranchy.init();
		

		sapling = new TreeClass("sapling", BlockSaplingVC.class, ItemWoodtyped.class, 0.4f, Block.soundTypeGrass, null, 0);
		sapling.init();

		tallgrass = new BlockTallGrass().registerMultiState("tallgrass", ItemTallGrassVC.class, EnumTallGrass.values()).setHardness(0.1f).setStepSound(Block.soundTypeGrass);

		topsoil = new BlockTopSoil().registerMultiState("topsoil", ItemTopSoil.class, EnumOrganicLayer.valuesWithFertilityForTopsoil()).setStepSound(Block.soundTypeGrass);
	
		farmland = new BlockFarmlandVC().registerMultiState("farmland", ItemFarmLand.class, EnumFertility.values());

		wheatcrops = new BlockCropsVC();
		register(wheatcrops, "wheatcrops", ItemBlock.class);
		
		crops = new CropClass();
		crops.init();

		spiderEgg = new BlockSpiderEgg();
		register(spiderEgg, "spideregg", ItemBlock.class);

		vine = new BlockVineVC().setHardness(0.2f).setStepSound(Block.soundTypeGrass);
		GameRegistry.registerBlock(vine, ItemBlock.class, "vine");
		vine.setUnlocalizedName("vine");
		VintageCraft.instance.proxy.registerItemBlockTextureVanilla(vine, "vine");

		
		
		/* Non-Organic Player created Blocks */
		
		register(firepit = new BlockFirepit(false), "firepit", ItemBlock.class);
		register(firepit_lit = new BlockFirepit(true), "firepit_lit", ItemBlock.class);
		
		register(stove = new BlockStove(false), "stove", ItemBlock.class);
		register(stove_lit = new BlockStove(true), "stove_lit", ItemBlock.class);

		register(bloomerybase = new BlockBloomeryBase(), "bloomerybase", ItemBlock.class);
		register(bloomerychimney = new BlockBloomeryChimney(), "bloomerychimney", ItemBlock.class);
		register(ingotPile = new BlockIngotPile(), "ingotpile", null);
		register(ceramicVessel = new BlockCeramicVessel(), "ceramicvessel2", ItemCeramicVessel.class);
		register(clayVessel = new BlockClayVessel(), "clayvessel2", ItemClayVessel.class);
		fireclaybricks = new BlockFireBrick().registerSingleState("fireclaybricks", ItemBlock.class).setStepSound(Block.soundTypeStone);		

		metalanvil = new BlockAnvilVC().registerMultiState("anvilvc", ItemAnvilVC.class, EnumMetal.anvilValues());
		
		stoneanvil = new RockClass("stoneanvil", BlockStoneAnvil.class, ItemRockTyped.class, 2.5f, Block.soundTypeStone, "pickaxe", 0); 
		stoneanvil.init();
		
		stonepot = new BlockStonePot().registerSingleState("stonepot", ItemBlock.class);

		metalplate = new MetalCoatingClass();
		metalplate.init();
		
		/* Woodtyped Player created Blocks */
		
		toolrack = new BlockToolRack().registerMultiState("toolrack", ItemToolRack.class, EnumTree.values()).setHardness(1.2f);
		
		planks = new TreeClass("planks", BlockPlanksVC.class, ItemPlanksVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		planks.init();
		
		fence = new TreeClass("fence", BlockFenceVC.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		fence.init();
		
		fencegate = new TreeClass("fencegate", BlockFenceGateVC.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		fencegate.init();
		
		stairs = new TreeClass("stairs", BlockStairsVC.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		stairs.init();

		singleslab = new TreeClass("singleslab", BlockSingleWoodenSlab.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		singleslab.init();

		//doubleslab = new TreeClass("doubleslab", BlockDoubleWoodenSlab.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		//doubleslab.init();
		
		quartzglass = new TreeClass("quartzglass", BlockQuartzGlass.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		quartzglass.init();

		

		axle = new BlockAxle().registerSingleState("axle", ItemMechanicalWooden.class);
		angledgearbox = new BlockAngledGearBox().registerSingleState("angledgearbox", ItemMechanicalWooden.class);
		windmillrotor = new BlockWindMillRotor().registerSingleState("windmillrotor", ItemMechanicalWoodenOppositePlacement.class);
		bellows = new BlockBellows().registerSingleState("bellows", ItemMechanicalWoodenOppositePlacement.class);
		grindstone = new BlockGrindstone().registerSingleState("grindstone", ItemMechanicalRock.class);
	}
	



	
	
	private static void initBlockProperties() {
		firepit_lit.setLightLevel(0.86f);
		stove_lit.setLightLevel(0.86f);
		
		firepit.setHardness(1f);
		firepit_lit.setHardness(1F);
		bloomerybase.setHardness(3.8f);
		bloomerychimney.setHardness(2.5f);
		ingotPile.setHardness(1f);
		
		stove.setHardness(3F);
		stove_lit.setHardness(3F);
		
		topsoil.setHardness(2F);
		topsoil.setHarvestLevel("shovel", 0);
		rawclay.setHarvestLevel("shovel", 0);
		rawclay.setHardness(2F);
		rawfireclay.setHarvestLevel("shovel", 0);
		rawfireclay.setHardness(2F);
		peat.setHarvestLevel("shovel", 1);
		bloomerybase.setHarvestLevel("pickaxe", 0);
		bloomerychimney.setHarvestLevel("pickaxe", 1);
		
		clayVessel.setHardness(0.8f);
		ceramicVessel.setHardness(0.8f);
		
		farmland.setHardness(2f).setStepSound(Block.soundTypeGravel);
		peat.setHardness(2F);
		spiderEgg.setHardness(2f);
		fireclaybricks.setHardness(2F);

		metalanvil.setHardness(2f).setHarvestLevel("pickaxe", 0);
		metalanvil.setStepSound(Block.soundTypeAnvil);

		stonepot.setHardness(2f).setHarvestLevel("pickaxe", 0);
		
		wheatcrops.setHardness(0.2f);
		
		grindstone.setStepSound(Block.soundTypeStone);
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

package at.tyron.vintagecraft.World;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockCobblestone;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Block.BlockFireBrick;
import at.tyron.vintagecraft.Block.BlockFireclayStairs;
import at.tyron.vintagecraft.Block.BlockGravelVC;
import at.tyron.vintagecraft.Block.BlockQuartzGlass;
import at.tyron.vintagecraft.Block.BlockRawClay;
import at.tyron.vintagecraft.Block.BlockRawFireClay;
import at.tyron.vintagecraft.Block.BlockRegolith;
import at.tyron.vintagecraft.Block.BlockRock;
import at.tyron.vintagecraft.Block.BlockSaltpeter;
import at.tyron.vintagecraft.Block.BlockSandVC;
import at.tyron.vintagecraft.Block.BlockUpperMantle;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Mechanics.BlockAngledGears;
import at.tyron.vintagecraft.Block.Mechanics.BlockAxle;
import at.tyron.vintagecraft.Block.Mechanics.BlockBellows;
import at.tyron.vintagecraft.Block.Mechanics.BlockGrindstone;
import at.tyron.vintagecraft.Block.Mechanics.BlockWindMillRotor;
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
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrass;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Block.Organic.BlockVineVC;
import at.tyron.vintagecraft.Block.Organic.BlockWoodenStairsVC;
import at.tyron.vintagecraft.Block.Utility.BlockAnvilVC;
import at.tyron.vintagecraft.Block.Utility.BlockCarpenterTable;
import at.tyron.vintagecraft.Block.Utility.BlockCeramicVessel;
import at.tyron.vintagecraft.Block.Utility.BlockClayVessel;
import at.tyron.vintagecraft.Block.Utility.BlockCokeOvenDoor;
import at.tyron.vintagecraft.Block.Utility.BlockFirepit;
import at.tyron.vintagecraft.Block.Utility.BlockFurnaceChimney;
import at.tyron.vintagecraft.Block.Utility.BlockFurnaceSection;
import at.tyron.vintagecraft.Block.Utility.BlockIngotPile;
import at.tyron.vintagecraft.Block.Utility.BlockOrePile;
import at.tyron.vintagecraft.Block.Utility.BlockStoneAnvil;
import at.tyron.vintagecraft.Block.Utility.BlockStonePot;
import at.tyron.vintagecraft.Block.Utility.BlockStove;
import at.tyron.vintagecraft.Block.Utility.BlockTallMetalMolds;
import at.tyron.vintagecraft.Block.Utility.BlockToolRack;
import at.tyron.vintagecraft.BlockClass.CoatingClass;
import at.tyron.vintagecraft.BlockClass.CropClass;
import at.tyron.vintagecraft.BlockClass.FlowerClass;
import at.tyron.vintagecraft.BlockClass.MetalCoatingClass;
import at.tyron.vintagecraft.BlockClass.OreInRockClass;
import at.tyron.vintagecraft.BlockClass.RockClass;
import at.tyron.vintagecraft.BlockClass.SoilRockClass;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Item.ItemAnvilVC;
import at.tyron.vintagecraft.Item.ItemCarpenterTable;
import at.tyron.vintagecraft.Item.ItemCeramicVessel;
import at.tyron.vintagecraft.Item.ItemClayVessel;
import at.tyron.vintagecraft.Item.ItemFarmLand;
import at.tyron.vintagecraft.Item.ItemLogVC;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemRock;
import at.tyron.vintagecraft.Item.ItemRockTyped;
import at.tyron.vintagecraft.Item.ItemSaltpeterBlock;
import at.tyron.vintagecraft.Item.ItemStonePot;
import at.tyron.vintagecraft.Item.ItemSubsoil;
import at.tyron.vintagecraft.Item.ItemTallGrassVC;
import at.tyron.vintagecraft.Item.ItemTallMetalMold;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.Item.ItemTopSoil;
import at.tyron.vintagecraft.Item.ItemWoodtyped;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalRock;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWooden;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWoodenOppositePlacement;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTallGrass;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	public static OreInRockClass rawore;

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
	public static CropClass crops;
	public static BlockContainer spiderEgg;	
	// Todo
	public static BlockVC charredtopsoil;  // Burned dirt when in contact with lava 
	public static BlockVC lichen; // Mossy stuff that grows on stones
	public static Block vine;
	
	
	
	/* Non-Organic Player created Blocks */
	public static RockClass stoneanvil;
	public static Block stove;
	public static Block stove_lit;
	public static Block furnaceSection;
	public static Block furnaceChimney;
	public static Block metalanvil;
	public static Block ceramicVessel;
	public static Block clayVessel;
	public static Block firepit_lit;
	public static Block firepit;
	public static Block stonepot;
	public static Block ingotPile;
	public static Block fireclaybricks;
	public static Block fireclaystairs;
	public static Block tallmetalmolds;
	public static BlockContainerVC bellows;
	public static BlockContainerVC grindstone;
	public static MetalCoatingClass metalplate;
	public static BlockContainerVC cokeovendoor;
	public static BlockContainerVC orepile;
	
	/* Woodtyped Player created Blocks */
	public static Block toolrack;
	public static TreeClass quartzglass;
	public static TreeClass planks;
	public static TreeClass fence;
	public static TreeClass fencegate;
	public static TreeClass plankstairs;
	public static TreeClass singleslab;
	public static TreeClass doubleslab;
	public static BlockContainerVC axle;
	public static BlockContainerVC angledgears;
	public static BlockContainerVC windmillrotor;
	
	public static BlockContainerVC carpenterTable;
	
	

	
	public static void init() {
		initBlocks();
		initBlockProperties();
	}
	

	
	/************** WARNING: DO NOT RESORT THOSE IT WILL MESS UP ORLD WORLDS :( ******************/
	public static void initBlocks() {
		register(firepit = new BlockFirepit(false), "firepit", ItemBlock.class);
		register(firepit_lit = new BlockFirepit(true), "firepit_lit", ItemBlock.class);
		
		register(stove = new BlockStove(false), "stove", ItemBlock.class);
		register(stove_lit = new BlockStove(true), "stove_lit", ItemBlock.class);

		register(furnaceSection = new BlockFurnaceSection(), "furnacesection", ItemBlock.class);
		register(furnaceChimney = new BlockFurnaceChimney(), "furnacechimney", ItemBlock.class);

		toolrack = new BlockToolRack().registerMultiState("toolrack", ItemToolRack.class, EnumTree.values()).setHardness(1.2f);

		ingotPile = new BlockIngotPile();
		register(ingotPile, "ingotpile", null);
		
		
		register(ceramicVessel = new BlockCeramicVessel(), "ceramicvessel2", ItemCeramicVessel.class);
		register(clayVessel = new BlockClayVessel(), "clayvessel2", ItemClayVessel.class);
		
		farmland = new BlockFarmlandVC().registerMultiState("farmland", ItemFarmLand.class, EnumFertility.values());
		topsoil = new BlockTopSoil().registerMultiState("topsoil", ItemTopSoil.class, EnumOrganicLayer.valuesWithFertilityForTopsoil()).setStepSound(Block.soundTypeGrass);

		tallgrass = new BlockTallGrass().registerMultiState("tallgrass", ItemTallGrassVC.class, EnumTallGrass.values()).setHardness(0.1f).setStepSound(Block.soundTypeGrass);

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
		
		plankstairs = new TreeClass("stairs", BlockWoodenStairsVC.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		plankstairs.init();

		singleslab = new TreeClass("singleslab", BlockSingleWoodenSlab.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		singleslab.init();

		//doubleslab = new TreeClass("doubleslab", BlockDoubleWoodenSlab.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		//doubleslab.init();
		
		quartzglass = new TreeClass("quartzglass", BlockQuartzGlass.class, ItemWoodtyped.class, 1.5f, Block.soundTypeWood, "axe", 1);
		quartzglass.init();
		
		

		leaves = new TreeClass("leaves", BlockLeavesVC.class, ItemWoodtyped.class, 0.2f, Block.soundTypeGrass, null, 0);
		leaves.init();
		
		leavesbranchy = new TreeClass("leavesbranchy", BlockLeavesBranchy.class, ItemWoodtyped.class, 0.4f, Block.soundTypeGrass, "axe", 1);
		leavesbranchy.init();
		

		sapling = new TreeClass("sapling", BlockSaplingVC.class, ItemWoodtyped.class, 0.4f, Block.soundTypeGrass, null, 0);
		sapling.init();

		rawore = new OreInRockClass();
		rawore.init();

		saltpeter = new CoatingClass("saltpeter", BlockSaltpeter.class, ItemSaltpeterBlock.class, 0.8f, Block.soundTypeSand, "shovel", 0);
		saltpeter.init();

		
		peat = new BlockPeat().registerMultiState("peat", ItemBlock.class, EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		rawclay = new BlockRawClay().registerSingleState("rawclay", ItemBlock.class).setStepSound(Block.soundTypeGrass);
		rawfireclay = new BlockRawFireClay().registerSingleState("rawfireclay", ItemBlock.class).setStepSound(Block.soundTypeGravel);
		fireclaybricks = new BlockFireBrick().registerSingleState("fireclaybricks", ItemBlock.class).setStepSound(Block.soundTypeStone);

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
		
		crops = new CropClass();
		crops.init();

		
		
		/* Terrain */

		vine = new BlockVineVC().setHardness(0.2f).setStepSound(Block.soundTypeGrass);
		GameRegistry.registerBlock(vine, ItemBlock.class, "vine");
		vine.setUnlocalizedName("vine");
		VintageCraft.instance.proxy.registerItemBlockTextureVanilla(vine, "vine");

		metalanvil = new BlockAnvilVC().registerMultiState("anvilvc", ItemAnvilVC.class, EnumMetal.anvilValues());

		stoneanvil = new RockClass("stoneanvil", BlockStoneAnvil.class, ItemRockTyped.class, 2.5f, Block.soundTypeStone, "pickaxe", 0); 
		stoneanvil.init();

		stonepot = new BlockStonePot().registerSingleState("stonepot", ItemStonePot.class);

		axle = new BlockAxle().registerSingleState("axle", ItemMechanicalWooden.class);
		angledgears = new BlockAngledGears().registerSingleState("angledgearbox", ItemMechanicalWooden.class);
		windmillrotor = new BlockWindMillRotor().registerSingleState("windmillrotor", ItemMechanicalWoodenOppositePlacement.class);

		spiderEgg = new BlockSpiderEgg();
		register(spiderEgg, "spideregg", ItemBlock.class);

		
		
		register(cokeovendoor = new BlockCokeOvenDoor(), "cokeovendoor", ItemBlock.class);
		register(orepile = new BlockOrePile(), "orepile", ItemBlock.class);
		
		fireclaystairs = new BlockFireclayStairs().registerSingleState("fireclaystairs", ItemBlock.class).setStepSound(Block.soundTypeStone);
		
		metalplate = new MetalCoatingClass();
		metalplate.init();

		bellows = new BlockBellows().registerSingleState("bellows", ItemMechanicalWoodenOppositePlacement.class);
		grindstone = new BlockGrindstone().registerSingleState("grindstone", ItemMechanicalRock.class);
		tallmetalmolds = new BlockTallMetalMolds().registerSingleState("tallmetalmolds", ItemTallMetalMold.class);
		
		carpenterTable = new BlockCarpenterTable().registerSingleState("carpentertable", ItemCarpenterTable.class);
	}
	



	
	
	private static void initBlockProperties() {
		firepit_lit.setLightLevel(0.86f);
		stove_lit.setLightLevel(0.86f);
		
		firepit.setHardness(1f);
		firepit_lit.setHardness(1F);
		furnaceSection.setHardness(3.8f);
		furnaceChimney.setHardness(2.5f);
		ingotPile.setHardness(1f);
		orepile.setHardness(1f);
		
		stove.setHardness(3F);
		stove_lit.setHardness(3F);
		
		topsoil.setHardness(2F);
		topsoil.setHarvestLevel("shovel", 0);
		rawclay.setHarvestLevel("shovel", 0);
		rawclay.setHardness(2F);
		rawfireclay.setHarvestLevel("shovel", 0);
		rawfireclay.setHardness(2F);
		peat.setHarvestLevel("shovel", 1);
		furnaceSection.setHarvestLevel("pickaxe", 0);
		furnaceChimney.setHarvestLevel("pickaxe", 1);
		
		clayVessel.setHardness(0.8f);
		ceramicVessel.setHardness(0.8f);
		
		farmland.setHardness(2f).setStepSound(Block.soundTypeGravel);
		peat.setHardness(2F);
		spiderEgg.setHardness(2f);
		fireclaybricks.setHardness(2F);
		fireclaystairs.setHardness(2F);
		tallmetalmolds.setHardness(2F);

		metalanvil.setHardness(2f).setHarvestLevel("pickaxe", 0);
		metalanvil.setStepSound(Block.soundTypeAnvil);

		stonepot.setHardness(2f).setHarvestLevel("pickaxe", 0);
		cokeovendoor.setHardness(2.5f).setHarvestLevel("pickaxe", 0);

		
	//	wheatcrops.setHardness(0.2f);
		
		grindstone.setStepSound(Block.soundTypeStone);
		
		carpenterTable.setStepSound(Block.soundTypeWood).setHardness(1.5f).setHarvestLevel("axe", 0);
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

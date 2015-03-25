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
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.FlowerClass;
import at.tyron.vintagecraft.BlockClass.OreClass;
import at.tyron.vintagecraft.BlockClass.RockClass;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.TileEntity.TESapling;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TileEntityStove;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.block.*;
import at.tyron.vintagecraft.client.VCraftModelLoader;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.item.ItemBrick;
import at.tyron.vintagecraft.item.ItemCobblestoneVC;
import at.tyron.vintagecraft.item.ItemDoublePlantVC;
import at.tyron.vintagecraft.item.ItemWoodProductVC;
import at.tyron.vintagecraft.item.ItemFlowerVC;
import at.tyron.vintagecraft.item.ItemGrassVC;
import at.tyron.vintagecraft.item.ItemGravel;
import at.tyron.vintagecraft.item.ItemLeaves;
import at.tyron.vintagecraft.item.ItemLeavesBranchy;
import at.tyron.vintagecraft.item.ItemLogVC;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemPlanksVC;
import at.tyron.vintagecraft.item.ItemSand;
import at.tyron.vintagecraft.item.ItemTopSoil;
import at.tyron.vintagecraft.item.ItemRock;

public class BlocksVC {
	public static String raworeName = "rawore";

	public static Block stove;
	public static Block stove_lit;
	
	//public static BlockVC[] flowers;
	//public static BlockVC[] doubleflowers;
	
	public static BlockVC tallgrass;
	public static BlockVC uppermantle;
	//public static BlockVC bedrock;
		
	public static BlockVC topsoil;
	public static Block farmland;
	public static Block wheatcrops;
	
	
	// Todo
	public static BlockVC charredtopsoil;  // Burned dirt when in contact with lava 
	public static BlockVC lichen; // Mossy stuff that grows on stones
	public static Block vine;
	
	
	
	public static BlockVC rawclay;
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
	
	public static RockClass rock;
	public static RockClass cobblestone;
	public static RockClass regolith;
	public static RockClass subsoil;
	public static RockClass sand;
	public static RockClass gravel;
	//public static RockClass brick;

	public static OreClass rawore;
	

	
	public static void init() {
		initBlocks();
		initHardness();
		initTileEntities();
	}
	

	public static void initBlocks() {
		stove = new BlockStove(false).setHardness(3F);
		/*GameRegistry.registerBlock(stove, ItemBlock.class, "stove");
		stove.setUnlocalizedName("stove");
		
		String modelLocation = ModInfo.ModID.toLowerCase() + ":stove.b3d";
		
		VintageCraft.instance.proxy.addVariantName(Item.getItemFromBlock(stove), modelLocation);
		Item item = Item.getItemFromBlock(stove);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(modelLocation, "inventory"));
        //ModelLoaderRegistry.registerLoader(VCraftModelLoader.instance);
		*/
		
		stove_lit = new BlockStove(true).setHardness(3F);
		stove_lit.setLightLevel(0.86f);
		register(stove, "stove", ItemBlock.class);
		register(stove_lit, "stove_lit", ItemBlock.class);
		
		farmland = new BlockFarmlandVC().setHardness(2F).setStepSound(Block.soundTypeGravel);
		registerMulti(farmland, "farmland", ItemBlock.class, EnumFertility.values());
		
		
		
		topsoil = new BlockTopSoil().setHardness(2F).registerMultiState("topsoil", ItemTopSoil.class, EnumOrganicLayer.valuesWithFertility()).setStepSound(Block.soundTypeGrass);
	
		tallgrass = new BlockTallGrass().registerMultiState("tallgrass", ItemGrassVC.class, EnumTallGrass.values()).setHardness(0.1f).setStepSound(Block.soundTypeGrass);
		
		flower = new FlowerClass();
		flower.init(false);
		
		doubleflower = new FlowerClass();
		doubleflower.init(true);
		
		log = new TreeClass("log", BlockLogVC.class, ItemLogVC.class, 3.5f, Block.soundTypeWood, "axe", 2);
		log.init();
		
		planks = new TreeClass("planks", BlockPlanksVC.class, ItemPlanksVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		planks.init();
		
		fence = new TreeClass("fence", BlockFenceVC.class, ItemWoodProductVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		fence.init();
		
		fencegate = new TreeClass("fencegate", BlockFenceGateVC.class, ItemWoodProductVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		fencegate.init();
		
		/*stairs = new TreeClass("stairs", BlockStairsVC.class, ItemWoodProductVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		stairs.init();
*/
		singleslab = new TreeClass("singleslab", BlockSingleWoodenSlab.class, ItemWoodProductVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		singleslab.init();

		doubleslab = new TreeClass("doubleslab", BlockDoubleWoodenSlab.class, ItemWoodProductVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		doubleslab.init();

		
		leaves = new TreeClass("leaves", BlockLeavesVC.class, ItemWoodProductVC.class, 0.2f, Block.soundTypeGrass, null, 0);
		leaves.init();
		
		leavesbranchy = new TreeClass("leavesbranchy", BlockLeavesBranchy.class, ItemWoodProductVC.class, 0.4f, Block.soundTypeGrass, "axe", 1);
		leavesbranchy.init();
		

		sapling = new TreeClass("sapling", BlockSaplingVC.class, ItemWoodProductVC.class, 0.4f, Block.soundTypeGrass, null, 0);
		sapling.init();
		
		
		rawore = new OreClass();
		rawore.init();
		
		
		
		rawclay = new BlockRawClay().setHardness(2F).registerSingleState("rawclay", ItemBlock.class).setStepSound(Block.soundTypeGrass);
		peat = new BlockPeat().setHardness(2F).registerMultiState("peat", ItemBlock.class, EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		
		
		sand = new RockClass("sand", BlockSandVC.class, ItemSand.class, 1f, Block.soundTypeSand, "shovel", 0);
		sand.init();
		gravel = new RockClass("gravel", BlockGravelVC.class, ItemGravel.class, 1.3f, Block.soundTypeGravel, "shovel", 0);
		gravel.init();
		subsoil = new RockClass("subsoil", BlockSubSoil.class, ItemRock.class, 1.5f, Block.soundTypeGravel, "shovel", 0);
		subsoil.init();
		regolith = new RockClass("regolith", BlockRegolith.class, ItemRock.class, 2.5f, Block.soundTypeGravel, "shovel", 0);
		regolith.init();
		rock = new RockClass("rock", BlockRock.class, ItemRock.class, 2f, Block.soundTypeStone, "pickaxe", 0);
		rock.init();
		cobblestone = new RockClass("cobblestone", BlockCobblestone.class, ItemCobblestoneVC.class, 1.5f, Block.soundTypeStone, "pickaxe", 0);
		cobblestone.init();
		
		
		
		/*subsoil = new BlockSubSoil().setHardness(1.5F).registerMultiState("subsoil", ItemRock.class, EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		regolith = new BlockRegolith().setHardness(2.5F).registerMultiState("regolith", ItemRock.class, EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		rock = new BlockRock().setHardness(2F).registerMultiState("rock", ItemRock.class, EnumRockType.values());
		gravel = new BlockGravelVC().setHardness(1.3F).registerMultiState("gravel", ItemGravel.class, EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		sand = new BlockSandVC().setHardness(1F).registerMultiState("sand", ItemSand.class, EnumRockType.values()).setStepSound(Block.soundTypeSand);
		cobblestone = new BlockCobblestone().setHardness(1.3f).registerMultiState("cobblestone", ItemRock.class, EnumRockType.values());*/
		
		uppermantle = new BlockUpperMantle().registerSingleState("uppermantle", ItemBlock.class).setBlockUnbreakable().setResistance(6000000.0F);
		

		wheatcrops = new BlockCropsVC().setHardness(0.2f);
		register(wheatcrops, "wheatcrops", ItemBlock.class);
		
		vine = new BlockVineVC().setHardness(0.2f).setStepSound(Block.soundTypeGrass);
		GameRegistry.registerBlock(vine, ItemBlock.class, "vine");
		vine.setUnlocalizedName("vine");
		VintageCraft.instance.proxy.registerItemBlockTextureVanilla(vine, "vine");
	}
	
	
	public static void initTileEntities() {
		GameRegistry.registerTileEntity(TileEntityStove.class, ModInfo.ModID + ":stove");
		GameRegistry.registerTileEntity(TEFarmland.class, ModInfo.ModID + ":farmlandte");
		GameRegistry.registerTileEntity(TESapling.class, ModInfo.ModID + ":saplingte");
	}






	private static void initHardness() {
		//rock.setHarvestLevel("pickaxe", 0);
		//cobblestone.setHarvestLevel("pickaxe", 0);
		
		topsoil.setHarvestLevel("shovel", 0);
		//subsoil.setHarvestLevel("shovel", 0);
		rawclay.setHarvestLevel("shovel", 0);
		//gravel.setHarvestLevel("shovel", 0);
		//sand.setHarvestLevel("shovel", 0);
		
		//regolith.setHarvestLevel("shovel", 1);
		peat.setHarvestLevel("shovel", 1);
	}
	

	
	
	
	public static void register(Block block, String blockclassname, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(block, itemclass, blockclassname);
		block.setUnlocalizedName(blockclassname);
		
		VintageCraft.instance.proxy.registerItemBlockTexture(block, blockclassname);
	}
	
	public static void registerMulti(Block block, String blockclassname, Class<? extends ItemBlock> itemclass, EnumFertility[] values) {
		GameRegistry.registerBlock(block, itemclass, blockclassname);
		block.setUnlocalizedName(blockclassname);
		
		for (EnumFertility state: values) {
			System.out.println("register multi vintagecraft:" + blockclassname + "/" + state.getStateName());
			//VintageCraft.instance.proxy.registerItemBlockTexture(block, blockclassname, state.shortName(), state.getMetaData());
		}
	}
	







	static <T> T[][] split(T[] elements, int chunksize) {
		int chunks = 1 + elements.length / chunksize;
		
		ArrayList<T> result = new ArrayList<T>(); 
		
		for (int i = 0; i < chunks; i++) {
			result.add((T) Arrays.copyOfRange(elements, i * chunksize, Math.min(elements.length, i*chunksize + chunksize)));
		}
		
		return (T[][]) result.toArray((T[])Array.newInstance(elements.getClass(), 0));
		
	}
	
	

}

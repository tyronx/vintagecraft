package at.tyron.vintagecraft.World;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.FlowerClass;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TileEntityStove;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.block.*;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.item.ItemBrick;
import at.tyron.vintagecraft.item.ItemDoublePlantVC;
import at.tyron.vintagecraft.item.ItemFlowerVC;
import at.tyron.vintagecraft.item.ItemGrassVC;
import at.tyron.vintagecraft.item.ItemGravel;
import at.tyron.vintagecraft.item.ItemLeaves;
import at.tyron.vintagecraft.item.ItemLeavesBranchy;
import at.tyron.vintagecraft.item.ItemLogVC;
import at.tyron.vintagecraft.item.ItemOre;
import at.tyron.vintagecraft.item.ItemPlanksVC;
import at.tyron.vintagecraft.item.ItemSand;
import at.tyron.vintagecraft.item.ItemTopSoil;
import at.tyron.vintagecraft.item.ItemRock;

public class BlocksVC {
	public static Block stove;
	public static Block stove_lit;
	
	//public static BlockVC[] flowers;
	//public static BlockVC[] doubleflowers;
	
	public static BlockVC tallgrass;

	public static BlockVC uppermantle;
	
	public static BlockVC rock;
	public static BlockVC bedrock;
	public static BlockVC brick;
	//public static VCBlock soil;

	public static BlockVC regolith;
	public static BlockVC subsoil;
	public static BlockVC topsoil;
	public static BlockVC sand;
	public static BlockVC gravel;

	// Todo
	public static BlockVC charredtopsoil;  // Burned dirt when in contact with lava 
	public static BlockVC lichen; // Mossy stuff that grows on stones
	
	
	// Deposits
	public static BlockOreVC rawore;
	public static String raworeName = "rawore";
	public static ModelResourceLocation oremodelLocation = new ModelResourceLocation(ModInfo.ModID + ":" + raworeName, null);
	
	public static BlockVC rawclay;
	public static BlockVC peat;
	public static BlockVC lignite;
	public static BlockVC coal;
	

	//public static BlockVC[] logs;
	public static FlowerClass flower;
	public static FlowerClass doubleflower;
	public static TreeClass log;
	public static TreeClass leaves;
	public static TreeClass leavesbranchy;
	
	public static TreeClass planks;
	
	//public static BlockVC leaves;
	//public static BlockVC leavesbranchy;
	
	public static void init() {
		initBlocks();
		initHardness();
		initTileEntities();
	}
	

	
	public static void initBlocks() {
		stove = new BlockStove(false).setHardness(3F);
		stove_lit = new BlockStove(true).setHardness(3F);
		stove_lit.setLightLevel(0.86f);
		register(stove, "stove", ItemBlock.class);
		register(stove_lit, "stove_lit", ItemBlock.class);
		
		tallgrass = new BlockTallGrass().registerMultiState("tallgrass", ItemGrassVC.class, "tallgrass", EnumTallGrass.values()).setHardness(0.1f).setStepSound(Block.soundTypeGrass);
		
		flower = new FlowerClass();
		flower.init(false);
		
		doubleflower = new FlowerClass();
		doubleflower.init(true);
		
		log = new TreeClass("log", BlockLogVC.class, ItemLogVC.class, 3.5f, Block.soundTypeWood, "axe", 2);
		log.init();
		
		planks = new TreeClass("planks", BlockPlanksVC.class, ItemPlanksVC.class, 1.5f, Block.soundTypeWood, "axe", 1);
		planks.init();
		
		leaves = new TreeClass("leaves", BlockLeavesVC.class, ItemLeaves.class, 0.2f, Block.soundTypeGrass, null, 0);
		leaves.init();
		
		leavesbranchy = new TreeClass("leavesbranchy", BlockLeavesBranchy.class, ItemLeavesBranchy.class, 0.4f, Block.soundTypeGrass, "axe", 1);
		leavesbranchy.init();
		
//		/logs = TreeClass.init("log", BlockLogVC.class, ItemLogVC.class, BlockLogVC.multistateAvailableTypes, 3F, Block.soundTypeWood, "axe", 2);
		
		//flowers = initMultiBlock(Flower.values(false), "flower", BlockFlowerVC.class, ItemFlowerVC.class, BlockFlowerVC.multistateAvailableTypes, 0.2f, Block.soundTypeGrass, null, 0);
		//doubleflowers = initMultiBlock(Flower.values(true), "doubleflower", BlockDoubleFlowerVC.class, ItemFlowerVC.class, BlockDoubleFlowerVC.multistateAvailableTypes, 0.6f, Block.soundTypeGrass, null, 0);

		//System.out.println("created " + flowers.length + " flower blocks and " + doubleflowers.length + " double flower blocks");
		
		topsoil = new BlockTopSoil().setHardness(2F).registerMultiState("topsoil", ItemTopSoil.class, "topsoil", EnumOrganicLayer.valuesWithFertility()).setStepSound(Block.soundTypeGrass);
		//topsoil = initMultiBlock(EnumFlower.values(true), "topsoil", BlockDoubleFlowerVC.class, ItemFlowerVC.class, BlockDoubleFlowerVC.multistateAvailableTypes, 0.6f, Block.soundTypeGrass, null, 0);
		
		
		rawclay = new BlockRawClay().setHardness(2F).registerSingleState("rawclay", ItemBlock.class).setStepSound(Block.soundTypeGrass);
		peat = new BlockPeat().setHardness(2F).registerMultiState("peat", ItemBlock.class, "peat", EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		
		
		subsoil = new BlockSubSoil().setHardness(1.5F).registerMultiState("subsoil", ItemRock.class, "subsoil", EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		regolith = new BlockRegolith().setHardness(2.5F).registerMultiState("regolith", ItemRock.class, "regolith", EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		rock = new BlockRock().setHardness(2F).registerMultiState("rock", ItemRock.class, "rock", EnumRockType.values());
		gravel = new BlockGravel().setHardness(1.3F).registerMultiState("gravel", ItemGravel.class, "gravel", EnumRockType.values());
		sand = new BlockGravel().setHardness(1F).registerMultiState("sand", ItemSand.class, "sand", EnumRockType.values());
		
		
		uppermantle = new BlockUpperMantle().registerSingleState("uppermantle", ItemBlock.class).setBlockUnbreakable().setResistance(6000000.0F);
		
		
		
		//planks = initMultiBlock(Tree.values(), "planks", BlockPlanksVC.class, ItemPlanksVC.class, BlockPlanksVC.multistateAvailableTypes, 1.5F, Block.soundTypeWood, "axe", 1);
		
	//	leaves = new BlockLeaves().setHardness(0.2f).registerMultiState("leaves", ItemLeaves.class, "leaves", Tree.values());
	//	leavesbranchy = new BlockLeavesBranchy().setHardness(0.4f).registerMultiState("leavesbranchy", ItemLeavesBranchy.class, "leavesbranchy", Tree.values());
	}
	
	
	public static void initTileEntities() {
		rawore = new BlockOreVC();
		rawore.setUnlocalizedName(ModInfo.ModID + ":" + raworeName);
		rawore.setHardness(2F);
		
		EnumMaterialDeposit.NATIVEGOLD.init(rawore);
		EnumMaterialDeposit.LIMONITE.init(rawore);
		EnumMaterialDeposit.LIGNITE.init(rawore);
		EnumMaterialDeposit.BITUMINOUSCOAL.init(rawore);
		EnumMaterialDeposit.NATIVECOPPER.init(rawore);
		
		GameRegistry.registerBlock(rawore, raworeName); //.registerSingleState("ore", ItemOre.class);	
		GameRegistry.registerTileEntity(TEOre.class, ModInfo.ModID + ":orete");
		
		GameRegistry.registerTileEntity(TileEntityStove.class, ModInfo.ModID + ":stove");
	}






	private static void initHardness() {
		rock.setHarvestLevel("pickaxe", 0);
		
		topsoil.setHarvestLevel("shovel", 0);
		subsoil.setHarvestLevel("shovel", 0);
		rawclay.setHarvestLevel("shovel", 0);
		gravel.setHarvestLevel("shovel", 0);
		sand.setHarvestLevel("shovel", 0);
		
		regolith.setHarvestLevel("shovel", 1);
		peat.setHarvestLevel("shovel", 1);
	}
	

	
	
	
	public static void register(Block block, String name, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(block, itemclass, name);
		block.setUnlocalizedName(name);
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("vintagecraft:" + name, "inventory"));
		ModelBakery.addVariantName(Item.getItemFromBlock(block), "vintagecraft:" + name);
	}
	









	
	
/*	static BlockVC[] initMultiBlock(BlockClass[] states, String name, Class<? extends BlockVC> blockclass, Class<? extends ItemBlock> itemclass, int typesperblock, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		BlockClass[][] chunked = split(states, typesperblock);
		ArrayList<BlockVC> blocks = new ArrayList<BlockVC>();
		
		
		for (IEnumState[] blockstates : chunked) {
			BlockVC block;
			try {
				block = blockclass.getDeclaredConstructor(new Class[]{BlockClass[].class}).newInstance(new Object[]{blockstates});
				block.setHardness(hardness).setStepSound(stepsound);
				
				if (harvesLevelTool != null) {
					block.setHarvestLevel(harvesLevelTool, harvestLevel);
				}
				
				blocks.add(block);
				
				int meta = 0;
				for (IEnumState blockstate : blockstates) {
					blockstate.init(block, meta++);
				}
				
				block.registerMultiState(name + ((blocks.size() > 1) ? blocks.size() : "") , itemclass, name, blockstates);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return blocks.toArray(new BlockVC[0]);
	}*/
	
	
	static <T> T[][] split(T[] elements, int chunksize) {
		int chunks = 1 + elements.length / chunksize;
		
		ArrayList<T> result = new ArrayList<T>(); 
		
		for (int i = 0; i < chunks; i++) {
			result.add((T) Arrays.copyOfRange(elements, i * chunksize, Math.min(elements.length, i*chunksize + chunksize)));
		}
		
		return (T[][]) result.toArray((T[])Array.newInstance(elements.getClass(), 0));
		
	}
	
	

}

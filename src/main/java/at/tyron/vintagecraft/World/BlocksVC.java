package at.tyron.vintagecraft.World;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.*;
import at.tyron.vintagecraft.item.ItemBrick;
import at.tyron.vintagecraft.item.ItemOre;
import at.tyron.vintagecraft.item.ItemTopSoil;
import at.tyron.vintagecraft.item.ItemRock;

public class BlocksVC {
	public static BlockVC uppermantle;
	
	public static BlockVC rock;
	public static BlockVC bedrock;
	public static BlockVC brick;
	//public static VCBlock soil;

	public static BlockVC regolith;
	public static BlockVC subsoil;
	public static BlockVC topsoil;

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
	

	
	public static void init() {
		initBlocks();
		initHardness();
		initTileEntities();
	}
	
	
	public static void initBlocks() {
		topsoil = new BlockTopSoil().setHardness(2F).registerMultiState("topsoil", ItemTopSoil.class, "topsoil", EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		rawclay = new BlockRawClay().setHardness(2F).registerSingleState("rawclay", ItemBlock.class).setStepSound(Block.soundTypeGrass);
		peat = new BlockPeat().setHardness(2F).registerMultiState("peat", ItemBlock.class, "peat", EnumOrganicLayer.values()).setStepSound(Block.soundTypeGrass);
		
		
		subsoil = new BlockSubSoil().setHardness(2F).registerMultiState("subsoil", ItemRock.class, "subsoil", EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		regolith = new BlockSubSoil().setHardness(2F).registerMultiState("regolith", ItemRock.class, "regolith", EnumRockType.values()).setStepSound(Block.soundTypeGravel);
		rock = new BlockRock().setHardness(2F).registerMultiState("rock", ItemRock.class, "rock", EnumRockType.values());
		
		uppermantle = new BlockUpperMantle().registerSingleState("uppermantle", ItemBlock.class).setBlockUnbreakable().setResistance(6000000.0F);
	}
	
	
	public static void initTileEntities() {
		rawore = new BlockOreVC();
		rawore.setUnlocalizedName(ModInfo.ModID + ":" + raworeName);
		rawore.setHardness(2F);
		
		EnumMaterialDeposit.NATIVEGOLD.block = rawore;
		EnumMaterialDeposit.LIMONITE.block = rawore;
		EnumMaterialDeposit.LIGNITE.block = rawore;
		EnumMaterialDeposit.BITUMINOUSCOAL.block = rawore;
		EnumMaterialDeposit.NATIVECOPPER.block = rawore;
		
		GameRegistry.registerBlock(rawore, raworeName); //.registerSingleState("ore", ItemOre.class);	
		GameRegistry.registerTileEntity(TEOre.class, ModInfo.ModID + ":orete");
	}






	private static void initHardness() {
		rock.setHarvestLevel("pickaxe", 0);
		
		topsoil.setHarvestLevel("shovel", 0);
		subsoil.setHarvestLevel("shovel", 0);
		rawclay.setHarvestLevel("shovel", 0);
		
		regolith.setHarvestLevel("shovel", 1);
		peat.setHarvestLevel("shovel", 1);
		
		
		
		
	}
	

}

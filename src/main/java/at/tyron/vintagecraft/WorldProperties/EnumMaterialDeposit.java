package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.List;

import sun.security.krb5.Realm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.VCBiome;
import at.tyron.vintagecraft.block.BlockRegolith;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockSubSoil;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;

public enum EnumMaterialDeposit implements IStringSerializable, IGenLayerSupplier {
	//						                color  weight hgt vari mind maxd 
	NODEPOSIT (-1, null,                 false, 0,  5000,   0,  0, 0, null, false),
	
	CLAY (0,           BlocksVC.rawclay, false, 20,   70,   1,  1,    2, new VCBiome[]{VCBiome.plains, VCBiome.beach, VCBiome.river, VCBiome.rollingHills}, true),
	PEAT (1,              BlocksVC.peat, false, 40,   70,   1,  1,    2, new VCBiome[]{VCBiome.plains, VCBiome.rollingHills, VCBiome.Mountains, VCBiome.MountainsEdge}, true),
	
	
	LIGNITE (2,        BlocksVC.rawore, true,   60,   40,   2,  10,  50, new VCBiome[]{VCBiome.plains, VCBiome.rollingHills, VCBiome.Mountains, VCBiome.MountainsEdge}, true),
	BITUMINOUSCOAL (3, BlocksVC.rawore, true,   80,   20,   2,  8, 103, new VCBiome[]{VCBiome.rollingHills, VCBiome.Mountains, VCBiome.MountainsEdge}, false),
	
	NATIVECOPPER (4,   BlocksVC.rawore, true,   80,   60,   2,  4,   40, null, true),
	
	LIMONITE (5,      BlocksVC.rawore,  true,  100,   30,   2,  15, 103, null, false),
	NATIVEGOLD (6,    BlocksVC.rawore,  true,  120,   10,   1,  50, 103, null, false),

	
	;

	

	public int id;
	public Block block;
	public boolean hasOre;
	int color;
	public int averageHeight;
	public int minDepth;
	public int maxDepth;
	public EnumMetal smelted;
	public int ore2IngotRatio;
	public final VCBiome[] biomes;
	public int weight;
	public boolean relativeDepth;
	
	private EnumMaterialDeposit(int id, Block block, boolean hasOre, int color, int weight, int averageHeight, int minDepth, int maxDepth, VCBiome[] biomes, boolean relativeDepth) {
		this.id = id;
		
		this.weight = weight;
		this.block = block;
		this.hasOre = hasOre;
		this.averageHeight = averageHeight;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		this.biomes = biomes;
		this.color = color;
		this.relativeDepth = relativeDepth;
	}
	
	
	
	public static EnumMaterialDeposit depositForColor(int color) {
		EnumMaterialDeposit[] deposits = values();
		for (int i = 0; i < deposits.length; i++) {
			if (deposits[i].color == color)
				return deposits[i];
		}
		return null;
	}
	
	
	public boolean isParentMaterial(IBlockState state) {
		if (this == CLAY || this == PEAT) return state.getBlock() instanceof BlockTopSoil || state.getBlock() instanceof BlockSubSoil || state.getBlock() instanceof BlockRegolith;
		
		return state.getBlock() instanceof BlockRock && state.getBlock().getMaterial() == Material.rock;
	}

	
	public static EnumMaterialDeposit byId(int id) {
		EnumMaterialDeposit[] deposits = values();
		for (int i = 0; i < deposits.length; i++) {
			if (deposits[i].id == id)
				return deposits[i];
		}
		return null;
		
	}


	
	@Override
	public int getColor() {
		return color;
	}


	@Override
	public int getWeight() {
		return weight;
	}
	
	
	@Override
	public int getDepthMax() {
		return maxDepth;
	}
	
	@Override
	public int getDepthMin() {
		return minDepth;
	}


	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	
	
    public static String[] getNames() {
    	String[] names = new String[values().length];
    	
    	for (int i = 0; i < values().length; i++) {
    		names[i] = values()[i].name().toLowerCase();
    	}
    	return names;
    }	

        
	
}

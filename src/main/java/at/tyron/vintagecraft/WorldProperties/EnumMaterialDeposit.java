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
import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.block.BlockRegolith;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockSubSoil;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;
import at.tyron.vintagecraft.interfaces.ISoil;

public enum EnumMaterialDeposit implements IStringSerializable, IGenLayerSupplier {
	//     int id, boolean hasOre, int color, int weight, int averageHeight, int minDepth, int maxDepth, boolean relativeDepth
	NODEPOSIT (-1,                  false, 0,  5000,   0,  0, 0),
	
	CLAY (0,           false, 30,   3,   2,  0,    1, true, 155),
	PEAT (1,            false, 40,   8,   2,  0,    1, true, 155),
	
	
	LIGNITE (2,         true,   60,   33,   1,  10,  50, true, 255),
	BITUMINOUSCOAL (3,  true,   80,   25,   1,  8, 103),
	
	NATIVECOPPER (4,    true,   90,   18,   1,  4,   40, true, 255),
	
	LIMONITE (5,       true,  100,   23,   1,  15, 103),
	NATIVEGOLD (6,     true,  120,   10,   1,  50, 103),

	REDSTONE (7,       true,  140,   20,   2,  30, 130),
	
	CASSITERITE (8,	   true,  160, 3, 1, 0, 35, true, 220) 
	;

	

	public int id;
	//Block block;
	public boolean hasOre;
	int color;
	public int height;
	public int minDepth;
	public int maxDepth;
	public EnumMetal smelted;
	public int ore2IngotRatio;
	//public final BiomeVC[] biomes;
	public int weight;
	public boolean relativeDepth;
	public int maxheightOnRelDepth; // 0..255    only relevant on relativeDepth = true 
	
	private EnumMaterialDeposit(int id, boolean hasOre, int color, int weight, int averageHeight, int minDepth, int maxDepth) {
		this(id, hasOre, color, weight, averageHeight, minDepth, maxDepth, false, 255);
	}
	
	private EnumMaterialDeposit(int id, boolean hasOre, int color, int weight, int averageHeight, int minDepth, int maxDepth, boolean relativeDepth, int maxheightOnRelDepth) {
		this.id = id;
		
		this.weight = weight;
		//this.block = block;
		this.hasOre = hasOre;
		this.height = averageHeight;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		//this.biomes = biomes;
		this.color = color;
		this.relativeDepth = relativeDepth;
		this.maxheightOnRelDepth = maxheightOnRelDepth;
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

	/*public Block getBlock() {
		return block;
	}*/
	
	public IBlockState getBlockStateForDepth(int depth, IBlockState parentmaterial) {
		IBlockState state;
		
		switch (this) {
			case PEAT: state = BlocksVC.peat.getDefaultState(); break;
			case CLAY: state = BlocksVC.rawclay.getDefaultState(); break;
			default:
				EnumRockType rocktype = (EnumRockType)parentmaterial.getValue(BlockRock.STONETYPE);
				state = BlocksVC.rawore.getBlockStateFor(this.getName() + "-" + rocktype.getName());
				break;
		}
		

		if (state == null) System.out.println("block state for " + this + " is null!");
		
		
		if (depth > 1 && state.getBlock() instanceof ISoil) {
			return state.withProperty(((ISoil)state.getBlock()).getOrganicLayerProperty(null, null), EnumOrganicLayer.NoGrass);
		}
		
		
		
		return state;
	}
	
	public void init(Block block) {
	//	this.block = block;
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

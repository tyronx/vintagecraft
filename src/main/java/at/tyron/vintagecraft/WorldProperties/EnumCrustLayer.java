package at.tyron.vintagecraft.WorldProperties;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.block.BlockVC;

public enum EnumCrustLayer {
	TOPSOIL (0, 1, 0),
	SUBSOIL (1, 1, 0),
	REGOLITH (2, 2, 0)
	
	/*TOPSOIL (-1, 1, 0),
	SUBSOIL (-1, 1, 0),
	REGOLITH(-1, 2, 1),
	ROCK_1   (0, -1, -1),
	ROCK_2   (1, -1, -1),
	ROCK_3   (2, -1, -1),
	ROCK_4   (3, -1, -1),
	ROCK_5   (4, -1, -1),
	ROCK_6   (5, -1, -1),
	ROCK_7   (6, -1, -1),
	*/
	;
	
	public static final int quantityFixedTopLayers = 3;
	
	public final int dataLayerIndex;
	public final int thickness;
	public final int underwaterThickness;
	
	private EnumCrustLayer(int index, int depthStart, int underwaterDepthStart) {
		this.dataLayerIndex = index;
		this.thickness = depthStart;
		this.underwaterThickness = underwaterDepthStart;
	}
	
	
	public static EnumCrustLayer fromDataLayerIndex(int datalayerindex) {
		for (int i = 0; i < values().length; i++) {
			if (values()[i].dataLayerIndex == datalayerindex) return values()[i];
		}
		return null;
	}
	
	
	public static IBlockState getFixedBlock(EnumRockType rocktype, int x, int y, int z, int depth, int steepness) {
		
		
		switch (depth) { 
			case 0:
				if (y < VCraftWorld.instance.seaLevel - 1) return null;
				return VCraftWorld.instance.getTopLayerAtPos(x, y, z, rocktype, steepness); 
			case 1: return VCraftWorld.instance.getSubLayerAtPos(x, y, z, rocktype, steepness);
			case 2:
			case 3: return VCraftWorld.instance.getReoglithLayerAtPos(x, y, z, rocktype, steepness);
			default: return null;
		}
	}


	public static IBlockState getBlock(Random rand, EnumRockType rocktype, int x, int y, int z, int depth) {
		int crustdepth;
		
		int i = 0;
		IBlockState blockstate = null;
		
		if (y < VCraftWorld.instance.seaLevel - 1) {
			depth--;
		}

		if (depth == 0 && y >= VCraftWorld.instance.seaLevel - 1) {
			blockstate = VCraftWorld.instance.getTopLayerAtPos(x, y, z, rocktype, 0);
		}
		if (blockstate == null && depth <= 1) {
			blockstate = VCraftWorld.instance.getSubLayerAtPos(x, y, z, rocktype, 0);
		}
		if (blockstate == null && depth <= 2 && rand.nextBoolean()) {
			blockstate = VCraftWorld.instance.getSubLayerAtPos(x, y, z, rocktype, 0);
		}
		if (blockstate == null && depth <= 3) {
			blockstate = VCraftWorld.instance.getReoglithLayerAtPos(x, y, z, rocktype, 0);
		}
		if (blockstate == null && depth <= 4 && rand.nextBoolean()) {
			blockstate = VCraftWorld.instance.getReoglithLayerAtPos(x, y, z, rocktype, 0);
		}
			
		if (blockstate == null) {
			BlockClassEntry rock = BlocksVC.rock.getFromKey(rocktype);
			if (rock != null) blockstate = rock.getBlockState();
		}
		
		if (blockstate == null) {
			blockstate = Blocks.stone.getDefaultState();
		}
		
		return blockstate;
	}
	

	
	/*public static EnumCrustLayer crustLayerForDepth(int targetdepth, int[][]rockdata, int arrayIndexChunk, boolean underwater) {
		int depth = 0, i = 0;
		
		for (EnumCrustLayer layer : EnumCrustLayer.values()) {
			if (layer.thickness == -1) {
				depth += (rockdata[i - quantityFixedTopLayers][arrayIndexChunk] >> 16) & 0xff;
			} else {
				depth += underwater ? layer.underwaterThickness : layer.thickness;
			}
			
			if (targetdepth < depth) return layer;
			i++;
		}
		
		return ROCK_7;
	}
	*/
	
}

package at.tyron.vintagecraft.World;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import at.tyron.vintagecraft.WorldGen.VCBiome;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.block.VCBlock;
import at.tyron.vintagecraft.block.VCBlocks;

public enum EnumCrustLayer {
	TOPSOIL (-1, 0, -999),
	SUBSOIL (-1, 1, 0),
	REGOLITH(-1, 2, 1),
	ROCK_1_THIN   (0, 4, 3),
	ROCK_2_THIN   (1, 9, 8),
	ROCK_3_THIN   (2, 14, 13),
	ROCK_4_MEDIUM   (3, 17, 16),
	ROCK_5_LARGE   (4, 35, 34),
	ROCK_6_THIN   (5, 72, 71),
	ROCK_7_LARGE   (6, 80, 79),
	
	;
	
	
	public final int dataLayerIndex;
	public final int depthStart;
	public final int underwaterDepthStart;
	
	private EnumCrustLayer(int index, int depthStart, int underwaterDepthStart) {
		this.dataLayerIndex = index;
		this.depthStart = depthStart;
		this.underwaterDepthStart = underwaterDepthStart;
	}
	
	
	public static EnumCrustLayer fromDataLayerIndex(int datalayerindex) {
		for (int i = 0; i < values().length; i++) {
			if (values()[i].dataLayerIndex == datalayerindex) return values()[i];
		}
		return null;
	}
	
	
	public IBlockState getFixedBlock(EnumRockType rocktype, int depth) {
		switch (this) {
			case TOPSOIL: return depth > 0 ? EnumOrganicLayer.None.getTopSoilVariantForBlock(VCBlocks.topsoil) : EnumOrganicLayer.NormalGrass.getTopSoilVariantForBlock(VCBlocks.topsoil);
			case SUBSOIL: return rocktype.getRockVariantForBlock(VCBlocks.subsoil);
			case REGOLITH: return rocktype.getRockVariantForBlock(VCBlocks.regolith);
			default: return null;
		}
	}
	
	public static EnumCrustLayer crustLayerForDepth(int depth, boolean underwater) {
		EnumCrustLayer curlayer, layer = null;
		for (int i = 0; i < values().length; i++) {
			curlayer = values()[i];
			int depthStart = underwater ? curlayer.underwaterDepthStart : curlayer.depthStart;
			
			if (depth >= depthStart) layer = curlayer;
		}
		return layer;
	}
	

	
}

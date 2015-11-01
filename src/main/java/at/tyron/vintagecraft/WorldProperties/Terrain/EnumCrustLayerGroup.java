package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.ArrayList;
import java.util.Random;

import at.tyron.vintagecraft.World.VCraftWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public enum EnumCrustLayerGroup {
	TOPSOIL     (0, new EnumCrustLayer[]{EnumCrustLayer.L1_TOPSOIL, EnumCrustLayer.L1_SAND, EnumCrustLayer.L1_GRAVEL, EnumCrustLayer.L1_SNOW}),
	SUBSOIL     (1, new EnumCrustLayer[]{EnumCrustLayer.L2_SUBSOIL, EnumCrustLayer.L2_SAND, EnumCrustLayer.L2_ICE}),
	SUBSOIL_RND (2, new EnumCrustLayer[]{EnumCrustLayer.L3_SUBSOIL}),
	REGOLITH    (3, new EnumCrustLayer[]{EnumCrustLayer.L3_REGOLITH, EnumCrustLayer.L3_SAND, EnumCrustLayer.L3_GRAVEL, EnumCrustLayer.L3_PACKEDICE}),
	REGOLITH_RND(4, new EnumCrustLayer[]{EnumCrustLayer.L4_REGOLITH}),
	ICE 	    (5, new EnumCrustLayer[]{EnumCrustLayer.L5_PACKEDICE}),
	ICE2 	    (6, new EnumCrustLayer[]{EnumCrustLayer.L6_PACKEDICE})
	;
	
	
	int id;
	EnumCrustLayer []varieties;
	
	private EnumCrustLayerGroup(int id, EnumCrustLayer []varieties) {
		this.id = id;
		this.varieties = varieties;
	}
	
	
	public static IBlockState[] getTopLayers(EnumRockType rocktype, BlockPos pos, Random rand) {
		EnumCrustLayerGroup layers[] = EnumCrustLayerGroup.values();
		
		ArrayList<IBlockState> selectedlayers = new ArrayList<IBlockState>();
		int []climate = VCraftWorld.instance.getClimate(pos);
		
		int depth = 0;
		if (pos.getY() < VCraftWorld.seaLevel - 1) depth++;
		
		for (int i = depth; i < layers.length; i++) {
			for (EnumCrustLayer variety : layers[i].varieties) {
				if (variety.valid(climate, pos.getY(), rand)) {
					selectedlayers.add(variety.blocktype.getBlock(rocktype, climate));
					break;
				}
			}
		}

		return selectedlayers.toArray(new IBlockState[0]);
	}
	
}

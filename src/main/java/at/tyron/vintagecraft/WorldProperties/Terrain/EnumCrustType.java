package at.tyron.vintagecraft.WorldProperties.Terrain;

import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public enum EnumCrustType {
	TOPSOIL,
	SUBOIL,
	REGOLITH,
	SNOW,
	ICE, 
	PACKEDICE,
	ROCK,
	SAND,
	GRAVEL;
	
	
	
	public IBlockState getBlock(EnumRockType rocktype, int []climate) {
		switch (this) {
			case TOPSOIL:
				EnumOrganicLayer layer = EnumOrganicLayer.fromClimate(climate[2], climate[0]);
				EnumFertility fertility = EnumFertility.fromFertilityValue(climate[1]);
				
				if (fertility == null || fertility == EnumFertility.VERYLOW) {
					fertility = EnumFertility.LOW;
				}
				return BlocksVC.topsoil.getDefaultState().withProperty(BlockTopSoil.organicLayer, layer).withProperty(BlockTopSoil.fertility, fertility);
				
				//break;
				
			case SUBOIL:
				return BlocksVC.subsoil.getBlockStateFor("nograss-" + rocktype.getName());
				
			case REGOLITH:
				return BlocksVC.regolith.getEntryFromKey(rocktype).getBlockState();
				
			case SNOW:
				return Blocks.snow.getDefaultState();
				
			case ICE:
				return Blocks.ice.getDefaultState();
				
			case PACKEDICE:
				return Blocks.packed_ice.getDefaultState();
						
			case SAND:
				return BlocksVC.sand.getEntryFromKey(rocktype).getBlockState();
				
			case GRAVEL:
				return BlocksVC.gravel.getEntryFromKey(rocktype).getBlockState();
				
			case ROCK:
				BlockClassEntry rock = BlocksVC.rock.getEntryFromKey(rocktype);
				if (rock != null) return rock.getBlockState();
				break;
		}
		
		
		
		System.out.println("block not found for " + this + " defaulting to null!! fertility = "  + climate[1]);
		
		return null; //Blocks.stone.getDefaultState();
	}
	
	
}

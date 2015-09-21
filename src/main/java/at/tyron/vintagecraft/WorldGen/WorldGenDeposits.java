package at.tyron.vintagecraft.WorldGen;

import java.util.HashMap;
import java.util.Random;

import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.EnumDepositOccurenceType;
import at.tyron.vintagecraft.WorldProperties.EnumDepositSize;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenDeposits implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX *= 16;
		chunkZ *= 16;
		
		/***** Medium, Large, Huge and Gigantic Deposits *****/
		int[] attemptsPerDeposits = EnumMaterialDeposit.getQuantityAttemptsPerDeposit();
		
		for (int i = 0; i < EnumMaterialDeposit.maxLayerGenAttemptsPerChunk; i++) {
			int[] depositLayer = GenLayerVC.genDeposits(world.getSeed() + i + 1).getInts(chunkX, chunkZ, 16, 16);
			
			createLayerGenDeposit(attemptsPerDeposits, depositLayer, chunkX, chunkZ, random, world);
		}
		
		
		
		/***** Single, Tiny and Small Deposits *****/
		int tries = 6;
		
		for (EnumMaterialDeposit deposit : EnumMaterialDeposit.getMapGenDeposits()) {
			tries = 6;
			while(tries-- > 0) {
				if (random.nextInt(100) < deposit.occurence.minweight) {
					createMapGenDeposit(deposit, world, random, chunkX + 2 + random.nextInt(12), chunkZ + 2 + random.nextInt(12));
				}
			}
		}
		
	
	}

	void createMapGenDeposit(EnumMaterialDeposit deposit, World world, Random rand, int x, int z) {
		BlockPos referencePos;
		
		
		
		boolean belowsealevel =
			deposit.occurence.type == EnumDepositOccurenceType.ANYBELOWSEALEVEL
			|| (deposit.occurence.type == EnumDepositOccurenceType.MIXEDDEPTHS && rand.nextFloat() < deposit.occurence.belowSealLevelRatio)
		;
		
		if (belowsealevel) {
			referencePos = new BlockPos(x, VCraftWorld.seaLevel, z);	
		} else {
			referencePos = world.getHorizon(new BlockPos(x, 0, z));
		}

		int depth = deposit.occurence.mindepth + rand.nextInt(deposit.occurence.maxdepth - deposit.occurence.mindepth);

		/*if (deposit == EnumMaterialDeposit.CLAY || deposit == EnumMaterialDeposit.FIRECLAY || deposit == EnumMaterialDeposit.PEAT) {
			System.out.println("gen " + deposit + " at depth = " + depth + " (ref pos = " + referencePos+")");
		}*/
		

		if (referencePos.getY() - depth <= 1) return;
			
		if (!deposit.isParentMaterial(world.getBlockState(referencePos.down(depth)), referencePos)) return;
		
		
		//if (deposit == EnumMaterialDeposit.NATIVECOPPER) System.out.println(surface + " / " + depth);
		
		generateSmallDeposit(deposit, world, rand, referencePos, depth);
	}
	
	
	void generateSmallDeposit(EnumMaterialDeposit deposit, World world, Random rand, BlockPos surface, int depth) {
		BlockPos pos;
		
		//if (deposit == EnumMaterialDeposit.NATIVEGOLD_QUARTZ || deposit == EnumMaterialDeposit.NATIVEGOLD_QUARTZ || deposit==EnumMaterialDeposit.SYLVITE_ROCKSALT)
		//	System.out.println(deposit + " @ " + surface.down(depth));
		
		//if (deposit == EnumMaterialDeposit.SYLVITE_ROCKSALT) System.out.println("sylvite @ " + surface + " + depth " + depth);
		
		int width = (rand.nextInt(9) + rand.nextInt(9)) / 2;
		
		
		if (deposit.size == EnumDepositSize.TINY) {
			width = 1 + rand.nextInt(3);
		}
		
		if (deposit.size == EnumDepositSize.SINGLE) {
			width = 0;
		}
		
		for (int dx = -width / 2; dx <= width/2; dx++) {
			if (rand.nextInt(8) == 0) depth+=rand.nextInt(2) * 2 - 1;
			
			for (int dz = -width / 2; dz <= width/2; dz++) {
				pos = surface.add(dx, -depth, dz);
				
				if (pos.getY() > 0 && dx * dx + dz * dz <= width * width && deposit.isParentMaterial(world.getBlockState(pos), pos)) {
					world.setBlockState(pos, deposit.getBlockStateForPos(world.getBlockState(pos), world, pos), 2);
				}
				
				if (rand.nextInt(8) == 0) depth+=rand.nextInt(2) * 2 - 1;
			}
		}
	}
	
	
	
	
	
	private void createLayerGenDeposit(int []attemptsPerDeposit, int[] depositLayer, int xCoord, int zCoord, Random random, World world) {
		BlockPos pos;
		IBlockState parentmaterial;
		
		int[] drawnDeposits = new int[EnumMaterialDeposit.values().length];
		
		HashMap<BlockPos, EnumMaterialDeposit> subdeposits = new HashMap<BlockPos, EnumMaterialDeposit>();
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int depositColor = depositLayer[x+z*16] & 0xFF;
				if (depositColor == 0) continue;
				
				EnumMaterialDeposit deposit = EnumMaterialDeposit.byColor(depositColor);
				
				if (attemptsPerDeposit[deposit.ordinal()] <= 0) continue;
				
				drawnDeposits[deposit.ordinal()] = 1;
				
				// Depositlayer:
				// r    bits 16 - 23   reference depth of deposit
				// g    bits 8 - 15    height variation of deposit (between 0 and 14)
				// b    bits 0 - 7     type of deposit
				
				int baseDepth = (depositLayer[x+z*16] >> 16) & 0xFF;
				int depthVariation = ((depositLayer[x+z*16] >> 8) & 0xFF) - 7;
				
				
				int horizon = VCraftWorld.instance.seaLevel;
				
				if (deposit.occurence.type == EnumDepositOccurenceType.ANYRELATIVEDEPTH || deposit.occurence.type == EnumDepositOccurenceType.FOLLOWSURFACE) {
					horizon = HeightmapCache.getHeightAt(world.provider.getDimensionId(), xCoord+x, zCoord + z); 
					
					if (horizon > deposit.occurence.untilyheight) continue;
				}
				

				int yCoord = horizon - (baseDepth + depthVariation);
				
				// Discard height variation for near surface deposits
				if (deposit.occurence.type == EnumDepositOccurenceType.FOLLOWSURFACE) {
					yCoord = horizon - baseDepth;
				}
				

				
				
				pos = new BlockPos(xCoord + x, yCoord, zCoord + z);

				
				
				
				if (pos.getY() < 1) continue;
				
				if (deposit.subdeposits != null) {
					for (EnumMaterialDeposit subdeposit : deposit.subdeposits) {
						if (subdeposit.occurence.minweight > random.nextInt(100) * 150) {
							subdeposits.put(pos, subdeposit);
						}
					}
				}
				
				int hgt = deposit.occurence.height;
				while (pos.getY() > 0 && deposit.isParentMaterial(parentmaterial = world.getBlockState(pos), pos) && hgt-- > 0) {
					world.setBlockState(pos, deposit.getBlockStateForPos(parentmaterial, world, pos), 2);
					pos = pos.down();
				}
				

			}
		}
		
		
		for (int i = 0; i < attemptsPerDeposit.length; i++) {
			if (drawnDeposits[i] > 0) {
				attemptsPerDeposit[i]--;
			}
		}
		
		
		for (BlockPos spos : subdeposits.keySet()) {
			generateSmallDeposit(subdeposits.get(spos), world, random, spos, 0);
		}
				
	}
}

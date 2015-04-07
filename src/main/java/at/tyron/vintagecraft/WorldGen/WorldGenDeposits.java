package at.tyron.vintagecraft.WorldGen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.BlockOreVC;
import at.tyron.vintagecraft.Block.BlockRock;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.Helper.DepositOccurence;
import at.tyron.vintagecraft.WorldProperties.EnumDepositOccurenceType;
import at.tyron.vintagecraft.WorldProperties.EnumDepositSize;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.asm.transformers.DeobfuscationTransformer;

public class WorldGenDeposits implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		chunkX *= 16;
		chunkZ *= 16;
		//if (true) return;
		
		EnumMaterialDeposit []deposits = EnumMaterialDeposit.values();
		int[] depositsPerChunk = new int[deposits.length]; 
		
		int depositsMax = 0;
		
		//if (true) return;
		
		for (int i = 0; i < depositsPerChunk.length; i++) {
			depositsPerChunk[i] = Math.max(1, (deposits[i].occurence.maxdepth - deposits[i].occurence.mindepth) / 8);
			depositsMax = Math.max(depositsMax, depositsPerChunk[i]);
		}
		
		
		/***** Large and Huge Deposits *****/
		
		for (int i = 0; i < depositsMax; i++) {
			int[] depositLayer = GenLayerVC.genDeposits(world.getSeed() + i + 1).getInts(chunkX, chunkZ, 16, 16);
			
			generateLargeDeposits(depositsPerChunk, depositLayer, chunkX, chunkZ, random, world);
		}
		
		
		
		/***** Single, Tiny and Small Deposits *****/
		int tries = 6;
		
		for (EnumMaterialDeposit deposit : EnumMaterialDeposit.values()) {
			if (deposit.size != EnumDepositSize.SMALL && deposit.size != EnumDepositSize.SMALLANDLARGE && deposit.size != EnumDepositSize.TINY && deposit.size != EnumDepositSize.SINGLE) continue;
			if (deposit.occurence.type == EnumDepositOccurenceType.INDEPOSIT || deposit.occurence.type == EnumDepositOccurenceType.NODEPOSIT) continue;   
			
			tries = 6;
			while(tries-- > 0) {
				if (random.nextInt(100) < deposit.occurence.weight) {
					generateSmallDeposit(deposit, world, random, chunkX + 2 + random.nextInt(12), chunkZ + 2 + random.nextInt(12));
				}
			}
		}
		
	
	}

	void generateSmallDeposit(EnumMaterialDeposit deposit, World world, Random rand, int x, int z) {
		BlockPos surface;
		
		//System.out.println("gen " + deposit);
		
		boolean underground =
			deposit.occurence.type == EnumDepositOccurenceType.ANYBELOWSEALEVEL
			|| (deposit.occurence.type == EnumDepositOccurenceType.MIXEDDEPTHS && rand.nextFloat() < deposit.occurence.belowSealLevelRatio)
		;
		
		if (underground) {
			surface = world.getHorizon(new BlockPos(x, 0, z));	
		} else {
			surface = new BlockPos(x, VCraftWorld.seaLevel, z);
		}

		int depth = deposit.occurence.mindepth + rand.nextInt(deposit.occurence.maxdepth - deposit.occurence.mindepth);
		
		if (surface.getY() - depth <= 1) return;
			
		if (!deposit.isParentMaterial(world.getBlockState(surface.down(depth)), surface)) return;
		
		
		//if (deposit == EnumMaterialDeposit.NATIVECOPPER) System.out.println(surface + " / " + depth);
		
		generateSmallDeposit(deposit, world, rand, surface, depth);
	}
	
	
	void generateSmallDeposit(EnumMaterialDeposit deposit, World world, Random rand, BlockPos surface, int depth) {
		BlockPos pos;
		
		//if (deposit == EnumMaterialDeposit.NATIVEGOLD_QUARTZ || deposit == EnumMaterialDeposit.NATIVEGOLD_QUARTZ || deposit==EnumMaterialDeposit.SYLVITE_ROCKSALT)
		//	System.out.println(deposit + " @ " + surface.down(depth));
		
		//if (deposit == EnumMaterialDeposit.NATIVECOPPER) System.out.println("overground copper @ " + surface + " + depth " + depth);
		
		int width = (rand.nextInt(9) + rand.nextInt(9)) / 2;
		if (deposit.size == EnumDepositSize.TINY) width = 1 + rand.nextInt(3);
		if (deposit.size == EnumDepositSize.SINGLE) width = 0;
		
		for (int dx = -width / 2; dx <= width/2; dx++) {
			if (rand.nextInt(8) == 0) depth+=rand.nextInt(2) * 2 - 1;
			
			for (int dz = -width / 2; dz <= width/2; dz++) {
				pos = surface.add(dx, -depth, dz);
				
				if (pos.getY() > 0 && dx * dx + dz * dz <= width * width && deposit.isParentMaterial(world.getBlockState(pos), pos)) {
					
					world.setBlockState(pos, deposit.getBlockStateForDepth(depth, world.getBlockState(pos)), 2);
				}
				
				if (rand.nextInt(8) == 0) depth+=rand.nextInt(2) * 2 - 1;
			}
		}
	}
	
	
	
	private void generateLargeDeposits(int []depositsPerChunk, int[] depositLayer, int xCoord, int zCoord, Random random, World world) {
		BlockPos pos;
		IBlockState parentmaterial;
		
		int[] drawnDeposits = new int[EnumMaterialDeposit.values().length];
		
		HashMap<BlockPos, EnumMaterialDeposit> subdeposits = new HashMap<BlockPos, EnumMaterialDeposit>();
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int depositColor = depositLayer[x+z*16] & 0xFF;
				if (depositColor == 0) continue;
				
				EnumMaterialDeposit deposit = EnumMaterialDeposit.byColor(depositColor);
				
				if (depositsPerChunk[deposit.id] <= 0) continue;
				
				drawnDeposits[deposit.id] = 1;
				
				
				int depositDepth = ((depositLayer[x+z*16] >> 16) & 0xFF) + (((depositLayer[x+z*16] >> 8) & 0xFF) - 7);

				
				int horizon = VCraftWorld.instance.seaLevel;
				
				if (deposit.occurence.type == EnumDepositOccurenceType.ANYRELATIVEDEPTH) {
					horizon = world.getChunkFromChunkCoords(xCoord >> 4, zCoord >> 4).getHeight(x, z);
					
					if (horizon > deposit.occurence.untilyheight) continue;
				}
				
				pos = new BlockPos(xCoord + x, depositDepth = Math.max(horizon - deposit.occurence.maxdepth, horizon - depositDepth), zCoord + z);
				//spos = new BlockPos(xCoord + x, depositDepth = Math.max(horizon - deposit.maxDepth, horizon - depositDepth), zCoord + z);
				
				if (pos.getY() < 1) continue;
				
				if (deposit.subdeposits != null) {
					for (EnumMaterialDeposit subdeposit : deposit.subdeposits) {
						if (subdeposit.occurence.weight > random.nextInt(100) * 150) {
							subdeposits.put(pos, subdeposit);
						}
					}
				}
				
				
				if (deposit.isParentMaterial(parentmaterial = world.getBlockState(pos), pos)) {
					int hgt = deposit.occurence.height;
					
					while (hgt-- > 0) {
						if (pos.getY() < 1) continue;
						world.setBlockState(pos, deposit.getBlockStateForDepth(horizon - depositDepth, parentmaterial), 2);
						//if (deposit == EnumMaterialDeposit.FIRECLAY) System.out.println("fireclay @ " + pos);
						pos = pos.down();
						depositDepth--;
					}
				}
				

			}
		}
		
		for (int i = 0; i < depositsPerChunk.length; i++) {
			if (drawnDeposits[i] > 0) depositsPerChunk[i]--;
		}
		
		
		for (BlockPos spos : subdeposits.keySet()) {
			generateSmallDeposit(subdeposits.get(spos), world, random, spos, 0);
		}
				
	}
}

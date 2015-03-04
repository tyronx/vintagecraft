package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.VintageCraftConfig;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldGen.GenLayers.GenLayerVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockOreVC;
import at.tyron.vintagecraft.block.BlockRock;
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
			depositsPerChunk[i] = Math.max(1, (deposits[i].maxDepth - deposits[i].minDepth) / 8);
			depositsMax = Math.max(depositsMax, depositsPerChunk[i]);
		}
		
		for (int i = 0; i < depositsMax; i++) {
			int[] depositLayer = GenLayerVC.genDeposits(world.getSeed() + i + 1).getInts(chunkX, chunkZ, 16, 16);
			
			generateDeposits(depositsPerChunk, depositLayer, chunkX, chunkZ, random, world);
		}
		
		

	
	}

	private void generateDeposits(int []depositsPerChunk, int[] depositLayer, int xCoord, int zCoord, Random random, World world) {
		BlockPos pos;
		IBlockState parentmaterial;
		
		int[] drawnDeposits = new int[EnumMaterialDeposit.values().length];
		
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int depositColor = depositLayer[x+z*16] & 0xFF;
				if (depositColor == 0) continue;
				
				EnumMaterialDeposit deposit = EnumMaterialDeposit.depositForColor(depositColor);
				
				if (depositsPerChunk[deposit.id] <= 0) continue;
				
				drawnDeposits[deposit.id] = 1;
				
				
				int depositDepth = ((depositLayer[x+z*16] >> 16) & 0xFF) + (((depositLayer[x+z*16] >> 8) & 0xFF) - 7);

				
				int horizon = VCraftWorld.instance.seaLevel;
				if (deposit.relativeDepth) {
					horizon = world.getChunkFromChunkCoords(xCoord >> 4, zCoord >> 4).getHeight(x, z);
					if (horizon > deposit.maxheightOnRelDepth) continue;
				}
				
				pos = new BlockPos(xCoord + x, depositDepth = Math.max(1, Math.max(horizon - deposit.maxDepth, horizon - depositDepth)), zCoord + z);
				
				if (deposit.isParentMaterial(parentmaterial = world.getBlockState(pos))) {
					int hgt = deposit.height;
					//if(deposit == EnumMaterialDeposit.CLAY) System.out.println(horizon - depositDepth);
					while (hgt-- > 0) {
						world.setBlockState(pos, deposit.getBlockStateForDepth(horizon - depositDepth, parentmaterial), 2);
						pos = pos.down();
						depositDepth--;
					}
				}
			}
		}
		
		for (int i = 0; i < depositsPerChunk.length; i++) {
			if (drawnDeposits[i] > 0) depositsPerChunk[i]--;
		}
	}
}

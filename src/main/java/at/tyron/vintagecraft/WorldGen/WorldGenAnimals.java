package at.tyron.vintagecraft.WorldGen;


import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.World.VCBiome;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;


public class WorldGenAnimals {
	// Broken in Block.class no idea why T_T
    public static boolean canCreatureSpawn(Block block, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
        IBlockState state = world.getBlockState(pos);
        if (block instanceof BlockSlab) {
            return (block.isFullBlock() || state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP);
        }
        else if (block instanceof BlockStairs) {
            return state.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP;
        }
        return block.isSideSolid(world, pos, EnumFacing.UP);
    }
    
    
	/**
	 * Returns whether or not the specified creature type can spawn at the specified location.
	 */
	public static boolean canCreatureTypeSpawnAtLocation(EnumCreatureType creatureType, World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		Block belowblock = world.getBlockState(pos.down(1)).getBlock();
		Block aboveblock = world.getBlockState(pos.up(1)).getBlock();
				
		if (creatureType.getCreatureClass() == EntityWaterMob.class) {
			return block.getMaterial().isLiquid() && belowblock.getMaterial().isLiquid() && !aboveblock.isNormalCube();
		}
			
		boolean spawnBlock = block != null && canCreatureSpawn(belowblock, world, pos.down(1), SpawnPlacementType.ON_GROUND);
		
		//System.out.println("tests: " + spawnBlock);
		
		
		return spawnBlock && block != Blocks.bedrock &&
				!block.isNormalCube() &&
				!block.getMaterial().isLiquid() &&
				!aboveblock.isNormalCube();
		
	}

	/**
	 * Called during chunk generation to spawn initial creatures.
	 */
	public static void performWorldGenSpawning(World world, VCBiome biome, int par2, int par3, int par4, int par5, Random par6Random) {
		List list = ChunkProviderGenerateVC.getCreatureSpawnsByChunk(world, biome, par2, par3);//par1BiomeGenBase.getSpawnableList(EnumCreatureType.creature);
		if (!list.isEmpty()) {
			while (par6Random.nextFloat() < biome.getSpawningChance()) {
				
				SpawnListEntry spawnlistentry = (SpawnListEntry)WeightedRandom.getRandomItem(world.rand, list);
				IEntityLivingData entitylivingdata = null;
				int i1 = spawnlistentry.minGroupCount + par6Random.nextInt(1 + spawnlistentry.maxGroupCount - spawnlistentry.minGroupCount);
				int j1 = par2 + par6Random.nextInt(par4);
				int k1 = par3 + par6Random.nextInt(par5);
				int l1 = j1;
				int i2 = k1;

				for (int j2 = 0; j2 < i1; ++j2) {
					boolean flag = false;
					for (int k2 = 0; !flag && k2 < 4; ++k2) {
						
						BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));
						//System.out.println("canCreatureTypeSpawnAtLocation " + pos + ": " + canCreatureTypeSpawnAtLocation(EnumCreatureType.CREATURE, world, pos));
						if (canCreatureTypeSpawnAtLocation(EnumCreatureType.CREATURE, world, pos)) {
							
							float f = j1 + 0.5F;
							float f1 = pos.getY();
							float f2 = k1 + 0.5F;
							EntityLiving entityliving;

							try
							{
								entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {world});
							}
							catch (Exception exception)
							{
								exception.printStackTrace();
								continue;
							}
							
					//		System.out.println("spawning " + entityliving.getName() + " at " + f + "/" + f1 + "/" + f2);
						
							entityliving.setLocationAndAngles(f, f1, f2, par6Random.nextFloat() * 360.0F, 0.0F);
							world.spawnEntityInWorld(entityliving);
							entitylivingdata = entityliving.func_180482_a(world.getDifficultyForLocation(new BlockPos(entityliving)), entitylivingdata);
							flag = true;
						}

						j1 += par6Random.nextInt(5) - par6Random.nextInt(5);
						for (k1 += par6Random.nextInt(5) - par6Random.nextInt(5);
								j1 < par2 || j1 >= par2 + par4 || k1 < par3 || k1 >= par3 + par4;
								k1 = i2 + par6Random.nextInt(5) - par6Random.nextInt(5))
						{
							j1 = l1 + par6Random.nextInt(5) - par6Random.nextInt(5);
						}
					}
				}
			}
		}
	}
}

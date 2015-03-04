package at.tyron.vintagecraft.WorldGen;


import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.World.BiomeVC;
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
	public static void performWorldGenSpawning(World world, BiomeVC biome, int xCoord, int zCoord, int xSize, int zSize, Random rand) {
		List list = ChunkProviderGenerateVC.getCreatureSpawnsByChunk(world, biome, xCoord, zCoord); //par1BiomeGenBase.getSpawnableList(EnumCreatureType.creature);
		if (!list.isEmpty()) {
			while (rand.nextFloat() < 0.1f) { //biome.getSpawningChance()) {
				
				SpawnListEntry spawnlistentry = (SpawnListEntry)WeightedRandom.getRandomItem(world.rand, list);
				IEntityLivingData entitylivingdata = null;
				int quantity = spawnlistentry.minGroupCount + rand.nextInt(1 + spawnlistentry.maxGroupCount - spawnlistentry.minGroupCount);
				int x = xCoord + rand.nextInt(xSize);
				int z = zCoord + rand.nextInt(zSize);
				int l1 = x;
				int i2 = z;

				for (int i = 0; i < quantity; ++i) {
					boolean flag = false;
					for (int k2 = 0; !flag && k2 < 4; ++k2) {
						
						BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
						//System.out.println("canCreatureTypeSpawnAtLocation " + pos + ": " + canCreatureTypeSpawnAtLocation(EnumCreatureType.CREATURE, world, pos));
						if (canCreatureTypeSpawnAtLocation(EnumCreatureType.CREATURE, world, pos)) {
							
							float f = x + 0.5F;
							float f1 = pos.getY();
							float f2 = z + 0.5F;
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
						
							entityliving.setLocationAndAngles(f, f1, f2, rand.nextFloat() * 360.0F, 0.0F);
							world.spawnEntityInWorld(entityliving);
							entitylivingdata = entityliving.func_180482_a(world.getDifficultyForLocation(new BlockPos(entityliving)), entitylivingdata);
							flag = true;
						}

						x += rand.nextInt(5) - rand.nextInt(5);
						for (z += rand.nextInt(5) - rand.nextInt(5); x < xCoord || x >= xCoord + xSize || z < zCoord || z >= zCoord + xSize; z = i2 + rand.nextInt(5) - rand.nextInt(5)) {
							x = l1 + rand.nextInt(5) - rand.nextInt(5);
						}
					}
				}
			}
		}
	}
}

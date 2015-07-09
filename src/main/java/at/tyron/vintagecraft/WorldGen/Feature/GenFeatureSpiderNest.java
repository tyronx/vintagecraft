package at.tyron.vintagecraft.WorldGen.Feature;

import java.util.Random;

import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrass;
import at.tyron.vintagecraft.Block.Organic.BlockVineVC;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class GenFeatureSpiderNest {
	/*
	 * Generates Spider Nests in
	 * - Thick forests temperate forests
	 * 
	 * Features:
	 * - Spawn cobwebs over a radius of 20 blocks, with gauss distribution
	 * - Spawn 1-3 spider nests = retextured monster spawner which spawns Forest Spiders 
	 * - Forests spiders = Cave spiders without posion effect
	 * 
	 */
	
	
	public static BlockPos toSurface(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		while (block instanceof BlockVineVC || block instanceof BlockLeavesVC || block instanceof BlockTallGrass || block == Blocks.air ) {
			pos = pos.down();
			block = world.getBlockState(pos).getBlock();
			if (pos.getY() == 0) return null;
		}
		return pos.up();
	}
	
	public static void generate(Random random, World world, BlockPos center) {
		int tries = 2000;
		
		int quantityCobWebs = 20 + (random.nextInt(80) + random.nextInt(80))/2;
		int quantityEggs = Math.max(1, (random.nextInt(4) + random.nextInt(4))/2);
		
		int radius = 15 + (random.nextInt(40) + random.nextInt(40))/2;
		
		center = toSurface(world, center);
		if (center == null || world.getBlockState(center.down()).getBlock().getMaterial() == Material.water) return;
	//	System.out.println("will place " + quantityCobWebs + " at " + center);
		
		BlockPos pos;
		while (quantityCobWebs > 0 && tries-- > 0) {
			pos = center.add(
				(random.nextInt(radius) + random.nextInt(radius)) - radius, 
				0, 
				(random.nextInt(radius) + random.nextInt(radius)) - radius
			);

			pos = toSurface(world, pos);
			if (pos == null) return;
			
			pos = pos.add(0, random.nextInt(4) + random.nextInt(4), 0);

			if (!world.isAirBlock(pos)) continue;
			
			if (
				!world.getBlockState(pos.east()).getBlock().isPassable(world, pos.east()) ||
				!world.getBlockState(pos.west()).getBlock().isPassable(world, pos.west()) ||
				!world.getBlockState(pos.north()).getBlock().isPassable(world, pos.north()) ||
				!world.getBlockState(pos.south()).getBlock().isPassable(world, pos.south()) ||
				!world.getBlockState(pos.up()).getBlock().isPassable(world, pos.up())
			) {
				
				quantityCobWebs--;
				
				world.setBlockState(pos, Blocks.web.getDefaultState(), 2);
				
			}
			
		}
		
		
		
		while (quantityEggs > 0 && tries-- > 0) {
			pos = center.add(random.nextInt(radius) - radius/2, 0, random.nextInt(radius) - radius/2);
			pos = toSurface(world, pos);
			if (pos == null || !world.isAirBlock(pos) || world.getBlockState(pos.down()).getBlock().getMaterial() == Material.water) continue;

			
			quantityCobWebs = 25 + random.nextInt(20) + random.nextInt(20);
			int tries2 = 80;
			BlockPos webpos;
			while (quantityCobWebs > 0 && tries2-- > 0) {
				webpos = pos.add(random.nextInt(7)-3, random.nextInt(4)-2, random.nextInt(7)-3);
				if (world.isAirBlock(webpos)) {
					if (
							!world.getBlockState(webpos.east()).getBlock().isPassable(world, webpos.east()) ||
							!world.getBlockState(webpos.west()).getBlock().isPassable(world, webpos.west()) ||
							!world.getBlockState(webpos.north()).getBlock().isPassable(world, webpos.north()) ||
							!world.getBlockState(webpos.south()).getBlock().isPassable(world, webpos.south()) ||
							!world.getBlockState(webpos.up()).getBlock().isPassable(world, webpos.up()) ||
							!world.getBlockState(webpos.down()).getBlock().isPassable(world, webpos.down())
						) {

						quantityCobWebs--;
						world.setBlockState(webpos, Blocks.web.getDefaultState());
					}
				}
			}
			
			//System.out.println("egg @ " + pos);
			world.setBlockState(pos, BlocksVC.spiderEgg.getDefaultState());
			quantityEggs--;
		}
		
		
		
	}
	
	
}

package at.tyron.vintagecraft.Interfaces;

import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockSoil {

	
	boolean canSpreadGrass(World world, BlockPos pos);
	boolean canGrowTree(World world, BlockPos pos, EnumTree tree);
	boolean canGrowGrass(World world, BlockPos pos);
	boolean canGrowTallGrass(World world, BlockPos pos);
	
	EnumFertility getFertility(World world, BlockPos pos);
	EnumOrganicLayer getOrganicLayer(World world, BlockPos pos);
	
	void setOrganicLayer(EnumOrganicLayer layer, World world, BlockPos pos);
	

}

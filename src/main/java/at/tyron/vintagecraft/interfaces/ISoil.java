package at.tyron.vintagecraft.interfaces;

import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface ISoil {

	
	boolean canSpreadGrass(World world, BlockPos pos);
	boolean canGrowTree(World world, BlockPos pos, EnumTree tree);
	boolean canGrowGrass(World world, BlockPos pos);
	
	EnumFertility getFertility(World world, BlockPos pos);
	
	IProperty getOrganicLayerProperty(World world, BlockPos pos);
}

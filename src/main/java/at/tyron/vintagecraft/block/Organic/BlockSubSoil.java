package at.tyron.vintagecraft.Block.Organic;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.Block.BlockRock;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.OreClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.BlockClass.SoilRockClassEntry;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSubSoil extends BlockRock implements IBlockSoil {
	public BlockSubSoil() {
		super(Material.ground);
		setTickRandomly(true);
	}


    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
    
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return plantable.getPlantType(world, pos) == EnumPlantType.Plains;
	}


	@Override
	public BlockClass getBlockClass() {
		return BlocksVC.subsoil;
	}

	
	
	
	@Override
	public int multistateAvailableTypes() {
		return 16;
	}
	
	
	
	
	
    // 1 = max speed, 0 = doesn't grow
    public float grassGrowthSpeed() {
    	return 0.3f;
    }


	@Override
	public boolean canSpreadGrass(World world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canGrowTree(World world, BlockPos pos, EnumTree tree) {
		return true;
	}

	@Override
	public boolean canGrowGrass(World world, BlockPos pos) {
		return true;
	}


	@Override
	public boolean canGrowTallGrass(World world, BlockPos pos) {
		return false;
	}

	@Override
	public EnumFertility getFertility(World world, BlockPos pos) {
		return EnumFertility.VERYLOW;
	}

	
	
	@Override
	public EnumOrganicLayer getOrganicLayer(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		
    	if (!BlocksVC.subsoil.containsBlock(state.getBlock())) {
    		System.out.println(state.getBlock() + " is ont in " + BlocksVC.subsoil);
    		return null;
    	}
    	
     	String[] type = ((SoilRockClassEntry)state.getValue(ROCKTYPE)).getName().split("-");
     	EnumOrganicLayer organiclayer = EnumOrganicLayer.valueOf(type[0].toUpperCase());
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase());

		return organiclayer;
	}
	
	@Override
	public void setOrganicLayer(EnumOrganicLayer layer, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		
    	if (!BlocksVC.subsoil.containsBlock(state.getBlock())) return;
    	
     	String[] type = ((SoilRockClassEntry)state.getValue(ROCKTYPE)).getName().split("-");
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase());
     	
		world.setBlockState(pos, BlocksVC.subsoil.getBlockStateFor(layer.getStateName() + "-" + rocktype.getStateName()));
	}



	
	

}

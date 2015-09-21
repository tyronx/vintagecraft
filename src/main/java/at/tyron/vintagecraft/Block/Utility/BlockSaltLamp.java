package at.tyron.vintagecraft.Block.Utility;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.TileEntity.TEBlastPowderSack;

public class BlockSaltLamp extends BlockVC {

	public BlockSaltLamp() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
		setLightLevel(0.75f);
	}

	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	
	@Override
	public int getRenderType() {
		return 3;
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		AxisAlignedBB bounds = getCollisionBoundingBox(null, pos, worldIn.getBlockState(pos));
		setBlockBounds((float)bounds.minX - pos.getX(), (float)bounds.minY - pos.getY(), (float)bounds.minZ - pos.getZ(), (float)bounds.maxX - pos.getX(), (float)bounds.maxY - pos.getY(), (float)bounds.maxZ - pos.getZ());
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.375, pos.getY(), pos.getZ() + 0.375, pos.getX() + 0.625f, pos.getY() + 0.375f, pos.getZ() + 0.625f);
	}

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}

	
	public boolean suitableGround(World world, BlockPos groundpos) {
		return world.isSideSolid(groundpos, EnumFacing.UP);
	}

	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!suitableGround(world, pos.down())) {
			world.destroyBlock(pos, true);
		}
	}

	

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(world, pos) && suitableGround(world, pos.down());
	}
	
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

    public boolean isFullCube() {
    	return false;
    }
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}
	
	// Why is this not getting called??
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		dropBlockAsItem(worldIn, pos, state, 0);
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}

}

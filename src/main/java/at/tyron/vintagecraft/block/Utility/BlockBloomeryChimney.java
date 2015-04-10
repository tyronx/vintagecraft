package at.tyron.vintagecraft.Block.Utility;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.TileEntity.TEBloomery;
import at.tyron.vintagecraft.World.ItemsVC;

public class BlockBloomeryChimney extends BlockVC implements IBlockItemSink {

	public BlockBloomeryChimney() {
		super(Material.rock);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public boolean isFullBlock() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemsVC.fireclay_brick;
	}
	
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 2 + random.nextInt(2);
	}
	
	
	public boolean hasBase(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() instanceof BlockBloomeryBase;
	}
	
	/*@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!hasBase(world, pos)) return false;
		return world.getBlockState(pos.down()).getBlock().onBlockActivated(world, pos.down(), world.getBlockState(pos.down()), entityplayer, EnumFacing.UP, hitX, 1f, hitZ);
	}*/
	

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.0625f, pos.getY(), pos.getZ() + 0.0625f, pos.getX() + 0.9375f, pos.getY() + 0.625f, pos.getZ() + 0.9375f);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, null);
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		this.setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 0.625f, 0.9375f);
	}

	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (!player.isSneaking() || !hasBase(world, pos)) return false;
		
		return ((IBlockItemSink)world.getBlockState(pos.down()).getBlock()).tryPutItemstack(world, pos, player, side, itemstack);
	}

}

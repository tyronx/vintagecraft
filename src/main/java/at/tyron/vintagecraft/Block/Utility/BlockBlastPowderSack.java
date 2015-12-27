package at.tyron.vintagecraft.Block.Utility;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.Block.IBlockIgniteable;
import at.tyron.vintagecraft.TileEntity.TEBlastPowderSack;

public class BlockBlastPowderSack extends BlockContainerVC implements IBlockIgniteable {

	public BlockBlastPowderSack() {
		super(Material.tnt);
		setCreativeTab(VintageCraft.resourcesTab);
	}

	@Override
	public String getSubType(ItemStack stack) {
		
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEBlastPowderSack();
	}

	@Override
	public boolean ignite(World world, BlockPos pos, ItemStack firestarter) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEBlastPowderSack) {
			return ((TEBlastPowderSack)te).tryIgnite();
		}
		return false;
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
		return AxisAlignedBB.fromBounds(pos.getX() + 0.3125, pos.getY(), pos.getZ() + 0.3125, pos.getX() + 0.6875f, pos.getY() + 0.625f, pos.getZ() + 0.6875f);
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
	    	TileEntity te = world.getTileEntity(pos);
			if (te instanceof TEBlastPowderSack) {
				((TEBlastPowderSack)te).groundRemoved();
			} else {
				world.destroyBlock(pos, true);
			}

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
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
	
    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }
    
    @Override
    public void onBlockExploded(World worldIn, BlockPos pos, Explosion explosionIn) {
    	TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TEBlastPowderSack) {
			((TEBlastPowderSack)te).destroyedByExplosion();
		}
    	super.onBlockExploded(worldIn, pos, explosionIn);
    	
    }
	

}

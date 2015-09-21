package at.tyron.vintagecraft.Block.Utility;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Item.ItemWoodBucket;
import at.tyron.vintagecraft.TileEntity.TEWoodBucket;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumBucketContents;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWoodBucketVC extends BlockContainerVC {

	public BlockWoodBucketVC() {
		super(Material.wood);
		setCreativeTab(VintageCraft.toolsarmorTab);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
        for (EnumTree treetype : EnumTree.values()) {
        	if (treetype.jankahardness > 800) {
        		list.add(((ItemWoodBucket)itemIn).withTreeTypeAndBucketContents(treetype, EnumBucketContents.EMPTY));
        	}
        }
        
        list.add(((ItemWoodBucket)itemIn).withTreeTypeAndBucketContents(EnumTree.OAK, EnumBucketContents.WATER));

	}

	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEWoodBucket();
	}

	
	@Override
	public int getRenderType() {
		return -1;
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		AxisAlignedBB bounds = getCollisionBoundingBox(null, pos, worldIn.getBlockState(pos));
		setBlockBounds((float)bounds.minX - pos.getX(), (float)bounds.minY - pos.getY(), (float)bounds.minZ - pos.getZ(), (float)bounds.maxX - pos.getX(), (float)bounds.maxY - pos.getY(), (float)bounds.maxZ - pos.getZ());
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.1875, pos.getY(), pos.getZ() + 0.1875, pos.getX() + 0.8125f, pos.getY() + 0.875f, pos.getZ() + 0.8125f);
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
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TEWoodBucket) {
			TEWoodBucket bucket = (TEWoodBucket)te;
			
			bucket.refreshModel = true;
		}
		
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}

	
	
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
		return ret;
	}
	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = (TileEntity)worldIn.getTileEntity(pos);
		if (te instanceof TEWoodBucket) {
			TEWoodBucket bucket = (TEWoodBucket)te;

			ItemWoodBucket item = (ItemWoodBucket)Item.getItemFromBlock(BlocksVC.woodbucket);
			ItemStack itemstack = item.withTreeTypeAndBucketContents(bucket.treetype, bucket.contents);
			
			spawnAsEntity(worldIn, pos, itemstack);
		}
		
		super.breakBlock(worldIn, pos, state);
	}

}

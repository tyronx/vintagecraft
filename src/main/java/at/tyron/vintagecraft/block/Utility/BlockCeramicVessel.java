package at.tyron.vintagecraft.Block.Utility;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Item.ItemCeramicVessel;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCeramicVessel extends BlockContainerVC {

	public BlockCeramicVessel() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	public BlockCeramicVessel(Material mat) {
		super(mat);
		setCreativeTab(CreativeTabs.tabTools);
	}
	

	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEVessel(); 
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
	public boolean isOpaqueCube() {
		return false;
	}	
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TEVessel) {
        	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
    		ItemCeramicVessel.putItemStacks(itemstack, ((TEVessel)tileentity).getContents());
    		
            worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemstack));
        }

        super.breakBlock(worldIn, pos, state);
    }
    
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new java.util.ArrayList<ItemStack>();
	}


	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.29f, pos.getY(), pos.getZ() + 0.29f, pos.getX() + 0.72f, pos.getY() + 0.7f, pos.getZ() + 0.72f);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, null);
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		this.setBlockBounds(0.29f, 0f, 0.29f, 0.72f, 0.5f, 0.72f);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (! (this instanceof BlockClayVessel)) {
			playerIn.openGui(VintageCraft.instance, 2, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}


	
	public boolean suitableGround(World world, BlockPos pos) {
		return world.isSideSolid(pos.down(), EnumFacing.UP);
	}
	
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!suitableGround(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(world, pos) && suitableGround(world, pos);
	}

	

	
}

package at.tyron.vintagecraft.Block.Utility;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.TileEntity.TETallMetalMold;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;

public class BlockTallMetalMolds extends BlockContainerVC {

	public BlockTallMetalMolds() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}

	@Override
	public String getSubType(ItemStack stack) {

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TETallMetalMold();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		/*TETallMetalMold te = (TETallMetalMold) worldIn.getTileEntity(pos);
		
		if (te != null) {
			te.receiveMoltenMetal(6, EnumMetal.STEEL);
			te.refreshModel = true;
		}*/
		
		return false;
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
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 0.9375f, pos.getZ() + 1f);
	}

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}
	
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		TETallMetalMold te = (TETallMetalMold) world.getTileEntity(pos);
		if (te != null) {
			if (te.getQuantityIngots() > 0 && te.getMetalTemperature() < 500) {
				ItemStack itemstack = ItemIngot.getItemStack(te.getMetal(), te.getQuantityIngots());
	    		spawnAsEntity(world, pos, itemstack);
	    		
	    		if (te.getMetal2() != null) {
					itemstack = ItemIngot.getItemStack(te.getMetal2(), te.getQuantityIngots2());
		    		spawnAsEntity(world, pos, itemstack);
	    			
	    		}
	    		
	    		te.onIngotsRemoved();
	    		return false;
			}
		}
		return super.removedByPlayer(world, pos, player, willHarvest);
	}

	
}

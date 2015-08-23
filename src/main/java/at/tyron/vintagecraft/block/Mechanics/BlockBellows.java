package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;

public class BlockBellows extends BlockMechanicalVC {

	public BlockBellows() {
		super(Material.wood);
		setCreativeTab(VintageCraft.mechanicsTab);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEBellows();
	}

	@Override
	public boolean isBlockedAllowedAt(World worldIn, BlockPos pos) {
		return suitableGround(worldIn, pos); 
	}

	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		//System.out.println("refresh");
/*		TEBellows te = (TEBellows)worldIn.getTileEntity(pos);
		if (te != null) {
			//te.refreshModel = true;
			
			if (te.getNetwork(side) != null) {
				System.out.println("N" + te.getNetwork(side).networkId + ": " + te.orientation + " / " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			} else {
				System.out.println(te.orientation + " / " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			}
		}
	*/	
		return false;
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		
		if (!isBlockedAllowedAt(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
			return;
		}
		
		TEBellows te = (TEBellows)worldIn.getTileEntity(pos);
		if (te != null) {
			te.connectToFurnace();
		}

	}
	
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	
}

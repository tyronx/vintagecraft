package at.tyron.vintagecraft.Block.Mechanics;

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
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;

public class BlockWindMillRotor extends BlockMechanicalVC {


	public BlockWindMillRotor() {
		super(Material.wood);
//		setCreativeTab(VintageCraft.mechanicsTab);
	}



	@Override
	public String getSubType(ItemStack stack) {

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEWindmillRotor();
	}
	
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		//System.out.println("refresh");
		TEWindmillRotor te = (TEWindmillRotor)worldIn.getTileEntity(pos);
		if (te != null) {
			//te.refreshModel = true;
			System.out.println("network: " + te.getNetwork(te.orientation));
			//te.orientation = side;
		}
		
		return false;
		
	}
	
	

	
}

package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public class BlockAngledGearBox extends BlockMechanicalVC {

	public BlockAngledGearBox() {
		super(Material.wood);
		//setCreativeTab(VintageCraft.mechanicsTab);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEAngledGearBox();
	}



	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		//System.out.println("refresh");
		TEAngledGearBox te = (TEAngledGearBox)worldIn.getTileEntity(pos);
		if (te != null) {
			//te.refreshModel = true;
			System.out.println("network: " + te.getNetwork(te.orientation));
			//te.input = side;
			//te.angle+=5;
			
		}
		
		return false;
		
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		
		TEAngledGearBox te = (TEAngledGearBox)worldIn.getTileEntity(pos);
		if (te != null) {
			te.connectToNeighbours();
		}
	}
	
	
  

}

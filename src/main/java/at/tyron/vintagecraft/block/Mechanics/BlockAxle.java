package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkRelay;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public class BlockAxle extends BlockMechanicalVC {
	
	public BlockAxle() {
		super(Material.wood);
		//setCreativeTab(VintageCraft.mechanicsTab);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEAxle();
	}


	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		//System.out.println("refresh");
		TEAxle te = (TEAxle)worldIn.getTileEntity(pos);
		if (te != null) {
			//te.refreshModel = true;
			//te.orientation = side;
			System.out.println("network: " + te.getNetwork(te.orientation));
		}
		
		return false;
		
	}
	
	


	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		
		TEAxle te = (TEAxle)worldIn.getTileEntity(pos);
		if (te != null) {
			te.recheckNeighbours();
		}
	}

}

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
import at.tyron.vintagecraft.TileEntity.Mechanics.TEMechanicalNetworkDeviceBase;
import at.tyron.vintagecraft.World.MechanicalNetwork;

public class BlockAngledGearBox extends BlockMechanicalVC {

	public BlockAngledGearBox() {
		super(Material.wood);
		setCreativeTab(VintageCraft.mechanicsTab);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEAngledGearBox();
	}
	
	@Override
	public boolean isBlockedAllowedAt(World worldIn, BlockPos pos) {
		return hasConnectibleDeviceAt(worldIn, pos); 
	}
	
	
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TEAngledGearBox te = (TEAngledGearBox)worldIn.getTileEntity(pos);
		if (te != null) {
			if (te.getNetwork(side) != null) {
				System.out.println("N" + te.getNetwork(side).networkId + ": " + te.orientation + " " + te.cagegearOrientation + " / " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			} else {
				System.out.println(te.orientation+ " " + te.cagegearOrientation + " / " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			}
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

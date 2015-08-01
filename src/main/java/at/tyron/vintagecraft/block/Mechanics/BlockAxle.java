package at.tyron.vintagecraft.Block.Mechanics;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
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
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public class BlockAxle extends BlockMechanicalVC {
	
	public BlockAxle() {
		super(Material.wood);
		setCreativeTab(VintageCraft.mechanicsTab);
	}
	


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEAxle();
	}
	
	@Override
	public boolean isBlockedAllowedAt(World worldIn, BlockPos pos) {
		return suitableGround(worldIn, pos) || suitableSide(worldIn, pos) != null;
	}
	


	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		//System.out.println("refresh");
		TEAxle te = (TEAxle)worldIn.getTileEntity(pos);
		if (te != null) {
			//te.refreshModel = true;
			//te.orientation = side;
			//System.out.println("network: " + te.getNetwork(te.orientation));
			if (te.getNetwork(side) != null) {
				System.out.println("N" + te.getNetwork(side).networkId + ": " + te.orientation + " / " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			} else {
				System.out.println(te.orientation + " / " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			}
		}
		
		return false;
		
	}
	
	


	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		
		if (!isBlockedAllowedAt(worldIn, pos)) {
			worldIn.destroyBlock(pos, true);
		}
		
		TEAxle te = (TEAxle)worldIn.getTileEntity(pos);
		if (te != null) {
			te.recheckNeighbours();
		}
	}

}

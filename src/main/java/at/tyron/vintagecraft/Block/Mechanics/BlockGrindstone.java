package at.tyron.vintagecraft.Block.Mechanics;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalRock;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockGrindstone extends BlockMechanicalVC  {

	public BlockGrindstone() {
		super(Material.rock);
		setCreativeTab(VintageCraft.mechanicsTab);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEGrindStone();
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (EnumRockType rocktype : EnumRockType.values()) {
			if (rocktype.group == EnumRockGroup.IGNEOUS_INTRUSIVE || rocktype.group == EnumRockGroup.IGNEOUS_EXTRUSIVE) {
				list.add(((ItemMechanicalRock)itemIn).withRockType(rocktype));
			}
		}
	}


	@Override
	public boolean isBlockedAllowedAt(World worldIn, BlockPos pos) {
		return suitableGround(worldIn, pos); 
	}
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TEGrindStone te = (TEGrindStone)worldIn.getTileEntity(pos);
		if (te != null) {
			te.refreshModel = true;
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
	
	


}

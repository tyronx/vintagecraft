package at.tyron.vintagecraft.Block.Mechanics;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalRock;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
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
		return hasConnectibleDeviceAt(worldIn, pos) && suitableGround(worldIn, pos); 
	}

}

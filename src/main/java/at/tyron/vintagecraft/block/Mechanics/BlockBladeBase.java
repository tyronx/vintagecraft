package at.tyron.vintagecraft.Block.Mechanics;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerNetworkNode;
import at.tyron.vintagecraft.World.MechanicalNetwork;

// Base for wind mills and water wheels
public class BlockBladeBase extends BlockMechanicalVC {

	protected BlockBladeBase(Material materialIn) {
		super(materialIn);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return null;
	}

	
}

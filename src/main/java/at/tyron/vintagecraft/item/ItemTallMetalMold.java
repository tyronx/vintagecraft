package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.Block.Mechanics.BlockMechanicalVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.TileEntity.TETallMetalMold;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEMechanicalNetworkDeviceBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemTallMetalMold extends ItemBlockVC {

	public ItemTallMetalMold(Block block) {
		super(block);
	}


	
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) {
        	return false;
        }

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
            
            int i = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing facing = EnumFacing.getHorizontal(i);
            
            TETallMetalMold te = (TETallMetalMold) world.getTileEntity(pos);
            if (te != null) {
            	te.setOrientation(facing);
            }
            
        }

        return true;
    }
}

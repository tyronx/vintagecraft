package at.tyron.vintagecraft.Item.Mechanics;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Item.ItemBlockVC;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;

public class ItemMechanical extends ItemBlockVC {
	boolean placeWithOppositeOrientation = false;
	
	public ItemMechanical(Block block) {
		super(block);
	}

	
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
            
            int i = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing facing = EnumFacing.getHorizontal(i);
            
            if (placeWithOppositeOrientation) facing = facing.getOpposite();
            
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IMechanicalPowerDevice) {
            	((IMechanicalPowerDevice)te).onDevicePlaced(world, pos, facing);
            }
            
        }

        return true;
    }
    
    
   
}

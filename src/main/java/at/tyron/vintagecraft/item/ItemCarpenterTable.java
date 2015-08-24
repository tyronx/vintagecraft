package at.tyron.vintagecraft.Item;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TECarpenterTable;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEMechanicalNetworkDeviceBase;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemCarpenterTable extends ItemBlockVC implements ISubtypeFromStackPovider {

	public ItemCarpenterTable(Block block) {
		super(block);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		return EnumTree.byId(getOrCreateNBT(itemstack).getInteger("treetype"));
	}
	
	public ItemStack withTreeType(EnumTree treetype) {
		ItemStack stack = new ItemStack(this);
		getOrCreateNBT(stack).setInteger("treetype", treetype.getId());
		return stack;
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
            
            TECarpenterTable te = (TECarpenterTable) world.getTileEntity(pos);
            if (te != null) {
            	te.setTreeType(getTreeType(stack));
            	te.setOrientation(facing);
            }
            
        }

        return true;
    }


	@Override
	public String getSubType(ItemStack stack) {
		if (getTreeType(stack) == null) return EnumTree.ASH.getStateName();
		return getTreeType(stack).getStateName();
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("tile.planks." + getTreeType(itemstack).getName() + ".name"));
	}
	
}

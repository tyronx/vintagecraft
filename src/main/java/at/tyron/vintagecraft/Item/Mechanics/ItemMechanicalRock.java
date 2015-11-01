package at.tyron.vintagecraft.Item.Mechanics;

import java.util.List;

import at.tyron.vintagecraft.Block.Mechanics.BlockMechanicalVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.ItemBlockVC;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
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

public class ItemMechanicalRock extends ItemBlockVC implements ISubtypeFromStackPovider {
	boolean placeWithOppositeOrientation = false;

	public ItemMechanicalRock(Block block) {
		super(block);
		this.setHasSubtypes(true);
		setMaxStackSize(1);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Mechanics;
	}


	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("rock." + getRockType(itemstack).getStateName() + ".name"));	
	}
	
	
	public static EnumRockType getRockType(ItemStack itemstack) {
		return EnumRockType.byId(getOrCreateNBT(itemstack).getInteger("rocktype"));
	}
	
	public ItemStack withRockType(EnumRockType rocktype) {
		ItemStack stack = new ItemStack(this);
		getOrCreateNBT(stack).setInteger("rocktype", rocktype.getId());
		return stack;
	}

	
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
    	if (this.block instanceof BlockMechanicalVC && !((BlockMechanicalVC)this.block).suitableGround(world, pos)) {
    		return false;
    	}
    	
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
            
            int i = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing facing = EnumFacing.getHorizontal(i);
            
            if (placeWithOppositeOrientation) facing = facing.getOpposite();
            
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IMechanicalPowerDevice) {
            	((IMechanicalPowerDevice)te).onDevicePlaced(world, pos, facing, side);
            }
            if (te instanceof TEGrindStone) {
            	((TEGrindStone)te).setRockType(getRockType(stack));
            }
            
        }

        return true;
    }

	@Override
	public String getSubType(ItemStack stack) {
		return getRockType(stack).getName();
	}


}

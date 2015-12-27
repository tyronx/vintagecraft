package at.tyron.vintagecraft.Item;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecartVC;
import at.tyron.vintagecraft.Entity.EntityEmptyMinecartVC;
import at.tyron.vintagecraft.Entity.EntityMinecartVC;
import at.tyron.vintagecraft.Interfaces.Item.IItemWorkableIngredient;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemMinecartVC extends ItemMinecart implements IItemWorkableIngredient {
	EnumMinecartType type;
	
	public ItemMinecartVC(EnumMinecartType type) {
		super(type);
		this.type = type;
		setCreativeTab(VintageCraft.toolsarmorTab);
	}
	
	
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (BlockRailBase.isRailBlock(iblockstate)) {
            if (!worldIn.isRemote)
            {
                BlockRailBase.EnumRailDirection enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty()) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                double d0 = 0.0D;

                if (enumraildirection.isAscending()) {
                    d0 = 0.5D;
                }

                EntityMinecart entityminecart;
                if (type == EnumMinecartType.FURNACE) {
                	entityminecart = new EntityCoalPoweredMinecartVC(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.0625D + d0, (double)pos.getZ() + 0.5D);
                } else {
                	entityminecart = new EntityEmptyMinecartVC(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.0625D + d0, (double)pos.getZ() + 0.5D);
                }
                
                if (stack.hasDisplayName()) {
                    entityminecart.setCustomNameTag(stack.getDisplayName());
                }

                worldIn.spawnEntityInWorld(entityminecart);
            }

            --stack.stackSize;
            return true;
        }
        else {
            return false;
        }
    }


	@Override
	public boolean isIngredient(ItemStack itemstack, ItemStack comparison, WorkableRecipeBase forrecipe) {
		return itemstack.getItem() == this;
	}

}

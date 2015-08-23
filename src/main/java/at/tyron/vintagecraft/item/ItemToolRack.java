package at.tyron.vintagecraft.Item;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Utility.BlockToolRack;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public class ItemToolRack extends ItemBlock implements ISubtypeFromStackPovider {
    public ItemToolRack(Block block) {
    	super(block);
		setHasSubtypes(true);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}


	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	for (EnumTree treetype : EnumTree.values()) {
    		subItems.add(withTreeType(treetype));
    	}
    }


	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("tile.planks." + getTreeType(itemstack).getStateName().toLowerCase(Locale.ROOT) + ".name"));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "toolrackitem";
	}
	
	
	
	@Override
	public int getMetadata(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound().getInteger("treetype");
		}
		return 0;
	}
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumTree.byId(itemstack.getTagCompound().getInteger("treetype"));
		}
		return EnumTree.ASH;
	}
	

	
	public static ItemStack withTreeType(EnumTree treetype) {
		ItemStack itemstack = new ItemStack(Item.getItemFromBlock(BlocksVC.toolrack)); // ItemsVC.toolrack);
		itemstack.setTagCompound(new NBTTagCompound());
		itemstack.getTagCompound().setInteger("treetype", treetype.getId());
		return itemstack;
	}


	

	@Override
	public String getSubType(ItemStack stack) {
		return getTreeType(stack).name().toLowerCase(Locale.ROOT);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Copied from ItemBlock
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.snow_layer && ((Integer)iblockstate.getValue(BlockSnow.LAYERS)).intValue() < 1)
        {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }

        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!playerIn.canPlayerEdit(pos, side, stack))
        {
            return false;
        }
        else if (pos.getY() == 255 && this.block.getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack))
        {
            int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);

            if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, iblockstate1))
            {
                worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		if (!this.block.canPlaceBlockOnSide(world, pos, side)) return false;
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            ((BlockToolRack)state.getBlock()).initTileEntity(world, pos, side, getTreeType(stack));
            
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
	}




	
}

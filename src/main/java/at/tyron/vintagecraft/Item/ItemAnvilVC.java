package at.tyron.vintagecraft.Item;

import java.util.Locale;

import at.tyron.vintagecraft.Block.Utility.BlockAnvilVC;
import at.tyron.vintagecraft.Interfaces.Item.IItemMetalTyped;
import at.tyron.vintagecraft.Interfaces.Item.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemAnvilVC extends ItemBlockVC implements ISubtypeFromStackPovider, IItemMetalTyped {
	public ItemAnvilVC(Block block) {
		super(block);
		setMaxStackSize(1);
	}


	public static EnumMetal getMetal(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMetal.byId(itemstack.getTagCompound().getInteger("metal"));
		}
		return EnumMetal.IRON;
	}


	@Override
	public int getMetadata(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound().getInteger("metal");
		}
		return 0;
	}
	
	
	public static ItemStack withMetal(EnumMetal metal) {
		ItemStack itemstack = new ItemStack(Item.getItemFromBlock(BlocksVC.metalanvil));
		itemstack.setTagCompound(new NBTTagCompound());
		itemstack.getTagCompound().setInteger("metal", metal.getId());
		return itemstack;
	}


	
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "item.anvil." + getMetal(itemstack).getName().toLowerCase(Locale.ROOT);
	}
	

	
	
	
	
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 3)) return false;

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            ((BlockAnvilVC)state.getBlock()).initTileEntity(world, pos, getMetal(stack));
            
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }

        return true;
	}


	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName().toLowerCase(Locale.ROOT);
	}
	
	// Useless extra code because Java is not able to inherit static methods... -.-
	@Override
	public ItemStack setItemMetal(ItemStack itemstack, EnumMetal metal) {
		return withMetal(metal);
	}

	@Override
	public EnumMetal getItemMetal(ItemStack itemstack) {
		return getMetal(itemstack);
	}

}

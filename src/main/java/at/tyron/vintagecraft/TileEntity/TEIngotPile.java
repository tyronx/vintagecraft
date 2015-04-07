package at.tyron.vintagecraft.TileEntity;


import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.Block.BlockIngotPile;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;


public class TEIngotPile extends TENoGUIInventory {
	public TEIngotPile() {
		storage = new ItemStack[1];
	}

	public void setItemStack(ItemStack copy) {
		storage[0] = copy;
	}

	public int getStackSize() {
		if (storage[0] == null) return 0;
		return storage[0].stackSize;
	}
	
	public EnumMetal getMetal() {
		EnumMetal metal = null;
		
		if (storage[0] != null) {
			metal = ItemIngot.getMetal(storage[0]); 
		}
		return metal == null ? EnumMetal.BISMUTH : metal;
	}

	
	
	public boolean tryTransferIngot(ItemStack stack) {
		TEIngotPile pile = getTopmostIngotPile();
		
		if (stack.getItem() != pile.storage[0].getItem() || ItemIngot.getMetal(stack) != getMetal()) return false;
		
		if (pile.storage[0].stackSize < ItemIngot.maxpilesize) {
			pile.storage[0].stackSize++;
			stack.stackSize--;
			
			getWorld().markBlockForUpdate(pile.getPos());
			
			return true;
		} else {
			return BlockIngotPile.tryCreatePile(stack, getWorld(), pile.getPos().up());
		}
	}

	

	public void tryGrabIngot(EntityPlayer player) {
		TEIngotPile pile = getTopmostIngotPile();
		
		if (pile.storage[0].stackSize > 0) {
			ItemStack ejectedstack = pile.storage[0].splitStack(1);
			
			if (!player.inventory.addItemStackToInventory(ejectedstack)) {
				if (!getWorld().isRemote) {
					getWorld().spawnEntityInWorld(
						new EntityItem(getWorld(),
							pile.getPos().getX(),
							pile.getPos().getY() + 1, 
							pile.getPos().getZ(), 
							ejectedstack
					));
				}
			}
		}
		
		if (pile.storage[0].stackSize <= 0) {
			getWorld().setBlockToAir(pile.getPos());
		}
		
		getWorld().markBlockForUpdate(pile.getPos());
	}
	

	
	public TEIngotPile getTopmostIngotPile() {
		BlockPos pos = getPos();
		TEIngotPile pile = this;
		
		while (getWorld().getBlockState(pos).getBlock() instanceof BlockIngotPile) {
			pile = (TEIngotPile)getWorld().getTileEntity(pos);
			pos = pos.up();
		}
		
		return pile;
	}
	

	@Override
	public int getSizeInventory() {
		return 1;
	}
	
	@Override
	public String getName() {
		return "ingotpile";
	}
	
	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Ingot Pile");
	}
}
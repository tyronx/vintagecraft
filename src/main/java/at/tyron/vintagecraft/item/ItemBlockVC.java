package at.tyron.vintagecraft.Item;

import java.util.Arrays;

import at.tyron.vintagecraft.Block.Utility.BlockToolRack;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlockVC extends ItemBlock {
	
	public ItemBlockVC(Block block) {
		super(block);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!entityplayer.canPlayerEdit(pos.offset(side), side, itemstack)) {
            return false;
        }
		
		IBlockState state = world.getBlockState(pos);
        
        if (state.getBlock() instanceof IBlockItemSink) {
    		if (((IBlockItemSink)state.getBlock()).tryPutItemstack(world, pos, entityplayer, side, itemstack)) {
    			return true;
    		} else {
    			return super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
    		}
    	}
    	
		return super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
	}
	
	
	
	public static NBTTagCompound getOrCreateNBT(ItemStack itemstack) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		return nbt;
	}

	
	
	
	
	
	
	
	/**** ISmithable methods ****/
	
	public ItemStack markOddlyShaped(ItemStack itemstack, boolean flag) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		
		if (flag) {
			nbt.setBoolean("oddlyshaped", true);
		} else {
			nbt.removeTag("oddlyshaped");
		}

		nbt.removeTag("anviltechniques");
		nbt.removeTag("lasttempupdate");
		nbt.removeTag("forgetemp");
		nbt.removeTag("startcoolingat");

		itemstack.setTagCompound(nbt);
		return itemstack;
	}
	
	
	public boolean isOddlyShaped(ItemStack itemstack) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		return nbt.getBoolean("oddlyshaped");
	}
	
	public ItemStack applyAnvilTechnique(ItemStack itemstack, EnumAnvilTechnique technique) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		int[] techniqueIds = nbt.getIntArray("anviltechniques");
		int[] newtechniqueIds = Arrays.copyOf(techniqueIds, techniqueIds.length + 1);
		
		newtechniqueIds[newtechniqueIds.length - 1] = technique.getId();
		
		nbt.setIntArray("anviltechniques", newtechniqueIds);
		
		itemstack.setTagCompound(nbt);
		
		return itemstack;
	}
	
	public EnumAnvilTechnique[] getAppliedAnvilTechniques(ItemStack itemstack) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		
		int[] techniqueIds = nbt.getIntArray("anviltechniques");
		EnumAnvilTechnique []techniques = new EnumAnvilTechnique[techniqueIds.length];
		for (int i = 0; i < techniqueIds.length; i++) {
			techniques[i] = EnumAnvilTechnique.fromId(techniqueIds[i]);
			//System.out.println("converted " + techniqueIds[i] + " => " + techniques[i]);
		}
		
		return techniques;
	}
	
	public void updateTemperature(ItemStack stack, World worldIn) {
		NBTTagCompound nbt = getOrCreateNBT(stack);
		
		if (nbt.getInteger("forgetemp") > 0 && worldIn.getWorldTime() > nbt.getLong("startcoolingat")) {
			int timeSinceLastUpdate = (int) (worldIn.getWorldTime() - nbt.getLong("lasttempupdate"));
			int newtemp = Math.max(0, nbt.getInteger("forgetemp") - 3 * timeSinceLastUpdate);
			
			nbt.setInteger("forgetemp", newtemp);
			
			if (newtemp <= 0) {
				nbt.removeTag("lasttempupdate");
				nbt.removeTag("forgetemp");
				nbt.removeTag("startcoolingat");
			} else {
				nbt.setLong("lasttempupdate", worldIn.getWorldTime());
			}
		}
	}
	

	public int getTemperature(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return itemstack.getTagCompound().getInteger("forgetemp") / 10;
		}
		return 0;
	}


}

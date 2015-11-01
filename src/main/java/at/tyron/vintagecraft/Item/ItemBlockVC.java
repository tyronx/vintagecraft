package at.tyron.vintagecraft.Item;

import java.util.Arrays;

import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.ICategorizedBlockOrItem;
import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import at.tyron.vintagecraft.World.Crafting.EnumAnvilTechnique;
import at.tyron.vintagecraft.World.Crafting.EnumWoodWorkingTechnique;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public abstract class ItemBlockVC extends ItemBlock implements ICategorizedBlockOrItem {
	
	public ItemBlockVC(Block block) {
		super(block);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		
		if (!entityplayer.canPlayerEdit(pos.offset(side), side, itemstack)) {
            return false;
        }
		
		
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

		nbt.removeTag("techniques");
		
		itemstack.setTagCompound(nbt);
		return itemstack;
	}
	
	
	public boolean isOddlyShaped(ItemStack itemstack) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		return nbt.getBoolean("oddlyshaped");
	}
	
	public ItemStack applyTechnique(ItemStack itemstack, EnumWorkableTechnique technique) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		int[] techniqueIds = nbt.getIntArray("techniques");
		int[] newtechniqueIds = Arrays.copyOf(techniqueIds, techniqueIds.length + 1);
		
		newtechniqueIds[newtechniqueIds.length - 1] = technique.getId();
		
		nbt.setIntArray("techniques", newtechniqueIds);
		
		itemstack.setTagCompound(nbt);
		
		return itemstack;
	}
	
	public EnumWorkableTechnique[] getAppliedTechniques(ItemStack itemstack) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		
		int[] techniqueIds = nbt.getIntArray("techniques");
		EnumWorkableTechnique []techniques = new EnumWorkableTechnique[techniqueIds.length];
		for (int i = 0; i < techniqueIds.length; i++) {
			if (itemstack.getItem() instanceof IItemWoodWorkable) {
				techniques[i] = EnumWoodWorkingTechnique.fromId(techniqueIds[i]);
			}
			if (itemstack.getItem() instanceof IItemSmithable) {
				techniques[i] = EnumAnvilTechnique.fromId(techniqueIds[i]);
			}
			//System.out.println("converted " + techniqueIds[i] + " => " + techniques[i]);
		}
		
		return techniques;
	}
	
	public void updateTemperature(ItemStack stack, World worldIn) {
		NBTTagCompound nbt = getOrCreateNBT(stack);
		
		if (nbt.getInteger("forgetemp") > 0 && worldIn.getTotalWorldTime() > nbt.getLong("startcoolingat")) {
			int timeSinceLastUpdate = (int) (worldIn.getTotalWorldTime() - nbt.getLong("lasttempupdate"));
			int newtemp = Math.max(0, nbt.getInteger("forgetemp") - 3 * timeSinceLastUpdate);
			
			nbt.setInteger("forgetemp", newtemp);
			
			if (newtemp <= 0) {
				nbt.removeTag("lasttempupdate");
				nbt.removeTag("forgetemp");
				nbt.removeTag("startcoolingat");
			} else {
				nbt.setLong("lasttempupdate", worldIn.getTotalWorldTime());
			}
		}
	}
	

	public int getTemperatureM10(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return itemstack.getTagCompound().getInteger("forgetemp");
		}
		return 0;
	}
	
	public void setTemperatureM10(ItemStack stack, int temperature, long worldtime) {
		NBTTagCompound nbt = getOrCreateNBT(stack);
		
		nbt.setInteger("forgetemp", temperature);
		if (temperature <= 0) {
			nbt.removeTag("lasttempupdate");				
		} else {
			nbt.setLong("lasttempupdate", worldtime);
		}
	}

	public long getStartCoolingAt(ItemStack stack) {
		NBTTagCompound nbt = getOrCreateNBT(stack);
		return nbt.getLong("startcoolingat");
	}
	public void setStartCoolingAt(ItemStack stack, long worldtime) {
		NBTTagCompound nbt = getOrCreateNBT(stack);
		nbt.setLong("startcoolingat", worldtime);
	}

}

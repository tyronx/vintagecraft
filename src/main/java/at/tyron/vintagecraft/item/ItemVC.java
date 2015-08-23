package at.tyron.vintagecraft.Item;

import java.util.Arrays;

import at.tyron.vintagecraft.Block.Utility.BlockFirepit;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.World.Crafting.EnumAnvilTechnique;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ItemVC extends Item {

	
	@Override
	public ItemVC setUnlocalizedName(String unlocalizedName) {
		return (ItemVC)super.setUnlocalizedName(unlocalizedName);
	}
	
	public Item register(String internalname) {
		setUnlocalizedName(internalname);
		GameRegistry.registerItem(this, internalname);
		return this;
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
	
	public ItemStack applyTechnique(ItemStack itemstack, EnumWorkableTechnique technique) {
		NBTTagCompound nbt = getOrCreateNBT(itemstack);
		int[] techniqueIds = nbt.getIntArray("anviltechniques");
		int[] newtechniqueIds = Arrays.copyOf(techniqueIds, techniqueIds.length + 1);
		
		newtechniqueIds[newtechniqueIds.length - 1] = technique.getId();
		
		nbt.setIntArray("anviltechniques", newtechniqueIds);
		
		itemstack.setTagCompound(nbt);
		
		return itemstack;
	}
	
	public EnumAnvilTechnique[] getAppliedTechniques(ItemStack itemstack) {
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

package at.tyron.vintagecraft.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import jdk.nashorn.internal.ir.LiteralNode.ArrayLiteralNode.ArrayUnit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumAlloy;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.interfaces.ISmeltable;

public class ItemVessel extends ItemVC implements ISmeltable {
	boolean burned;
		
	public ItemVessel(boolean burned) {
		this.burned = burned;
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (burned) {
			ItemStack alloy = EnumAlloy.getSmeltedItemStack(getContainedItemStacks(raw));
			//System.out.println(alloy.getItem());
			if (alloy != null) {
				return saveContents(new ItemStack(ItemsVC.ceramicVessel, 1), new ItemStack[]{alloy, null, null, null});
			}
			return null;
		} else {
			return new ItemStack(ItemsVC.ceramicVessel, 1);
		}
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		return 1;
	}

	
	@Override
	public float getSmeltingSpeedModifier(ItemStack raw) {
		if (burned) {
			ItemStack alloy = EnumAlloy.getSmeltedItemStack(getContainedItemStacks(raw));
			if (alloy != null) {
				return 2f / alloy.stackSize;
			}
		}
		return 2f;
	}

	
	@Override
	public int getMeltingPoint(ItemStack raw) {
		if (burned) {
			EnumAlloy alloy = EnumAlloy.getSuitableAlloy(getContainedItemStacks(raw)); 
			return alloy == null ? 0 : alloy.meltingpoint;

		} else {
			return 450;
		}
	}
	
	
	
	
	public ItemStack[] getContainedItemStacks(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) return null;
		
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
			
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		for(int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < 4) {
				stacks.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
			}
		}
		
		return stacks.toArray(new ItemStack[0]);
	}
	
	
	public static ItemStack saveContents(ItemStack vessel, ItemStack[] bag)  {
		NBTTagList nbttaglist = new NBTTagList();
		for(int i = 0; i < 4; i++) {
			if(bag[i] != null) {
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte)i);
				bag[i].writeToNBT(nbttagcompound1);
				//System.out.println("save " + bag[i].getItem());
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		if(vessel != null) {
			if(!vessel.hasTagCompound()) {
				vessel.setTagCompound(new NBTTagCompound());
			}
			vessel.getTagCompound().setTag("Items", nbttaglist);
		}
		
		return vessel;
	}
	
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (burned) {
			playerIn.openGui(VintageCraft.instance, 1, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
			return itemStackIn;
		} else {
			return super.onItemRightClick(itemStackIn, worldIn, playerIn);
		}
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		if (burned) {
			ItemStack[] stacks = getContainedItemStacks(itemstack);
			EnumAlloy alloy = EnumAlloy.getSuitableAlloy(stacks);
			
			if (alloy != null) {
				tooltip.add("Will create " + alloy.toMetal.getName().toLowerCase() + " if melted");
				tooltip.add("Melting Point: " + alloy.meltingpoint + " deg.");
			}
			
			if (stacks != null) {
				if (stacks.length > 0) tooltip.add("Contents");
				for (ItemStack stack : stacks) {
					tooltip.add("  " + stack.stackSize + "x " + stack.getDisplayName());
				}
			}
			
		} else {
			tooltip.add("Baking Temperature: " + getMeltingPoint(itemstack) + " deg.");
		}
	}
	
	
	

	private static int[] apply_cd(int[] input) {
	    int result = input[0];
	    for (int i = 1; i < input.length; i++) {
	    	result = gcd(result, input[i]);
	    }
	    
	    int[] output = new int[input.length];
	    for (int i = 1; i < input.length; i++) {
	    	output[i] = input[i] / result;
	    }
	    return output;
	}
	
	private static int gcd(int a, int b) {
	    while (b > 0) {
	        int temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}

	
}

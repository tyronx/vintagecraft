package at.tyron.vintagecraft.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.interfaces.ISmeltable;

public class ItemVessel extends ItemVC implements ISmeltable {
	boolean burned;
	String []ratios = new String[]{"co-9 ti-1", "ti-1 co-9"};
	ItemStack []smelted = null;
	
	void initSmelted() {
		 smelted = new ItemStack[]{ItemIngot.getItemStack(EnumMetal.BRONZE, 10), ItemIngot.getItemStack(EnumMetal.BRONZE, 10) };
	}
	
	public ItemVessel(boolean burned) {
		this.burned = burned;
	}
	
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (burned) {
			ItemStack alloy = getSmeltableAlloy(raw);
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
	public int getMeltingPoint(ItemStack raw) {
		if (burned) {
			/*ItemStack alloy = getSmeltableAlloy(raw);
			if (alloy == null) return 0;
			return ItemIngot.getMetal(alloy).meltingpoint;*/
			return EnumMetal.COPPER.meltingpoint;
		} else {
			return 450;
		}
	}
	
	
	
	public ItemStack getSmeltableAlloy(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) return null;
		
		String ratio = "";
		
		NBTTagList nbttaglist = nbt.getTagList("Items", 10);
		for(int i = 0; i < nbttaglist.tagCount(); i++) {
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			byte byte0 = nbttagcompound1.getByte("Slot");
			if(byte0 >= 0 && byte0 < 4) {
				ItemStack is = ItemStack.loadItemStackFromNBT(nbttagcompound1);
				if(is != null && is.stackSize >= 1 && is.getItem() instanceof ItemIngot) {
					if (ratio.length() > 0) ratio+=" ";
					ratio += ItemIngot.getMetal(is).getCode() + "-" + is.stackSize;
				}
			}
		}
		
		if (smelted == null) initSmelted();
		for (int i = 0; i < ratios.length; i++) {
			if (ratios[i].equals(ratio)) {
				//System.out.println(i + " / " + smelted[i].getItem());
				return smelted[i];
			}
		}
		
		
		return null;		
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
		if (getSmelted(itemstack) != null) {
			if (burned) {
				tooltip.add("Will create " + ItemIngot.getMetal(getSmeltableAlloy(itemstack)).getName().toLowerCase());
				tooltip.add("Melting Point: " + getMeltingPoint(itemstack) + " deg.");
			} else {
				tooltip.add("Baking Point: " + getMeltingPoint(itemstack) + " deg.");
			}
		}
	}
}

package at.tyron.vintagecraft.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumAlloy;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemClayVessel extends ItemBlockVC implements IItemSmeltable {
	boolean burned;
		
	public ItemClayVessel(Block block) {
		super(block);
		this.burned = false;
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabMisc);
	}
	
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (burned) {
			ItemStack alloy = EnumAlloy.getSmeltedItemStack(getContainedItemStacks(raw));
			//System.out.println(alloy.getItem());
			if (alloy != null) {
				return putItemStacks(new ItemStack(Item.getItemFromBlock(BlocksVC.ceramicVessel), 1), new ItemStack[]{alloy, null, null, null});
			}
			return null;
		} else {
			return new ItemStack(Item.getItemFromBlock(BlocksVC.ceramicVessel), 1);
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
		return 0.5f;
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
	
	
	
	
	public static ItemStack[] getContainedItemStacks(ItemStack stack) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null) return new ItemStack[4];
		
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
	
	
	public static ItemStack putItemStacks(ItemStack vessel, ItemStack[] bag)  {
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
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!playerIn.isSneaking()) return false;
		
		return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (burned && !playerIn.isSneaking()) {
			playerIn.openGui(VintageCraft.instance, 1, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
			return itemStackIn;
		} else {
			return super.onItemRightClick(itemStackIn, worldIn, playerIn);
		}
	}
	
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		boolean ok = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
		
		if (ok) {
			TEVessel tevessel = (TEVessel) world.getTileEntity(pos);
			if (burned) tevessel.setContents(getContainedItemStacks(stack));
		}
		
		return ok;
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		if (burned) {
			ItemStack[] stacks = getContainedItemStacks(itemstack);
			EnumAlloy alloy = EnumAlloy.getSuitableAlloy(stacks);
			
			if (alloy != null) {
				tooltip.add("Will create " + alloy.toMetal.getName().toLowerCase(Locale.ROOT) + " if melted");
				tooltip.add("Melting Point: " + alloy.meltingpoint + " deg.");
			}
			
			if (stacks != null) {
				boolean hascontents = false;
				
				for (ItemStack stack : stacks) {
					if (stack != null) hascontents = true;
					
				}
				
				if (hascontents) {
					if (stacks.length > 0) tooltip.add("Contents");
					for (ItemStack stack : stacks) {
						if(stack != null) tooltip.add("  " + stack.stackSize + "x " + stack.getDisplayName());
					}
				}
				
			}
			
		} else {
			tooltip.add("Baking Temperature: " + getMeltingPoint(itemstack) + " deg.");
		}
	}
	
	

}

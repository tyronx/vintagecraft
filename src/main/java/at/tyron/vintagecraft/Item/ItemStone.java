package at.tyron.vintagecraft.Item;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Entity.EntityStone;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemStone extends ItemVC implements ISubtypeFromStackPovider, ISizedItem {

	public ItemStone() {
		setHasSubtypes(true);
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	for (EnumRockType rocktype : EnumRockType.values()) {
    		subItems.add(setRockType(new ItemStack(ItemsVC.stone), rocktype));
    	}
    }

	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("rock." + ItemStone.getRockType(itemstack) + ".name"));
		tooltip.add(StatCollector.translateToLocal("rocktype." + ItemStone.getRockType(itemstack).group.name().toLowerCase(Locale.ROOT) + ".name"));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "stone";
	}
	
	
	@Override
	public int getMetadata(ItemStack stack) {
		if (stack.getTagCompound() != null) {
			return stack.getTagCompound().getInteger("rocktype");
		}
		return 0;
	}
	
	public static EnumRockType getRockType(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumRockType.byId(itemstack.getTagCompound().getInteger("rocktype"));
		}
		return EnumRockType.ANDESITE;
	}
	
	public static ItemStack setRockType(ItemStack itemstack, EnumRockType rocktype) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("rocktype", rocktype.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}

	

	@Override
	public String getSubType(ItemStack stack) {
		return getRockType(stack).getName();
	}
	


	public boolean firepitPlaceable(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side) {

		return
			entityplayer.isSneaking() &&
			itemstack.stackSize >= 3 &&
			side == EnumFacing.UP &&
			world.isAirBlock(pos.offset(side)) &&
			world.isSideSolid(pos, EnumFacing.UP)
		;
	}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		if (!firepitPlaceable(itemstack, entityplayer, world, pos, side)) return false;
		
		world.setBlockState(pos.offset(side), BlocksVC.firepit.getDefaultState());
		itemstack.stackSize -=3;
		
		return true;
	}


	
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            worldIn.spawnEntityInWorld(new EntityStone(stack.splitStack(1), worldIn, playerIn));
            // Throwing a rock is very Energy intensive
	        playerIn.addExhaustion(1F);
        }

        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        
        return stack;
    }

	
	 /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack stack) {
        return 15;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.SMALL;
	}

	
	public static ItemStack getItemStackFor(EnumRockType rocktype, int i) {
		return setRockType(new ItemStack(ItemsVC.stone, i), rocktype);
	}

    

}

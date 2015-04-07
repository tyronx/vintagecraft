package at.tyron.vintagecraft.Item;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import at.tyron.vintagecraft.Block.BlockRock;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.Interfaces.ISmeltable;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRock extends ItemBlock implements ISmeltable {

	public ItemRock(Block block) {
		super(block);
		this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}


	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (BlocksVC.rock.containsBlock(item.getBlock())) {
			return getUnlocalizedName() + "." + getRockType(itemstack).getUnlocalizedName();
		} else {
			return getUnlocalizedName();
		}
	}
	
	
	public static EnumRockType getRockType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumRockType) getBlockClass(block).getBlockClassfromMeta(block, itemstack.getItemDamage()).getKey();
	}
	
	public static BlockClass getBlockClass(Block block) {
		return BlocksVC.rock;
	}

	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (getBlockClass(item.getBlock()).containsBlock(item.getBlock())) {
			EnumRockType rocktype = getRockType(itemstack);
			tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getName() + "." + rocktype.getUnlocalizedName() + ".name"));
		}
	}
	
	
	
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
    	if (((ItemRock)stack.getItem()).getBlock() == BlocksVC.topsoil) {
    		return BlocksVC.topsoil.getRenderColor(BlocksVC.topsoil.getStateFromMeta(stack.getMetadata()));
    	}
    	return 16777215;
    }
	
	

	@Override
    public int getMetadata(int damage) {
        return damage;
    }
	
	
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (getRockType(raw) == EnumRockType.QUARTZITE) {
			return new ItemStack(Blocks.glass);
		}
		return null;
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		if (getRockType(raw) == EnumRockType.QUARTZITE) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		if (getRockType(raw) == EnumRockType.QUARTZITE) {
			return 1800;
		}
		return 0;
	}
	
	@Override
	public float getSmeltingSpeedModifier(ItemStack raw) {
		return 1f;
	}


}

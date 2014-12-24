package at.tyron.vintagecraft.item;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import at.tyron.vintagecraft.World.EnumRockType;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.VCBlocks;

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

public class ItemRock extends ItemBlock {

	public ItemRock(Block block) {
		super(block);
		this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}


	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (item.getBlock() == VCBlocks.rock) {
			return getUnlocalizedName() + "." + EnumRockType.byMetadata(itemstack.getMetadata()).getUnlocalizedName();
		} else {
			return getUnlocalizedName();
		}
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (item.getBlock() != VCBlocks.rock) {
			tooltip.add(StatCollector.translateToLocal(VCBlocks.rock.getUnlocalizedName() + "." + EnumRockType.byMetadata(itemstack.getItemDamage()).getUnlocalizedName() + ".name"));
		}
	}
	
	
	
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
    	if (((ItemRock)stack.getItem()).getBlock() == VCBlocks.topsoil) {
    		return VCBlocks.topsoil.getRenderColor(VCBlocks.topsoil.getStateFromMeta(stack.getMetadata()));
    	}
    	return 16777215;
    }
	
	

	@Override
    public int getMetadata(int damage) {
        return damage;
    }
	
/*	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		String rockname = EnumRockType.byMetadata(stack.getMetadata()).getUnlocalizedName();
		ModelResourceLocation mrl = new ModelResourceLocation("vintagecraft:block/rock/" + rockname);
		System.out.println(mrl.toString());
		return mrl;
    }
*/

}

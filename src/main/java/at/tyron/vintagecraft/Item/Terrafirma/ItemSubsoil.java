package at.tyron.vintagecraft.Item.Terrafirma;

import java.util.List;

import at.tyron.vintagecraft.Block.Terrafirma.BlockSubSoil;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.SoilRockClassEntry;
import at.tyron.vintagecraft.Item.ItemBlockVC;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemSubsoil extends ItemBlockVC {

	public ItemSubsoil(Block block) {
		super(block);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Terrafirma;
	}

	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getBlockClass(((ItemBlock)stack.getItem()).block).getName();
	}

	
	
	// Workaround for Java being too fail to allow overriding static methods
	public static BaseBlockClass getBlockClass(Block block) {
		if (block instanceof BlockSubSoil) return BlocksVC.subsoil;
		
		return null; 
	}
	
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		// Soo many type casts -.-
		// Totally should be programmed using generic types
		SoilRockClassEntry entry = (SoilRockClassEntry)getBlockClass(((ItemBlock)itemstack.getItem()).block).getEntryFromItemStack(itemstack);
		
		tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getName() + "." + entry.rocktype.getUnlocalizedName() + ".name"));
		tooltip.add(StatCollector.translateToLocal(entry.organiclayer.getStateName() + ".name"));
	}
	
	
	

}

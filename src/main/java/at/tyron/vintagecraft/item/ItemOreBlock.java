package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.BlockClass.OreClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemOreBlock extends ItemBlock {

	public ItemOreBlock(Block block) {
		super(block);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		OreClassEntry entry = (OreClassEntry) BlocksVC.rawore.getBlockClassfromMeta(((ItemBlock)stack.getItem()).block, stack.getItemDamage());
		
     	String[] type = entry.getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase());
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase());
     	
		return "item.ore." + oretype.getName().toLowerCase();
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		OreClassEntry entry = (OreClassEntry) BlocksVC.rawore.getBlockClassfromMeta(((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
		
     	String[] type = entry.getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase());
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase());

		tooltip.add(StatCollector.translateToLocal("tile.rock."+rocktype.getName()+".name"));
	}
}

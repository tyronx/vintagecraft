package at.tyron.vintagecraft.Item.Terrafirma;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.BlockClass.OreInRockClassEntry;
import at.tyron.vintagecraft.World.BlocksVC;
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
		OreInRockClassEntry entry = (OreInRockClassEntry) BlocksVC.rawore.getEntryFromMeta(((ItemBlock)stack.getItem()).block, stack.getItemDamage());
		
     	String[] type = entry.getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase(Locale.ROOT));
     	
		return "item.ore." + oretype.getName().toLowerCase(Locale.ROOT);
	}

	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		OreInRockClassEntry entry = (OreInRockClassEntry) BlocksVC.rawore.getEntryFromMeta(((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
		
     	String[] type = entry.getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase(Locale.ROOT));

		tooltip.add(StatCollector.translateToLocal("rock."+rocktype.getName()+".name"));
	}
}

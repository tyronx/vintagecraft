package at.tyron.vintagecraft.BlockClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import scala.reflect.internal.Trees.Super;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.Block.SoundType;
import net.minecraft.item.ItemBlock;
import at.tyron.vintagecraft.WorldProperties.EnumFlora;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.block.BlockDoubleFlowerVC;
import at.tyron.vintagecraft.block.BlockFlowerVC;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.item.ItemFlowerVC;

public class FlowerClass extends BlockClass {
	String getBlockClassName() { return name; }
	Class<? extends BlockVC> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return hardness; }
	SoundType getStepSound() { return Block.soundTypeGrass; }
	String getHarvestTool() { return null; }
	int getHarvestLevel() { return 0; }
	String getTypeName() { return "flowertype"; }



	
/*	public FlowerClass ORANGEMILKWEED = new FlowerClassEntry("orangemilkweed", EnumFlora.ORANGEMILKWEED);
	public FlowerClass ORANGEMILKWEED2 = new FlowerClass("orangemilkweed2", EnumFlora.ORANGEMILKWEED);

	public FlowerClass PURPLEMILKWEED = new FlowerClass("purplemilkweed", EnumFlora.PURPLEMILKWEED);
	public FlowerClass PURPLEMILKWEED2 = new FlowerClass("purplemilkweed2", EnumFlora.PURPLEMILKWEED);

	public FlowerClass CATMINT = new FlowerClass("catmint", EnumFlora.CATMINT);
	public FlowerClass CALENDULA = new FlowerClass("calendula", EnumFlora.CALENDULA);
	public FlowerClass CORNFLOWER = new FlowerClass("cornflower", EnumFlora.CORNFLOWER);
	public FlowerClass CORNFLOWER2 = new FlowerClass("cornflower2", EnumFlora.CORNFLOWER);

	public FlowerClass LILYOFTHEVALLEY = new FlowerClass("lilyofthevalley", EnumFlora.LILYOFTHEVALLEY);
	public FlowerClass LILYOFTHEVALLEY2 = new FlowerClass("lilyofthevalley2", EnumFlora.LILYOFTHEVALLEY);
	public FlowerClass LILYOFTHEVALLEY3 = new FlowerClass("lilyofthevalley3", EnumFlora.LILYOFTHEVALLEY);

	public FlowerClass CLOVER = new FlowerClass("clover", EnumFlora.CLOVER);

	public FlowerClass GOLDENROD = new FlowerClass("goldenrod", EnumFlora.GOLDENROD, true);
	public FlowerClass GOLDENROD2 = new FlowerClass("goldenrod", EnumFlora.GOLDENROD, true);
	public FlowerClass GOLDENROD3 = new FlowerClass("goldenrod", EnumFlora.GOLDENROD, true);

	public FlowerClass FORGETMENOT = new FlowerClass("forgetmenot", EnumFlora.FORGETMENOT);
	public FlowerClass FORGETMENOT2 = new FlowerClass("forgetmenot2", EnumFlora.FORGETMENOT, false);
	public FlowerClass FORGETMENOT3 = new FlowerClass("forgetmenot3", EnumFlora.FORGETMENOT, false);
	public FlowerClass FORGETMENOT4 = new FlowerClass("forgetmenot4", EnumFlora.FORGETMENOT, false);
	public FlowerClass FORGETMENOT5 = new FlowerClass("forgetmenot5", EnumFlora.FORGETMENOT, false);

	public FlowerClass NARCISSUS = new FlowerClass("narcissus", EnumFlora.NARCISSUS);
	public FlowerClass NARCISSUS2 = new FlowerClass("narcissus2", EnumFlora.NARCISSUS);
	public FlowerClass NARCISSUS3 = new FlowerClass("narcissus3", EnumFlora.NARCISSUS);

	public FlowerClass PURPLETULIP = new FlowerClass("purpletulip", EnumFlora.PURPLETULIP);
	public FlowerClass PURPLETULIP2 = new FlowerClass("purpletulip2", EnumFlora.PURPLETULIP);
	public FlowerClass PURPLETULIP3 = new FlowerClass("purpletulip3", EnumFlora.PURPLETULIP);*/
	
	


	public FlowerClass init(boolean doublehigh) {
		if (doublehigh) {
			name = "doubleflower";
			blockclass = BlockDoubleFlowerVC.class;
			itemclass = ItemFlowerVC.class;
			hardness = 0.2f;
		} else {
			name = "flower";
			blockclass = BlockFlowerVC.class;
			itemclass = ItemFlowerVC.class;
			hardness = 0.4f;
		}
		
		for (EnumFlora group : EnumFlora.values()) {
			for (EnumFlower flower : group.variants) {
				if (doublehigh == flower.doubleHigh) {
					values.put((IEnumState) flower, new FlowerClassEntry((IEnumState)flower, flower.doubleHigh, group));
				}
			}
		}
		
		initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
		
		return this;
	}
	
	
	public BlockClassEntry[] values(boolean doublehigh) {
		ArrayList<BlockClassEntry> flowers = new ArrayList<BlockClassEntry>();
		
		for (BlockClassEntry flower : values()) {
			if (doublehigh == ((FlowerClassEntry)flower).doubleHigh) {
				flowers.add(flower);
			}
		}
		
		return flowers.toArray(new BlockClassEntry[0]);
	}

	
	public BlockClassEntry[] values(EnumFlora group) {
		ArrayList<BlockClassEntry> flowers = new ArrayList<BlockClassEntry>();
		
		for (BlockClassEntry flower : values()) {
			if (group == ((FlowerClassEntry)flower).group) {
				flowers.add(flower);
			}
		}
		
		return flowers.toArray(new BlockClassEntry[0]);	
	}

	
}
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
	Class<? extends Block> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return hardness; }
	SoundType getStepSound() { return Block.soundTypeGrass; }
	String getHarvestTool() { return null; }
	int getHarvestLevel() { return 0; }
	String getTypeName() { return "flowertype"; }


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
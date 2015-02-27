package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockCropsVC extends BlockCrops {
	
	protected boolean canPlaceBlockOn(Block ground) {
        return true;
    }
	
    protected Item getSeed() {
        return ItemsVC.wheatSeeds;
    }

}

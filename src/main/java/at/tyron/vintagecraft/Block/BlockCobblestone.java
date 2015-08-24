package at.tyron.vintagecraft.Block;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.material.Material;

public class BlockCobblestone extends BlockRock {
	public BlockCobblestone() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.cobblestone;
	}

}

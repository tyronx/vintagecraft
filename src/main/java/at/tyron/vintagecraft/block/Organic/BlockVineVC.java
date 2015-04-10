package at.tyron.vintagecraft.Block.Organic;

import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;

public class BlockVineVC extends BlockVine {
	public BlockVineVC() {
        super();
        // Doesn't grow naturally for now
        this.setTickRandomly(false);
	}
}

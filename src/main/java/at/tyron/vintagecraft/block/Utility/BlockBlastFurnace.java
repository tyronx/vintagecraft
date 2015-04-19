package at.tyron.vintagecraft.Block.Utility;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import net.minecraft.block.material.Material;

public class BlockBlastFurnace extends BlockVC {

	protected BlockBlastFurnace(Material materialIn) {
		super(materialIn);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}

}

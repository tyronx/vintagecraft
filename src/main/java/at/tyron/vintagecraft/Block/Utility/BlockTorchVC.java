package at.tyron.vintagecraft.Block.Utility;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import net.minecraft.block.BlockTorch;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockTorchVC extends BlockTorch {

	public BlockTorchVC() {
		super();
		setLightLevel(0.9375F);
	}
	
	public BlockTorchVC registerSingleState(String name, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		VintageCraft.proxy.registerItemBlockTexture(this, name);
		
		
		return this;
	}
}

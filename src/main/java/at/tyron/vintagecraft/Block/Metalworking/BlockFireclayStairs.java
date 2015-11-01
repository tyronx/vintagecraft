package at.tyron.vintagecraft.Block.Metalworking;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ICategorizedBlockOrItem;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFireclayStairs extends BlockStairs implements ICategorizedBlockOrItem {

	public BlockFireclayStairs() {
		super(BlocksVC.fireclaybricks.getDefaultState());
		setCreativeTab(VintageCraft.craftedBlocksTab);
		this.setLightOpacity(1);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Metalworking;
	}

	
    public static boolean isBlockStairs(Block block) {
        return block instanceof BlockFireclayStairs;
    }
    
    public boolean isOpaqueCube() {
        return false;
    } 

    public boolean isVisuallyOpaque() {
        return false;
    }

	public Block registerSingleState(String name, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		VintageCraft.proxy.registerItemBlockTexture(this, name);
		
		
		return this;
	}


}

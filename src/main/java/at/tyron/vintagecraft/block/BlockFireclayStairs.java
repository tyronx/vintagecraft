package at.tyron.vintagecraft.Block;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.Organic.BlockWoodenStairsVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFireclayStairs extends BlockStairs {

	public BlockFireclayStairs() {
		super(BlocksVC.fireclaybricks.getDefaultState());
		setCreativeTab(VintageCraft.craftedBlocksTab);
		this.setLightOpacity(1);
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
		
		VintageCraft.instance.proxy.registerItemBlockTexture(this, name);
		
		
		return this;
	}


}

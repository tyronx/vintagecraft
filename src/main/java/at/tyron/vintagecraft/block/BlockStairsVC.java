package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.IMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockStairsVC extends BlockStairs implements IMultiblock {
	public static PropertyBlockClass TREETYPE;
	BlockClassEntry[] subtypes;

	protected BlockStairsVC() {
		super(Blocks.planks.getDefaultState());
	}

	
    public static boolean isBlockStairs(Block block) {
        return block instanceof BlockStairsVC;
    }

	@Override
	public int multistateAvailableTypes() {
		return 2;
	}
	
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = super.getMetaFromState(state);
		return meta + (getBlockClass().getMetaFromState(state) << 3);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getBlockClass().getBlockClassfromMeta(this, meta).getBlockState(super.getStateFromMeta(meta));
	}


	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}

	@Override
	public IProperty getTypeProperty() {
		return TREETYPE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.TREETYPE = property;		
	}

	@Override
	public BlockClass getBlockClass() {
		return BlocksVC.fence;
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState[] types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState[] types, String folderprefix) {
		System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IEnumState enumstate = types[i]; 
			
			VintageCraft.instance.proxy.registerItemBlockTexture(this, folderprefix, enumstate.getStateName(), enumstate.getMetaData(this));
		}
		return this;
	}

    
    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return new BlockState(this, new IProperty[] {FACING, HALF, SHAPE});
    	}
    	
    	return new BlockState(this, new IProperty[] {FACING, HALF, SHAPE, getTypeProperty()});
    }

}

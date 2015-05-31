package at.tyron.vintagecraft.Block.Organic;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStairsVC extends BlockStairs implements IMultiblock {
	public PropertyBlockClass TREETYPE;
	BlockClassEntry[] subtypes;

	public BlockStairsVC() {
		super(Blocks.planks.getDefaultState());
		setCreativeTab(VintageCraft.craftedBlocksTab);
		this.setLightOpacity(1);
	}

	
    public static boolean isBlockStairs(Block block) {
        return block instanceof BlockStairsVC;
    }
    
    
    public boolean isOpaqueCube() {
        return false;
    } 

    public boolean isVisuallyOpaque() {
        return false;
    }

	@Override
	public int multistateAvailableTypes() {
		return 2;
	}
	
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state) & 1;
    }

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
	}
	
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = getBlockClass().getMetaFromState(state);

		if (state.getValue(HALF) == BlockStairs.EnumHalf.TOP) {
			meta |= 8;
		}

		meta |= (5 - ((EnumFacing)state.getValue(FACING)).getIndex()) << 1;
		return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean halftop = (meta >> 3) == 1;
		
		IBlockState iblockstate = this.getDefaultState().withProperty(HALF, halftop ? BlockStairs.EnumHalf.TOP : BlockStairs.EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(5 - ((meta >> 1) & 3)));
        
		return getBlockClass().getBlockClassfromMeta(this, meta & 1).getBlockState(iblockstate);
	}

	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos);
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
	public BaseBlockClass getBlockClass() {
		return BlocksVC.stairs;
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum[] types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum[] types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IStateEnum enumstate = types[i]; 
			
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

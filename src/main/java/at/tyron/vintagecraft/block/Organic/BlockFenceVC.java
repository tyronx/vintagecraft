package at.tyron.vintagecraft.Block.Organic;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFenceVC extends BlockFence implements IMultiblock {
	public PropertyBlockClass TREETYPE;
	BlockClassEntry[] subtypes;
	
	public int multistateAvailableTypes() {
		return 16;
	}
	
	public BlockFenceVC() {
		super(Material.wood);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
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
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
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

	
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getBlockClassfromMeta(this, meta).getBlockState();
    }


    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state);
    }
    
    

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state
        	.withProperty(NORTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.north())))
        	.withProperty(EAST, Boolean.valueOf(this.canConnectTo(worldIn, pos.east())))
        	.withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(worldIn, pos.south())))
        	.withProperty(WEST, Boolean.valueOf(this.canConnectTo(worldIn, pos.west())));
    }

    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return new BlockState(this, new IProperty[]{NORTH, EAST, WEST, SOUTH});
    	}
    	
    	return new BlockState(this, new IProperty[] {NORTH, EAST, WEST, SOUTH, getTypeProperty()});
    }
    
}

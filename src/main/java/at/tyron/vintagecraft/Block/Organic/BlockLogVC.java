package at.tyron.vintagecraft.Block.Organic;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.Block.IMultiblock;
import at.tyron.vintagecraft.Interfaces.Item.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.ItemLogVC;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLogVC extends BlockVC implements ISubtypeFromStackPovider, IMultiblock {
	public PropertyBlockClass TREETYPE;
	
	public BlockLogVC() {
		super(Material.wood);
		setCreativeTab(VintageCraft.floraTab);
		minDropChance = 0f;
	}
	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}

	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
	}
	
	
	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
		return true;
	}
	
	
    @Override
    protected BlockState createBlockState() {
    	if (getTypeProperty() != null) {
    		return new BlockState(this, new IProperty[] {getTypeProperty()});
    	}
    	return new BlockState(this, new IProperty[0]);
    }
	
	
    @Override
    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state);
    }
      
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getEntryFromMeta(this, meta).getBlockState();
    }

    

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        ItemLogVC.withTreeType(itemstack, getTreeType(state));
        ret.add(itemstack);
        
    	return ret;
    }

	public BlockClassEntry getTreeType(IBlockState state) {
		return (BlockClassEntry)state.getValue(getTypeProperty());
	}

	@Override
	public String getSubType(ItemStack stack) {
		ItemBlock itemblock = (ItemBlock)stack.getItem();
		return getBlockClass().getEntryFromMeta((BlockVC) itemblock.block, stack.getItemDamage()).getName();
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
		return BlocksVC.log;
	}

	@Override
	public int multistateAvailableTypes() {
		return 16;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "tile.log";
	}
    
}

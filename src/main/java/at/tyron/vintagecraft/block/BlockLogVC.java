package at.tyron.vintagecraft.block;

import java.util.List;

import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.item.ItemLogVC;
import at.tyron.vintagecraft.item.ItemOre;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLogVC extends BlockVC {
	public static final PropertyEnum TREETYPE = PropertyEnum.create("treetype", EnumTree.class);
	
	
	public BlockLogVC() {
		super(Material.wood);
		
		setCreativeTab(CreativeTabs.tabMaterials);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(TREETYPE, EnumTree.MOUNTAINDOGWOOD));
	}
	
	@Override
	public boolean canSustainLeaves(IBlockAccess world, BlockPos pos) {
		return true;
	}
	
	
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {TREETYPE});
    }
	
	
    @Override
    public int getMetaFromState(IBlockState state) {
    	return ((EnumTree)state.getValue(TREETYPE)).meta;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return super.getStateFromMeta(meta).withProperty(TREETYPE, EnumTree.byMeta(meta));
    }

    

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        ItemLogVC.setTreeType(itemstack, getTreeType(state));
        ret.add(itemstack);
        
    	return ret;
    }

	public EnumTree getTreeType(IBlockState state) {
		return (EnumTree)state.getValue(TREETYPE);
	}
    
    
}

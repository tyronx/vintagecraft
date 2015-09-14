package at.tyron.vintagecraft.Block.Utility;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Item.ItemCarpenterTable;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TECarpenterTable;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpenterTable extends BlockContainerVC {
	public static PropertyEnum TREETYPE = PropertyEnum.create("treetype", EnumTree.class);
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public BlockCarpenterTable() {
		super(Material.wood);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (EnumTree treetype : EnumTree.values()) {
			if (treetype.jankahardness > 800) {
				list.add(((ItemCarpenterTable)itemIn).withTreeType(treetype));
			}
		}
	}

	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(TREETYPE, getTreeType(worldIn, pos)).withProperty(FACING, getOrientation(worldIn, pos));
	}

	
	public EnumTree getTreeType(IBlockAccess worldIn, BlockPos pos) {
		TECarpenterTable te = (TECarpenterTable) worldIn.getTileEntity(pos);
		if (te != null) {
			return te.treeType;
		}
		return null;
	}
	
	public EnumFacing getOrientation(IBlockAccess worldIn, BlockPos pos) {
		TECarpenterTable te = (TECarpenterTable) worldIn.getTileEntity(pos);
		if (te != null) {
			return te.getOrientation();
		}
		return null;
	}
	
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{TREETYPE, FACING});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}
	
	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TECarpenterTable();
	}
	
	public ItemStack getItemStackFor(EnumTree treeType) {
		return ((ItemCarpenterTable)Item.getItemFromBlock(this)).withTreeType(treeType);
	}

	
	@Override
	public int getRenderType() {
		return 3;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

    public boolean isFullCube() {
    	return false;
    }
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    	playerIn.openGui(VintageCraft.instance, 6, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	
	

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TECarpenterTable) {
        	((TECarpenterTable) tileentity).ejectContents();
        }

        
		ItemStack stack = ((ItemCarpenterTable)Item.getItemFromBlock(this)).withTreeType(getTreeType(worldIn, pos));		
		spawnAsEntity(worldIn, pos, stack);

		
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new java.util.ArrayList<ItemStack>();
	}
	
}

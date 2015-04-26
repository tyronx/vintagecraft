package at.tyron.vintagecraft.Block.Utility;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.TileEntity.TEForge;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;

public class BlockForge extends BlockContainerVC implements IBlockItemSink, IMultiblock {
	public PropertyBlockClass ROCKTYPE;
	public static PropertyInteger filllevel = PropertyInteger.create("filllevel", 0, 12);
	
	protected BlockClassEntry[] subtypes;
	
	
	public BlockForge() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}


	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos).withProperty(filllevel, getFillLevel(worldIn, pos));
	}
	

	
	public int getFillLevel(IBlockAccess worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TEForge) {
			return ((TEForge)te).getFillLevel();
		}
		return 0;
	}




	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEForge();
	}

	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (itemstack.getItem() instanceof ItemOreVC && ((ItemOreVC)itemstack.getItem()).getOreType(itemstack) == EnumOreType.BITUMINOUSCOAL) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TEForge) {
				boolean ok = ((TEForge)te).tryFill();
				if (ok) itemstack.stackSize--;
				return ok;
			}	
		}
		return false;
	}

	@Override
	public int multistateAvailableTypes() {
		return 16;
	}
	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}

    @Override
    protected BlockState createBlockState() {
    	if (getTypeProperty() != null) {
    		return new BlockState(this, new IProperty[] {getTypeProperty(), filllevel});
    	}
    	return new BlockState(this, new IProperty[]{filllevel});
    }

	@Override
	public IProperty getTypeProperty() {
		return ROCKTYPE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.ROCKTYPE = property;
	}

	@Override
	public BlockClass getBlockClass() {
		return BlocksVC.forge;
	}
	
    @Override
    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state);
    }
      
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getBlockClassfromMeta(this, meta).getBlockState();
    }
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
	}



	@Override
	public String getSubType(ItemStack stack) {
		return getBlockClass().getFromItemStack(stack).getKey().getStateName();
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.0625f, pos.getY(), pos.getZ() + 0.0625f, pos.getX() + 0.9375f, pos.getY() + 0.875f, pos.getZ() + 0.9375f);
	}

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}

	
	
	public boolean suitableGround(World world, BlockPos groundpos) {
		return world.isSideSolid(groundpos, EnumFacing.UP);
	}


	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(world, pos) && suitableGround(world, pos.down());
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!suitableGround(world, pos.down())) {
			world.destroyBlock(pos, true);
		}
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
	
	
    
}

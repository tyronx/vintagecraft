package at.tyron.vintagecraft.Block.Metalworking;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.TileEntity.TECokeOvenDoor;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCokeOvenDoor extends BlockContainerVC {
	public static PropertyBool OPENED = PropertyBool.create("opened");
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockCokeOvenDoor() {
		super(Material.iron);
		setCreativeTab(VintageCraft.craftedBlocksTab);
		setDefaultState(blockState.getBaseState().withProperty(OPENED, false).withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Metalworking;
	}

	
	
	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TECokeOvenDoor();
	}
	
	
    
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta >> 1);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        
        boolean opened = (meta & 1) > 0;
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(OPENED, opened);
    }

    public int getMetaFromState(IBlockState state) {
    	boolean opened = (Boolean)state.getValue(OPENED);
        return (((EnumFacing)state.getValue(FACING)).getIndex() << 1) + (opened ? 1 : 0);
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {OPENED, FACING});
    }

    @Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing facing = (EnumFacing) state.getValue(FACING);

	//	if (!suitableGround(world, pos.down())) {
	//		world.destroyBlock(pos, true);
	//	}
	}
    
    
    @Override
    public int damageDropped(IBlockState state) {
    	return 0;
    }

	

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return true; //this.canPlaceBlockAt(world, pos) && suitableGround(world, pos.down());
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

	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    	boolean opened = (Boolean)state.getValue(OPENED);
    	worldIn.setBlockState(pos, state.withProperty(OPENED, !opened));
    	
    	if (opened) {
    		worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:ironstovedoor_close", 1f, 1f, false);
    	} else {
    		worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:ironstovedoor_open", 1f, 1f, false);
    	}
    	
    	worldIn.getBlockState(pos).getBlock().setBlockBoundsBasedOnState(worldIn, pos);
    	
//    	TECokeOvenDoor te = (TECokeOvenDoor)worldIn.getTileEntity(pos);
//    	if (te != null) {
//    		System.out.println("valid: " + te.isValidCokeOven());
//    	}
    	
    	return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }
    
    


	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing face = (EnumFacing) state.getValue(FACING);
		boolean opened = (Boolean)state.getValue(OPENED);
		
		if (opened) {
		
			switch (face) {
				case NORTH:
					return AxisAlignedBB.fromBounds(pos.getX() + 1f, pos.getY(), pos.getZ() - 0.8125f, pos.getX() + 0.8125f, pos.getY() + 1f, pos.getZ() + 0.1875f);
				case SOUTH:
					return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ() + 0.8125f, pos.getX() + 0.1875f, pos.getY() + 1f, pos.getZ() + 1.8125f);
				case WEST:
					return AxisAlignedBB.fromBounds(pos.getX() - 0.8125f, pos.getY(), pos.getZ(), pos.getX() + 0.1875f, pos.getY() + 1f, pos.getZ() + 0.1875f);
				case EAST:
					return AxisAlignedBB.fromBounds(pos.getX() + 0.8125f, pos.getY(), pos.getZ() + 0.8125f, pos.getX() + 1.8125f, pos.getY() + 1f, pos.getZ() + 1f);
				default: return null; 
			}
		} else {

			switch (face) {
				case NORTH:
					return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 0.1875f);
				case SOUTH:
					return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ() + 0.8125f, pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 1f);
				case WEST:
					return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 0.1875f, pos.getY() + 1f, pos.getZ() + 1f);
				case EAST:
					return AxisAlignedBB.fromBounds(pos.getX() + 0.8125f, pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 1f);
				default: return null; 
			}

		}
		
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		AxisAlignedBB bounds = getCollisionBoundingBox(null, pos, worldIn.getBlockState(pos));
		setBlockBounds((float)bounds.minX - pos.getX(), (float)bounds.minY - pos.getY(), (float)bounds.minZ - pos.getZ(), (float)bounds.maxX - pos.getX(), (float)bounds.maxY - pos.getY(), (float)bounds.maxZ - pos.getZ());
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}
	
}

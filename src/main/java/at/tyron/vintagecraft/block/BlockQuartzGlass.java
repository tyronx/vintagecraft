package at.tyron.vintagecraft.Block;

import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Organic.BlockLogVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.World.BlocksVC;
import javafx.beans.property.BooleanProperty;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockQuartzGlass extends BlockLogVC {
	public static PropertyBool eastwest = PropertyBool.create("eastwest");
	
	
	public BlockQuartzGlass() {
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()).withProperty(eastwest, false));
	}
	
	
	@Override
	public int multistateAvailableTypes() {
		return 8;
	}
	
	
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }
    
    @Override
    public int getRenderType() {
    	return 3;
    } 

    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
    	return false;
    }
    
    @Override
    public boolean isVisuallyOpaque() {
    	return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    	return true;
    }
        
    @Override
    protected BlockState createBlockState() {
    	if (getTypeProperty() != null) {
    		return new BlockState(this, new IProperty[] {getTypeProperty(), eastwest});
    	}
    	return new BlockState(this, new IProperty[0]);
    }
	
	
    @Override
    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state) + ((Boolean)state.getValue(eastwest) ? 8 : 0);
    }
      
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getEntryFromMeta(this, meta & 7).getBlockState().withProperty(eastwest, (meta & 8) > 0);
    }

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.quartzglass;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "tile.quartzglass";
	}

	
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		state = world.getBlockState(pos); // passed state is null, strange
		
		if (state.getBlock() == this && (Boolean)state.getValue(eastwest)) {
			return AxisAlignedBB.fromBounds(pos.getX() + 0.375f, pos.getY(), pos.getZ(), pos.getX() + 0.625f, pos.getY() + 1f, pos.getZ() + 1f);
		} else {
			return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ() + 0.375f, pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 0.625f);
		}
		
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, null);
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		if ((Boolean)world.getBlockState(pos).getValue(eastwest)) {
			this.setBlockBounds(0.375f, 0f, 0f, 0.625f, 1f, 1f);
		} else {		
			this.setBlockBounds(0f, 0.375f, 0f, 1f, 1f, 0.625f);
		}
	}

    
	

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    	EnumFacing playerfacing = placer.getHorizontalFacing().getOpposite();
    	
    	IBlockState state = super.onBlockPlaced(worldIn, pos, playerfacing, hitX, hitY, hitZ, meta, placer);
    	
    	if (playerfacing == EnumFacing.WEST || playerfacing == EnumFacing.EAST) {
    		return state.withProperty(eastwest, true);
    	}
    	
        return state;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    	EnumFacing playerfacing = placer.getHorizontalFacing().getOpposite();
    	
    	if (playerfacing == EnumFacing.WEST || playerfacing == EnumFacing.EAST) {
    		worldIn.setBlockState(pos, state.withProperty(eastwest, true));
    	}
    	


    }
    
}

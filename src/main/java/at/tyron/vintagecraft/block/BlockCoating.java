package at.tyron.vintagecraft.Block;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;

// Block for representing materials that coat themselfs or stick on surfaces
// see also http://en.wikipedia.org/wiki/Efflorescence
public abstract class BlockCoating extends BlockVC implements IMultiblock {
	public PropertyBlockClass FACINGS;
	
	public BlockCoating(Material materialIn) {
		super(materialIn);
		setCreativeTab(VintageCraft.terrainTab);
	}

	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}


	
	public BlockClassEntry getFacings(IBlockState state) {
		return (BlockClassEntry)state.getValue(getTypeProperty());
	}
	

	
	
	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		AxisAlignedBB bounds = getSelectedBoundingBox2(worldIn, pos);
		setBlockBounds((float)bounds.minX - pos.getX(), (float)bounds.minY - pos.getY(), (float)bounds.minZ - pos.getZ(), (float)bounds.maxX - pos.getX(), (float)bounds.maxY - pos.getY(), (float)bounds.maxZ - pos.getZ());
    }

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getSelectedBoundingBox2(worldIn, pos);
	}
	
	
	public AxisAlignedBB getSelectedBoundingBox2(IBlockAccess worldIn, BlockPos pos) {
		String facings = getBlockClass().getEntryFromState(worldIn.getBlockState(pos)).getKey().getStateName();
		
		if (facings.length() == 1) {
			char facing = facings.charAt(0);
			
			return AxisAlignedBB.fromBounds(
				pos.getX() + (facing == 'e' ? 0.9375f : 0f), 
				pos.getY() + (facing == 'u' ? 0.9375f : 0f), 
				pos.getZ() + (facing == 's' ? 0.9375f : 0f), 
				pos.getX() + (facing == 'w' ? 0.0625f : 1f), 
				pos.getY() + (facing == 'd' ? 0.0625f : 1f), 
				pos.getZ() + (facing == 'n' ? 0.0625f : 1f)
			);
		}
		
		return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 1f, pos.getZ() + 1f);
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
      
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getEntryFromMeta(this, meta).getBlockState();
    }

    

	
	

	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
	}

	@Override
	public int multistateAvailableTypes() {
		return 16;
	}

	@Override
	public IProperty getTypeProperty() {
		return FACINGS;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		FACINGS = property;
	}

	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
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
    public boolean isFullCube() {
    	return false;
    }
    

    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    	String facings = ((BlockClassEntry)worldIn.getBlockState(pos).getValue(FACINGS)).getKey().getStateName();
    	
    	return facings.indexOf(side.getName().substring(1).toLowerCase(Locale.ROOT)) != -1;
    }
	
    
}

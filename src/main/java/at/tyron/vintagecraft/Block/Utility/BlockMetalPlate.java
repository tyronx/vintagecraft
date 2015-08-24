package at.tyron.vintagecraft.Block.Utility;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.MetalPlatingClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockMetalPlate extends BlockVC implements IMultiblock {
	public PropertyBlockClass METALANDFACINGS;
	public PropertyBool CUTOUT;
	
	public BlockMetalPlate() {
		super(Material.iron);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = EnumFacing.UP;
		String side = getSides(state);
		
		for (EnumFacing face : EnumFacing.values()) {
			if (face.name().toLowerCase(Locale.ROOT).charAt(0) == side.charAt(0)) {
				facing = face;
				break;
			}
		}
		
		IBlockState attachedstate = worldIn.getBlockState(pos.offset(facing)); 
		if (
			attachedstate.getBlock() instanceof BlockFurnaceSection &&
			(EnumFacing)attachedstate.getValue(BlockFurnaceSection.FACING) == facing.getOpposite()
		) {
			return super.getActualState(state, worldIn, pos).withProperty(CUTOUT, true);
		}
		return super.getActualState(state, worldIn, pos);
	}
	   
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			MetalPlatingClassEntry metalentry = (MetalPlatingClassEntry)entry;
			if (metalentry.side.equals("d")) {
				list.add(entry.getItemStack());
			}
		}
	}

		
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		CUTOUT = PropertyBool.create("cutout");
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
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
		if (worldIn.isAirBlock(pos) || !(worldIn.getBlockState(pos).getBlock() instanceof BlockMetalPlate)) return null;
		String facings = getSides(worldIn.getBlockState(pos));
		
		
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
    		return new BlockState(this, new IProperty[] {getTypeProperty(), CUTOUT});
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

 
	
	public String getSides(IBlockState state) {
		return ((BlockClassEntry)state.getValue(METALANDFACINGS)).getKey().getStateName().split("-")[1];
	}
	
    public static EnumMetal getMetal(IBlockState state) {
    	String[] type = ((MetalPlatingClassEntry)state.getValue(((IMultiblock)BlocksVC.metalplate.getEntryFromState(state).block).getTypeProperty())).getName().split("-");
    	return EnumMetal.valueOf(type[0].toUpperCase(Locale.ROOT));
    }
    
    public static EnumFacing getFacing(IBlockState state) {
    	String side = ((BlockMetalPlate)state.getBlock()).getSides(state);
    	for (EnumFacing facing : EnumFacing.values()) {
    		if (facing.getName().charAt(0) == side.charAt(0)) return facing;
    	}
    	return null;
    }

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
     	EnumMetal metal = getMetal(state);
     	ret.add(BlocksVC.metalplate.getItemStackFor(metal));
        return ret;
    }


	
	
	@Override
	public int multistateAvailableTypes() {
		return 16;
	}

	@Override
	public IProperty getTypeProperty() {
		return METALANDFACINGS;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		METALANDFACINGS = property;
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
		return getSelectedBoundingBox2(worldIn, pos);
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
    	String facings = getSides(worldIn.getBlockState(pos));
    	
    	return facings.indexOf(side.getName().substring(1).toLowerCase(Locale.ROOT)) != -1;
    }
	
	public BaseBlockClass getBlockClass() {
		return BlocksVC.metalplate;
	}
	
	
	// Remove registration of multiple item variants
    @Override
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types, String folderprefix) {
		//if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (IStateEnum type : types) {
			String[] metalandfacing = type.getStateName().split("-");
			if (metalandfacing[1].equals("d")) {
				VintageCraft.instance.proxy.registerItemBlockTexture(this, folderprefix, metalandfacing[0].toLowerCase(Locale.ROOT), type.getMetaData(this));
			}
		}
		
		return this;
	}

    
}

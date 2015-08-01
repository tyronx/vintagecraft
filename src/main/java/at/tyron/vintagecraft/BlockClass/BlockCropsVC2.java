package at.tyron.vintagecraft.BlockClass;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.ItemLogVC;
import at.tyron.vintagecraft.World.BlocksVC;

public class BlockCropsVC2 extends BlockVC implements ISubtypeFromStackPovider, IMultiblock {
	public PropertyBlockClass CROPTYPEANDSTAGE;
	
	protected BlockCropsVC2() {
		super(Material.plants);
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
	
	
	public BlockClassEntry getCropType(IBlockState state) {
		return (BlockClassEntry)state.getValue(getTypeProperty());
	}


	    
	

	@Override
	public IProperty getTypeProperty() {
		return CROPTYPEANDSTAGE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.CROPTYPEANDSTAGE = property;
	}

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.crops;
	}

	@Override
	public int multistateAvailableTypes() {
		return 16;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "tile.crops";
	}
	
	
	 
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	/*ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
        ItemLogVC.withTreeType(itemstack, getTreeType(state));
        ret.add(itemstack);*/
        
    	return ret;
    }
    
    // Remove registration of multiple variants, as there is not items for these
    @Override
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		return this;
	}

    
    
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }
    public boolean isOpaqueCube() {
        return false;
    }
    public boolean isFullCube() {
        return false;
    }
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

}

package at.tyron.vintagecraft.Block.Carpentry;

import java.lang.reflect.InvocationTargetException;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Interfaces.ICategorizedBlockOrItem;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BlockWoodenRail extends BlockRailBase implements ICategorizedBlockOrItem {
    public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class);

    public BlockWoodenRail() {
        super(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
    }
    
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Carpentry;
	}


    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    	Rail rbase = new BlockRailBase.Rail(worldIn, pos, state);
    	
        try {
			if (neighborBlock.canProvidePower() && (Integer)ReflectionHelper.findMethod(Rail.class, rbase, new String[]{"countAdjacentRails"}, new Class[]{}).invoke(new Class[]{}) == 3) {
			    this.func_176564_a(worldIn, pos, state, false);
			}
		} catch (Exception e) {
			
		}
    }
    
    
    

    public IProperty getShapeProperty() {
        return SHAPE;
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).getMetadata();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {SHAPE});
    }
    
    public float getRailMaxSpeed(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos) {
        return 0.25f;
    }

    
   	public Block register(String blockclassname, Class<? extends ItemBlock> itemclass) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		//VintageCraft.instance.proxy.registerItemBlockTexture(this, EnumRailDirection.NORTH_SOUTH.getName());
				
		return this;
	}

}

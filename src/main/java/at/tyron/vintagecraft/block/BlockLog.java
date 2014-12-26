package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLog extends BlockVC {
	public static final PropertyEnum TREETYPE = PropertyEnum.create("treetype", EnumTree.class);
	
	
	public BlockLog() {
		super(Material.wood);
		
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
    	return ((EnumTree)state.getValue(TREETYPE)).id;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return super.getStateFromMeta(meta).withProperty(TREETYPE, EnumTree.byId(meta));
    }

}

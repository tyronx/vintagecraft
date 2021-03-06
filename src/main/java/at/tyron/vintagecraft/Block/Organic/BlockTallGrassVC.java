package at.tyron.vintagecraft.Block.Organic;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.Block.IBlockEatableGrass;
import at.tyron.vintagecraft.Interfaces.Block.IBlockSoil;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTallGrass;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTallGrassGroup;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockTallGrassVC extends BlockVC implements IPlantable, IBlockEatableGrass {
	public static final PropertyEnum GRASSTYPE = PropertyEnum.create("grasstype", EnumTallGrass.class);
	
	public BlockTallGrassVC() {
		super(Material.plants);
		setCreativeTab(VintageCraft.floraTab);
		this.setDefaultState(this.blockState.getBaseState().withProperty(GRASSTYPE, EnumTallGrass.LONG));
		this.setTickRandomly(true);
		float f = 0.5F;
	    this.setBlockBounds(
	    		0.5F - f, 0.0F, 0.5F - f, 
	    		0.5F + f, f, 0.5F + f
	    );
	}
	
	
	
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemsVC.dryGrass;
    }
    
    @Override
    public int damageDropped(IBlockState state) {
    	return 0;
    }
    
    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
    	EnumTallGrass grasstype = (EnumTallGrass) state.getValue(GRASSTYPE);
    	
    	if (random.nextFloat() < grasstype.drygrassDropChance) {
    		return 1;
    	}
    	
    	return 0;
    }
    


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumTallGrass grass : EnumTallGrass.values()) {
    		list.add(new ItemStack(itemIn, 1, grass.getMetaData(this)));
    	}
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(GRASSTYPE, EnumTallGrass.fromMeta(meta));
    }


    public int getMetaFromState(IBlockState state) {
        return ((EnumTallGrass)state.getValue(GRASSTYPE)).getMetaData(this);
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {GRASSTYPE});
    }
    
    
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return ColorizerGrass.getGrassColor(0.5D, 1.0D);
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state) {
        return this.getBlockColor();
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return VCraftWorld.instance.getGrassColorAtPos(pos);
    }
	

    
	
	
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }
    

    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos) {
    	return true;
    }
    
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    	
        return super.canPlaceBlockAt(worldIn, pos) && canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    protected boolean canPlaceBlockOn(Block ground) {
        return ground instanceof IBlockSoil && ((IBlockSoil)ground).canGrowTallGrass(null, null);
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.checkAndDropBlock(worldIn, pos, state);
        
        if (state.getValue(GRASSTYPE) == EnumTallGrass.EATEN && rand.nextFloat() < 0.001f) {
        	int[]climate = VCraftWorld.instance.getClimate(pos);
        	
        	EnumTallGrass newgrass = EnumTallGrassGroup.fromClimate(climate[1], climate[0], rand);
        	if (newgrass != null && newgrass.getId() < 6) { // Only until tallgrass flowering
        		worldIn.setBlockState(pos, state.withProperty(GRASSTYPE, newgrass));
//        		System.out.println("regrown to " + newgrass + " at " + pos);
        	}
        }
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
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

    
    
    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return net.minecraftforge.common.EnumPlantType.Plains;
    }
    
    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.getDefaultState();
    }

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

    public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }



	@Override
	public boolean canBeEatenBy(EntityLiving entity, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return 
			state.getBlock() instanceof BlockTallGrassVC && (
				state.getValue(GRASSTYPE) == EnumTallGrass.SHORT ||
				state.getValue(GRASSTYPE) == EnumTallGrass.MEDIUM ||
				state.getValue(GRASSTYPE) == EnumTallGrass.LONG ||
				state.getValue(GRASSTYPE) == EnumTallGrass.VERYLONG ||
				state.getValue(GRASSTYPE) == EnumTallGrass.VERYLONG_FLOWERING
			)
		;
	}



	@Override
	public void setEatenBy(EntityLiving entity, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		world.setBlockState(pos, state.withProperty(GRASSTYPE, EnumTallGrass.EATEN));
	}

}

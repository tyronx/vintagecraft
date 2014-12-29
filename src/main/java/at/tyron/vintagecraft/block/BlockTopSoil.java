package at.tyron.vintagecraft.block;

import java.util.Random;





import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTopSoil extends BlockVC {
	
	
	public static final PropertyEnum organicLayer = PropertyEnum.create("organiclayer", EnumOrganicLayer.class);

	public BlockTopSoil() {
		super(Material.grass);
		
		isSoil = true;
		
		this.setTickRandomly(true);
		
        this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.None));
        this.setCreativeTab(CreativeTabs.tabBlock);

	}
	
	@Override
	public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return plantable.getPlantType(world, pos) == EnumPlantType.Plains;
	}
	
	
	@Override
	public BlockVC setHardness(float hardness) {
		return (BlockVC) super.setHardness(hardness);
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
        return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
    }

	
	
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }
	
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	
        if (!worldIn.isRemote && state.getValue(organicLayer) != EnumOrganicLayer.None) {
            if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getBlock().getLightOpacity(worldIn, pos.up()) > 2) {
                worldIn.setBlockState(pos, getDefaultState());
            }
            else {
                if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                        Block block = worldIn.getBlockState(blockpos1.up()).getBlock();
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

                        if (iblockstate1.getBlock() instanceof BlockTopSoil && worldIn.getLightFromNeighbors(blockpos1.up()) >= 4 && block.getLightOpacity(worldIn, blockpos1.up()) <= 2) {
                            worldIn.setBlockState(blockpos1, getDefaultState().withProperty(BlockTopSoil.organicLayer, EnumOrganicLayer.NormalGrass));
                        }
                    }
                }
                
               /* if (worldIn.getLightFromNeighbors(pos.up()) >= 14 && worldIn.getBlockState(pos.up()).getBlock() == Blocks.air) {
                	if (rand.nextInt(10) == 0) {
                		worldIn.setBlockState(pos.up(), Blocks.tallgrass.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS));
                	}
                }*/
            }
        }
    }

	
	

	

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		/*if(worldIn.isAirBlock(x, y - 1, z)) {
			int meta = world.getBlockMetadata(x, y, z);
			world.setBlock(x, y, z, TFC_Core.getTypeForDirtFromGrass(this), meta, 0x2);
			world.scheduleBlockUpdate(x, y, z, TFC_Core.getTypeForDirtFromGrass(this), 5);
		}*/
	}
	
	
	
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {organicLayer});
    }
    
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumOrganicLayer)state.getValue(organicLayer)).getMetaData();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.byMetadata(meta));
    }

    
	
}

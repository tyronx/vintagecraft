package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;








import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemLogVC;
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
import net.minecraft.item.ItemStack;
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
	public static final PropertyEnum fertility = PropertyEnum.create("fertility", EnumFertility.class);

	public BlockTopSoil() {
		this(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.None).withProperty(fertility, EnumFertility.MEDIUM));
	}
	
	public BlockTopSoil(boolean istopsoil) {
		super(Material.grass);
		isSoil = true;
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumOrganicLayer organiclayer : EnumOrganicLayer.values()) {
    		for (EnumFertility fertility : EnumFertility.values()) {
    			list.add(new ItemStack(itemIn, 1, organiclayer.meta + (fertility.getMetaData() << 2)));
    		}
    	}
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
    
    
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
    	itemstack.setItemDamage(((EnumFertility)state.getValue(fertility)).getMetaData() << 2);
        ret.add(itemstack);
        
    	return ret;
    }

    
    
    
    
	
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	if (worldIn.isRemote) return;
    	
    	// Up or downgrade grass depending on light conditions
    	EnumOrganicLayer organiclayer = (EnumOrganicLayer)state.getValue(organicLayer);
    	if (organiclayer != EnumOrganicLayer.None) {
    		EnumOrganicLayer adjustedorganiclayer = organiclayer.adjustToLight(worldIn.getLight(pos.up()));
    		if (adjustedorganiclayer != organiclayer) {
    		//	System.out.println("curr: " + organiclayer + " next: " + adjustedorganiclayer + " light: " + worldIn.getLight(pos.up()));
    			worldIn.setBlockState(pos, state.withProperty(organicLayer, adjustedorganiclayer));
    		}
        }
            
        // Spread grass when at least medium grown 
        if (organiclayer != EnumOrganicLayer.None) {
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    Block block = worldIn.getBlockState(blockpos1.up()).getBlock();
                    IBlockState neighbourblockstate = worldIn.getBlockState(blockpos1);

                    if (
                    	neighbourblockstate.getBlock() instanceof BlockTopSoil 
                    	&& worldIn.getLight(blockpos1.up()) >= EnumOrganicLayer.VerySparseGrass.minblocklight 
                    	&& block.getLightOpacity(worldIn, blockpos1.up()) <= 2
                    	&& ((EnumOrganicLayer)neighbourblockstate.getValue(organicLayer)) == EnumOrganicLayer.None
                    ) {
                        worldIn.setBlockState(blockpos1, neighbourblockstate.withProperty(BlockTopSoil.organicLayer, EnumOrganicLayer.VerySparseGrass));
                    }
                }
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
        return new BlockState(this, new IProperty[] {organicLayer, fertility});
    }
    
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 
        	((EnumOrganicLayer)state.getValue(organicLayer)).getMetaData()
        	+ 
        	(((EnumFertility)state.getValue(fertility)).getMetaData() << 2);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return 
    		this.blockState.getBaseState()
    			.withProperty(organicLayer, EnumOrganicLayer.fromMeta(meta & 3))
    			.withProperty(fertility, EnumFertility.fromMeta((meta >> 2) & 3))
    	;
    }

    
	
}

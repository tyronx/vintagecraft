package at.tyron.vintagecraft.Block.Terrafirma;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Item.Terrafirma.ItemFarmLand;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;

public class BlockFarmlandVC extends BlockContainerVC {
	public static final PropertyEnum fertility = PropertyEnum.create("fertility", EnumFertility.class);
	
	
	public BlockFarmlandVC() {
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState());
		setCreativeTab(VintageCraft.floraTab);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Terrafirma;
	}

	
	
	public boolean isFertile(World world, BlockPos pos) {
		return false;
    }


    @Override
    public int getRenderType() { return 3; }

    

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEFarmland();
	}
	    

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		ItemFarmLand farmlanditem = (ItemFarmLand)stack.getItem();
		setFertility(farmlanditem.getFertility(stack).getAsNumber(), worldIn, pos);
	}
	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TEFarmland) {
			TEFarmland cte = (TEFarmland) te;
			EnumFertility fert = EnumFertility.fromFertilityValue(cte.getFertility());
			
			if (fert != null) return getDefaultState().withProperty(fertility, fert);
		}
		
		
		return this.getDefaultState();
	}
    

 
    public int getMetaFromState(IBlockState state) {
        return  (((EnumFertility)state.getValue(fertility)).getMetaData(this) << 2);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return this.blockState.getBaseState().withProperty(fertility, EnumFertility.fromMeta((meta >> 2) & 3));
    }

    
  

    @Override
    protected BlockState createBlockState() {
    	return new BlockState(this, new IProperty[] {fertility});
    }
    
    
   /* public void reduceFertility(World worldIn, BlockPos pos) {
    	TileEntity te = worldIn.getTileEntity(pos);
    	
        if(te instanceof TEFarmland) {
        	TEFarmland teFarmland = (TEFarmland) te;
        	teFarmland.consumeFertility(5);
        }
    }*/
    
    
    public EnumFertility getFertility(IBlockAccess worldIn, BlockPos pos) {
    	TileEntity te = worldIn.getTileEntity(pos);
    	
        if(te instanceof TEFarmland) {
        	TEFarmland teFarmland = (TEFarmland) te;
        	return EnumFertility.fromFertilityValue(teFarmland.getFertility());
        }
        
        return null;
    }
    
    
    public void setFertility(int fertility, World worldIn, BlockPos pos) {
    	TileEntity te = worldIn.getTileEntity(pos);
    	
        if(te instanceof TEFarmland) {
        	TEFarmland teFarmland = (TEFarmland) te;
        	teFarmland.setFertility(fertility);
        }
    }
    
        
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	/*EnumFertility fertility = getFertility(worldIn, pos);
    	
    	if (fertility != null && fertility != EnumFertility.VERYLOW) {
    		ItemStack itemstack = new ItemStack(BlocksVC.topsoil, 1, EnumOrganicLayer.NOGRASS.getMetaData(null) + (fertility.getMetaData(null) << 2));
    		spawnAsEntity(worldIn, pos, itemstack);
    	}
    	*/
    	
    	return;
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	return new java.util.ArrayList<ItemStack>();
    }
    
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
    	net.minecraftforge.common.EnumPlantType plantType = plantable.getPlantType(world, pos.up());
    	
    	if (plantType == EnumPlantType.Crop || plantType == EnumPlantType.Plains) {
    		return true;
    }
		return false;
    }



	@Override
	public String getSubType(ItemStack stack) {
		EnumFertility fertility = EnumFertility.fromMeta((stack.getItemDamage() >> 2) & 3);		
		return fertility.getStateName();
	}

	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	@Override
	public boolean isFullBlock() {
		return false;
	}
	@Override
	public boolean isFullCube() {
		return false;
	}
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
}

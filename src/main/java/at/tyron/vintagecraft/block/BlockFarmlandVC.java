package at.tyron.vintagecraft.block;

import java.util.Random;

import at.tyron.vintagecraft.TileEntity.TEFarmland;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

public class BlockFarmlandVC extends BlockContainer {
	public static final PropertyEnum fertility = PropertyEnum.create("fertility", EnumFertility.class);
	
	public static final IUnlistedProperty<Integer> fertilityExact = Properties.toUnlisted(PropertyInteger.create("fertility", 0, 40));

	
	public BlockFarmlandVC() {
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState());
	}
	

	/*public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		int i = ((Integer)state.getValue(MOISTURE)).intValue();

		if (!this.hasWater(worldIn, pos) && !worldIn.canLightningStrike(pos.up()))
		{
			if (i > 0)
			{
				worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(i - 1)), 2);
			}
			else if (!this.hasCrops(worldIn, pos))
			{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
			}
		}
		else if (i < 7)
		{
			worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(7)), 2);
		}
	}*/

	
	
	public boolean isFertile(World world, BlockPos pos) {
       /* if (this == net.minecraft.init.Blocks.farmland) {
            return ((Integer)world.getBlockState(pos).getValue(BlockFarmland.MOISTURE)) > 0;
        }

        return false;*/
		return false;
    }


    @Override
    public int getRenderType() { return 3; }

	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEFarmland();
	}

	
	 @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TEFarmland) {
        	TEFarmland cte = (TEFarmland) te;
            return cte.getState();
        } else {
        	/*if (te == null) {
        		System.out.println("getExtendedState() Error: tileentity is null!");
        	} else {
        		System.out.println("getExtendedState() Error: te is NOT of instance TEOre at pos " + pos);
        	}*/
        }
        return state;
    }
	 
	 
	    public IBlockState getStateFromMeta(int meta) {
	        return this.getDefaultState().withProperty(fertility, EnumFertility.fromMeta(meta));
	    }

	    public int getMetaFromState(IBlockState state) {
	        return ((EnumFertility)state.getValue(fertility)).getMetaData();
	    }
	    

    @Override
    protected BlockState createBlockState() {
    	return new BlockState(this, new IProperty[] {fertility});
    }
    
    
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	
    	TileEntity te = worldIn.getTileEntity(pos);
    	
        if(te instanceof TEFarmland) {
        	TEFarmland teFarmland = (TEFarmland) te;
        	
        	EnumFertility fertility = EnumFertility.values()[teFarmland.getFertility() / 10];
        	
        	ItemStack itemstack = new ItemStack(BlocksVC.topsoil, EnumOrganicLayer.NoGrass.getMetaData(null) + (fertility.getMetaData(null) << 2));           
            spawnAsEntity(worldIn, pos, itemstack);

        }
    	
    	super.breakBlock(worldIn, pos, state);
    }
    
    
    public boolean canSustainPlant(IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
    	net.minecraftforge.common.EnumPlantType plantType = plantable.getPlantType(world, pos.up());
    	
    	if (plantType == EnumPlantType.Crop || plantType == EnumPlantType.Plains) {
    		return true;
    }
		return false;
    }
	    
	    
}

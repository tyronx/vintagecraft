package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.TileEntity.TEFarmland;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.interfaces.ISoil;
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
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTopSoil extends BlockVC implements ISoil {
	public static final PropertyEnum organicLayer = PropertyEnum.create("organiclayer", EnumOrganicLayer.class);
	public static final PropertyEnum fertility = PropertyEnum.create("fertility", EnumFertility.class);

	
	public BlockTopSoil() {
		super(Material.grass);
		this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.NormalGrass).withProperty(fertility, EnumFertility.MEDIUM));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumOrganicLayer organiclayer : EnumOrganicLayer.values()) {
    		for (EnumFertility fertility : EnumFertility.values()) {
    			list.add(new ItemStack(itemIn, 1, organiclayer.getMetaData(this) + (fertility.getMetaData(this) << 2)));
    		}
    	}
    }
    
	

	
	
   
    
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
    	itemstack.setItemDamage(((EnumFertility)state.getValue(fertility)).getMetaData(this) << 2);
        ret.add(itemstack);
        
    	return ret;
    }

    
    
    


    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {organicLayer, fertility});
    }
    
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 
        	((EnumOrganicLayer)state.getValue(organicLayer)).getMetaData(this)
        	+ 
        	(((EnumFertility)state.getValue(fertility)).getMetaData(this) << 2);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return 
    		this.blockState.getBaseState()
    			.withProperty(organicLayer, EnumOrganicLayer.fromMeta(meta & 3))
    			.withProperty(fertility, EnumFertility.fromMeta((meta >> 2) & 3))
    	;
    }

    
    
    
    
    
	@Override
	public boolean canSpreadGrass(World world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canGrowGrass(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public boolean canGrowTallGrass(World world, BlockPos pos) {
		return true;
	}


	@Override
	public EnumFertility getFertility(World world, BlockPos pos) {
		return (EnumFertility)world.getBlockState(pos).getValue(fertility);
	}

	@Override
	public IProperty getOrganicLayerProperty(World world, BlockPos pos) {
		return organicLayer;
	}


	@Override
	public boolean canGrowTree(World world, BlockPos pos, EnumTree tree) {
		return true;
	}

	
	@Override
	public void hoeUsed(UseHoeEvent event) {
		EnumFertility fertility = getFertility(event.world, event.pos);
		
		event.world.setBlockState(event.pos, BlocksVC.farmland.getDefaultState().withProperty(BlockFarmlandVC.fertility, fertility));
		TEFarmland tileentity = (TEFarmland)event.world.getTileEntity(event.pos);
		if (tileentity != null) {
			tileentity.setFertility(fertility.getId() * 10);			
		} else {
			System.out.println("tileentity was not created?");
		}
		
		event.setResult(Result.ALLOW);
		
		
		super.hoeUsed(event);
	}
}


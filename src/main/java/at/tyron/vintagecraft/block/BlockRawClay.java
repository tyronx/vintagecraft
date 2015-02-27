package at.tyron.vintagecraft.block;

import java.util.Random;

import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.interfaces.ISoil;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRawClay extends BlockVC implements ISoil {
	public static final PropertyEnum organicLayer = PropertyEnum.create("organiclayer", EnumOrganicLayer.class);

	public BlockRawClay() {
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.NormalGrass));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	

	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.clay_ball;
	}
	
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
	

	public int quantityDropped(Random random) {
	    return 1 + random.nextInt(3);
	}
	
	
    
	@Override
	protected BlockState createBlockState() {
	    return new BlockState(this, new IProperty[] {organicLayer});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
	    return ((EnumOrganicLayer)state.getValue(organicLayer)).getMetaData(this);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.fromMeta(meta));
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
	public EnumFertility getFertility(World world, BlockPos pos) {
		return EnumFertility.LOW;
	}

	@Override
	public IProperty getOrganicLayerProperty(World world, BlockPos pos) {
		return organicLayer;
	}


	@Override
	public boolean canGrowTree(World world, BlockPos pos, EnumTree tree) {
		return true;
	}
	    
}

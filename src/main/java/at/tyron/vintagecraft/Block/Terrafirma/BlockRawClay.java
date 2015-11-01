package at.tyron.vintagecraft.Block.Terrafirma;

import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRawClay extends BlockVC implements IBlockSoil {
	public static final PropertyEnum organicLayer = PropertyEnum.create("organiclayer", EnumOrganicLayer.class);

	public BlockRawClay() {
		super(Material.ground);
		this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.NORMALGRASS));
		this.setTickRandomly(true);
		setCreativeTab(VintageCraft.terrainTab);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Terrafirma;
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
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		return VCraftWorld.instance.getGrassColorAtPos(pos, 12);
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
	public boolean canGrowTallGrass(World world, BlockPos pos) {
		return true;
	}


	@Override
	public EnumFertility getFertility(World world, BlockPos pos) {
		return EnumFertility.LOW;
	}

	@Override
	public EnumOrganicLayer getOrganicLayer(World world, BlockPos pos) {
		return (EnumOrganicLayer)world.getBlockState(pos).getValue(organicLayer);
	}
	
	@Override
	public void setOrganicLayer(EnumOrganicLayer layer, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(organicLayer, layer));		
	}


	@Override
	public boolean canGrowTree(World world, BlockPos pos, EnumTree tree) {
		return true;
	}
	    
}

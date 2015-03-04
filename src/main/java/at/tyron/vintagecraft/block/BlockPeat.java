package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.interfaces.ISoil;
import at.tyron.vintagecraft.item.ItemLogVC;

public class BlockPeat extends BlockVC implements ISoil {
	public static final PropertyEnum organicLayer = PropertyEnum.create("organiclayer", EnumOrganicLayer.class);
	
	
	public BlockPeat() {
		super(Material.grass);
		this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.NormalGrass));
		this.setTickRandomly(true);
		this.setCreativeTab(CreativeTabs.tabBlock);
	}
	
	
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumOrganicLayer organiclayer : EnumOrganicLayer.values()) {
    		list.add(new ItemStack(itemIn, 1, organiclayer.getMetaData(this)));
    	}
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
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemsVC.peatbrick;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(3) + 1;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
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
		return EnumFertility.HIGH;
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

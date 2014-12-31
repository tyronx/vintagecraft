package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemLogVC;

public class BlockPeat extends BlockTopSoil {

	public BlockPeat() {
		super(false);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.NormalGrass));
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumOrganicLayer organiclayer : EnumOrganicLayer.values()) {
    		list.add(new ItemStack(itemIn, 1, organiclayer.meta));
    	}
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
		return this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.fromMeta(meta));
	}
	
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ItemsVC.peatbrick;
	}
	
	@Override
	public int quantityDropped(Random random) {
		return random.nextInt(3) + 2;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
	
}

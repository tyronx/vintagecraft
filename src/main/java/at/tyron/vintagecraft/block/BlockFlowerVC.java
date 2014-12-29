package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFlowerVC extends BlockVC implements IPlantable {
	public static final PropertyEnum FLOWERTYPE = PropertyEnum.create("flowertype", EnumFlower.class);
	
	public BlockFlowerVC() {
		super(Material.plants);
		setCreativeTab(CreativeTabs.tabDecorations);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FLOWERTYPE, EnumFlower.ORANGEBUTTERFLYMILKWEED));	
		this.setTickRandomly(true);
		float f = 0.2F;
	    this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
	}
	
	
	
	
	
	public int damageDropped(IBlockState state) {
        return ((EnumFlower)state.getValue(FLOWERTYPE)).getMetaData();
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumFlower flower : EnumFlower.values()) {
    		list.add(new ItemStack(itemIn, 1, flower.getMetaData()));
    	}
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FLOWERTYPE, EnumFlower.fromMeta(meta));
    }


    public int getMetaFromState(IBlockState state) {
        return ((EnumFlower)state.getValue(FLOWERTYPE)).getMetaData();
    }

    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {FLOWERTYPE});
    }

    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    

    
    
    
    
    
    
    
    
    
    
    
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    protected boolean canPlaceBlockOn(Block ground) {
        return ground == BlocksVC.topsoil || ground == BlocksVC.subsoil;
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
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

    
    
    
}


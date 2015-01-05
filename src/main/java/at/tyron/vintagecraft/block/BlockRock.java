package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemRock;
import at.tyron.vintagecraft.item.ItemStone;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


//- rock is solid rock - is below bedrock until world bottom - 1
//- hard to mine
//- graphics will be like smooth stone 


public class BlockRock extends BlockVC {
	// Does the block fall like sand?
	public boolean unstable;
	
	public static final PropertyEnum STONETYPE = PropertyEnum.create("type", EnumRockType.class);
    

	protected BlockRock(Material materialIn) {
		super(materialIn);
		
        this.setDefaultState(this.blockState.getBaseState().withProperty(STONETYPE, EnumRockType.GNEISS));
        this.setCreativeTab(CreativeTabs.tabBlock);
	}

	
    public BlockRock() {
        this(Material.rock);
	}
    

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	if (state.getBlock() == BlocksVC.rock) {
	        List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
	
	        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
	
	        ItemStack itemstack = new ItemStack(ItemsVC.stone, 2 + rand.nextInt(3));
	        ItemStone.setRockType(itemstack, (EnumRockType) state.getValue(STONETYPE));
	        
	        ret.add(itemstack);
	        
			return ret;
    	}
    	return super.getDrops(world, pos, state, fortune);
    }
    
    
   
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {STONETYPE});
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumRockType)state.getValue(STONETYPE)).getMetaData();
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return this.blockState.getBaseState().withProperty(STONETYPE, EnumRockType.byMetadata(meta));
    }


    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	EnumRockType[] aenumtype = EnumRockType.values();
        int i = aenumtype.length;

        for (int j = 0; j < i; ++j) {
        	EnumRockType enumtype = aenumtype[j];
            list.add(new ItemStack(itemIn, 1, enumtype.getMetaData()));
        }
    }
    
    

    
}

package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemOre;
import at.tyron.vintagecraft.item.ItemStone;

public class BlockOreVC extends BlockContainer {
	public static final IUnlistedProperty<Enum>[] properties = new IUnlistedProperty[2];

	static {
		properties[0] = Properties.toUnlisted(PropertyEnum.create("rocktype", EnumRockType.class));
		properties[1] = Properties.toUnlisted(PropertyEnum.create("oretype", EnumMaterialDeposit.class));
    }

	 
	public BlockOreVC() {
		super(Material.iron);
		
		this.setDefaultState(this.blockState.getBaseState());
	}

    @Override
    public int getRenderType() { return 3; }

    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TEOre();
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    
    
    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TEOre) {
        	TEOre cte = (TEOre) te;
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

    @Override
    protected BlockState createBlockState() {
        return new ExtendedBlockState(this, new IProperty[0], properties);
    }
    
   
    
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    	
    	TileEntity te = worldIn.getTileEntity(pos);
    	
        if(te instanceof TEOre) {
        	TEOre teOre = (TEOre) te;
        	
        	ItemStack itemstack = new ItemStack(ItemsVC.stone, worldIn.rand.nextInt(2));
            ItemStone.setRockType(itemstack, teOre.getRockType());          
            spawnAsEntity(worldIn, pos, itemstack);

            itemstack = new ItemStack(ItemsVC.ore, 1 + (worldIn.rand.nextInt(7) == 0 ? 1 : 0));
            ItemOre.setOreType(itemstack, teOre.getOreType());          
            spawnAsEntity(worldIn, pos, itemstack);
        }
    	
    	super.breakBlock(worldIn, pos, state);
    }

    
    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos,
    		IBlockState state, float chance, int fortune) {
    	
    }

}

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
import net.minecraft.init.Items;
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
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.OreClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.World.BlocksVC;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.interfaces.IMultiblock;
import at.tyron.vintagecraft.item.ItemOreVC;
import at.tyron.vintagecraft.item.ItemStone;

public class BlockOreVC extends BlockVC implements IMultiblock {
	public PropertyBlockClass OREANDROCKTYPE;
	
	public BlockOreVC() {
		super(Material.iron);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setDefaultState(this.blockState.getBaseState());
	}

	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}
	
	
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
	}
	
	
    
    @Override
    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return new BlockState(this, new IProperty[0]);
    	}
    	
        return new BlockState(this, new IProperty[] {getTypeProperty()});
    }
    
    
    
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getBlockClassfromMeta(this, meta).getBlockState();
    }


    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state);
    }
 
    
    public static EnumOreType getOreType(IBlockState state) {
    	String[] type = ((OreClassEntry)state.getValue(((IMultiblock)BlocksVC.rawore.getBlockClassfromState(state).block).getTypeProperty())).getName().split("-");
    	return EnumOreType.valueOf(type[0].toUpperCase());
    }

    
    public static EnumRockType getRockType(IBlockState state) {
    	String[] type = ((OreClassEntry)state.getValue(((IMultiblock)BlocksVC.rawore.getBlockClassfromState(state).block).getTypeProperty())).getName().split("-");
    	return EnumRockType.valueOf(type[1].toUpperCase());
    }

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	World worldIn = (World)world;
    	
     	String[] type = ((OreClassEntry)state.getValue(OREANDROCKTYPE)).getName().split("-");
     	
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase());
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase());
     	

     //	System.out.println("bla!");
     	
     	ItemStack itemstack;
     	if (worldIn.rand.nextInt(2) > 0) {
	     	ret.add(ItemStone.setRockType(new ItemStack(ItemsVC.stone, 1), rocktype));
	        //spawnAsEntity(worldIn, pos, itemstack);
     	}
        
        switch (oretype) {
        	case REDSTONE:
        		itemstack = new ItemStack(Items.redstone, 2 + worldIn.rand.nextInt(2));
        		ret.add(itemstack);
        		break;
        	
        		
        	case OLIVINE:
        		/*itemstack = new ItemStack(ItemsVC.stone, worldIn.rand.nextInt(2));
    	        ItemStone.setRockType(itemstack, rocktype);
    	        spawnAsEntity(worldIn, pos, itemstack);*/
    	        break;
        		
        	default: 
	        	itemstack = new ItemStack(ItemsVC.ore, 1 + (worldIn.rand.nextInt(7) == 0 ? 1 : 0));
	        	ItemOreVC.setOreType(itemstack, oretype);
	        	ret.add(itemstack);
	        	break;
        }
        
        //spawnAsEntity(worldIn, pos, itemstack);
        
    	
    	//super.breakBlock(worldIn, pos, state);
        return ret;
    }




	@Override
	public IProperty getTypeProperty() {
		return OREANDROCKTYPE;
	}


	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		OREANDROCKTYPE = property;
	}


	@Override
	public BlockClass getBlockClass() {
		return BlocksVC.rawore;
	}


	@Override
	public int multistateAvailableTypes() {
		return 16;
	}

}

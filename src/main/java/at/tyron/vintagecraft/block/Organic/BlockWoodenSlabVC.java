package at.tyron.vintagecraft.Block.Organic;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockWoodenSlabVC extends BlockWoodSlab implements IMultiblock {
	public PropertyBlockClass TREETYPE;
	BlockClassEntry[] subtypes;

	
	public BlockWoodenSlabVC() {
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}

	@Override
	public IProperty getTypeProperty() {
		return TREETYPE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.TREETYPE = property;
	}


	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum[] types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum[] types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration)  System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IStateEnum enumstate = types[i]; 
			
			VintageCraft.instance.proxy.registerItemBlockTexture(this, folderprefix, enumstate.getStateName(), enumstate.getMetaData(this));
		}
		
		return this;
	}

	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = getBlockClass().getMetaFromState(state);
		
		if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
            meta += 8;
        }
        return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getBlockClass().getBlockClassfromMeta(this, isDouble() ? meta : meta & 7).getBlockState();
		if (!this.isDouble()) {
            state = state.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }
		return state;
	}

   
    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return this.isDouble() ? new BlockState(this, new IProperty[] {VARIANT}): new BlockState(this, new IProperty[] {HALF, VARIANT});
    	} else {
    	//	System.out.println("created actual default state " + getTypeProperty());
    		return this.isDouble() ? new BlockState(this, new IProperty[] {getTypeProperty()}): new BlockState(this, new IProperty[] {HALF, getTypeProperty()});
    	}
    }
    
    
    @SideOnly(Side.CLIENT)
    protected static boolean isSlab(Block blockIn) {
        return blockIn instanceof IMultiblock && ((IMultiblock)blockIn).getBlockClass() == BlocksVC.singleslab;
    }
    
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	return BlocksVC.singleslab.getItemStackFor(state).getItem();
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {
    	return BlocksVC.singleslab.getItemStackFor(worldIn.getBlockState(pos)).getItem();
    }

}

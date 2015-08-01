package at.tyron.vintagecraft.Block;

import java.util.List;
import java.util.Locale;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.OreClassEntry;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class BlockSaltpeter extends BlockCoating {

	public BlockSaltpeter() {
		super(Material.circuits);
	}

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.saltpeter;
	}
	
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		
	}
	
    @Override
    public boolean isReplaceable(World worldIn, BlockPos pos) {
    	return true;
    }

    
    // Remove registration of multiple variants, as there is no items for these
    @Override
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		return this;
	}

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	String facings = ((BlockClassEntry)state.getValue(FACINGS)).getKey().getStateName();
    	int quantity = 1;
    	for (int i = 1; i < facings.length(); i++) {
    		if (((World)world).rand.nextBoolean()) quantity++;
    	}
    	
    	java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
     	ret.add(ItemOreVC.setOreType(new ItemStack(ItemsVC.ore, quantity), EnumOreType.SALTPETER));
     	
        return ret;
    }
    
    
	public float getBlockHardnessMultiplier(IBlockState state) {
		String facings = ((BlockClassEntry)state.getValue(FACINGS)).getKey().getStateName();
		
		return facings.length();
	}
    

    
}

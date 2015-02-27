package at.tyron.vintagecraft.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.item.ItemStone;

public class BlockCobblestone extends BlockRock {
	public BlockCobblestone() {
		super(Material.rock);
	}
	
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	if (state.getBlock() == BlocksVC.rock) {
	        List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
	
	        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
	
	        ItemStack itemstack = new ItemStack(ItemsVC.stone, 2 + rand.nextInt(3));
	        ItemStone.setRockType(itemstack, (EnumRockType) state.getValue(STONETYPE));
	        
	        ret.add(itemstack);
	        
	        
	        itemstack = new ItemStack(Items.clay_ball, 3 + rand.nextInt(2));
	        ret.add(itemstack);
	        
			return ret;
    	}
    	return super.getDrops(world, pos, state, fortune);
    }
    
}

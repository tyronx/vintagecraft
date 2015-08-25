package at.tyron.vintagecraft.Item;

import java.util.HashMap;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrass;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTallGrass;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemPeatBrick extends ItemVC implements IItemFuel {

	public ItemPeatBrick() {
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 900;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 1f;
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		EnumStrongHeatSource.addItemStackInformation(itemstack, tooltip);
	}
	
	
	@Override
	public boolean isMetalWorkingFuel(ItemStack stack) {
		return false;
	}	
	
	
	public boolean validBlockForTallGrass(World world, BlockPos pos) {
		IBlockState iblockstate = world.getBlockState(pos);
		IBlockState aboveblockstate = world.getBlockState(pos.up());
		
		return 
			iblockstate.getBlock() instanceof IBlockSoil &&
			((IBlockSoil)iblockstate.getBlock()).canGrowTallGrass(world, pos) &&
			(aboveblockstate.getBlock() == Blocks.air || aboveblockstate.getBlock() instanceof BlockTallGrass)
		;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos centerpos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (super.onItemUse(itemstack, entityplayer, world, centerpos, side, hitX, hitY, hitZ)) {
			return true;
		}
		
		
        IBlockState iblockstate = world.getBlockState(centerpos);
        Block block = iblockstate.getBlock();
        
        if (block instanceof BlockTallGrass) {
        	centerpos = centerpos.down();
            iblockstate = world.getBlockState(centerpos);
            block = iblockstate.getBlock();
        }
        
    	int quantity = 25 + world.rand.nextInt(10);
    	
    	HashMap<BlockPos, Double> positions = new HashMap<BlockPos, Double>();
    	
		BlockPos pos;
		
		for (int dx = -4; dx <= 4; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -4; dz <= 4; dz++) {
					pos = centerpos.add(dx, dy, dz);
					if (validBlockForTallGrass(world, pos)) {
						positions.put(pos, centerpos.distanceSq(pos.getX(), pos.getY(), pos.getZ()));
					}
				}
			}
    	}
    		
		ImmutableList<BlockPos> nearestblocks = Ordering.natural().onResultOf(Functions.forMap(positions)).immutableSortedCopy(positions.keySet());
		int numTallGrassSizes = 5;
		int numGrown = 0;
		IBlockState abovestate;
		
		for (int i = 0; i < quantity; i++) {
			if (nearestblocks.size() <= i) break;
			pos = nearestblocks.get(i);
			int tallgrassindex = 0;
			
			abovestate = world.getBlockState(pos.up());
			if (abovestate.getBlock() instanceof BlockTallGrass) {
				tallgrassindex = 1 + ((EnumTallGrass)abovestate.getValue(BlockTallGrass.GRASSTYPE)).getId();
				if (tallgrassindex >= 5) continue;
			} else {
				int distance = (int)MathHelper.sqrt_float((float) centerpos.distanceSq(pos.getX(), pos.getY(), pos.getZ()));
				tallgrassindex = Math.min(5, Math.max(0, 3 - distance));
			}
			
			if (!world.isRemote) {
				world.setBlockState(
					pos.up(), 
					BlocksVC.tallgrass.getDefaultState().withProperty(BlockTallGrass.GRASSTYPE, EnumTallGrass.fromMeta(tallgrassindex))
				);
			}
			numGrown++;
			
		}
		
		if (numGrown > 0) itemstack.stackSize--;

        return numGrown > 0;
	}

	@Override
	public int smokeLevel(ItemStack stack) {
		return 250;
	}

	@Override
	public ItemStack getCokedOutput(ItemStack stack) {
		return ItemOreVC.getItemStackFor(EnumOreType.COKE, stack.stackSize / 7);
	}
}

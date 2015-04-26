package at.tyron.vintagecraft.Block.Organic;

import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.TileEntity.TESapling;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCropsVC extends BlockCrops {
	
	public BlockCropsVC() {
		setCreativeTab(VintageCraft.floraTab);
	}
	
	protected boolean canPlaceBlockOn(Block ground) {
        return true;
    }
	
    protected Item getSeed() {
        return ItemsVC.wheatSeeds;
    }
    
    
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.suiteableGround(worldIn, pos, worldIn.getBlockState(pos.down()))) {
           if (!worldIn.isRemote) this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
        }
    }
    
    
    public boolean suiteableGround(World worldIn, BlockPos pos, IBlockState ground) {
        return worldIn.getLight(pos.up()) >= 10 && (ground.getBlock() instanceof BlockFarmlandVC || ground.getBlock() instanceof IBlockSoil);
    }

    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        
        this.checkAndDropBlock(worldIn, pos, state);

        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int i = ((Integer)state.getValue(AGE)).intValue();

            if (i < 7) {
                float f = growthChance(this, worldIn, pos);

                if (rand.nextFloat() < f) {
                    worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
                    reduceFertility(worldIn, pos.down());
                }
            }
        }
    }

    
    // 0.1f == average growth speed
	private float growthChance(BlockCropsVC blockCropsVC, World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		
		if (state.getBlock() instanceof BlockFarmlandVC) {
			EnumFertility fertility = ((BlockFarmlandVC)state.getBlock()).getFertility(worldIn, pos.down());
			if (fertility != null) {
				return 0.1f * fertility.growthspeedmultiplier;
				
			}
		}

		return 0.05f;
	}

	
	void reduceFertility(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
    	
		if (state.getBlock() instanceof BlockFarmlandVC) {
			TileEntity te = world.getTileEntity(pos);
	    	
	        if(te instanceof TEFarmland) {
	        	TEFarmland teFarmland = (TEFarmland) te;
	        	teFarmland.consumeFertility();
	        }
		}
	}
	
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && playerIn.getHeldItem() == null) {
			TileEntity te = worldIn.getTileEntity(pos.down());
			
	        if(te instanceof TEFarmland) {
	        	TEFarmland cte = (TEFarmland) te;
	            
	        	playerIn.addChatMessage(new ChatComponentText("Farmland fertility: " + cte.getFertility()));
	        }
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
	
}

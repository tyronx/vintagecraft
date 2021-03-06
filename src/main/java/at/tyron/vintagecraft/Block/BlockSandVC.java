package at.tyron.vintagecraft.Block;

import java.util.Random;

import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.Interfaces.Block.IBlockSoil;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSandVC extends BlockRock implements IBlockSoil {
	public static boolean fallInstantly;
	
	
	public BlockSandVC() {
		super(Material.sand);
	}
	
	 public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote) {
        	tryToFall(worldIn, pos);
        }
    }

	
	
	
	private void tryToFall(World world, BlockPos pos) {
		IBlockState blockstate = world.getBlockState(pos);
		
		if(!world.isRemote) {
			
			if (canFallBelow(world, pos.down()) && pos.getY() >= 0) {
				if (!fallInstantly && world.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
					EntityFallingBlock ent = new EntityFallingBlock(world, (double)(pos.getX() + 0.5F), (double)(pos.getY() + 0.5F), (double)(pos.getZ()+ 0.5F), blockstate);
					world.spawnEntityInWorld(ent);
					world.playSoundAtEntity(ent, Block.soundTypeSand.soundName, 1.0F, 0.8F);
					
				} else {
					world.setBlockToAir(pos);
					while (canFallBelow(world, pos = pos.down()) && pos.getY() > 0) { }
					if (pos.getY() > 0) {
						world.destroyBlock(pos, true);
						world.setBlockState(pos, blockstate, 2);
					}
				}
			}
		}
	}

	
	public static boolean canFallBelow(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		Material material = block.getMaterial();
		
		return 
			   block == Blocks.air 
			|| block == Blocks.fire 
			|| material == Material.air 
			|| material == Material.water 
			|| material == Material.lava
			|| block.getCollisionBoundingBox(world, pos, world.getBlockState(pos)) == null
		;
	}

	
	
	
	
	@Override
	public boolean canSpreadGrass(World world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canGrowGrass(World world, BlockPos pos) {
		return false;
	}

	@Override
	public boolean canGrowTallGrass(World world, BlockPos pos) {
		return true;
	}

	
	@Override
	public EnumFertility getFertility(World world, BlockPos pos) {
		return null;
	}

	@Override
	public EnumOrganicLayer getOrganicLayer(World world, BlockPos pos) {
		return null;
	}
	
	@Override
	public void setOrganicLayer(EnumOrganicLayer layer, World world, BlockPos pos) {
				
	}

	@Override
	public boolean canGrowTree(World world, BlockPos pos, EnumTree tree) {
		return true;
	}
	
	

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.sand;
	}


	
}

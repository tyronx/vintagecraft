package at.tyron.vintagecraft.Block.Utility;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.IBlockIgniteable;
import at.tyron.vintagecraft.Item.ItemStonePot;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStonePotUtilization;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockStonePot extends BlockContainerVC implements IBlockItemSink, IBlockIgniteable {
	public BlockStonePot() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (EnumRockType rocktype : EnumRockType.values()) {
			list.add(ItemStonePot.setRockType(new ItemStack(itemIn), rocktype));
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!entityplayer.isSneaking()) {
			TileEntity te = world.getTileEntity(pos);
			if (te instanceof TEStonePot) {
				return ((TEStonePot)te).tryGrabItemStack(entityplayer);
			}
			
			world.markBlockForUpdate(pos);
		}
		
		return true;
	}
	



	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEStonePot();
	}

	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (!player.isSneaking()) return false;
		
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TEStonePot) {
			return ((TEStonePot)te).tryPutItemStack(itemstack);
		}
		
		return false;
	}

	

	
    @Override
    public int getMetaFromState(IBlockState state) {
    	return 0;
    }
      
    public IBlockState getStateFromMeta(int meta) {
    	return getDefaultState();
    }
    
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TEStonePot) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (TEStonePot)tileentity);
            spawnAsEntity(worldIn, pos, ItemStonePot.setRockType(new ItemStack(BlocksVC.stonepot), ((TEStonePot)tileentity).rocktype));
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	return ret;
    }

	
	



    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		TileEntity te = worldIn.getTileEntity(pos);
		if (te instanceof TEStonePot) {
			TEStonePot testonepot = ((TEStonePot)te); 

			if (testonepot.utilization == EnumStonePotUtilization.FORGE && testonepot.burning) {
			    double x = (double)pos.getX() + 0.3D + rand.nextDouble()*0.3;
	            double y = (double)pos.getY() + rand.nextDouble()*0.2 + (testonepot.burnTime / testonepot.burnTimePerCoal) / 16f;
	            double z = (double)pos.getZ() + 0.3D + rand.nextDouble()*0.3;
	
	            if (rand.nextBoolean()) {
	            	worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);	
	            }
	            if (rand.nextBoolean()) {
	            	worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
	            }
			}
        }
    }

	
	


	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.0625f, pos.getY(), pos.getZ() + 0.0625f, pos.getX() + 0.9375f, pos.getY() + 0.875f, pos.getZ() + 0.9375f);
	}

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}

	
	
	public boolean suitableGround(World world, BlockPos groundpos) {
		return world.isSideSolid(groundpos, EnumFacing.UP);
	}


	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(world, pos) && suitableGround(world, pos.down());
	}
	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!suitableGround(world, pos.down())) {
			world.destroyBlock(pos, true);
		}
	}

	
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

    public boolean isFullCube() {
    	return false;
    }



	@Override
	public boolean ignite(World world, BlockPos pos, ItemStack firestarter) {
		TEStonePot testonepot = (TEStonePot)world.getTileEntity(pos);

		if (testonepot != null) {
			return testonepot.tryIgnite();
		}
		return false;

	}

	
	
	


	@Override
	public String getSubType(ItemStack stack) {
		return ItemStonePot.getRockType(stack).getName();
	}
	
	
    
}

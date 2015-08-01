package at.tyron.vintagecraft.Block.Mechanics;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase.TempCategory;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Item.ItemStonePot;
import at.tyron.vintagecraft.Item.Mechanics.ItemMechanicalWooden;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEMechanicalNetworkDeviceBase;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public abstract class BlockMechanicalVC extends BlockContainerVC {

	protected BlockMechanicalVC(Material materialIn) {
		super(materialIn);
		setHardness(1.2f);
		setStepSound(soundTypeWood);
		setHarvestLevel("axe", 0);
	}
	
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (EnumTree treetype : EnumTree.values()) {
			if (treetype.jankahardness >= 1000) {
				list.add(((ItemMechanicalWooden)itemIn).withTreeType(treetype));
			}
		}
	}


	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		ItemStack stack = new ItemStack(Item.getItemFromBlock(state.getBlock()));

		TileEntity te = worldIn.getTileEntity(pos);
		
		if (stack.getItem() instanceof ItemMechanicalWooden && te instanceof TEMechanicalNetworkDeviceBase) {
			((ItemMechanicalWooden)stack.getItem()).withTreeType(((TEMechanicalNetworkDeviceBase)te).getTreeType());
		}
		
		if (stack.getItem() instanceof ItemMechanicalWooden && te instanceof TEMechanicalNetworkDeviceBase) {
			stack = ((ItemMechanicalWooden)stack.getItem()).withTreeType(((TEMechanicalNetworkDeviceBase)te).getTreeType());
		}		
		spawnAsEntity(worldIn, pos, stack);

		super.breakBlock(worldIn, pos, state);
		
		if (te instanceof IMechanicalPowerDevice) {
			((IMechanicalPowerDevice)te).onDeviceRemoved(worldIn, pos);
		}

	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new java.util.ArrayList<ItemStack>();
	}
	
	
	
	public abstract boolean isBlockedAllowedAt(World worldIn, BlockPos pos);

	public boolean suitableGround(World worldIn, BlockPos pos) {
		IBlockState ground = worldIn.getBlockState(pos.down());
		return ground.getBlock().isSideSolid(worldIn, pos, EnumFacing.UP);
	}

	public EnumFacing suitableSide(World worldIn, BlockPos pos) {
		for (int i = 0; i <= 3; i++) {
			EnumFacing facing = EnumFacing.getHorizontal(i);
			IBlockState side = worldIn.getBlockState(pos.offset(facing));
			if (side.getBlock().isSideSolid(worldIn, pos, facing.getOpposite())) {
				return facing;
			}
		}
		return null;
	}
	
	public boolean hasConnectibleDeviceAt(World worldIn, BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			if (TEMechanicalNetworkDeviceBase.getNeighbourDevice(worldIn, pos, facing, false) != null) {
				return true;
			}
		}
		return false;
	}

	
    public boolean isOpaqueCube() {
        return false;
    } 
    public boolean isFullCube() {
    	return false;
    }
    public boolean isVisuallyOpaque() {
        return false;
    }
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
    @Override
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
    	return true;
    }

}

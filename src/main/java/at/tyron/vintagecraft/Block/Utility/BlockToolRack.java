package at.tyron.vintagecraft.Block.Utility;


import java.util.ArrayList;
import java.util.List;

import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IItemRackable;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockToolRack extends BlockContainerVC {
	public BlockToolRack() {
		super(Material.wood);
		this.setDefaultState(this.blockState.getBaseState());
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for (EnumTree tree : EnumTree.values()) {
			list.add(getItemStackFor(tree));
		}
	}

	
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEToolRack();
	}
	

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}
	

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
	
	
	public EnumFacing getFacing(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TEToolRack) {
			return ((TEToolRack) te).facing == null ? EnumFacing.NORTH : ((TEToolRack) te).facing;
		}
		return EnumFacing.NORTH;
	}
	
	
	// Called when user places a ItemToolRack
	public void initTileEntity(World world, BlockPos pos, EnumFacing placedontoside, EnumTree tree) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TEToolRack) {
			((TEToolRack) te).facing = placedontoside;
			((TEToolRack) te).woodtype = tree;
		}
	}
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TEToolRack) {
			TEToolRack tet = (TEToolRack) te;
			EnumFacing facing = tet.facing;
			
			int slot = 0 + (hitY < 0.5 ? 2 : 0);
			
			switch (facing) {
				case NORTH: if (hitX > 0.5) slot++; break;
				case SOUTH: if (hitX < 0.5) slot++; break;
				case WEST: if (hitZ > 0.5) slot++; break;
				case EAST: if (hitZ < 0.5) slot++; break;
				default: break;
			}
			
			if (handleSlot(world, pos, entityplayer, tet, slot)) { 
				world.markBlockForUpdate(pos);
				return true;
			}
		}
		return true;
	}

	private boolean handleSlot(World world, BlockPos pos, EntityPlayer entityplayer, TEToolRack te, int slot) {
		ItemStack itemstack = entityplayer.getCurrentEquippedItem();
		
		if(te.storage[slot] == null && isRackable(itemstack)) {
			te.storage[slot] = entityplayer.getCurrentEquippedItem().copy();
			entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = null;
			return true;
		}
		else if(te.storage[slot] != null && entityplayer.getCurrentEquippedItem() == null) {
			te.grabItem(slot, entityplayer);
			te.storage[slot] = null;
			return true;
		}
		
		return false;
	}
	
	public static boolean isRackable(ItemStack stack) {
		return
			stack != null &&
			(stack.getItem() instanceof IItemRackable || stack.getItem() instanceof ItemBow)
		;
	}

	
	
	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if(!world.isRemote) {
			TileEntity te = world.getTileEntity(pos);
			
			if((te != null) && (te instanceof TEToolRack)) {
				TEToolRack rack = (TEToolRack) te;
				
				rack.ejectContents();
				spawnAsEntity(world, pos, ItemToolRack.withTreeType(rack.woodtype));
			}
		}
		return world.setBlockToAir(pos);
	}

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		// don't drop here, we dropped in removedByPlayer instead
	}
	

	public EnumTree getTreeType(IBlockAccess worldIn, BlockPos pos) {
		EnumTree treetype = EnumTree.ASH;
		
		TileEntity te = worldIn.getTileEntity(pos);
		if((te != null) && (te instanceof TEToolRack)) {
			if (((TEToolRack)te).woodtype != null) {
				treetype = ((TEToolRack)te).woodtype;
			}
		}
		return treetype;
	}
	
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(getItemStackFor(getTreeType(world, pos)));		
		return ret;
	}


	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		EnumFacing facing = getFacing((World)worldIn, pos);
		
		switch (facing) {
			case NORTH: this.setBlockBounds(0.0F, 0F, 0.85F, 1F, 1F, 1F); break;
			case EAST: this.setBlockBounds(0.0F, 0F, 0.0F, 0.15F, 1F, 1F); break;
			case SOUTH: this.setBlockBounds(0.0F, 0F, 0.00F, 1F, 1F, 0.15F); break;
			case WEST: this.setBlockBounds(0.85F, 0F, 0.0F, 1F, 1F, 1F); break;
		default:
			break;
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		EnumFacing facing = getFacing(worldIn, pos);
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		switch (facing) {
			case NORTH: return AxisAlignedBB.fromBounds(x + 0.0F, y + 0F, z + 0.85F, x + 1F, y + 1F, z + 1F);
			case EAST: return AxisAlignedBB.fromBounds(x + 0.0F, y + 0F, z + 0.0F, x + 0.15F, y + 1F, z + 1F);
			case SOUTH: return AxisAlignedBB.fromBounds(x + 0.0F, y + 0F, z + 0.00F, x + 1F, y + 1F, z + 0.15F);
			case WEST: return AxisAlignedBB.fromBounds(x + 0.85F, y + 0F, z + 0.0F, x + 1F, y + 1F, z + 1F);
			default: return AxisAlignedBB.fromBounds(x, y, z, x + 1, y + 1, z + 1);
		
		}		
	}
	
	
	public boolean suitableWall(World world, BlockPos wallpos, EnumFacing side) {
		return world.isSideSolid(wallpos, side);
	}

	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing facing = getFacing(world, pos);

		if (!suitableWall(world, pos.offset(facing.getOpposite()), facing)) {
			removedByPlayer(world, pos, null, true);
		}
	}

	

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(world, pos) && suitableWall(world, pos.offset(side.getOpposite()), side);
	}

	

	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	
	

	
	public ItemStack getItemStackFor(IStateEnum key) {
		return ItemToolRack.withTreeType((EnumTree)key);
	}

	@Override
	public String getSubType(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}

    public boolean isFullCube() {
    	return false;
    }

}
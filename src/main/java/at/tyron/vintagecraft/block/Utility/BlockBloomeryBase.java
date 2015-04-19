package at.tyron.vintagecraft.Block.Utility;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Item.ItemCeramicVessel;
import at.tyron.vintagecraft.TileEntity.TEBloomery;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumBloomeryState;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBloomeryBase extends BlockContainerVC implements IBlockItemSink {
	public static PropertyEnum fillheight = PropertyEnum.create("fillheight", EnumBloomeryState.class);
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public BlockBloomeryBase() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
		setDefaultState(blockState.getBaseState().withProperty(fillheight, EnumBloomeryState.UNLIT_EMPTY).withProperty(FACING, EnumFacing.NORTH));
	}
	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TEBloomery tebloomery = (TEBloomery)worldIn.getTileEntity(pos);
		if(tebloomery == null) return super.getActualState(state, worldIn, pos);
		
		return super.getActualState(state, worldIn, pos).withProperty(fillheight, EnumBloomeryState.stateFor(tebloomery.getMode(), tebloomery.getFillHeight()));
	}
	
	@Override
	public int getRenderType() {
		return 3;
	}
	


	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	public int getMetaFromState(IBlockState state) {
		return ((EnumFacing)state.getValue(FACING)).getIndex();
	}

	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] {FACING, fillheight});
	}	
	

	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
	public boolean isFullBlock() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	
	public boolean hasChimney(World world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() instanceof BlockBloomeryChimney;
	}
	
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
		TEBloomery tebloomery = (TEBloomery)world.getTileEntity(pos);

		
		if (tebloomery != null && entityplayer.getCurrentEquippedItem() == null) {
			if (side == state.getValue(FACING) && hitY < 0.4f) {
				return tebloomery.tryIgnite();
			}
		}
		
		return super.onBlockActivated(world, pos, state, entityplayer, side, hitX, hitY, hitZ);
	}
	
	
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TEBloomery) {
			TEBloomery teblomoery = (TEBloomery)tileentity;
			
			teblomoery.ejectContents();
			
			ItemStack firebricks = new ItemStack(ItemsVC.fireclay_brick, 6 + worldIn.rand.nextInt(3));
			
			worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, firebricks));
		}
		
		if (hasChimney(worldIn, pos)) {
			worldIn.destroyBlock(pos.up(), true);
		}

		super.breakBlock(worldIn, pos, state);
	}

	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new java.util.ArrayList<ItemStack>();
	}
	

	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEBloomery();
	}

	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }


	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (!player.isSneaking()) return false;
		
		TEBloomery tebloomery = (TEBloomery)world.getTileEntity(pos);
		if(tebloomery == null) return false;

		return tebloomery.tryPutItemStack(itemstack);
	}
}

package at.tyron.vintagecraft.Block.Utility;

import java.util.List;

import org.lwjgl.Sys;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Item.ItemAnvilVC;
import at.tyron.vintagecraft.Item.ItemCeramicVessel;
import at.tyron.vintagecraft.Item.ItemToolRack;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEBloomery;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.WorldProperties.EnumBloomeryState;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;

public class BlockAnvilVC extends BlockContainerVC implements IBlockItemSink {
	public static PropertyEnum METALTYPE = PropertyEnum.create("metal", EnumMetal.class, EnumMetal.anvilValues());
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public BlockAnvilVC() {
		super(Material.iron);
		setCreativeTab(VintageCraft.craftedBlocksTab);
		
		setDefaultState(blockState.getBaseState().withProperty(METALTYPE, EnumMetal.COPPER).withProperty(FACING, EnumFacing.NORTH));
	}
	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TEAnvil teanvil = (TEAnvil)worldIn.getTileEntity(pos);
		if(teanvil == null) return super.getActualState(state, worldIn, pos);
		
		return super.getActualState(state, worldIn, pos).withProperty(METALTYPE, teanvil.getMetal());
    	
	}
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
    	playerIn.openGui(VintageCraft.instance, 4, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
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
	
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tabs, List list) {
		for (EnumMetal metal : EnumMetal.anvilValues()) {
			list.add(getItemStackFor(metal));
		}
	}
	
	

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		EnumFacing face = (EnumFacing) state.getValue(FACING);
		
		if (face == EnumFacing.EAST || face == EnumFacing.WEST) {
			return AxisAlignedBB.fromBounds(pos.getX() + 0.1875f, pos.getY(), pos.getZ(), pos.getX() + 0.8125f, pos.getY() + 0.625f, pos.getZ() + 1f);
		} else {
			return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ() + 0.1875, pos.getX() + 1f, pos.getY() + 0.625f, pos.getZ() + 0.8125f);
		}
	}

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}

	
	public boolean suitableGround(World world, BlockPos groundpos) {
		return world.isSideSolid(groundpos, EnumFacing.UP);
	}

	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		EnumFacing facing = (EnumFacing) state.getValue(FACING);

		if (!suitableGround(world, pos.down())) {
			world.destroyBlock(pos, true);
		}
	}

	

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return this.canPlaceBlockAt(world, pos) && suitableGround(world, pos.down());
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
	public boolean isOpaqueCube() {
		return false;
	}

    
	
	
	// Called when user places a ItemToolRack
	public void initTileEntity(World world, BlockPos pos, EnumMetal metal) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TEAnvil) {
			((TEAnvil) te).setMetal(metal);
			
		}
	}
	
	
	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }

    

	public EnumMetal getMetal(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te != null && te instanceof TEAnvil) {
			return ((TEAnvil) te).getMetal() == null ? EnumMetal.IRON : ((TEAnvil) te).getMetal();
		}
		
		return EnumMetal.IRON;
	}

	
	
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TEAnvil) {
        	ItemStack itemstack = getItemStackFor(getMetal((World) worldIn, pos));
        	
        	((TEAnvil) tileentity).ejectContents();
        	
            worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, itemstack));
        }

        super.breakBlock(worldIn, pos, state);
    }
    
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new java.util.ArrayList<ItemStack>();
	}
	
	
	
	public ItemStack getItemStackFor(EnumMetal metal) {
		return ItemAnvilVC.getItemStack(metal);
	}


	@Override
	public String getSubType(ItemStack stack) {
		return ItemAnvilVC.getMetal(stack).getName();
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEAnvil();
	}

	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {

		
		return false;
	}

    @Override
    protected BlockState createBlockState() {
    	return new BlockState(this, new IProperty[]{FACING, METALTYPE});
    }

}

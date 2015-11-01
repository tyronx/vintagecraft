package at.tyron.vintagecraft.Block.Metalworking;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.IBlockIgniteable;
import at.tyron.vintagecraft.TileEntity.TEFurnaceSection;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnaceState;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFurnaceSection extends BlockContainerVC implements IBlockItemSink, IBlockIgniteable {
	public static PropertyEnum fillheight = PropertyEnum.create("fillheight", EnumFurnaceState.class);
	public static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	
	public BlockFurnaceSection() {
		super(Material.rock);
		setCreativeTab(VintageCraft.craftedBlocksTab);
		setDefaultState(blockState.getBaseState().withProperty(fillheight, EnumFurnaceState.UNLIT_EMPTY).withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Metalworking;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TEFurnaceSection tefurnacesection = (TEFurnaceSection)worldIn.getTileEntity(pos);
		if(tefurnacesection == null) return super.getActualState(state, worldIn, pos);
		
		return super.getActualState(state, worldIn, pos).withProperty(fillheight, EnumFurnaceState.stateFor(tefurnacesection.getState(), tefurnacesection.getFillHeight()));
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
		return world.getBlockState(pos.up()).getBlock() instanceof BlockFurnaceChimney;
	}	
	
	
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity tileentity = worldIn.getTileEntity(pos);

		if (tileentity instanceof TEFurnaceSection) {
			TEFurnaceSection tefurnacesection = (TEFurnaceSection)tileentity;
			
			if (tefurnacesection.getState() != 0) {
				tefurnacesection.clearFuel();
			}
			
			tefurnacesection.ejectContents();
			
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
		return new TEFurnaceSection();
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
		
		TEFurnaceSection tefurnacesection = (TEFurnaceSection)world.getTileEntity(pos);
		if(tefurnacesection == null) return false;

		if (tefurnacesection.getFurnaceType() == null) {
			player.addChatMessage(new ChatComponentText("Not a furnace!"));
			return false;
		}
		

		return tefurnacesection.tryPutItemStack(itemstack);
	}


	@Override
	public boolean ignite(World world, BlockPos pos, ItemStack firestarter) {
		TEFurnaceSection tefurnacesection = (TEFurnaceSection)world.getTileEntity(pos);

		if (tefurnacesection != null) {
			return tefurnacesection.tryIgnite();
		}
		return false;
	}
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TEFurnaceSection tefurnacesection = (TEFurnaceSection)worldIn.getTileEntity(pos);
		if(tefurnacesection == null) return false;
		
		return false;
	}
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side.getHorizontalIndex() != -1;
	}
}

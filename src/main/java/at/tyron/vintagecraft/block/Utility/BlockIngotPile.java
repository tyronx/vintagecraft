package at.tyron.vintagecraft.Block.Utility;

import java.util.Random;

import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockIngotPile extends BlockContainer implements IBlockItemSink {
	PropertyBool sideways = PropertyBool.create("sideways");
	
	private Random random = new Random();

	public BlockIngotPile() {
		super(Material.iron);
	}

	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TEIngotPile();
	}
	
	
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	public static boolean suitableGround(World world, BlockPos pos, EnumMetal metal) {
		boolean fullequalstackbelow = false;
		
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockIngotPile) {
			if (world.getTileEntity(pos.down()) instanceof TEIngotPile) {
				TEIngotPile belowpile = (TEIngotPile)world.getTileEntity(pos.down());
				
				fullequalstackbelow = belowpile != null && belowpile.getMetal() == metal && belowpile.getStackSize() >= ItemIngot.maxpilesize;
			}
		}
		
		return 
			!world.isAirBlock(pos.down())
			&& (fullequalstackbelow || world.isSideSolid(pos.down(), EnumFacing.UP))
		;
	}
	
	
	
	public static boolean tryCreatePile(ItemStack itemstack, World world, BlockPos pos) {
		if (world.isRemote || !suitableGround(world, pos, ItemIngot.getMetal(itemstack))) return false;
		
		world.setBlockState(pos, BlocksVC.ingotPile.getDefaultState());
		
		TEIngotPile teingotpile = (TEIngotPile)world.getTileEntity(pos);
		teingotpile.setItemStack(itemstack.splitStack(1));
		
		world.markBlockForUpdate(pos);
		
		return true;
	}

	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack equippedItem = entityplayer.getCurrentEquippedItem();

		TEIngotPile teingotpile = (TEIngotPile)world.getTileEntity(pos);
		if(teingotpile == null) return false;
		
		if (entityplayer.isSneaking()) {
			if (equippedItem != null) {
				teingotpile.tryTransferIngot(equippedItem);
			}
		} else {
			teingotpile.tryGrabIngot(entityplayer);
		}
		
		world.markBlockForUpdate(pos);
		
		return true;
	}
	
	
	
	public boolean containsFullStack(World world, BlockPos pos) {
		TEIngotPile teingotpile = (TEIngotPile)world.getTileEntity(pos);
		if(teingotpile == null) return false;

		return teingotpile.getStackSize() < 64;
	}
	
	
	

	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (!player.isSneaking()) return false;
		
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TEIngotPile) { 
			return ((TEIngotPile)tileentity).tryTransferIngot(itemstack);
		}
		return false;
	}

	

	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		TEIngotPile te = (TEIngotPile)world.getTileEntity(pos);

		if (te != null) {
			return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + ((te.getStackSize() + 7) / 8) * 0.125, pos.getZ() + 1);
		}

		return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.25, pos.getZ() + 1);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, null);
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TEIngotPile te = (TEIngotPile)world.getTileEntity(pos);

		if (te.getStackInSlot(0)!=null)
			this.setBlockBounds(0f, 0f, 0f, 1f, (float) (((te.getStackInSlot(0).stackSize + 7)/8)*0.125), 1f);
		else
			this.setBlockBounds(0f, 0f, 0f, 0f, 0.25f, 0f);
	}

	
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
		return true;
	}

	

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		return true;
	}


	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
	}


	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
		int l = MathHelper.floor_double(placer.rotationYaw * 4F / 360F + 0.5D) & 3;
		
		if (l == 0 || l == 2) {
			worldIn.setBlockState(pos, state.withProperty(sideways, true));
		}		
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TEIngotPile) {
            InventoryHelper.dropInventoryItems(world, pos, (TEIngotPile)tileentity);
        }

		super.breakBlock(world, pos, state);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}


	
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!suitableGround(world, pos, ((TEIngotPile)world.getTileEntity(pos)).getMetal())) {
			((TEIngotPile)world.getTileEntity(pos)).ejectContents();
			world.setBlockToAir(pos);
			return;
		}
	}

	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return null;
	}
	
	
	
    public boolean isVisuallyOpaque() {
        return false;
    }
    
    public boolean isFullCube() {
    	return false;
    }
}
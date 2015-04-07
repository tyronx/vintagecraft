package at.tyron.vintagecraft.Block;

import java.util.Random;

import at.tyron.vintagecraft.Item.ItemIngot;
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

public class BlockIngotPile extends BlockContainer
{
	PropertyBool sideways = PropertyBool.create("sideways");
	
	private Random random = new Random();

	public BlockIngotPile()
	{
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
		
//		System.out.println(fullequalstackbelow+" || "+world.isSideSolid(pos.down(), EnumFacing.UP));
		
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

        /*
		TEIngotPile te = (TEIngotPile)world.getTileEntity(pos);
		if (te != null)
		{
			for (int var6 = 0; var6 < te.getSizeInventory(); ++var6)
			{
				ItemStack var7 = te.getStackInSlot(var6);

				if (var7 != null)
				{
					float var8 = this.random.nextFloat() * 0.8F + 0.1F;
					float var9 = this.random.nextFloat() * 0.8F + 0.1F;
					EntityItem var12;

					for (float var10 = this.random.nextFloat() * 0.8F + 0.1F; var7.stackSize > 0; world.spawnEntityInWorld(var12))
					{
						int var11 = this.random.nextInt(21) + 10;

						if (var11 > var7.stackSize)
							var11 = var7.stackSize;

						var7.stackSize -= var11;
						var12 = new EntityItem(world, pos.getX() + var8, pos.getY() + var9, pos.getZ() + var10, new ItemStack(var7.getItem(), var11, var7.getItemDamage()));
						float var13 = 0.05F;
						var12.motionX = (float)this.random.nextGaussian() * var13;
						var12.motionY = (float)this.random.nextGaussian() * var13 + 0.2F;
						var12.motionZ = (float)this.random.nextGaussian() * var13;

						if (var7.hasTagCompound()) {
							var12.getEntityItem().setTagCompound((NBTTagCompound)var7.getTagCompound().copy());
						}
					}
				}
			}*/
			super.breakBlock(world, pos, state);
		//}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	
	

	public static int getAnvilTypeFromMeta(int j)
	{
		int l = 7;
		int k = j & l;
		return k;
	}

	public static int getDirectionFromMetadata(int i)
	{
		int d = i >> 3;

		if (d == 1)
			return 1;
		else
			return 0;
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
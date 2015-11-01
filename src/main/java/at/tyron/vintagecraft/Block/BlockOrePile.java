package at.tyron.vintagecraft.Block;

import at.tyron.vintagecraft.AchievementsVC;
import at.tyron.vintagecraft.Interfaces.IBlockIgniteable;
import at.tyron.vintagecraft.Item.Metalworking.ItemIngot;
import at.tyron.vintagecraft.Item.Terrafirma.ItemOreVC;
import at.tyron.vintagecraft.TileEntity.TEOrePile;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOrePile extends BlockIngotPile implements IBlockIgniteable {
	public static PropertyInteger HEIGHT = PropertyInteger.create("height", 0, 15);
	public static PropertyEnum ORETYPE = PropertyEnum.create("oretype", EnumOreType.class);
	public static PropertyBool BURNING = PropertyBool.create("burning");
	
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TEOrePile te = (TEOrePile)worldIn.getTileEntity(pos);
		
		if (te != null) {
			return getDefaultState()
				.withProperty(HEIGHT, te.blockHeight())
				.withProperty(ORETYPE, te.getOreType())
				.withProperty(BURNING, te.isBurning());
		}
		
		return super.getActualState(state, worldIn, pos);
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEOrePile();
	}


	public static boolean suitableGround(World world, BlockPos pos, EnumOreType ore) {
		boolean fullequalstackbelow = false;
		
		if (world.getBlockState(pos.down()).getBlock() instanceof BlockOrePile) {
			if (world.getTileEntity(pos.down()) instanceof TEOrePile) {
				TEOrePile belowpile = (TEOrePile)world.getTileEntity(pos.down());
				
				fullequalstackbelow = belowpile != null && belowpile.getOreType() == ore && belowpile.getStackSize() >= ItemOreVC.maxpilesize;
			}
		}
		
		return 
			!world.isAirBlock(pos.down())
			&& (fullequalstackbelow || world.isSideSolid(pos.down(), EnumFacing.UP))
		;
	}
	
	public static boolean tryCreatePile(ItemStack itemstack, World world, BlockPos pos) {
		if (world.isRemote || !suitableGround(world, pos, ItemIngot.getMetal(itemstack)) || !world.isAirBlock(pos)) return false;
		
		world.setBlockState(pos, BlocksVC.orepile.getDefaultState().withProperty(ORETYPE, ItemOreVC.getOreType(itemstack)));
		
		TEOrePile teorepile = (TEOrePile)world.getTileEntity(pos);
		teorepile.setItemStack(itemstack.splitStack(itemstack.stackSize >= 4 ? 4 : 1));
		
		world.markBlockForUpdate(pos);
		
		return true;
	}
	
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityplayer, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack equippedItem = entityplayer.getCurrentEquippedItem();

		TEOrePile teorepile = (TEOrePile)world.getTileEntity(pos);
		if(teorepile == null) return false;
		
		if (entityplayer.isSneaking()) {
			if (equippedItem != null) {
				teorepile.tryTransferOre(equippedItem);
			}
		} else {
			ItemStack content = teorepile.getStackInSlot(0);
			
			boolean iscoke = content != null && content.getItem() instanceof ItemOreVC && ItemOreVC.getOreType(content) == EnumOreType.COKE; 
			boolean grabbed = teorepile.tryGrabOre(entityplayer);
			
			if (grabbed && iscoke) {
				entityplayer.triggerAchievement(AchievementsVC.acquireCoke);
			}
		}
		
		world.markBlockForUpdate(pos);
		
		return true;
	}

	public boolean containsFullStack(World world, BlockPos pos) {
		TEOrePile teorepile = (TEOrePile)world.getTileEntity(pos);
		if(teorepile == null) return false;

		return teorepile.getStackSize() < 64;
	}
	
	
	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (!player.isSneaking()) return false;
		
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TEOrePile) { 
			return ((TEOrePile)tileentity).tryTransferOre(itemstack);
		}
		return false;
	}

	

	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		TEOrePile te = (TEOrePile)world.getTileEntity(pos);

		if (te != null) {
			return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + ((te.getStackSize() + 7) / 8) * 0.125, pos.getZ() + 1);
		}

		return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 0.25, pos.getZ() + 1);
	}
	
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		TEOrePile te = (TEOrePile)world.getTileEntity(pos);

		if (te == null) return;
		
		if (te.getStackInSlot(0)!=null)
			this.setBlockBounds(0f, 0f, 0f, 1f, (float) (((te.getStackInSlot(0).stackSize + 7)/8)*0.125), 1f);
		else
			this.setBlockBounds(0f, 0f, 0f, 0f, 0.25f, 0f);
	}
	
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TEOrePile) {
        	if (!((TEOrePile)tileentity).isBurning()) {
        		InventoryHelper.dropInventoryItems(world, pos, (TEOrePile)tileentity);
        	}
        }

		super.breakBlock(world, pos, state);
	}

	

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!suitableGround(world, pos, ((TEOrePile)world.getTileEntity(pos)).getOreType())) {
			((TEOrePile)world.getTileEntity(pos)).ejectContents();
			world.setBlockToAir(pos);
			return;
		}
	}
	
	
	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[]{BURNING, HEIGHT, ORETYPE});
	}


	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}
	
	
	@Override
	public int getRenderType() {
		return 3;
	}


	@Override
	public boolean ignite(World world, BlockPos pos, ItemStack firestarter) {
		TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TEOrePile) {

        	return ((TEOrePile)tileentity).tryIgnite();
        }
        
        
        return false;
	}

	
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    	TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TEOrePile) {
        	if (((TEOrePile)tileentity).isBurning()) {
        		entityIn.attackEntityFrom(DamageSource.inFire, 1.0F);
        	}
        }
    }

    
    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos) {
    	TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TEOrePile) {
        	if (((TEOrePile)tileentity).isBurning()) {
                float f1 = 1F;

                int i = super.getMixedBrightnessForBlock(worldIn, pos);
                int j = i & 255;
                int k = i >> 16 & 255;
                j += (int)(f1 * 15.0F * 16.0F);

                if (j > 240)
                {
                    j = 240;
                }

                return j | k << 16;    
        		
        	}
        }

        return super.getMixedBrightnessForBlock(worldIn, pos);
	}

    
}


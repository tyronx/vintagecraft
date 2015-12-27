package at.tyron.vintagecraft.Block.Utility;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.Block.IMultiblock;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStoneAnvil extends BlockContainerVC implements IMultiblock {
	protected BlockClassEntry[] subtypes;
	
	public PropertyBlockClass ROCKTYPE;
	public static PropertyInteger STAGE = PropertyInteger.create("stage", 0, 2);
	
	private boolean keepInventory;
	
	public BlockStoneAnvil() {
		super(Material.rock);
	}
	
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return null;
	}
	
	public void setStage(World worldIn, BlockPos pos, IBlockState state, int stage) {
        keepInventory = true;
        worldIn.setBlockState(pos, state.withProperty(STAGE, stage));
        keepInventory = false;
        
        if (stage == 0) {
        	EnumRockType rocktype = (EnumRockType)BlocksVC.stoneanvil.getEntryFromMeta(state.getBlock(), getMetaFromState(state) & 3) .getKey();
        	
        	TileEntity te = worldIn.getTileEntity(pos);
			if (te instanceof TEAnvil) {
				
				int multiplier = 2;
				if (rocktype.getRockGroup() == EnumRockGroup.METAMORPHIC) multiplier = 3;
				if (rocktype.getRockGroup() == EnumRockGroup.IGNEOUS_EXTRUSIVE || rocktype.getRockGroup() == EnumRockGroup.IGNEOUS_INTRUSIVE) { 
					multiplier = 5;
				}
				
				((TEAnvil)te).setUses(3 * multiplier  + worldIn.rand.nextInt(5));
			}
        }
	}
	
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TEAnvil) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TEAnvil)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

	
	
	
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
	
		EnumRockType rocktype = (EnumRockType) getRockType(state).getKey();
		ItemStack itemstack = new ItemStack(ItemsVC.stone, 1 + rand.nextInt(2));
		ItemStone.setRockType(itemstack, rocktype);
	        
		ret.add(itemstack);
	   	return ret;
    }

   
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}

	
	public BlockClassEntry getRockType(IBlockState state) {
		return (BlockClassEntry)state.getValue(getTypeProperty());
	}
    
   
    @Override
    protected BlockState createBlockState() {
    	if (getTypeProperty() != null) {
    		return new BlockState(this, new IProperty[] {STAGE, getTypeProperty()});
    	}
    	return new BlockState(this, new IProperty[]{STAGE});
    }
	
	
    @Override
    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state) + ((Integer)state.getValue(STAGE) << 2);
    }
     
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getEntryFromMeta(this, meta & 3).getBlockState().withProperty(STAGE, meta >> 2);
    }


	@Override
	public String getSubType(ItemStack stack) {
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEAnvil();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if ((Integer)state.getValue(STAGE) == 0) {
			playerIn.openGui(VintageCraft.instance, 4, world, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	public int multistateAvailableTypes() {
		return 4;
	}

	@Override
	public IProperty getTypeProperty() {
		return ROCKTYPE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		ROCKTYPE = property;
	}

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.stoneanvil;
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

	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 0.8125f, pos.getZ() + 1f);
	}

	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
	}

	

}

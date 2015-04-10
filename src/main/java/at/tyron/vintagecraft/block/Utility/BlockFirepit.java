package at.tyron.vintagecraft.Block.Utility;

import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Interfaces.IStrongHeatSource;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFirepit extends BlockContainerVC implements IStrongHeatSource, IBlockItemSink {
	public static PropertyEnum buildstage = PropertyEnum.create("buildstage", EnumBuildStage.class);
	
	private boolean keepInventory;
	boolean burning;

	
	
	public BlockFirepit(boolean burning) {
		super(Material.wood);
		this.burning = burning;
		
		if (!burning) { 
			setCreativeTab(CreativeTabs.tabDecorations);
			setDefaultState(getDefaultState().withProperty(buildstage, EnumBuildStage.STONES));
		}
	}
	
	
	@Override
	protected BlockState createBlockState() {
		if (burning) return new BlockState(this, new IProperty[0]);
		return new BlockState(this, new IProperty[]{buildstage});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		if (burning) return 0;
		return ((EnumBuildStage)state.getValue(buildstage)).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (burning) return getDefaultState();
		return getDefaultState().withProperty(buildstage, EnumBuildStage.byOrdinal(meta));
	}
	

    public int getRenderType() {
        return 3;
    }

    
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
	
	
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
        	return true;
        }
        
        if (!isBurning() && state.getValue(buildstage) != EnumBuildStage.LOG3) {
        	return true;
        } 

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TEHeatSourceWithGUI) {
        	playerIn.openGui(VintageCraft.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }
    
    
    
    public Block getLitVersion() {
    	return BlocksVC.firepit_lit;
    }
    public Block getExtinguishedVersion() {
    	return BlocksVC.firepit;
    }
    
    public boolean isBurning() {
    	return burning;
    }

    
    
    public void setState(boolean burning, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        
        Block block = getExtinguishedVersion();
        if (burning) block = getLitVersion();
        
        keepInventory = true;
        IBlockState state = block.getDefaultState();
        if (!burning) {
        	state = state.withProperty(buildstage, EnumBuildStage.LOG3);
        }
        worldIn.setBlockState(pos, state, 3);
        keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }
    
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {

	        TileEntity tileentity = worldIn.getTileEntity(pos);
	
	        if (tileentity instanceof TEHeatSourceWithGUI) {
	            InventoryHelper.dropInventoryItems(worldIn, pos, (TEHeatSourceWithGUI)tileentity);
	            worldIn.updateComparatorOutputLevel(pos, this);
	        }
        }

        super.breakBlock(worldIn, pos, state);
    }

    
    
	
	
	public BlockFirepit.EnumBuildStage getNextFirepitStage(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() instanceof BlockFirepit) {
			BlockFirepit.EnumBuildStage stage = (EnumBuildStage) state.getValue(BlockFirepit.buildstage);
			return stage.getNextStage();
		} else {
			return null;
		}
	}
	
	
    
	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		BlockFirepit.EnumBuildStage stage = getNextFirepitStage(itemstack, player, world, pos, side);
		if (stage != null) {
			world.setBlockState(pos, BlocksVC.firepit.getDefaultState().withProperty(BlockFirepit.buildstage, stage));
			itemstack.stackSize--;
			return true;
		}

		if (!player.isSneaking()) return false;
		
		TileEntity tileentity = world.getTileEntity(pos);
		if (tileentity instanceof TEHeatSourceWithGUI) { 
			return ((TEHeatSourceWithGUI)tileentity).tryPutItemStack(itemstack);
		}
		return false;
	}

    
    
    
    
    @Override
    public boolean isOpaqueCube() { return false; }

    @Override
    public boolean isFullCube() { return false; }

    @Override
    public boolean isVisuallyOpaque() { return false; }

	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEHeatSourceWithGUI(EnumStrongHeatSource.FIREPIT);
	}

	
	
	
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (burning) {
            double x = (double)pos.getX() + 0.3D + rand.nextDouble()*0.3;
            double y = (double)pos.getY() + 0.2D + rand.nextDouble()*0.2;
            double z = (double)pos.getZ() + 0.3D + rand.nextDouble()*0.3;

            worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
            worldIn.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

	
    

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state) {
		return AxisAlignedBB.fromBounds(pos.getX() + 0.0625f, pos.getY(), pos.getZ() + 0.0625f, pos.getX() + 0.9375f, pos.getY() + 0.35f, pos.getZ() + 0.9375f);
	}
	
	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		return getCollisionBoundingBox(worldIn, pos, null);
	}
	

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos) {
		this.setBlockBounds(0.0625f, 0f, 0.0625f, 0.9375f, 0.35f, 0.9375f);
	}
	
	
	@Override
	public String getSubType(ItemStack stack) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	public enum EnumBuildStage implements IStringSerializable, IStateEnum {
		STONES,
		LOG1,
		LOG2,
		LOG3;

		@Override
		public String getName() {
			return name().toLowerCase();
		}
		
		public EnumBuildStage getNextStage() {
			if (this == STONES) return LOG1;
			if (this == LOG1) return LOG2;
			if (this == LOG2) return LOG3;
			return null;
		}
		
		
		public static EnumBuildStage byOrdinal(int ordinal) {
			for (EnumBuildStage stage : values()) {
				if (stage.ordinal() == ordinal) return stage;
			}
			return null;
		}

		@Override
		public int getMetaData(Block block) {
			return ordinal();
		}

		@Override
		public String getStateName() {
			return name().toLowerCase();
		}

		@Override
		public void init(Block block, int meta) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getId() {
			// TODO Auto-generated method stub
			return 0;
		}
	}




}

package at.tyron.vintagecraft.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFlower;
import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDoubleFlowerVC extends BlockFlowerVC implements IPlantable {
	public static int multistateAvailableTypes = 8;
	
    public static final PropertyEnum HALF = PropertyEnum.create("half", EnumBlockHalf.class);
    

	public BlockDoubleFlowerVC(IEnumState []subtypes) {
		super();		
		this.subtypes = subtypes;
		FLOWERTYPE = PropertyEnum.create("flowertype", EnumFlower.class, Lists.newArrayList(subtypes));
		
		blockState = this.createBlockState();
		setDefaultState(this.blockState.getBaseState().withProperty(FLOWERTYPE, (EnumFlower)subtypes[0]).withProperty(HALF, EnumBlockHalf.LOWER));
	}

	

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public EnumFlower getVariant(IBlockAccess worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == this) {
            iblockstate = this.getActualState(iblockstate, worldIn, pos);
            return (EnumFlower)iblockstate.getValue(FLOWERTYPE);
        }
        else {
            return EnumFlower.GOLDENROD;
        }
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    public boolean isReplaceable(World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() != this) {
            return true;
        }
        else {
            return false;
        }
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            boolean flag = state.getValue(HALF) == EnumBlockHalf.UPPER;
            BlockPos blockpos1 = flag ? pos : pos.up();
            BlockPos blockpos2 = flag ? pos.down() : pos;
            Object object = flag ? this : worldIn.getBlockState(blockpos1).getBlock();
            Object object1 = flag ? worldIn.getBlockState(blockpos2).getBlock() : this;

            if (!flag) this.dropBlockAsItem(worldIn, pos, state, 0); //Forge move above the setting to air.

            if (object == this) {
                worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 3);
            }

            if (object1 == this) {
                worldIn.setBlockState(blockpos2, Blocks.air.getDefaultState(), 3);
            }
        }
    }

    

    
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() != this) return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            return worldIn.getBlockState(pos.down()).getBlock() == this;
        } else {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.up());
            return iblockstate1.getBlock() == this && canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            return null;
        } else {
            return Item.getItemFromBlock(this);
        }
    }

    public int damageDropped(IBlockState state) {
        return state.getValue(HALF) != EnumBlockHalf.UPPER ? ((EnumFlower)state.getValue(FLOWERTYPE)).getMetaData() : 0;
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
    	EnumFlower enumplanttype = this.getVariant(worldIn, pos);
        return 16777215; //: BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
    }

    public void placeAt(World worldIn, BlockPos lowerPos, EnumFlower variant, int flags) {
        worldIn.setBlockState(lowerPos, this.getDefaultState().withProperty(HALF, EnumBlockHalf.LOWER).withProperty(FLOWERTYPE, variant), flags);
        worldIn.setBlockState(lowerPos.up(), this.getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER), flags);
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER), 2);
    }

    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
    	super.harvestBlock(worldIn, player, pos, state, te);
    }

    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            if (worldIn.getBlockState(pos.down()).getBlock() == this) {
                if (!player.capabilities.isCreativeMode) {
                    IBlockState iblockstate1 = worldIn.getBlockState(pos.down());
                    EnumFlower enumplanttype = (EnumFlower)iblockstate1.getValue(FLOWERTYPE);

                    worldIn.destroyBlock(pos.down(), true);
                } else {
                    worldIn.setBlockToAir(pos.down());
                }
            }
        }
        else if (player.capabilities.isCreativeMode && worldIn.getBlockState(pos.up()).getBlock() == this) {
            worldIn.setBlockState(pos.up(), Blocks.air.getDefaultState(), 2);
        }

        super.onBlockHarvested(worldIn, pos, state, player);
    }

    private boolean onHarvest(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
    	EnumFlower enumplanttype = (EnumFlower)state.getValue(FLOWERTYPE);

    	return false;
    }


    public int getDamageValue(World worldIn, BlockPos pos) {
        return this.getVariant(worldIn, pos).getMetaData();
    }

    public IBlockState getStateFromMeta(int meta) {
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(HALF, EnumBlockHalf.UPPER) : this.getDefaultState().withProperty(HALF, EnumBlockHalf.LOWER).withProperty(FLOWERTYPE, EnumFlower.fromMeta(this, meta & 7));
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        if (state.getValue(HALF) == EnumBlockHalf.UPPER) {
            IBlockState iblockstate1 = worldIn.getBlockState(pos.down());

            if (iblockstate1.getBlock() == this) {
                state = state.withProperty(FLOWERTYPE, iblockstate1.getValue(FLOWERTYPE));
            }
        }

        return state;
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF) == EnumBlockHalf.UPPER ? 8 : ((EnumFlower)state.getValue(FLOWERTYPE)).getMetaData();
    }

    protected BlockState createBlockState() {
    	if (FLOWERTYPE == null) {
    		return new BlockState(this, new IProperty[0]);
    	}
        return new BlockState(this, new IProperty[] {HALF, FLOWERTYPE});
    }

    
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

   
    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        //Forge: Break both parts on the client to prevent the top part flickering as default type for a few frames.
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() ==  this && state.getValue(HALF) == EnumBlockHalf.LOWER && world.getBlockState(pos.up()).getBlock() == this)
            world.setBlockToAir(pos.up());
        return world.setBlockToAir(pos);
    }

	
	
	
	
	
	
	

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }	

	

	
	
	 public static enum EnumBlockHalf implements IStringSerializable {
        UPPER,
        LOWER;

        public String toString() {
            return this.getName();
        }

        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }
	 
	 
	    
	    @Override
	    public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
	        return net.minecraftforge.common.EnumPlantType.Plains;
	    }
	    @Override
	    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
	        return this.getDefaultState();
	    }
	    
	    
}

package at.tyron.vintagecraft.Block.Organic;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockContainerVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IBlockItemSink;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.TileEntity.TESapling;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSaplingVC extends BlockContainerVC implements IMultiblock, IGrowable, IBlockItemSink {
	public PropertyBlockClass TREETYPE;
	BlockClassEntry[] subtypes;
	

	public BlockSaplingVC() {
		super(Material.plants);
		setCreativeTab(VintageCraft.floraTab);
		setTickRandomly(true);
		this.setDefaultState(this.blockState.getBaseState());
		
		float f = 0.2F;
	    this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
	}
	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}
	
	
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = ((BlockClassEntry)state.getValue(getTypeProperty())).getItemStack();
        ret.add(itemstack);
        
    	return ret;
    }
	
    
    
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry tree : subtypes) {
			list.add(new ItemStack(itemIn, 1, tree.getMetaData(this)));
		}
		super.getSubBlocks(itemIn, tab, list);
	}
	

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		TESapling cte = new TESapling();
		
		return new TESapling();
	}

	@Override
	public int multistateAvailableTypes() {
		return 16;
	}

	@Override
	public IProperty getTypeProperty() {
		return TREETYPE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.TREETYPE = property;
	}

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.sapling;
	}
	
	public boolean fertilize(World world, Random rand, BlockPos pos, IBlockState state, ItemStack itemstack) {
		TileEntity te = world.getTileEntity(pos);
        if(te instanceof TESapling) {
        	
        	TESapling cte = (TESapling) te;
        	
        	if(!cte.canUseFertilizer(itemstack)) return false;
        	
            cte.incSize(itemstack);
        }
        return true;
	}
	
	public void growTree(World worldIn, Random rand, BlockPos pos, IBlockState state, float size) {
		EnumTree tree = (EnumTree) getTreeType(state).getKey();
		
		worldIn.setBlockState(pos, Blocks.air.getDefaultState());
		if (tree.defaultGenerator != null) {
			tree.defaultGenerator.growTree(worldIn, pos.down(), size);
		}
	}
	
	
	public BlockClassEntry getTreeType(IBlockState state) {
		return (BlockClassEntry)state.getValue(getTypeProperty());
	}
 
	
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = getBlockClass().getMetaFromState(state);
        return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getBlockClass().getEntryFromMeta(this, meta).getBlockState();
		return state;
	}

   
    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return new BlockState(this, new IProperty[] {});
    	} else {
    		return new BlockState(this, new IProperty[] {getTypeProperty()});
    	}
    }
    
	
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
    	return canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }

    protected boolean canPlaceBlockOn(Block ground) {
        return ground == BlocksVC.topsoil || BlocksVC.subsoil.containsBlock(ground);
    }


    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TESapling) {
        	TESapling cte = (TESapling) te;
            
        	if (worldIn.getWorldTime() > cte.getGrowthEnd(worldIn.getWorldTime(), (EnumTree) getTreeType(state).getKey())) {
        		growTree(worldIn, rand, pos, state, cte.getSize());
        	}
        }
    	
        this.checkAndDropBlock(worldIn, pos, state);
    }
    
    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return this.canPlaceBlockOn(worldIn.getBlockState(pos.down()).getBlock());
    }
	
	
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
    	if (!worldIn.isRemote) {
			TileEntity te = worldIn.getTileEntity(pos);
			
	        if(te instanceof TESapling) {
	        	TESapling cte = (TESapling) te;
	            cte.updateGrowthEnd(worldIn.getWorldTime(), (EnumTree) getTreeType(worldIn.getBlockState(pos)).getKey());
	        }
		}
    	
    	
    	return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
    }
	

	
	
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
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
    
    @Override
    public int getRenderType() {
    	return 3;
    }
    
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state;
	}
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && playerIn.getHeldItem() == null) {
			TileEntity te = worldIn.getTileEntity(pos);
			
	        if(te instanceof TESapling) {
	        	TESapling cte = (TESapling) te;
	            
	        	playerIn.addChatMessage(new ChatComponentText("Sapling growth: " + (Math.round(100 * cte.size)/100f) + ", growth in " + Math.round((cte.getGrowthEnd(worldIn.getWorldTime(), (EnumTree) getTreeType(state).getKey()) - worldIn.getWorldTime())/24000) + " days"));
	        }
		}
		
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		TileEntity te = worldIn.getTileEntity(pos);
		
        if(te instanceof TESapling) {
        	TESapling cte = (TESapling) te;
        	return cte.canApplyBonemeal(worldIn.getWorldTime());
        }
        return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	
	@Override
	// Called when bone meal is applied
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TESapling) {
        	TESapling cte = (TESapling) te;
        	cte.getGrowthEnd(worldIn.getWorldTime(), (EnumTree) getTreeType(state).getKey()); // Refresh growth end
        	cte.applyBonemeal(worldIn.getWorldTime());
        	
        	if (cte.getGrowthEnd(worldIn.getWorldTime(), (EnumTree) getTreeType(state).getKey()) <= worldIn.getWorldTime()) 
        		growTree(worldIn, rand, pos, state, cte.getSize());
        }
		
	}

	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		TileEntity te = world.getTileEntity(pos);
        if(te instanceof TESapling) {
        	boolean ok = ((TESapling)te).tryPutItemStack(itemstack);
        	
        	if (ok) {
        		ItemDye.spawnBonemealParticles(world, pos, 10);
        		world.markBlockForUpdate(pos);
        	}
        	return ok; 
        }
        return false;
	}

	@Override
	public String getSubType(ItemStack stack) {
		return BlocksVC.sapling.getEntryFromItemStack(stack).getStateName();
	}
	
	
	
}

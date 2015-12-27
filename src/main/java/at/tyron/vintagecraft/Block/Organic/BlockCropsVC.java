package at.tyron.vintagecraft.Block.Organic;

import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.AchievementsVC;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.CropClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Interfaces.Block.IBlockSoil;
import at.tyron.vintagecraft.Interfaces.Block.IMultiblock;
import at.tyron.vintagecraft.Interfaces.Item.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.ItemSeedVC;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrop;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCropsVC extends BlockVC implements ISubtypeFromStackPovider, IMultiblock {
	public PropertyBlockClass CROPTYPEANDSTAGE;
	
	public BlockCropsVC() {
		super(Material.plants);
		setTickRandomly(true);
		minDropChance = 0f;
	}
	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}
	
	
	
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
    	if (!worldIn.isRemote && state.getBlock() == this) {
    		this.checkAndDropBlock(worldIn, pos, state);
    	}
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.suitableGround(worldIn, pos, worldIn.getBlockState(pos.down()))) {
        	worldIn.destroyBlock(pos, true);
        }
    }
    
    
    public boolean suitableGround(World worldIn, BlockPos pos, IBlockState ground) {
        return worldIn.getLight(pos.up()) >= 10 && (ground.getBlock() instanceof BlockFarmlandVC || ground.getBlock() instanceof IBlockSoil);
    }

    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
        
        this.checkAndDropBlock(worldIn, pos, state);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int growthStage = getGrowthStage(state);
            EnumCrop crop = getCropType(state);
            
            if (growthStage < crop.growthstages - 1) {
                float growthchance = growthChance(this, worldIn, pos);

                if (growthchance < 0) {
                	worldIn.destroyBlock(pos, false);
                	return;
                }
                
                if (rand.nextFloat() < growthchance) {
                    worldIn.setBlockState(pos, BlocksVC.crops.getBlockStateFor(crop, growthStage + 1), 2);
                    reduceFertility(worldIn, pos.down());
                }
            }
        }
    }

    
    private int getGrowthStage(IBlockState state) {
    	return ((CropClassEntry)BlocksVC.crops.getEntryFromState(state)).stage;
	}
    
	public EnumCrop getCropType(IBlockState state) {
		return ((CropClassEntry)BlocksVC.crops.getEntryFromState(state)).crop;
	}


	// 0.1f == average growth speed
	// negative = crop should die
	private float growthChance(BlockCropsVC blockCropsVC, World worldIn, BlockPos pos) {
		IBlockState state = worldIn.getBlockState(pos.down());
		
		if (state.getBlock() instanceof BlockFarmlandVC) {
			EnumFertility fertility = ((BlockFarmlandVC)state.getBlock()).getFertility(worldIn, pos.down());
			if (fertility != null) {
				return 0.05f * fertility.growthspeedmultiplier;
			} else {
				if (worldIn.rand.nextFloat() < 0.1f) return -1f;
				return 0.05f;
			}
		}

		return 0.05f;
	}

	
	void reduceFertility(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
    	
		if (state.getBlock() instanceof BlockFarmlandVC) {
			TileEntity te = world.getTileEntity(pos);
	    	
	        if(te instanceof TEFarmland) {
	        	TEFarmland teFarmland = (TEFarmland) te;
	        	teFarmland.consumeFertility(5);
	        }
		}
	}
	
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && playerIn.getHeldItem() == null) {
			TileEntity te = worldIn.getTileEntity(pos.down());
			
	        if(te instanceof TEFarmland) {
	        	TEFarmland cte = (TEFarmland) te;
	            
	        	playerIn.addChatMessage(new ChatComponentText("Farmland fertility: " + cte.getFertility()));
	        }
		}
		
		/*EnumCrop crop = getCropType(state);
		int growthStage = getGrowthStage(state);
		if (growthStage - 1 < crop.growthstages)
			worldIn.setBlockState(pos, BlocksVC.crops.getBlockStateFor(crop, growthStage + 1), 2);
		*/
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		
		if (getCropType(state) == EnumCrop.FLAX) {
			player.triggerAchievement(AchievementsVC.findFlax);
		}
		
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	
	@Override
	protected BlockState createBlockState() {
		if (getTypeProperty() != null) {
			return new BlockState(this, new IProperty[] {getTypeProperty()});
		}
		return new BlockState(this, new IProperty[0]);
	}


	@Override
	public int getMetaFromState(IBlockState state) {
		return getBlockClass().getMetaFromState(state);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getBlockClass().getEntryFromMeta(this, meta).getBlockState();
	}
	
	


	    
	

	@Override
	public IProperty getTypeProperty() {
		return CROPTYPEANDSTAGE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.CROPTYPEANDSTAGE = property;
	}

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.crops;
	}

	@Override
	public int multistateAvailableTypes() {
		return 16;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "tile.crops";
	}
	
	
	 
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	EnumCrop croptype = getCropType(state);
    	if (getGrowthStage(state) == croptype.growthstages - 1) {
    		ItemStack stack = ItemSeedVC.withCropType(croptype);
    		stack.stackSize = croptype.quantitySeedsDroped + (((World)world).rand.nextBoolean() ? 1 : 0);
    		ret.add(stack);
    		
    		if (croptype == EnumCrop.FLAX) {
    			ret.add(new ItemStack(ItemsVC.flaxFibers));
    		}
    	
    	} else {
    		ItemStack stack = ItemSeedVC.withCropType(croptype);
    		stack.stackSize = 1;
    		ret.add(stack);
    	}
    	
    	return ret;
    }
    
    // Remove registration of multiple variants, as there is not items for these
    @Override
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		return this;
	}

    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
    	return AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1f, pos.getY() + 0.25f, pos.getZ() + 1f);
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		AxisAlignedBB bounds = getSelectedBoundingBox(null, pos);
		setBlockBounds((float)bounds.minX - pos.getX(), (float)bounds.minY - pos.getY(), (float)bounds.minZ - pos.getZ(), (float)bounds.maxX - pos.getX(), (float)bounds.maxY - pos.getY(), (float)bounds.maxZ - pos.getZ());
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

}

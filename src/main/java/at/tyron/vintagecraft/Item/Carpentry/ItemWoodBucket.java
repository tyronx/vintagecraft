package at.tyron.vintagecraft.Item.Carpentry;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.Mechanics.BlockMechanicalVC;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.Interfaces.IMechanicalPowerDevice;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.ItemBlockVC;
import at.tyron.vintagecraft.TileEntity.TEWoodBucket;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEMechanicalNetworkDeviceBase;
import at.tyron.vintagecraft.WorldProperties.EnumBucketContents;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.IEntitySelector;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemWoodBucket extends ItemBlockVC implements IItemFuel, ISubtypeFromStackPovider {

	public ItemWoodBucket(Block block) {
		super(block);
		//setCreativeTab(VintageCraft.craftedBlocksTab);
		setMaxStackSize(1);
	}
	
	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Carpentry;
	}

	
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		EnumBucketContents contents = getBucketContents(stack);
		
		switch (contents) {
		case EMPTY:
			return "item.emptywoodbucket";
		case WATER:
			return "item.waterwoodbucket";
		default:
			return "item.unkownwoodbucket";
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("tile.planks." + getTreeType(stack).getName() + ".name"));
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		return EnumTree.byId(getOrCreateNBT(itemstack).getInteger("treetype"));
	}

	public static EnumBucketContents getBucketContents(ItemStack itemstack) {
		return EnumBucketContents.byId(getOrCreateNBT(itemstack).getInteger("bucketcontents"));
	}

	
	public ItemStack withTreeTypeAndBucketContents(EnumTree treetype, EnumBucketContents contents) {
		ItemStack stack = new ItemStack(this);
		getOrCreateNBT(stack).setInteger("treetype", treetype.getId());
		getOrCreateNBT(stack).setInteger("bucketcontents", contents.getId());
		return stack;
	}

	
	
	
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
    	if (this.block instanceof BlockMechanicalVC && !((BlockMechanicalVC)this.block).isBlockedAllowedAt(world, pos)) {
    		return false;
    	}
    	
        if (!world.setBlockState(pos, newState, 3)) {
        	return false;
        }

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block) {
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
            
            int i = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing facing = EnumFacing.getHorizontal(i);
            
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TEWoodBucket) {
            	TEWoodBucket bucket = (TEWoodBucket)te; 
            	bucket.treetype = getTreeType(stack);
            	bucket.orientation = facing;
            	bucket.contents = getBucketContents(stack);
            	
            }
            
        }

        return true;
    }

	
	@Override
	public int getBurningHeat(ItemStack stack) {
		return 800;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 0.5f;
	}
	
	@Override
	public int smokeLevel(ItemStack stack) {
		return 50;
	}	
    
	@Override
	public boolean isMetalWorkingFuel(ItemStack stack) {
		return false;
	}

	
	@Override
	public ItemStack getCokedOutput(ItemStack stack) {
		return null;
	}

	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (getBucketContents(itemStackIn) == EnumBucketContents.WATER && tryExtinguishStuff(worldIn, playerIn.getPosition(), playerIn)) {
			getOrCreateNBT(itemStackIn).setInteger("bucketcontents", EnumBucketContents.EMPTY.getId());
			return itemStackIn;
		}
		
		if (getBucketContents(itemStackIn) == EnumBucketContents.EMPTY) {
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
	        if (movingobjectposition == null) {
	            return itemStackIn;
	        } else {
	            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
	                BlockPos blockpos = movingobjectposition.getBlockPos();

                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Material material = iblockstate.getBlock().getMaterial();

                    if (material == Material.water) {
                    	getOrCreateNBT(itemStackIn).setInteger("bucketcontents", EnumBucketContents.WATER.getId());
                    	worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "game.player.swim.splash", 1f, 1f);
                    }

                    
	            }

	        }

		}
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}
	
	
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (getBucketContents(stack) == EnumBucketContents.WATER && tryExtinguishStuff(world, pos, player)) {
			getOrCreateNBT(stack).setInteger("bucketcontents", EnumBucketContents.EMPTY.getId());
			return false;
		}
		
		if (player.isSneaking()) {
			return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
		} else {
			return false;
		}
	}

	
	
	public boolean tryExtinguishStuff(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		int extinguishedPlayers = 0;
		double distance = 2.5;
		
		for (int i = 0; i < worldIn.playerEntities.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer)worldIn.playerEntities.get(i);

            if (entityplayer1.isBurning()) {
                double dist = entityplayer1.getDistanceSq(playerIn.posX, playerIn.posY, playerIn.posZ);
                if (dist < distance * distance) {
                	entityplayer1.extinguish();
                	extinguishedPlayers++;
        			extinguishEffectsAt(worldIn, entityplayer1.posX, entityplayer1.posY + 1, entityplayer1.posZ);
                }
            }
		}
		
	
		int extinguishedFires = 0;
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					if (worldIn.getBlockState(pos.add(dx, dy, dz)).getBlock() instanceof BlockFire) {
						worldIn.setBlockToAir(pos.add(dx, dy, dz));
						extinguishEffectsAt(worldIn, pos.getX() + dx + 0.5f, pos.getY() + dy + 0.5f, pos.getZ() + dz + 0.5f);
						extinguishedFires++;
					}
					
				}
			}
		}
		
		if (extinguishedFires > 0 || extinguishedPlayers > 0) {
			worldIn.playSoundEffect(playerIn.posX, playerIn.posY, playerIn.posZ, "random.fizz", 1f, 1f);
			return true;
		}

		return false;
	}
	
	
	public void extinguishEffectsAt(World worldIn, double xpos, double ypos, double zpos) {
		for (int x = 0; x < 40; x++) {
			worldIn.spawnParticle(
				EnumParticleTypes.WATER_SPLASH, 
				xpos + worldIn.rand.nextFloat() / 2 - 0.25f, 
				ypos + worldIn.rand.nextFloat() / 2 + 0.25f, 
				zpos + worldIn.rand.nextFloat() / 2 - 0.25f, 
				worldIn.rand.nextFloat() / 2 - 0.25f, 
				-0.25f - worldIn.rand.nextFloat() / 2, 
				worldIn.rand.nextFloat() / 2 - 0.25f, 
				0);
		}		
	}

	@Override
	public String getSubType(ItemStack stack) {
		return getTreeType(stack).getName() + "-" + getBucketContents(stack).getName();
	}

	
	
}

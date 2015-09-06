package at.tyron.vintagecraft.Entity;

import java.util.Iterator;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCoalPoweredMinecart extends EntityMinecart {
	public boolean refreshModel = false;

    public static final int[][][] railFromToVector = new int[][][] {
    	{{0, 0, -1}, {0, 0, 1}}, 
    	{{ -1, 0, 0}, {1, 0, 0}}, 
    	{{ -1, -1, 0}, {1, 0, 0}}, 
    	{{ -1, 0, 0}, {1, -1, 0}}, 
    	{{0, 0, -1}, {0, -1, 1}}, 
    	{{0, -1, -1}, {0, 0, 1}}, 
    	{{0, 0, 1}, {1, 0, 0}}, 
    	{{0, 0, 1}, { -1, 0, 0}}, 
    	{{0, 0, -1}, { -1, 0, 0}}, 
    	{{0, 0, -1}, {1, 0, 0}}
    };
    
    public int fuel;
    public double pushX;
    public double pushZ;

    /** appears to be the progress of the turn */
    int turnProgress;
    double minecartX;
    double minecartY;
    double minecartZ;
    double minecartYaw;
    double minecartPitch;
    @SideOnly(Side.CLIENT)
    double velocityX;
    @SideOnly(Side.CLIENT)
    double velocityY;
    @SideOnly(Side.CLIENT)
    double velocityZ;

	public EntityCoalPoweredMinecart(World worldIn, double p_i1719_2_, double p_i1719_4_, double p_i1719_6_) {
		super(worldIn, p_i1719_2_, p_i1719_4_, p_i1719_6_);
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}
	

	public EntityCoalPoweredMinecart(World worldIn) {
		super(worldIn);
		this.dataWatcher.addObject(16, new Byte((byte)0));
	}


	public void onUpdate() {
		onUpdateMinecartCode();
		
		this.setMinecartPowered(this.fuel > 0);
		   
		if (isMinecartPowered() && this.rand.nextInt(3) == 0) {
			worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 1.4D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
		}
	}
	
	
	
	
	
	

    
    
    // Called by handle entity movement packets on client side
    @SideOnly(Side.CLIENT)
    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.minecartX = p_180426_1_;
        this.minecartY = p_180426_3_;
        this.minecartZ = p_180426_5_;
      //  this.minecartYaw = (double)p_180426_7_;
      //  this.minecartPitch = (double)p_180426_8_;
        this.turnProgress = p_180426_9_ + 2;
        this.motionX = this.velocityX;
        this.motionY = this.velocityY;
        this.motionZ = this.velocityZ;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }
    
	        
	private void onUpdateMinecartCode() {
        if (this.getRollingAmplitude() > 0) {
            this.setRollingAmplitude(this.getRollingAmplitude() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.posY < -64.0D) {
            this.kill();
        }

        if (this.worldObj.isRemote) {
        	onUpdateClient();
        }
        else {
        	onUpdateServer();
        }
	}
	

	
	void onUpdateServer() {
        int blockPosY;
        
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033D;
        int blockPosX = MathHelper.floor_double(this.posX);
        blockPosY = MathHelper.floor_double(this.posY);
        int blockPosZ = MathHelper.floor_double(this.posZ);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(blockPosX, blockPosY - 1, blockPosZ))) {
            --blockPosY;
        }

        BlockPos blockpos = new BlockPos(blockPosX, blockPosY, blockPosZ);
        IBlockState iblockstate = this.worldObj.getBlockState(blockpos);

        if (canUseRail() && BlockRailBase.isRailBlock(iblockstate)) {
            this.func_180460_a(blockpos, iblockstate);

            if (iblockstate.getBlock() == Blocks.activator_rail) {
                this.onActivatorRailPass(blockPosX, blockPosY, blockPosZ, ((Boolean)iblockstate.getValue(BlockRailPowered.POWERED)).booleanValue());
            }
        } else {
            this.moveDerailedMinecart();
        }
        

        this.doBlockCollisions();
        
        //this.rotationPitch = 0.0F;
        double d0 = this.prevPosX - this.posX;
        double d2 = this.prevPosZ - this.posZ;

        if (d0 * d0 + d2 * d2 > 0.001D) {
            //this.rotationYaw = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI);

  /*          if (this.isInReverse)
            {
                this.rotationYaw += 180.0F;
            }*/
        }

        double d3 = (double)MathHelper.wrapAngleTo180_float(this.rotationYaw - this.prevRotationYaw);

        if (d3 < -170.0D || d3 >= 170.0D) {
//            this.rotationYaw += 180.0F;
//            this.isInReverse = !this.isInReverse;
        }

        //this.setRotation(this.rotationYaw, this.rotationPitch);
        
        AxisAlignedBB box;
        if (getCollisionHandler() != null) box = this.getCollisionHandler().getMinecartCollisionBox(this);
        else                               box = this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D);
        
        Iterator iterator = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, box).iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();

            if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityMinecart) {
                entity.applyEntityCollision(this);
            }
        }

        if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
            if (this.riddenByEntity.ridingEntity == this) {
                this.riddenByEntity.ridingEntity = null;
            }

            this.riddenByEntity = null;
        }

        this.handleWaterMovement();
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartUpdateEvent(this, this.getCurrentRailPosition()));
	}
	
	
	void onUpdateClient() {
        if (this.turnProgress > 0) {
            double d4 = this.posX + (this.minecartX - this.posX) / (double)this.turnProgress;
            double d5 = this.posY + (this.minecartY - this.posY) / (double)this.turnProgress;
            double d6 = this.posZ + (this.minecartZ - this.posZ) / (double)this.turnProgress;
            double d1 = MathHelper.wrapAngleTo180_double(this.minecartYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d1 / (double)this.turnProgress);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.minecartPitch - (double)this.rotationPitch) / (double)this.turnProgress);
            --this.turnProgress;
            this.setPosition(d4, d5, d6);
            //this.setRotation(this.rotationYaw, this.rotationPitch);
            
        } else {
            this.setPosition(this.posX, this.posY, this.posZ);
            //this.setRotation(this.rotationYaw, this.rotationPitch);
        }
	}
	
	
	
	
	
	
	

	protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_) {
       super.func_180460_a(p_180460_1_, p_180460_2_);

       fuel = Math.max(0, fuel - 1);
       

   }
   

   protected void applyDrag() {
	   if (fuel <= 0) {
           this.motionX *= 0.9599999785423279D;
           this.motionY *= 0.0D;
           this.motionZ *= 0.9599999785423279D;
	   } else {
		   
		   // Why so compliated? Just give the powered cart some negative drag
		   
		   if (Math.abs(motionX) < 0.2) {
			   motionX *= 1.1; 
		   }
		   if (Math.abs(motionZ) < 0.2) {
			   motionZ *= 1.1;
		   }
		   
           motionY *= 0.0D;
	   }
	   
      // super.applyDrag();
   }
   
   @Override
   public boolean shouldDoRailFunctions() {
	   return false;
   }
   
   @Override
   protected void setRotation(float yaw, float pitch) {
	   
	   //super.setRotation(yaw, pitch);
   }
   
   
	@Override
	public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch) {
        this.prevPosX = this.posX = x;
        this.prevPosY = this.posY = y;
        this.prevPosZ = this.posZ = z;

	   this.setPosition(this.posX, this.posY, this.posZ);
	}
   
	@Override
	public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
		//super.setLocationAndAngles(x, y, z, yaw, pitch);
        this.lastTickPosX = this.prevPosX = this.posX = x;
        this.lastTickPosY = this.prevPosY = this.posY = y;
        this.lastTickPosZ = this.prevPosZ = this.posZ = z;
//        this.rotationYaw = yaw;
//        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
	}
	
	@Override
    @SideOnly(Side.CLIENT)
    public void setAngles(float yaw, float pitch) {
    
    }

	@Override
	public void onEntityUpdate() {
		
	}


   public boolean interactFirst(EntityPlayer playerIn) {
       if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, playerIn))) return true;
       ItemStack itemstack = playerIn.inventory.getCurrentItem();

       if (itemstack != null && itemstack.getItem() == Items.coal) {
           if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize == 0) {
               playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
           }

           this.fuel += 3600;
           
           this.pushX =  playerIn.posX - posX;
           this.pushZ =  playerIn.posZ - posZ;
           
           this.motionX += this.pushX;
           this.motionZ += this.pushZ;
       }

       refreshModel = true;
       
       return true;
   }

   
   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
       super.writeEntityToNBT(tagCompound);
       tagCompound.setDouble("PushX", this.pushX);
       tagCompound.setDouble("PushZ", this.pushZ);
       tagCompound.setShort("Fuel", (short)this.fuel);
   }

   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
       super.readEntityFromNBT(tagCompund);
       this.pushX = tagCompund.getDouble("PushX");
       this.pushZ = tagCompund.getDouble("PushZ");
       this.fuel = tagCompund.getShort("Fuel");
   }

   protected boolean isMinecartPowered() {
       return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   protected void setMinecartPowered(boolean powered) {
       if (powered) {
           this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 1)));
       } else {
           this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & -2)));
       }
   }



	@Override
	public EnumMinecartType getMinecartType() {
		return EntityMinecart.EnumMinecartType.FURNACE;
	}
	
	
	
    public Vec3 getClientSideRailDirectionVector(double dx, double dy, double dz, double unknownoffset) {
        int blockX = MathHelper.floor_double(dx);
        int blockY = MathHelper.floor_double(dy);
        int blockZ = MathHelper.floor_double(dz);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(blockX, blockY - 1, blockZ))) {
            --blockY;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(blockX, blockY, blockZ));

        if (BlockRailBase.isRailBlock(iblockstate)) {
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            dy = blockY;
            if (enumraildirection.isAscending()) {
                dy = (blockY + 1);
            }

            int[][] fromtoVec = railFromToVector[enumraildirection.getMetadata()];
            
            
            double railDirectionDeltaX = fromtoVec[1][0] - fromtoVec[0][0];
            double railDirectionDeltaZ = fromtoVec[1][2] - fromtoVec[0][2];
            
            double horizontalDistanceSq = Math.sqrt(railDirectionDeltaX * railDirectionDeltaX + railDirectionDeltaZ * railDirectionDeltaZ);
            
            railDirectionDeltaX /= horizontalDistanceSq;
            railDirectionDeltaZ /= horizontalDistanceSq;
            
            dx += railDirectionDeltaX * unknownoffset;
            dz += railDirectionDeltaZ * unknownoffset;

            if (fromtoVec[0][1] != 0 && MathHelper.floor_double(dx) - blockX == fromtoVec[0][0] && MathHelper.floor_double(dz) - blockZ == fromtoVec[0][2]) {
                dy += (double)fromtoVec[0][1];
            }
            else if (fromtoVec[1][1] != 0 && MathHelper.floor_double(dx) - blockX == fromtoVec[1][0] && MathHelper.floor_double(dz) - blockZ == fromtoVec[1][2]) {
                dy += (double)fromtoVec[1][1];
            }

            return this.getRailDirectionVector(dx, dy, dz);
        }
        else {
            return null;
        }
    }
    
    
	public Vec3 getRailDirectionVector(double dx, double dy, double dz) {
        int blockx = MathHelper.floor_double(dx);
        int blocky = MathHelper.floor_double(dy);
        int blockz = MathHelper.floor_double(dz);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(blockx, blocky - 1, blockz))) {
            --blocky;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(blockx, blocky, blockz));

        if (BlockRailBase.isRailBlock(iblockstate)) {
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            int[][] aint = railFromToVector[enumraildirection.getMetadata()];
            
            double d3 = 0.0D;
            double d4 = (double)blockx + 0.5D + (double)aint[0][0] * 0.5D;
            double d5 = (double)blocky + 0.1875D + (double)aint[0][1] * 0.5D;
            double d6 = (double)blockz + 0.5D + (double)aint[0][2] * 0.5D;
            
            double d7 = (double)blockx + 0.5D + (double)aint[1][0] * 0.5D;
            double d8 = (double)blocky + 0.1875D + (double)aint[1][1] * 0.5D;
            double d9 = (double)blockz + 0.5D + (double)aint[1][2] * 0.5D;
            
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2.0D;
            double d12 = d9 - d6;

            if (d10 == 0.0D) {
                dx = (double)blockx + 0.5D;
                d3 = dz - (double)blockz;
            }
            else if (d12 == 0.0D) {
                dz = (double)blockz + 0.5D;
                d3 = dx - (double)blockx;
            }
            else {
                double d13 = dx - d4;
                double d14 = dz - d6;
                d3 = (d13 * d10 + d14 * d12) * 2.0D;
            }

            dx = d4 + d10 * d3;
            dy = d5 + d11 * d3;
            dz = d6 + d12 * d3;

            if (d11 < 0.0D) {
                ++dy;
            }

            if (d11 > 0.0D) {
                dy += 0.5D;
            }

            return new Vec3(dx, dy, dz);
        } else {
            return null;
        }
    }
	
	
    private BlockPos getCurrentRailPosition() {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(x, y - 1, z))) y--;
        return new BlockPos(x, y, z);
    }

}

package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Entity.EntityForestSpider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class TileEntityForestSpiderSpawner extends TileEntity implements IUpdatePlayerListBox {
	int spawnDelay = 20;
    int minSpawnDelay = 200;
    int maxSpawnDelay = 600;
    int spawnCount = 4;
    int maxNearbyEntities = 8;
    /** The distance from which a player activates the spawner. */
    int activatingRangeFromPlayer = 32;
    /** The range coefficient for spawning entities around. */
    int spawnRange = 12;


	private boolean isActivated() {
		BlockPos blockpos = getPos();
		return worldObj.func_175636_b(
			(double)blockpos.getX() + 0.5D, 
			(double)blockpos.getY() + 0.5D, 
			(double)blockpos.getZ() + 0.5D, 
			(double)this.activatingRangeFromPlayer
		);
	}

	
	public void spawnParticles() {
		BlockPos blockpos = getPos();
		
		double xPos = blockpos.getX() + worldObj.rand.nextFloat();
		double yPos = blockpos.getY() + worldObj.rand.nextFloat();
		double zPos = blockpos.getZ() + worldObj.rand.nextFloat();
		
		if (worldObj.rand.nextFloat() > 0.8f) {
			worldObj.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, xPos, yPos, zPos, 0.0D, 0.0D, 0.0D, new int[0]);
		}

	}
	
	public void update() {
		if (!this.isActivated()) return;
		
		if (worldObj.isRemote) {
			spawnParticles();
		} else {
			spawnSpidersIfReady();
		}
	}
		

		
	void spawnSpidersIfReady() {
		boolean spawned = false;
		
		if (this.spawnDelay > 0) {
			this.spawnDelay--;
			return;
		}
		

		
		if (this.spawnDelay == -1) {
			this.resetTimer();
		}

		
		BlockPos blockpos = getPos();
		
		
		int quantitySpidersNearby = worldObj.getEntitiesWithinAABB(
			EntityForestSpider.class,
			(new AxisAlignedBB(
				(double)blockpos.getX(), 
				(double)blockpos.getY(), 
				(double)blockpos.getZ(), 
				(double)(blockpos.getX() + 1), 
				(double)(blockpos.getY() + 1), 
				(double)(blockpos.getZ() + 1))
			).expand((double)this.spawnRange * 2, (double)this.spawnRange * 2, (double)this.spawnRange * 2)
		
		).size();

		if (quantitySpidersNearby >= this.maxNearbyEntities) {
			this.resetTimer();
			return;
		}
		
		int tries = spawnCount * 10;
		int leftToSpawn = spawnCount - 1 + worldObj.rand.nextInt(2);
		
		while (tries-- > 0 && leftToSpawn > 0) {
			EntityLiving entity = new EntityForestSpider(worldObj);
			
			double posX = blockpos.getX() + (worldObj.rand.nextDouble()) * this.spawnRange - spawnRange/2 + 0.5D;
			double posY = blockpos.getY() + worldObj.rand.nextInt(3);
			double posZ = blockpos.getZ() + (worldObj.rand.nextDouble()) * this.spawnRange - spawnRange/2 + 0.5D;
			
			AxisAlignedBB bb = entity.getEntityBoundingBox();
			bb = new AxisAlignedBB(posX + bb.minX, posY + bb.minY, posZ + bb.minZ, posX + bb.maxX, posY + bb.maxY, posZ + bb.maxZ);
			if (isSolidBlockInBB(worldObj, bb)) continue;
			
			entity.setLocationAndAngles(posX, posY, posZ, worldObj.rand.nextFloat() * 360.0F, 0.0F);

			if (entity.getCanSpawnHere()) {
				this.spawnIfPossible(entity);
				worldObj.playAuxSFX(2004, blockpos, 0);
				spawned = true;
			}
			
			leftToSpawn--;
		}

		if (spawned) {
			this.resetTimer();
		}
	}
		

	
	private void resetTimer() {
		if (this.maxSpawnDelay <= this.minSpawnDelay) {
			this.spawnDelay = this.minSpawnDelay;
		}
		else {
			int i = this.maxSpawnDelay - this.minSpawnDelay;
			this.spawnDelay = this.minSpawnDelay + worldObj.rand.nextInt(i);
		}

		//worldObj.addBlockEvent(pos, Blocks.mob_spawner, 1, 0);
	}



	private Entity spawnIfPossible(Entity entity) {
		if (entity instanceof EntityLivingBase && entity.worldObj != null) {
			((EntityLiving)entity).func_180482_a(entity.worldObj.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);

			entity.worldObj.spawnEntityInWorld(entity);
		}
		return entity;
	}	

	
	
	
    /**
     * Returns true if the given bounding box contains solid blocks
     */
    public boolean isSolidBlockInBB(World world, AxisAlignedBB boundingbox) {
        int i = MathHelper.floor_double(boundingbox.minX);
        int j = MathHelper.floor_double(boundingbox.maxX + 1.0D);
        int k = MathHelper.floor_double(boundingbox.minY);
        int l = MathHelper.floor_double(boundingbox.maxY + 1.0D);
        int i1 = MathHelper.floor_double(boundingbox.minZ);
        int j1 = MathHelper.floor_double(boundingbox.maxZ + 1.0D);

        BlockPos pos;
        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                	pos = new BlockPos(k1, l1, i2);
                    if (!world.getBlockState(pos).getBlock().isPassable(world, pos)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


}

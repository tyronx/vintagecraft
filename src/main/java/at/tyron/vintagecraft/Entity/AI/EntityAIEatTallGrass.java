package at.tyron.vintagecraft.Entity.AI;

import at.tyron.vintagecraft.Interfaces.IEntityGrassEater;
import at.tyron.vintagecraft.Interfaces.Block.IBlockEatableGrass;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EntityAIEatTallGrass extends EntityAIBase {
    /** The entity owner of this AITask */
    EntityLiving grassEaterEntity;
    IEntityGrassEater grassEater;
    /** The world the grass eater entity is eating from */
    World entityWorld;
    /** Number of ticks since the entity started to eat grass */
    int eatingGrassTimer;
    
    public EntityAIEatTallGrass(EntityLiving entity) {
    	this.grassEaterEntity = entity;
    	grassEater = (IEntityGrassEater)entity;
        this.entityWorld = entity.worldObj;
        this.setMutexBits(7);
	}
    
    
	@Override
	public boolean shouldExecute() {
		if (eatingGrassTimer < 0) {
			eatingGrassTimer++;
			return false;
		}
		
		long worldtime = grassEaterEntity.worldObj.getWorldTime();
		
		if (worldtime < 0 || worldtime > 14000) {
			return false;
		}
		
		return getEatableGrassPos() != null;
	}
	
	

	
    public void startExecuting() {
        this.eatingGrassTimer = 80;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
        this.grassEaterEntity.getNavigator().clearPathEntity();
    }

    public void resetTask() {
        this.eatingGrassTimer = -30 + entityWorld.rand.nextInt(60);
    }
    
    public boolean continueExecuting() {
        return this.eatingGrassTimer > 0;
    }
    
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }
    
    
    public void updateTask() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);

        if (this.eatingGrassTimer == 10) {
            BlockPos blockpos = getEatableGrassPos();

            if (blockpos != null) {
            	IBlockState state = entityWorld.getBlockState(blockpos);
            	((IBlockEatableGrass)state.getBlock()).setEatenBy(grassEaterEntity, entityWorld, blockpos);
            	entityWorld.playSoundAtEntity(grassEaterEntity, "dig.grass", 1f, 1f);
            	grassEater.didEatGrass();
            }
        }
    }


    // Checks if the block right below or right in front of the entity is eatable grass
	public BlockPos getEatableGrassPos() {
		BlockPos blockpos = new BlockPos(this.grassEaterEntity.posX, this.grassEaterEntity.posY, this.grassEaterEntity.posZ);
		IBlockState state = entityWorld.getBlockState(blockpos);
		IBlockState infrontstate = entityWorld.getBlockState(blockpos.offset(grassEaterEntity.getHorizontalFacing()));
		
		if (state.getBlock() instanceof IBlockEatableGrass && 
			((IBlockEatableGrass)state.getBlock()).canBeEatenBy(grassEaterEntity, entityWorld, blockpos))
			
			return blockpos;
		
		if (infrontstate.getBlock() instanceof IBlockEatableGrass && 
			((IBlockEatableGrass)infrontstate.getBlock()).canBeEatenBy(grassEaterEntity, entityWorld, blockpos)) {
			
			return blockpos.offset(grassEaterEntity.getHorizontalFacing());
		}
		
		return null;
	}

    
}

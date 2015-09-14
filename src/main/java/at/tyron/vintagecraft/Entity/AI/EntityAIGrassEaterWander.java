package at.tyron.vintagecraft.Entity.AI;

import at.tyron.vintagecraft.Interfaces.IBlockEatableGrass;
import at.tyron.vintagecraft.Interfaces.IEntityGrassEater;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIGrassEaterWander extends EntityAIBase {

    public EntityCreature entity;
    IEntityGrassEater grassEater;
    
    public double xPosition;
    public double yPosition;
    public double zPosition;
    public double speed;
    public float wanderchance;

    public EntityAIGrassEaterWander(EntityCreature entity, double speed) {
        this(entity, speed, 1f/100f);
    }

    public EntityAIGrassEaterWander(EntityCreature entity, double speed, float wanderchance) {
        this.entity = entity;
        grassEater = (IEntityGrassEater)entity;
        this.speed = speed;
        this.wanderchance = wanderchance;
        this.setMutexBits(1);
    }


    public boolean shouldExecute() {
    	float adjustedchance = wanderchance;
        long worldtime = entity.worldObj.getWorldTime();
        if (worldtime > 14000) {
        	adjustedchance /= 8;
        } else {
        
	        if (grassEater.isHungry()) {
	        	adjustedchance = 0.4f;
	        }
        }
        
        if (entity.getRNG().nextFloat() > adjustedchance) {
            return false;
        }
        
        // Food just in front of me?! I will never leave this place <3
        if (getEatableGrassPos() != null) return false;
        
        //System.out.println("look for grass");
        Vec3 target = null;
        
        if (grassEater.isHungry()) {
        	
        	for (int i = 0; i < 4; i++) {
        		target = RandomPositionGenerator.findRandomTarget(this.entity, 3, 3);
        		if (target != null) {
        			BlockPos pos = new BlockPos(target.xCoord, target.yCoord, target.zCoord);
        			IBlockState state = entity.worldObj.getBlockState(pos);

        			if (state.getBlock() instanceof IBlockEatableGrass && ((IBlockEatableGrass)state.getBlock()).canBeEatenBy(entity, entity.worldObj, pos)) {
//        				System.out.println("found tallgrass nearby " + wanderchance);
        				break;
        			} else {
        				target = null;
        			}
        			
        		}
        	}
        }
        
        
        if (target == null && grassEater.isVeryHungry()) {
        	target = RandomPositionGenerator.findRandomTarget(this.entity, 35, 10);
        }

        
        if (target == null) {
        	target = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
        }

        if (target != null) {
            xPosition = target.xCoord;
            yPosition = target.yCoord;
            zPosition = target.zCoord;
            
            return true;
        }
        
        return false;
    }
    

    public boolean continueExecuting() {
    	//System.out.println("going " + this.entity.getNavigator().noPath());
        return !this.entity.getNavigator().noPath();
    }

    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    
    // Checks if the block right below or right in front of the entity is eatable grass
	public BlockPos getEatableGrassPos() {
		BlockPos blockpos = new BlockPos(this.entity.posX, this.entity.posY, this.entity.posZ);
		IBlockState state = entity.worldObj.getBlockState(blockpos);
		IBlockState infrontstate = entity.worldObj.getBlockState(blockpos.offset(entity.getHorizontalFacing()));
		
		if (state.getBlock() instanceof IBlockEatableGrass && 
			((IBlockEatableGrass)state.getBlock()).canBeEatenBy(entity, entity.worldObj, blockpos))
			
			return blockpos;
		
		if (infrontstate.getBlock() instanceof IBlockEatableGrass && 
			((IBlockEatableGrass)infrontstate.getBlock()).canBeEatenBy(entity, entity.worldObj, blockpos)) {
			
			return blockpos.offset(entity.getHorizontalFacing());
		}
		
		return null;
	}

}

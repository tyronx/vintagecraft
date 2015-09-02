package at.tyron.vintagecraft.Entity.AI;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;

public class EntityAIGoBackHome extends EntityAIBase {
	EntityCreature taskOwner;
	PathEntity pathentity;
	double speed;
	
    public EntityAIGoBackHome(EntityCreature owner, double speed) {
        this.taskOwner = owner;
        this.speed = speed;
        this.setMutexBits(0xffff);
    }

	
	@Override
	public boolean shouldExecute() {
		if (!taskOwner.hasHome()) return false;
		BlockPos home = taskOwner.func_180486_cf();
		
		if (isTooFarOff()) {
			pathentity = taskOwner.getNavigator().getPathToXYZ(home.getX() + 0.5f, home.getY(), home.getZ() + 0.5f);
			if (pathentity != null) {
				taskOwner.getNavigator().setPath(pathentity, speed);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean continueExecuting() {
		return !taskOwner.getNavigator().noPath();
	}
	

	public boolean isTooFarOff() {
		return
			taskOwner.hasHome() &&
			taskOwner.func_180486_cf().distanceSq(taskOwner.getPosition()) > (double)(4 * taskOwner.getMaximumHomeDistance() * taskOwner.getMaximumHomeDistance())
		;
	}

}

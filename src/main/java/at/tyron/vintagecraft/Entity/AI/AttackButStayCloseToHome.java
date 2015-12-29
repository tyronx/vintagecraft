package at.tyron.vintagecraft.Entity.AI;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class AttackButStayCloseToHome extends EntityAINearestAttackableTarget {

	public AttackButStayCloseToHome(EntityCreature p_i45878_1_, Class p_i45878_2_, boolean p_i45878_3_) {
		super(p_i45878_1_, p_i45878_2_, p_i45878_3_);
	}
	
	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && !isTooFarOff();
	}
	
	@Override
	public boolean continueExecuting() {
		return super.continueExecuting() && !isTooFarOff();
	}
	
	public boolean isTooFarOff() {
		return
			taskOwner.hasHome() &&
			taskOwner.getHomePosition().distanceSq(taskOwner.getPosition()) > (double)(4 * taskOwner.getMaximumHomeDistance() * taskOwner.getMaximumHomeDistance())
		;
	}

}

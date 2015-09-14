package at.tyron.vintagecraft.Entity.AI;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class EntityAIStayCloseToGroup extends EntityAIBase {
	public EntityCreature entity;
	public double speed;
	public int maxdistance;
	public int maxsearchdistance;
	
	public int checkdelay = 200;

	
    public double xPosition;
    public double yPosition;
    public double zPosition;
    
    public int lastApproachedEntityId = 0;

	
	public EntityAIStayCloseToGroup(EntityCreature entity, double speed, int maxdistance, int maxsearchdistance) {
		this.speed = speed;
		this.entity = entity;
		this.maxdistance = maxdistance;
		this.maxsearchdistance = maxsearchdistance;
		this.setMutexBits(1);
	}
	
	@Override
	public boolean shouldExecute() {
		
		
		if (checkdelay <= 0) {
			checkdelay = 101 + entity.worldObj.rand.nextInt(200);
		
			AxisAlignedBB aabb = new AxisAlignedBB(entity.getPosition(), entity.getPosition()).expand(maxsearchdistance, maxsearchdistance, maxsearchdistance);
			Entity nearest = findNearestEntityWithinAABB(entity.getClass(), aabb, entity, lastApproachedEntityId);
			
			int maxdistSq = maxdistance*maxdistance;
			
			// Randomly come extra close
			if (entity.worldObj.rand.nextInt(5) == 0) {
				maxdistSq = 3*3;
			}
			
			if (nearest != null && nearest.getDistanceToEntity(entity) > maxdistSq) {
				//System.out.println("too far from group");
				Vec3 target = null;
				
				for (int i = 0; i < 4; i++) {
					target = RandomPositionGenerator.findRandomTarget((EntityCreature) nearest, 5, 3);
					if (target != null) {
						// Sometimes go to another cow
					 	if (entity.worldObj.rand.nextInt(4) == 0) {
					 		lastApproachedEntityId = nearest.getEntityId();
					// 		System.out.println("next time other cow");
					 	}
			            xPosition = target.xCoord;
			            yPosition = target.yCoord;
			            zPosition = target.zCoord;
			          //  System.out.println(entity.getPosition() + " phew found my group again @ " + xPosition + "/"+yPosition+"/"+zPosition);
			             
			            return true;
					}
				}
			}
		}
		
		checkdelay--;
		
		return false;
	}
	
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }

    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
    
    
    
    public Entity findNearestEntityWithinAABB(Class entityType, AxisAlignedBB aabb, Entity closestTo, int excludingEntityId) {
        List list = entity.worldObj.getEntitiesWithinAABB(entityType, aabb);
        Entity closestEntity = null;
        double smallestDistance = Double.MAX_VALUE;

        for (int i = 0; i < list.size(); ++i) {
            Entity entity2 = (Entity)list.get(i);

            if (entity2 != closestTo && IEntitySelector.NOT_SPECTATING.apply(entity2)) {
                double dist = closestTo.getDistanceSqToEntity(entity2);

                if (dist <= smallestDistance && excludingEntityId != entity2.getEntityId()) {
                    closestEntity = entity2;
                    smallestDistance = dist;
                }
            }
        }

        return closestEntity;
    }


}

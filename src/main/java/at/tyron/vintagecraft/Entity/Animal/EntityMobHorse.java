package at.tyron.vintagecraft.Entity.Animal;

import at.tyron.vintagecraft.AchievementsVC;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMobHorse extends EntityHorse {
	boolean riderReadyForDespawn;
	
    protected boolean canDespawn() {
        return riddenByEntity == null;
    }
    
    @Override
    protected void despawnEntity() {
    	super.despawnEntity();
    	
    	// Despawn immediately if the rider is gone, and before being gone it was ready for despawn 
    	if (riddenByEntity != null && riddenByEntity instanceof EntityLiving) {
    		riderReadyForDespawn = ((EntityLiving)riddenByEntity).getAge() > 600;
    	}
    	if (riderReadyForDespawn && riddenByEntity == null && entityAge > 600) {
    		setDead();
    	}
    }
    
    
    public void onLivingUpdate() {
        if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild()) {
            float f = this.getBrightness(1.0F);
            BlockPos blockpos = new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);

            if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos)) {
            	this.setFire(8);
            }
        }
        
        if(worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
        	isDead = true;
        }

        super.onLivingUpdate();
    }

    
	public EntityMobHorse(World worldIn) {
		super(worldIn);

		tasks.taskEntries.clear();
		targetTasks.taskEntries.clear();
		
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.8D, false));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.8D));
		tasks.addTask(6, new EntityAIWander(this, 1.4D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));    
	}
	

	@Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
        getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
    }


	
	@Override
    public boolean attackEntityAsMob(Entity p_70652_1_) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;

        if (p_70652_1_ instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)p_70652_1_).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0)
            {
                p_70652_1_.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                p_70652_1_.setFire(j * 4);
            }

            this.func_174815_a(this, p_70652_1_);
        }

        return flag;
    }

	
	@Override
	public void onDeath(DamageSource cause) {
    	if (cause.getEntity() instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)cause.getEntity();
    		player.triggerAchievement(AchievementsVC.killUndeadHorse);
    	}
    	
		super.onDeath(cause);
	}
	
    
}

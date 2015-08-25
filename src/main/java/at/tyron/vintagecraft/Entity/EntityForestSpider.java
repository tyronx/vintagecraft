package at.tyron.vintagecraft.Entity;

import at.tyron.vintagecraft.Entity.AI.EntityAIGoBackHome;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityForestSpider extends EntityCaveSpider {

	public EntityForestSpider(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 0.5F);
        this.targetTasks.taskEntries.clear();
        this.tasks.taskEntries.clear();
        

        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.field_175455_a);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, true));
        this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIGoBackHome(this, 1D));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        
        
	}
	
	

    protected void updateAITasks() {
        super.updateAITasks();

        if (!this.hasHome()) {
            this.func_175449_a(worldObj.getHorizon(new BlockPos(this)), 14);
        }
    }
    
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(45.0D);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
    }
    
    public float getEyeHeight() {
        return 0.45F;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && worldObj.getBlockState(getPosition()).getBlock().isPassable(worldObj, getPosition());
    }
    
    
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


}

package at.tyron.vintagecraft.Entity.Animal;

import at.tyron.vintagecraft.Entity.AI.EntityAIEatTallGrass;
import at.tyron.vintagecraft.Entity.AI.EntityAIGrassEaterWander;
import at.tyron.vintagecraft.Entity.AI.EntityAIStayCloseToGroup;
import at.tyron.vintagecraft.Interfaces.IEntityGrassEater;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySheepVC extends EntitySheep implements IEntityGrassEater {
	int grassEatingTimer;
	int fullness;
	int saturation;

	EntityAIEatTallGrass entityAIEatGrass = new EntityAIEatTallGrass(this);
	
	
	public EntitySheepVC(World worldIn) {
		super(worldIn);
		tasks.taskEntries.clear();
		
		
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        tasks.addTask(2, new EntityAIMate(this, 1.0D));
        tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
       	tasks.addTask(5, new EntityAIStayCloseToGroup(this, 1.5, 5, 40));
        tasks.addTask(6, this.entityAIEatGrass);
        tasks.addTask(7, new EntityAIGrassEaterWander(this, 1.0D));
        tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        tasks.addTask(9, new EntityAILookIdle(this));
	}
	
	
	// Spawn init
	@Override
	public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_) {
		fullness = rand.nextInt(40000);
		saturation = rand.nextInt(1000);
		
		return super.func_180482_a(p_180482_1_, p_180482_2_);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompund) {
		super.readFromNBT(tagCompund);
		
		fullness = tagCompund.getInteger("fullness");
		saturation = tagCompund.getInteger("saturation");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompund) {
		super.writeToNBT(tagCompund);
		tagCompund.setInteger("fullness", fullness);
		tagCompund.setInteger("saturation", saturation);
	}
	
	protected void updateAITasks() {
        this.grassEatingTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }
	
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
    }

	
	@SideOnly(Side.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_) {		
		return this.grassEatingTimer <= 0 ? 0.0F : 
				(this.grassEatingTimer >= 4 && this.grassEatingTimer <= 76 ? 0.8F : 
					(this.grassEatingTimer < 4 ? (this.grassEatingTimer - p_70894_1_) / 4.0F : -((float)(this.grassEatingTimer - 80) - p_70894_1_) / 4.0F));
	}

	@SideOnly(Side.CLIENT)
	public float getHeadRotationAngleX(float p_70890_1_) {
		if (this.grassEatingTimer > 4 && this.grassEatingTimer <= 76) {
			float f1 = ((float)(this.grassEatingTimer - 4) - p_70890_1_) / 32.0F;
			return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f1 * 28.7F);
		} else {
			return this.grassEatingTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
		}
	}
	
	
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte packetid) {
		if (packetid == 10) {
			grassEatingTimer = 80;
		}
		else {
			super.handleHealthUpdate(packetid);
		}
	}
	
	@Override
	public void onLivingUpdate() {
		if (saturation <= 0) {
	        long worldtime = worldObj.getWorldTime();
	        // Hunger goes down half as much during night
	        if (worldtime < 14000 || rand.nextBoolean()) { 
	        	fullness--;
	        }
		} else {
			saturation--;
		}
        if (this.worldObj.isRemote) {
            grassEatingTimer = Math.max(0, grassEatingTimer - 1);
        }
        
        if (fullness > 26000 && worldObj.rand.nextInt(900) == 0) {
        	System.out.println("can breed");
        	setInLove(null);
        }

		super.onLivingUpdate();
	}

	@Override
	public boolean isHungry() {
		return fullness < 24000;
	}


	@Override
	public boolean isVeryHungry() {
		return fullness < 18000;
	}


	@Override
	public void didEatGrass() {
		fullness += 1000;
		saturation = 1000;
	}


}

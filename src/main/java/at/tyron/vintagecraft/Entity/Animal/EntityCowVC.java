package at.tyron.vintagecraft.Entity.Animal;

import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCowVC extends EntityCow {
	int grassEatingTimer;
	private EntityAIEatGrass entityAIEatGrass = new EntityAIEatGrass(this);
	
	public EntityCowVC(World worldIn) {
		super(worldIn);
	}

	
	protected void updateAITasks() {
        this.grassEatingTimer = this.entityAIEatGrass.getEatingGrassTimer();
        super.updateAITasks();
    }
	
	@SideOnly(Side.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_) {
		return this.grassEatingTimer <= 0 ? 0.0F : (this.grassEatingTimer >= 4 && this.grassEatingTimer <= 36 ? 1.0F : (this.grassEatingTimer < 4 ? ((float)this.grassEatingTimer - p_70894_1_) / 4.0F : -((float)(this.grassEatingTimer - 40) - p_70894_1_) / 4.0F));
	}

	@SideOnly(Side.CLIENT)
	public float getHeadRotationAngleX(float p_70890_1_) {
		if (this.grassEatingTimer > 4 && this.grassEatingTimer <= 36) {
			float f1 = ((float)(this.grassEatingTimer - 4) - p_70890_1_) / 32.0F;
			return ((float)Math.PI / 5F) + ((float)Math.PI * 7F / 100F) * MathHelper.sin(f1 * 28.7F);
		} else {
			return this.grassEatingTimer > 0 ? ((float)Math.PI / 5F) : this.rotationPitch / (180F / (float)Math.PI);
		}
	}

}

package at.tyron.vintagecraft.Client.Render.Model;

import at.tyron.vintagecraft.Entity.Animal.EntityCowVC;
import net.minecraft.client.model.ModelCow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;

public class ModelCowVC extends ModelCow {
    private float rotationAngleX;

	public ModelCowVC() {
		super();
	}
	
    public void setLivingAnimations(EntityLivingBase entity, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
        super.setLivingAnimations(entity, p_78086_2_, p_78086_3_, p_78086_4_);
        this.head.rotationPointY = 4.0F + ((EntityCowVC)entity).getHeadRotationPointY(p_78086_4_) * 9.0F;
        this.rotationAngleX = ((EntityCowVC)entity).getHeadRotationAngleX(p_78086_4_);
    }

    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
        this.head.rotateAngleX = rotationAngleX;
    }

}

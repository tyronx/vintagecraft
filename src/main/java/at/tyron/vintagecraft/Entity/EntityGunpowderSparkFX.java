package at.tyron.vintagecraft.Entity;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.SideOnly;



public class EntityGunpowderSparkFX extends EntityFX {

    public EntityGunpowderSparkFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
    	super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
    	particleMaxAge /= 4;
    	this.particleBlue = 0.5f;
    }
    	
	protected EntityGunpowderSparkFX(World worldIn, double lastTickPosX, double lastTickPosY, double lastTickPosZ) {
		super(worldIn, lastTickPosX, lastTickPosY, lastTickPosZ);
	}
	
	

    public int getBrightnessForRender(float par1) {
        float f1 = 1.0F;

        int i = super.getBrightnessForRender(par1);
        int j = i & 255;
        int k = i >> 16 & 255;
        j += (int)(f1 * 15.0F * 16.0F);

        if (j > 240)
        {
            j = 240;
        }

        return j | k << 16;
    }

    public float getBrightness(float par1) {
        float f1 = super.getBrightness(par1);
        float f2 = (float)this.particleAge / (float)this.particleMaxAge;
        f2 = f2 * f2 * f2 * f2;
        return f1 * (1.0F - f2) + f2;
    }
    
    
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        float f = (float)this.particleAge / (float)this.particleMaxAge;
        float f1 = f;
        f = -f + f * f * 2.0F;
        f = 1.0F - f;
        
        this.motionY -= 0.001D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9990000128746033D;
        this.motionY *= 0.9990000128746033D;
        this.motionZ *= 0.9990000128746033D;

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        
        
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
    }
    
}

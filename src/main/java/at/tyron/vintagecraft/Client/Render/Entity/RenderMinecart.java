package at.tyron.vintagecraft.Client.Render.Entity;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Model.ModelMinecartVC;
import at.tyron.vintagecraft.Client.Render.Math.Matrix4f;
import at.tyron.vintagecraft.Client.Render.Math.Quaternion;
import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecartVC;
import at.tyron.vintagecraft.Entity.EntityMinecartVC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecart.EnumMinecartType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderMinecart extends Render {
	static final ResourceLocation minecartTextures = new ResourceLocation("vintagecraft:textures/entity/minecart.png");
	
	ModelMinecartVC modelMinecart = new ModelMinecartVC();
	

	public RenderMinecart(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5F;
	}
	
	
	
	
    public void doRender(EntityMinecartVC entity, double x, double y, double z, float rotationYaw, float partialTicks) {
    	/*if (entity.refreshModel) {
    		modelMinecart.initComponents();
    		entity.refreshModel = false;
    	}*/
    	
    	
    	

        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        long i = (long)entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f2 = (((float)(i >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f3 = (((float)(i >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float f4 = (((float)(i >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GlStateManager.translate(f2, f3, f4);
        double d3 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
        double d4 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
        double d5 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
        double d6 = 0.30000001192092896D;
        Vec3 vec3 = entity.func_70489_a(d3, d4, d5);
        float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;

        if (vec3 != null)
        {
            Vec3 vec31 = entity.func_70495_a(d3, d4, d5, d6);
            Vec3 vec32 = entity.func_70495_a(d3, d4, d5, -d6);

            if (vec31 == null)
            {
                vec31 = vec3;
            }

            if (vec32 == null)
            {
                vec32 = vec3;
            }

            x += vec3.xCoord - d3;
            y += (vec31.yCoord + vec32.yCoord) / 2.0D - d4;
            z += vec3.zCoord - d5;
            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);

            if (vec33.lengthVector() != 0.0D) {
                vec33 = vec33.normalize();
                rotationYaw = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
                f5 = (float)(Math.atan(vec33.yCoord) * 73.0D);
            }
        }
        
        


        GlStateManager.translate((float)x, (float)y + 0.375F, (float)z);
        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-f5, 0.0F, 0.0F, 1.0F);
        float f7 = (float)entity.getRollingAmplitude() - partialTicks;
        float f8 = entity.getDamage() - partialTicks;

        if (f8 < 0.0F) {
            f8 = 0.0F;
        }

        if (f7 > 0.0F) {
            GlStateManager.rotate(MathHelper.sin(f7) * f7 * f8 / 10.0F * (float)entity.getRollingDirection(), 1.0F, 0.0F, 0.0F);
        }

        int j = entity.getDisplayTileOffset();
        IBlockState iblockstate = entity.getDisplayTile();

       

        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        
        if (entity.getMinecartType() == EnumMinecartType.FURNACE) {
        	modelMinecart.renderCoalPoweredCart(0.0625F);        	
        } else {
        	modelMinecart.renderEmptyCart(0.0625F);
        }
        
        
        GlStateManager.popMatrix();        
        
		/*EntityPlayer player = Minecraft.getMinecraft().thePlayer;
       
		double playerX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks; 
		double playerY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
		double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
		
        double entityX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks; 
        double entityY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25f;
        double entityZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        
        
		WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
		worldrenderer.startDrawing(GL11.GL_LINES);
		GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glLineWidth(2f);
			GL11.glColor3f(1, 1, 1);
			GL11.glTranslated(entityX-playerX, entityY-playerY, entityZ-playerZ);
			worldrenderer.addVertexWithUV(0, 0, 0, 0, 0);
			worldrenderer.addVertexWithUV(20 * entity.motionX, 20 * entity.motionY, 20 * entity.motionZ, 1, 1);
			
			Tessellator.getInstance().draw();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		*/
		
     //   super.doRender(entity, x, y, z, rotationYaw, partialTicks);
    }
	
	
	
	
	
	
	
	
	

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return minecartTextures;
	}

	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float rotationYaw, float partialTicks) {
		this.doRender((EntityMinecartVC)entity, x, y, z, rotationYaw, partialTicks);
	}
}

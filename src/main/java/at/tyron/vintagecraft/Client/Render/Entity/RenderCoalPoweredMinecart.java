package at.tyron.vintagecraft.Client.Render.Entity;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Model.ModelCoalPoweredMinecart;
import at.tyron.vintagecraft.Client.Render.Math.Matrix4f;
import at.tyron.vintagecraft.Client.Render.Math.Quaternion;
import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecart;
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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderCoalPoweredMinecart extends Render {
	static final ResourceLocation minecartTextures = new ResourceLocation("vintagecraft:textures/entity/minecart.png");
	
	ModelCoalPoweredMinecart modelMinecart = new ModelCoalPoweredMinecart();
	

	public RenderCoalPoweredMinecart(RenderManager renderManager) {
		super(renderManager);
		this.shadowSize = 0.5F;
	}
	
	
	
	
    public void doRender(EntityCoalPoweredMinecart entity, double x, double y, double z, float rotationYaw, float partialTicks) {
    	if (entity.refreshModel) {
    		modelMinecart.initComponents();
    		entity.refreshModel = false;
    	}
    	
    	
    	
        GlStateManager.pushMatrix();
        
	        this.bindEntityTexture(entity);
	        long entityid = (long)entity.getEntityId() * 493286711L;
	        entityid = entityid * entityid * 4392167121L + entityid * 98761L;
	        float xOffset = (((float)(entityid >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
	        float yOffset = (((float)(entityid >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
	        float zOffset = (((float)(entityid >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
	        GlStateManager.translate(xOffset, yOffset, zOffset);
	        
	        double dx = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
	        double dy = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
	        double dz = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
	        
	        double d6 = 0.30000001192092896D;
	        Vec3 vec3 = entity.getRailDirectionVector(dx, dy, dz);
	        float zAngle = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
	
	        //System.out.println(zAngle + " / " + rotationYaw);
	        
	        if (vec3 != null) {
	            Vec3 vec31 = entity.getClientSideRailDirectionVector(dx, dy, dz, d6);
	            Vec3 vec32 = entity.getClientSideRailDirectionVector(dx, dy, dz, -d6);
	
	            if (vec31 == null) {
	                vec31 = vec3;
	            }
	
	            if (vec32 == null) {
	                vec32 = vec3;
	            }
	
	            x += vec3.xCoord - dx;
	            y += (vec31.yCoord + vec32.yCoord) / 2.0D - dy;
	            z += vec3.zCoord - dz;
	            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);
	
	            if (vec33.lengthVector() != 0.0D) {
	                vec33 = vec33.normalize();
	                rotationYaw = (float)(Math.atan2(vec33.zCoord, vec33.xCoord) * 180.0D / Math.PI);
	                
	                zAngle = (float)(Math.atan(vec33.yCoord) * 73.0D);
	            }
	        }
	
	        GlStateManager.translate((float)x, (float)y + 0.375F, (float)z);
	        
	        float xAngle = (float)entity.getRollingAmplitude() - partialTicks;
	        float shake = entity.getDamage() - partialTicks;
	        float val = 0;
	        if (shake < 0.0F) {
	            shake = 0.0F;
	        }
	
	        if (xAngle > 0.0F) {
	        	val = MathHelper.sin(xAngle) * xAngle * shake / 10.0F * (float)entity.getRollingDirection();
	        }
	        
	        
	
	      //  System.out.println(rotationYaw);
	        
			Quaternion q = new Quaternion(); // Identity
			
			q.eulerToQuat(val, 180.0F - rotationYaw, -zAngle);
			
			Matrix4f mat = new Matrix4f();
			q.createMatrix(mat);
	
			FloatBuffer buf = BufferUtils.createFloatBuffer(16).put(mat.matrix);
			buf.rewind();
					
			GL11.glMultMatrix(buf);		
	//        GlStateManager.rotate(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
	//        GlStateManager.rotate(-zAngle, 0.0F, 0.0F, 1.0F);
	
	        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
	        this.modelMinecart.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, (float) (entity.motionX * entity.motionX + entity.motionZ * entity.motionZ), 0.0625F);
        GlStateManager.popMatrix();
        
        
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
       
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

		
        super.doRender(entity, x, y, z, rotationYaw, partialTicks);
    }
	
	
	
	
	
	
	
	
	

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return minecartTextures;
	}

	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float rotationYaw, float partialTicks) {
		this.doRender((EntityCoalPoweredMinecart)entity, x, y, z, rotationYaw, partialTicks);
	}
}

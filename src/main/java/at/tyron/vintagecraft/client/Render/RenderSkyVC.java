package at.tyron.vintagecraft.Client.Render;

import java.util.ArrayList;
import java.util.Vector;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.VintageCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;

public class RenderSkyVC extends IRenderHandler {
    private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
    public static final ResourceLocation shootingStarPng = new ResourceLocation("vintagecraft:textures/environment/shootingstar.png");

    public static volatile ArrayList<ShootingStar> shootingStars = new ArrayList<ShootingStar>();
    
    private static final ResourceLocation[] nightSkies = new ResourceLocation[]{
    	new ResourceLocation("vintagecraft:textures/environment/sky1.png"),
    	new ResourceLocation("vintagecraft:textures/environment/sky2.png"),
    	new ResourceLocation("vintagecraft:textures/environment/sky3.png"),
    	new ResourceLocation("vintagecraft:textures/environment/sky4.png")
    };
    
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private VertexFormat vertexBufferFormat;

    boolean vboEnabled;
    /** The star GL Call list */
    private int starGLCallList = -1;
    /** OpenGL sky list */
    private int glSkyList = -1;
    /** OpenGL sky list 2 */
    private int glSkyList2 = -1;
    /** A reference to the Minecraft object. */
    private final Minecraft mc;

    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    private VertexBuffer starVBO;

    WorldClient world;
    
	public RenderSkyVC() {
		Minecraft mcIn = Minecraft.getMinecraft();
		world = mcIn.theWorld;
		
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.getTextureManager();
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GlStateManager.bindTexture(0);
        
        this.vboEnabled = OpenGlHelper.useVbo();

        this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateSky();
        this.generateSky2();		
	}
    
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		GlStateManager.pushMatrix();
			GlStateManager.color(1f, 1f, 1f, 0.5f);
			renderSkyVanillaStyle(partialTicks, 2);
		GlStateManager.popMatrix();
	}
	
	
	
	public void renderSkyImage(ResourceLocation sky) {
//        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
    //    GlStateManager.depthMask(false);
        this.renderEngine.bindTexture(sky);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        
        
        if (mc.isFancyGraphicsEnabled()) {
        	/*GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_LINE);
            GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_LINE);*/
        	drawSphere(200F, 40, 40);
        	/*GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
            GL11.glPolygonMode(GL11.GL_BACK, GL11.GL_FILL);*/
        } else {
	        for (int i = 0; i < 6; ++i) {
	            GlStateManager.pushMatrix();
	
	            if (i == 1) {
	                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
	            }
	
	            if (i == 2)
	            {
	                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
	            }
	
	            if (i == 3)
	            {
	                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
	            }
	
	            if (i == 4)
	            {
	                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
	            }
	
	            if (i == 5)
	            {
	                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
	            }
	            
	            	
            	worldrenderer.startDrawingQuads();
  				worldrenderer.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
  				worldrenderer.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 1.0D);
				worldrenderer.addVertexWithUV(100.0D, -100.0D, 100.0D, 1.0D, 1.0D);
  				worldrenderer.addVertexWithUV(100.0D, -100.0D, -100.0D, 1.0D, 0.0D);

  				tessellator.draw();
	            
	            GlStateManager.popMatrix();
	        }
		}

       // GlStateManager.depthMask(true);
        
        //GlStateManager.enableAlpha();
	}
	
	
	public void renderSkyVanillaStyle(float partialTicks, int pass) {
		
        GlStateManager.disableTexture2D();
        Vec3 vec3 = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
        float f1 = (float)vec3.xCoord;
        float f2 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;

        if (pass != 2)
        {
            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        GlStateManager.color(f1, f2, f3);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        GlStateManager.color(f1, f2, f3);

        
        /* Sky colors */
        if (vboEnabled) {
            this.skyVBO.bindBuffer();
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
            this.skyVBO.drawArrays(7);
            this.skyVBO.unbindBuffer();
            GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        } else {
            GlStateManager.callList(this.glSkyList);
        }

        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        float[] afloat = world.provider.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;

        if (afloat != null) {
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            f7 = afloat[0];
            f8 = afloat[1];
            f9 = afloat[2];
            float f12;

            if (pass != 2)
            {
                f10 = (f7 * 30.0F + f8 * 59.0F + f9 * 11.0F) / 100.0F;
                f11 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
                f12 = (f7 * 30.0F + f9 * 70.0F) / 100.0F;
                f7 = f10;
                f8 = f11;
                f9 = f12;
            }

            worldrenderer.startDrawing(6);
            worldrenderer.setColorRGBA_F(f7, f8, f9, afloat[3]);
            worldrenderer.addVertex(0.0D, 100.0D, 0.0D);
            boolean flag = true;
            worldrenderer.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0F);

            for (int j = 0; j <= 16; ++j)
            {
                f12 = (float)j * (float)Math.PI * 2.0F / 16.0F;
                float f13 = MathHelper.sin(f12);
                float f14 = MathHelper.cos(f12);
                worldrenderer.addVertex((double)(f13 * 120.0F), (double)(f14 * 120.0F), (double)(-f14 * 40.0F * afloat[3]));
            }

            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        GlStateManager.pushMatrix();
        f7 = 1.0F - world.getRainStrength(partialTicks);
        f8 = 0.0F;
        f9 = 0.0F;
        f10 = 0.0F;
        GlStateManager.color(1.0F, 1.0F, 1.0F, f7);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        f11 = 30.0F;
        this.renderEngine.bindTexture(locationSunPng);
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(-f11), 100.0D, (double)(-f11), 0.0D, 0.0D);
        worldrenderer.addVertexWithUV((double)f11, 100.0D, (double)(-f11), 1.0D, 0.0D);
        worldrenderer.addVertexWithUV((double)f11, 100.0D, (double)f11, 1.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)(-f11), 100.0D, (double)f11, 0.0D, 1.0D);
        tessellator.draw();
        f11 = 20.0F;
        this.renderEngine.bindTexture(locationMoonPhasesPng);
        int k = world.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f15 = (float)(l + 0) / 4.0F;
        float f16 = (float)(i1 + 0) / 2.0F;
        float f17 = (float)(l + 1) / 4.0F;
        float f18 = (float)(i1 + 1) / 2.0F;
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(-f11), -100.0D, (double)f11, (double)f17, (double)f18);
        worldrenderer.addVertexWithUV((double)f11, -100.0D, (double)f11, (double)f15, (double)f18);
        worldrenderer.addVertexWithUV((double)f11, -100.0D, (double)(-f11), (double)f15, (double)f16);
        worldrenderer.addVertexWithUV((double)(-f11), -100.0D, (double)(-f11), (double)f17, (double)f16);
        tessellator.draw();
        
      //  GlStateManager.disableTexture2D();
       // float f19 = world.getStarBrightness(partialTicks) * f7;

        /*if (f19 > 0.0F)
        {
            GlStateManager.color(f19, f19, f19, f19);

            if (this.vboEnabled)
            {
                this.starVBO.bindBuffer();
                GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                this.starVBO.drawArrays(7);
                this.starVBO.unbindBuffer();
                GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            }
            else
            {
                GlStateManager.callList(this.starGLCallList);
            }
        }*/
        GlStateManager.popMatrix();
        
        
		GlStateManager.enableBlend();
		GlStateManager.pushMatrix();
//			System.out.println((worldtime / 3f) % 360f);
			
			
			
			
			// sunrise about 22.000    lighter beyond
			// sunset about 14.000     darker beyond that
			long worldtime = world.getWorldTime() % 24000;
			
			float nightSkyopacity = Math.min(0.8f, Math.max(0f, 
				// Rising opacity during sunset
				Math.min(2000, Math.max(0, worldtime - 13000)) / 2000f -
				// Falling opacity during sunrise
				Math.min(2000, Math.max(0, worldtime - 21000)) / 2000f
			));
			

			if (nightSkyopacity > 0.01f) {
				GlStateManager.pushMatrix();
					GlStateManager.rotate((worldtime / 133f) % 360f, 0.5f, 0.5f, 0f);
					GlStateManager.color(1.0F, 1.0F, 1.0F, nightSkyopacity);
				
					renderSkyImage(nightSkies[VintageCraft.instance.proxy.getNightSkyType()]);
				GlStateManager.popMatrix();
			}
		
		
        GlStateManager.enableAlpha();
		
        if (nightSkyopacity > 0.01f) {
			GlStateManager.pushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GlStateManager.color(1.0F, 1.0F, 1.0F, nightSkyopacity);
				for (ShootingStar shootingstar : shootingStars) {
					shootingstar.render();
				}
				GL11.glEnable(GL11.GL_TEXTURE_2D);
			GlStateManager.popMatrix();
        }		
		

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        double d0 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - world.getHorizon();

        if (d0 < 0.0D)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 12.0F, 0.0F);

            if (this.vboEnabled)
            {
                this.sky2VBO.bindBuffer();
                GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                this.sky2VBO.drawArrays(7);
                this.sky2VBO.unbindBuffer();
                GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            }
            else
            {
                GlStateManager.callList(this.glSkyList2);
            }

            GlStateManager.popMatrix();
            f9 = 1.0F;
            f10 = -((float)(d0 + 65.0D));
            f11 = -1.0F;
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA_I(0, 255);
            worldrenderer.addVertex(-1.0D, (double)f10, 1.0D);
            worldrenderer.addVertex(1.0D, (double)f10, 1.0D);
            worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
            worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
            worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
            worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
            worldrenderer.addVertex(1.0D, (double)f10, -1.0D);
            worldrenderer.addVertex(-1.0D, (double)f10, -1.0D);
            worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
            worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
            worldrenderer.addVertex(1.0D, (double)f10, 1.0D);
            worldrenderer.addVertex(1.0D, (double)f10, -1.0D);
            worldrenderer.addVertex(-1.0D, (double)f10, -1.0D);
            worldrenderer.addVertex(-1.0D, (double)f10, 1.0D);
            worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
            worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
            worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
            worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
            worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
            worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
            tessellator.draw();
        }

        if (world.provider.isSkyColored()) {
            GlStateManager.color(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
        }
        else {
            GlStateManager.color(f1, f2, f3);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, -((float)(d0 - 16.0D)), 0.0F);
        GlStateManager.callList(this.glSkyList2);
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        
    }

	
    private void generateSky() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.skyVBO != null) {
            this.skyVBO.deleteGlBuffers();
        }

        if (this.glSkyList >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }

        if (this.vboEnabled) {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, 16.0F, false);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.skyVBO.bufferData(worldrenderer.getByteBuffer(), worldrenderer.getByteIndex());
        }
        else {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
            this.renderSky(worldrenderer, 16.0F, false);
            tessellator.draw();
            GL11.glEndList();
        }
    }
    
    
    private void generateSky2() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.sky2VBO != null) {
            this.sky2VBO.deleteGlBuffers();
        }

        if (this.glSkyList2 >= 0) {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }

        if (this.vboEnabled) {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, -16.0F, true);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.sky2VBO.bufferData(worldrenderer.getByteBuffer(), worldrenderer.getByteIndex());
        }
        else {
            this.glSkyList2 = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
            this.renderSky(worldrenderer, -16.0F, true);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    
    private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_) {
        boolean flag1 = true;
        boolean flag2 = true;
        worldRendererIn.startDrawingQuads();

        for (int i = -384; i <= 384; i += 64) {
            for (int j = -384; j <= 384; j += 64) {
                float f1 = (float)i;
                float f2 = (float)(i + 64);

                if (p_174968_3_) {
                    f2 = (float)i;
                    f1 = (float)(i + 64);
                }

                worldRendererIn.addVertex((double)f1, (double)p_174968_2_, (double)j);
                worldRendererIn.addVertex((double)f2, (double)p_174968_2_, (double)j);
                worldRendererIn.addVertex((double)f2, (double)p_174968_2_, (double)(j + 64));
                worldRendererIn.addVertex((double)f1, (double)p_174968_2_, (double)(j + 64));
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    public void drawSphere(float radius, int slices, int stacks) {
    	//Icosahedron.drawIcosahedron(1, radius);
    	
    	float textureScale = 2f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        
        double stack = ((2*Math.PI) / stacks);
        double slice = ((2*Math.PI) / slices);
        
        for (double theta = 0; theta < 2 * Math.PI; theta += stack) {
            for (double phi = 0; phi < 2 * Math.PI; phi += slice) {
            	Vec3 p1 = getPoints(phi, theta, radius);
                Vec3 p2 = getPoints(phi + slice, theta, radius);
                
                Vec3 p3 = getPoints(phi + slice, theta + stack, radius);
                Vec3 p4 = getPoints(phi, theta + stack, radius);
                
                double t0 = textureScale * phi / (2 * Math.PI);
                double t1 = textureScale * (phi + slice) / (2 * Math.PI);
                double s0 = textureScale * theta / (2 * Math.PI);
                double s1 = textureScale * (theta + stack) / (2 * Math.PI);
                
                /*if (Math.abs(theta - Math.PI) < 0.5 || Math.abs(phi - Math.PI) < 0.5) {
                	t0 = t0/2;
                	t1 = t1/2;
                	s0 = s0/2;
                	s1 = s1/2;
                }*/
                
                worldrenderer.addVertexWithUV(p1.xCoord, p1.yCoord, p1.zCoord, t0, s0);   // bottom left
                worldrenderer.addVertexWithUV(p2.xCoord, p2.yCoord, p2.zCoord, t1, s0);   // top left
                worldrenderer.addVertexWithUV(p3.xCoord, p3.yCoord, p3.zCoord, t1, s1);   // top right
                worldrenderer.addVertexWithUV(p4.xCoord, p4.yCoord, p4.zCoord, t0, s1);   // bottom right
            }
        }
        
        tessellator.draw();
    }

    
    Vec3 getPoints(double phi, double theta, double radius) {
    	double x = radius * Math.cos(theta) * Math.sin(phi);
    	double y = radius * Math.sin(theta) * Math.sin(phi);
    	double z = radius * Math.cos(phi);
        return new Vec3(x, y, z);
    }
    
    
    
    

}

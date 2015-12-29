package at.tyron.vintagecraft.Client.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class RenderCloudsVC extends IRenderHandler {
    private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");

    private final Minecraft mc;
    WorldClient world;
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    
    
    /** counts the cloud render updates. Used with mod to stagger some updates */
    private int cloudTickCounter;

    
    public void updateClouds() {
        ++this.cloudTickCounter;
    }
    
    @SubscribeEvent
    public void clientTicket(TickEvent.ClientTickEvent evt) {
    	updateClouds();
    	
    }

    
    public RenderCloudsVC() {
		Minecraft mcIn = Minecraft.getMinecraft();
		world = mcIn.theWorld;
		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.getTextureManager();
        
	}
    
	@Override
	public void render(float partialTicks, WorldClient world, Minecraft mc) {
		renderVanillaClouds(partialTicks, 2);
	}	

	
    public void renderVanillaClouds(float partialTicks, int pass) {
    	
        if (this.mc.theWorld.provider.isSurfaceWorld()) {
            if (this.mc.gameSettings.fancyGraphics) {
                this.renderCloudsFancy(partialTicks, pass);
                
            }
            else {
            	
                GlStateManager.disableCull();
                float f1 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);

            
                this.renderEngine.bindTexture(locationCloudsPng);
                GlStateManager.enableBlend();
                // 770 is 302 hex
                // 771 is 303 hex
                //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Vec3 vec3 = world.getCloudColour(partialTicks);
                float x = (float)vec3.xCoord;
                float y = (float)vec3.yCoord;
                float z = (float)vec3.zCoord;
                float f5;

                if (pass != 2) {
                    f5 = (x * 30.0F + y * 59.0F + z * 11.0F) / 100.0F;
                    float f6 = (x * 30.0F + y * 70.0F) / 100.0F;
                    float f7 = (x * 30.0F + z * 70.0F) / 100.0F;
                    x = f5;
                    y = f6;
                    z = f7;
                }

                f5 = 4.8828125E-4F;
                double cloudPos = (double)((float)this.cloudTickCounter + partialTicks);
                double d0 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)partialTicks + cloudPos * 0.029999999329447746D;
                double d1 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)partialTicks;
                int j = MathHelper.floor_double(d0 / 2048.0D);
                int k = MathHelper.floor_double(d1 / 2048.0D);
                d0 -= (double)(j * 2048);
                d1 -= (double)(k * 2048);
                float yCoord = world.provider.getCloudHeight() - f1 + 0.33F;
                float texOffsetX = (float)(d0 * 4.8828125E-4D);
                float textOffsetY = (float)(d1 * 4.8828125E-4D);
                RenderUtils.startDrawingQuads();
//                RenderUtils.setColorRGBA(x,y,z,1);
//                worldrenderer.setColorRGBA_F(x, y, z, 1F);
            
                int xCoord = -1024;
                int zCoord = -1024;
                int sizeX = 2048;
                int sizeZ = 2048;
                RenderUtils.addVertexWithUV(xCoord + 0, yCoord, zCoord + sizeZ, (xCoord + 0) * 4.8828125E-4F + texOffsetX, (zCoord + sizeZ) * 4.8828125E-4F + textOffsetY);
                RenderUtils.addVertexWithUV(xCoord + sizeX, yCoord, zCoord + sizeZ, (xCoord + sizeX) * 4.8828125E-4F + texOffsetX, (zCoord + sizeZ) * 4.8828125E-4F + textOffsetY);
                RenderUtils.addVertexWithUV(xCoord + sizeX, yCoord, zCoord + 0, (xCoord + sizeX) * 4.8828125E-4F + texOffsetX, (zCoord + 0) * 4.8828125E-4F + textOffsetY);
                RenderUtils.addVertexWithUV(xCoord + 0,     yCoord, zCoord + 0, (xCoord + 0) * 4.8828125E-4F + texOffsetX, (zCoord + 0) * 4.8828125E-4F + textOffsetY);

                
                for ( xCoord = -512; xCoord < 512; xCoord += 32) {
                   for ( zCoord = -512; zCoord < 512; zCoord += 32) {
                    	
                	   RenderUtils.addVertexWithUV((double)(xCoord + 0), (double)yCoord, (double)(zCoord + 32), (double)((float)(xCoord + 0) * 4.8828125E-4F + texOffsetX), (double)((float)(zCoord + 32) * 4.8828125E-4F + textOffsetY));
                	   RenderUtils.addVertexWithUV((double)(xCoord + 32), (double)yCoord, (double)(zCoord + 32), (double)((float)(xCoord + 32) * 4.8828125E-4F + texOffsetX), (double)((float)(zCoord + 32) * 4.8828125E-4F + textOffsetY));
                	   RenderUtils.addVertexWithUV((double)(xCoord + 32), (double)yCoord, (double)(zCoord + 0), (double)((float)(xCoord + 32) * 4.8828125E-4F + texOffsetX), (double)((float)(zCoord + 0) * 4.8828125E-4F + textOffsetY));
                	   RenderUtils.addVertexWithUV((double)(xCoord + 0), (double)yCoord, (double)(zCoord + 0), (double)((float)(xCoord + 0) * 4.8828125E-4F + texOffsetX), (double)((float)(zCoord + 0) * 4.8828125E-4F + textOffsetY));
                    }
                }

                RenderUtils.tessellator.draw();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
            }
        }
    }


    private void renderCloudsFancy(float partialTicks, int pass) {
        GlStateManager.disableCull();
        float f1 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)partialTicks);
        
        
        float f2 = 12.0F;
        float f3 = 4.0F;
        double d0 = (double)((float)this.cloudTickCounter + partialTicks);
        double d1 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)partialTicks + d0 * 0.029999999329447746D) / 12.0D;
        double d2 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)partialTicks) / 12.0D + 0.33000001311302185D;
        float yCoord = world.provider.getCloudHeight() - f1 + 0.33F;
        int j = MathHelper.floor_double(d1 / 2048.0D);
        int k = MathHelper.floor_double(d2 / 2048.0D);
        d1 -= (double)(j * 2048);
        d2 -= (double)(k * 2048);
        this.renderEngine.bindTexture(locationCloudsPng);
        GlStateManager.enableBlend();
        
        /*
         * 		GL_ZERO = 0x0,
		GL_ONE = 0x1,
		GL_SRC_COLOR = 0x300,
		GL_ONE_MINUS_SRC_COLOR = 0x301,
		GL_SRC_ALPHA = 0x302,
		GL_ONE_MINUS_SRC_ALPHA = 0x303,
		GL_DST_ALPHA = 0x304,
		GL_ONE_MINUS_DST_ALPHA = 0x305,
		GL_DST_COLOR = 0x306,
		GL_ONE_MINUS_DST_COLOR = 0x307,
		GL_SRC_ALPHA_SATURATE = 0x308,
		GL_CONSTANT_COLOR = 0x8001,
		GL_ONE_MINUS_CONSTANT_COLOR = 0x8002,
		GL_CONSTANT_ALPHA = 0x8003,
		GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004,
         */
        
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        //GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_SRC_COLOR, 1, 0);
        Vec3 vec3 = world.getCloudColour(partialTicks);
        float x = (float)vec3.xCoord;
        float y = (float)vec3.yCoord;
        float z = (float)vec3.zCoord;
        float f8;
        float texOffsetX;
        float texOffsetY;

        if (pass != 2) {
            f8 = (x * 30.0F + y * 59.0F + z * 11.0F) / 100.0F;
            texOffsetX = (x * 30.0F + y * 70.0F) / 100.0F;
            texOffsetY = (x * 30.0F + z * 70.0F) / 100.0F;
            x = f8;
            y = texOffsetX;
            z = texOffsetY;
        }

        f8 = 0.00390625F;
        texOffsetX = (float)MathHelper.floor_double(d1) * 0.00390625F;
        texOffsetY = (float)MathHelper.floor_double(d2) * 0.00390625F;
        float f11 = (float)(d1 - (double)MathHelper.floor_double(d1));
        float f12 = (float)(d2 - (double)MathHelper.floor_double(d2));
        boolean flag = true;
        boolean flag1 = true;
        float f13 = 9.765625E-4F;
        GlStateManager.scale(12.0F, 1.0F, 12.0F);

        for (int l = 0; l < 2; ++l) {
            if (l == 0) {
                GlStateManager.colorMask(false, false, false, false);
            }
            else {
                switch (pass) {
                    case 0:
                        GlStateManager.colorMask(false, true, true, true);
                        break;
                    case 1:
                        GlStateManager.colorMask(true, false, false, true);
                        break;
                    case 2:
                        GlStateManager.colorMask(true, true, true, true);
                }
            }

            //System.out.println(VCraftWorld.instance.isCloudAt(14, 50));
            
            for (int i1 = -7; i1 <= 8; ++i1) {
                for (int j1 = -7; j1 <= 8; ++j1) {
                	 RenderUtils.startDrawingQuads();
                    
                    float texPosX = (float)(i1 * 8);
                    float texPosY = (float)(j1 * 8);
                    float xCoord = texPosX - f11;
                    float zCoord = texPosY - f12;


                    if (yCoord > -5.0F) {

                    	RenderUtils.worldrenderer.color(x * 0.7F, y * 0.7F, z * 0.7F, 1F);
                    	RenderUtils.worldrenderer.normal(0.0F, -1.0F, 0.0F);
                    	RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 0.0F, zCoord + 8.0F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                    	RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 0.0F, zCoord + 8.0F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                    	RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 0.0F, zCoord + 0.0F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                    	RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 0.0F, zCoord + 0.0F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                    }

                    if (yCoord <= 5.0F) {
                    	
                    	RenderUtils.worldrenderer.color(x, y, z, 1F);
                    	RenderUtils.worldrenderer.normal(0.0F, 1.0F, 0.0F);
                    	RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 4.0F - 9.765625E-4F, zCoord + 8.0F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                    	RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 4.0F - 9.765625E-4F, zCoord + 8.0F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                    	RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 4.0F - 9.765625E-4F, zCoord + 0.0F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                    	RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 4.0F - 9.765625E-4F, zCoord + 0.0F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                    }

                   RenderUtils.worldrenderer.color(x * 0.9F, y * 0.9F, z * 0.9F, 1F);
                    int k1;

                    if (i1 > -1) {
                        RenderUtils.worldrenderer.normal(-1.0F, 0.0F, 0.0F);

                        for (k1 = 0; k1 < 8; ++k1) {
                        	RenderUtils.addVertexWithUV(xCoord + k1 + 0.0F, yCoord + 0.0F, zCoord + 8.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                        	RenderUtils.addVertexWithUV(xCoord + k1 + 0.0F, yCoord + 4.0F, zCoord + 8.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                        	RenderUtils.addVertexWithUV(xCoord + k1 + 0.0F, yCoord + 4.0F, zCoord + 0.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                        	RenderUtils.addVertexWithUV(xCoord + k1 + 0.0F, yCoord + 0.0F, zCoord + 0.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                        }
                    }

                    if (i1 <= 1) {
                        RenderUtils.worldrenderer.normal(1.0F, 0.0F, 0.0F);

                        for (k1 = 0; k1 < 8; ++k1) {
                        	RenderUtils.addVertexWithUV(xCoord + k1 + 1.0F - 9.765625E-4F, yCoord + 0.0F, zCoord + 8.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                        	RenderUtils.addVertexWithUV(xCoord + k1 + 1.0F - 9.765625E-4F, yCoord + 4.0F, zCoord + 8.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 8.0F) * 0.00390625F + texOffsetY));
                            RenderUtils.addVertexWithUV(xCoord + k1 + 1.0F - 9.765625E-4F, yCoord + 4.0F, zCoord + 0.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                            RenderUtils.addVertexWithUV(xCoord + k1 + 1.0F - 9.765625E-4F, yCoord + 0.0F, zCoord + 0.0F, (texPosX + k1 + 0.5F) * 0.00390625F + texOffsetX, ((texPosY + 0.0F) * 0.00390625F + texOffsetY));
                        }
                    }

                    RenderUtils.worldrenderer.color(x * 0.8F, y * 0.8F, z * 0.8F, 1F);

                    if (j1 > -1) {
                         RenderUtils.worldrenderer.normal(0.0F, 0.0F, -1.0F);

                        for (k1 = 0; k1 < 8; ++k1) {
                        	RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 4.0F, zCoord + k1 + 0.0F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                        	RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 4.0F, zCoord + k1 + 0.0F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                        	RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 0.0F, zCoord + k1 + 0.0F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                            RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 0.0F, zCoord + k1 + 0.0F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                        }
                    }

                    if (j1 <= 1) {
                      RenderUtils.worldrenderer.normal(0.0F, 0.0F, 1.0F);

                        for (k1 = 0; k1 < 8; ++k1) {
                        	RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 4.0F, zCoord + k1 + 1.0F - 9.765625E-4F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                            RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 4.0F, zCoord + k1 + 1.0F - 9.765625E-4F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                            RenderUtils.addVertexWithUV(xCoord + 8.0F, yCoord + 0.0F, zCoord + k1 + 1.0F - 9.765625E-4F, (texPosX + 8.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                            RenderUtils.addVertexWithUV(xCoord + 0.0F, yCoord + 0.0F, zCoord + k1 + 1.0F - 9.765625E-4F, (texPosX + 0.0F) * 0.00390625F + texOffsetX, ((texPosY + k1 + 0.5F) * 0.00390625F + texOffsetY));
                        }
                    }

                    RenderUtils.tessellator.draw();
                }
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }
}

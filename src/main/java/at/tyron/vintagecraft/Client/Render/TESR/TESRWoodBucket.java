package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelBucketVC;
import at.tyron.vintagecraft.TileEntity.TEWoodBucket;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;
import at.tyron.vintagecraft.WorldProperties.EnumBucketContents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWoodBucket extends TESRBase {
	ResourceLocation ironTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/iron_darkened.png");
	ResourceLocation linenTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/linen.png");
	ResourceLocation waterTex = new ResourceLocation("textures/blocks/water_still.png");
	
	ModelBucketVC bucketmodel = new ModelBucketVC();
	

	
	void renderAt(TEWoodBucket te, float posX, float posY, float posZ, float partialTicks) {
		if (te.getWorld() == null) return;
		
		if (te.refreshModel) {
			bucketmodel.initComponents();
			te.refreshModel = false;
		}
		
		GL11.glPushMatrix();
			GL11.glTranslatef(posX, posY, posZ);
			GL11.glTranslatef(+0.5f, +0.5f, +0.5f);
			float []angles = getAnglesBetween(EnumFacing.NORTH, te.orientation);
			GL11.glRotatef(angles[0], 0, 1f, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/planks/" + te.treetype.getName() + ".png"));
			bucketmodel.renderWood();
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(linenTex);
			bucketmodel.renderRope();
			
			
			GL11.glPushMatrix();
				Minecraft.getMinecraft().getTextureManager().bindTexture(ironTex);
				for (int ring = 0; ring <= 1; ring++) {
					for (int i = 0; i < 360; i += 90) {
						GL11.glTranslatef(+0.5f, +0.5f, +0.5f);
						GL11.glRotatef(i, 0, 1f, 0);
						GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
						drawIronStripe();
					}
					GL11.glTranslatef(0, 5/16f, 0);
				}
			GL11.glPopMatrix();
			
			if (te.contents == EnumBucketContents.WATER) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(waterTex);
				drawContents(te.getWorld().getWorldTime(), 32);
			}
			
		GL11.glPopMatrix();
		
	}

	
	public void drawIronStripe() {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		worldrenderer.startDrawingQuads();
		
		float wdt = 10.2f / 16f;
		float hgt = 1f / 16f;
		float x = 2.9f / 16f;
		float y = 3f / 16f;
		
		worldrenderer.addVertexWithUV(x, y, x, 0, 0);   
		worldrenderer.addVertexWithUV(x, y + hgt, x, 0, hgt);
		worldrenderer.addVertexWithUV(x + wdt, y + hgt, x, 1, hgt);
		worldrenderer.addVertexWithUV(x + wdt, y, x, wdt, 0);
										
		tessellator.draw();
	}
	
	
	// quantityTextures = for animations
	public void drawContents(long worldtime, int quantityTextures) {
		GL11.glPushMatrix();
	        Tessellator tessellator = Tessellator.getInstance();
	        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	
	        worldrenderer.startDrawingQuads();
	
	        float start = 0.25f;
	        float end = 0.75f;
	        float height = 0.6875f;
	        int currentAnimation = (int) (worldtime % quantityTextures);
	        
	        float yStart = (float)currentAnimation / quantityTextures;
	        float yEnd = yStart + 1f / quantityTextures;
	        
	        
	        GlStateManager.enableBlend();
	        GlStateManager.disableCull();
	        GlStateManager.tryBlendFuncSeparate(770, GL11.GL_ONE, GL11.GL_ONE, 0);
	        
			worldrenderer.addVertexWithUV(start, height, start, 0, yStart);   
			worldrenderer.addVertexWithUV(start, height, end, 1, yStart);
			worldrenderer.addVertexWithUV(end, height, end, 1, yEnd);
			worldrenderer.addVertexWithUV(end, height, start, 1, yEnd);
			
			tessellator.draw();
	
	        GlStateManager.disableBlend();
	        GlStateManager.enableCull();
        GL11.glPopMatrix();		
	}
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEWoodBucket)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}


}

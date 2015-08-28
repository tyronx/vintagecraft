package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelTallMetallMold;
import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import at.tyron.vintagecraft.TileEntity.TETallMetalMold;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRTallMetalMold extends TESRBase {
	ResourceLocation fireclayTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/fireclaybricks.png");
	ResourceLocation ironTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/steel.png");
	ResourceLocation whiteTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/white.png");
	
	ModelTallMetallMold tallmetalmold = new ModelTallMetallMold();
	
	private void renderAt(TETallMetalMold te, double posX, double posY, double posZ, float partialTicks) {
		if (te.refreshModel) {
			tallmetalmold.initComponents();
			te.refreshModel = false;
		}
		
		
		
		GL11.glPushMatrix();
			GL11.glTranslated(posX, posY, posZ);
			GL11.glTranslated(+0.5f, +0.5f, +0.5f);
			if (te.orientation != null) {
				float []angles = getAnglesBetween(EnumFacing.NORTH, te.orientation);
				GL11.glRotatef(angles[0], 0, 1f, 0);
			}
			
			GL11.glTranslated(-0.5f, -0.5f, -0.5f);
			Minecraft.getMinecraft().getTextureManager().bindTexture(fireclayTex);			
			tallmetalmold.fireclayMolds.render(0.0625F / 2f);
			
			int flowtimer = te.getLiquidMetalFlowTimer();
			float progress = (100 - flowtimer)/100f;
			int ingotTemp = te.getMetalTemperature() / 4;
			int light = te.getWorld().getCombinedLight(te.getPos().up(), 0); 
			int i1 = Math.min(240, light % 65536 + ingotTemp);
			int k1 = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / 1.0F, k1 / 1.0F);
			float[]colors = EnumMetal.getIncandescenceColorAsColor4f(te.getMetalTemperature());
			
		
			if (flowtimer > 0) {
				GL11.glPushMatrix();
					
				GL11.glTranslated(0.01f, -0.125 + -0.125 * Math.max(0, progress * 5 - 3f), 0f);
					Minecraft.getMinecraft().getTextureManager().bindTexture(ironTex);
					renderMetal(tallmetalmold.furnaceHole, colors);
				GL11.glPopMatrix();

				GL11.glPushMatrix();
					GL11.glScalef(0.98f, 1f, 1f);
					GL11.glTranslated(0.01f, Math.min(0, (-0.063 + 0.063 * progress*20)) + -0.063 * Math.max(0, progress * 5 - 4f), 0f);  
					renderMetal(tallmetalmold.canal, colors);
				GL11.glPopMatrix();
				
				GL11.glPushMatrix();
					GL11.glTranslated(0f, Math.min(0, -0.1563f + 0.15625 * 1.1f * progress) - 0.01f, 0f);
					renderMetal(tallmetalmold.ingots, colors);
				GL11.glPopMatrix();
			}
			

			if (te.getQuantityIngots() > 0 && flowtimer == 0) {
				
				GL11.glPushMatrix();
					GL11.glTranslated(0f, -0.01f, 0f);
					renderMetal(tallmetalmold.ingots, colors);
				GL11.glPopMatrix();
			}
			
					
			//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (light % 65536) / 1.0F, k1 / 1.0F);
			//RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
		
	}
	
	public void renderMetal(ModelRendererVC model, float[] colors) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(ironTex);
		model.render(0.0625F / 2f);
		
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
		Minecraft.getMinecraft().getTextureManager().bindTexture(whiteTex);
		model.render(0.0625F / 2f);
		GL11.glDisable(GL11.GL_BLEND);
		
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	
	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float partialTicks, int p_180535_9_) {
		renderAt((TETallMetalMold)te, posX, posY, posZ, partialTicks);
	}

	
}

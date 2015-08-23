package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelBellows;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TESRBellows extends TESRMechanicalBase {
	ResourceLocation leatherTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/leather.png");
	ResourceLocation ironTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/iron.png");
	
	ModelBellows modelbellows = new ModelBellows();
	
	public void renderAt(TEBellows te, float posX, float posY, float posZ, float f) {
		if (te.getWorld() == null || te.texture == null) return;
		if (te.refreshModel) {
			te.refreshModel = false;
			System.out.println("refreshed " + te.orientation);
			modelbellows.initComponents();
		}
		
		//System.out.println("render");
		float angle = te.getAngle();
		float squeeze = MathHelper.sin((float) ((angle+90) / 180 * Math.PI)) / 6 + 0.166f;
		
		GL11.glPushMatrix();
			GL11.glTranslatef(posX, posY, posZ);
			GL11.glTranslatef(+0.5f, +0.5f, +0.5f);
			float []angles = getAnglesBetween(EnumFacing.NORTH, te.orientation);
			GL11.glRotatef(angles[0], 0, 1f, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(te.texture);
			modelbellows.woodCover.render(0.0625F / 2f);
			
			GL11.glPushMatrix();
				GL11.glTranslatef(0, 13 / 16f - squeeze, 0);
				modelbellows.woodCover.render(0.0625F / 2f);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
				GL11.glTranslatef(+0.5f, +0.5f, +0.5f);
				GL11.glRotatef(180, 0, 1f, 0);
				GL11.glRotatef(angle, 0, 0, 1f);				
				GL11.glTranslatef(-0.5f, -0.5f, -0.5f - 1/16f);
				modelbellows.gearBase.render(0.0625F / 2f);
				GL11.glPushMatrix();			
					GL11.glTranslatef(0.5f, -0.225f, 0.001f);
					GL11.glRotatef(45, 0, 0, 0.5f);
					modelbellows.gearBase.render(0.0625F / 2f);
					modelbellows.gearPegs.render(0.0625F / 2f);
				GL11.glPopMatrix();
			GL11.glPopMatrix();
			
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(leatherTex);
			GL11.glPushMatrix();
				GL11.glScalef(1f, 1f - squeeze * 1.26f, 1f);
				modelbellows.leather.render(0.0625F / 2f);
			GL11.glPopMatrix();
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(ironTex);
			GL11.glPushMatrix();
				GL11.glTranslatef(0f, 0f, -1f);
				modelbellows.ironTipAndTuyere.render(0.0625F / 2f);
			GL11.glPopMatrix();
			
		GL11.glPopMatrix();
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEBellows)tileentity, (float)posX, (float)posY, (float)posZ, par8);

	}

}

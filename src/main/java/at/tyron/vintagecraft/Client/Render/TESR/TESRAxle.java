package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Model.ModelAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TESRAxle extends TESRMechanicalBase {
	ModelAxle axle = new ModelAxle();
	
	public void renderAt(TEAxle te, float posX, float posY, float posZ, float f) {
		if (te.getWorld() == null || te.texture == null) return;
		if (te.refreshModel) {
			te.refreshModel = false;
			System.out.println("refreshed " + te.orientation);
			axle.initComponents();
		}

		GL11.glPushMatrix();
			setupRotationsAndPos(EnumFacing.NORTH, te.orientation, te.getAngle(), posX, posY, posZ);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(te.texture);
			axle.renderAxle();
			
		GL11.glPopMatrix();
		
		
		// Axle Supports
		GL11.glPushMatrix();
			GL11.glTranslatef(posX + 0.5f, posY + 0.5f, posZ + 0.5f);
			
			if (te.attachment == EnumFacing.DOWN) {
				float[] angles = getAnglesBetween(EnumFacing.NORTH, te.orientation);
				GL11.glRotatef(angles[0], 0f, 1f, 0f);
				
			} else {
				float[] angles = getAnglesBetween(EnumFacing.NORTH, te.attachment);
				GL11.glRotatef(angles[0], 0f, 1f, 0f);
				GL11.glRotatef(90, 1f, 0f, 0f);
			}
			
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			if (!te.connectedLeft) axle.renderSupport();			
			GL11.glTranslatef(0f, 0f, 0.7f);
			if (!te.connectedRight) axle.renderSupport();
			
			
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEAxle)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}
	
	
}

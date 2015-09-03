package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Model.ModelAxle;
import at.tyron.vintagecraft.Client.Model.ModelGrindstone;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TESRGrindstone extends TESRMechanicalBase {
	ModelGrindstone grindstonemodel = new ModelGrindstone();
	ModelAxle axlemodel = new ModelAxle();
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		renderAtPos((TEGrindStone)tileentity, posX, posY, posZ);
	}

	private void renderAtPos(TEGrindStone te, double posX, double posY, double posZ) {
		if (te.getWorld() == null) return;
		if (te.refreshModel) {
			grindstonemodel.initComponents();
			te.refreshModel = false;
		}
		
		EnumRockType rocktype = te.getRockType();
		Minecraft.getMinecraft().getTextureManager().bindTexture(te.rockTexture);
		
		GL11.glPushMatrix();
			GL11.glTranslated(posX, posY, posZ);
			grindstonemodel.renderGrindStone();
		GL11.glPopMatrix();	
		
		GL11.glPushMatrix();
			GL11.glTranslated(posX + 0.5, posY + 0.5, posZ + 0.5);
			GL11.glRotatef(360 - te.getAngle() + 2f, 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, -0.5f + 0.375f, -0.5f);
			grindstonemodel.renderGrindStone();
		GL11.glPopMatrix();
			
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(te.woodTexture);
		GL11.glPushMatrix();
			setupRotationsAndPos(EnumFacing.NORTH, EnumFacing.UP, te.getAngle(), posX, posY, posZ);
		
			Minecraft.getMinecraft().getTextureManager().bindTexture(te.woodTexture);
			axlemodel.renderAxle();
		
		GL11.glPopMatrix();

		

	}


}

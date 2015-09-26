package at.tyron.vintagecraft.Client.Render.TESR;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Model.ModelGearComponents;
import at.tyron.vintagecraft.Client.Render.Math.Matrix4f;
import at.tyron.vintagecraft.Client.Render.Math.Quaternion;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGears;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public abstract class TESRMechanicalBase extends TESRBase {
	ModelGearComponents modelcagegear = new ModelGearComponents();
	
	
	
	void setupRotationsAndPos(EnumFacing base, EnumFacing desired, float angle, double posX, double posY, double posZ) {
		if (desired == null) {
			desired = EnumFacing.NORTH;
		}
		
		float[] angles = getAnglesBetween(base, desired);
		GL11.glTranslated(posX + 0.5f, posY + 0.5f, posZ + 0.5f);
		
		Quaternion q = new Quaternion(); // Identity
		q.eulerToQuat(angles[1], angles[0], 0);
		Matrix4f mat = new Matrix4f();
		q.createMatrix(mat);

		FloatBuffer buf = BufferUtils.createFloatBuffer(16).put(mat.matrix);
		buf.rewind();
		
		
		GL11.glMultMatrix(buf);		
		GL11.glRotatef(angle, 0f, 0f, 1f);
		GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
	}
	


	public void renderAngledGearBox(TEAngledGears te, float angle, float posX, float posY, float posZ) {
		if (te.refreshModel) {
			te.refreshModel = false;
			modelcagegear.initComponents();
		}
		
		
		// Peg Gear
		GL11.glPushMatrix();
			setupRotationsAndPos(EnumFacing.NORTH, te.orientation, te.getAngle(), posX, posY, posZ);
			renderPegGear(te.woodTexture);
		GL11.glPopMatrix();
		
		// Cage Gear
		if (te.cagegearOrientation != null) {
			GL11.glPushMatrix();
				setupRotationsAndPos(EnumFacing.NORTH, te.cagegearOrientation, 360 - te.getAngle() - 18, posX, posY, posZ);
				renderCageGear(te.woodTexture);
			GL11.glPopMatrix();
		}
	}
	
	
	public void renderCageGear(ResourceLocation texture) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		GL11.glPushMatrix();
			
			modelcagegear.renderBase();
			modelcagegear.renderPegs();
			
			GL11.glPushMatrix();			
				GL11.glTranslatef(0.5f, -0.225f, 0.001f);
				GL11.glRotatef(45, 0, 0, 0.5f);
				modelcagegear.renderBase();
				modelcagegear.renderPegs();
				
				GL11.glTranslatef(0f, 0f, 0.28f);
				modelcagegear.renderBase();
			GL11.glPopMatrix();
			
			GL11.glTranslatef(0f, 0f, 0.28f);
			modelcagegear.renderBase();
		GL11.glPopMatrix();
	}

	
	public void renderPegGear(ResourceLocation texture) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		GL11.glPushMatrix();			
			modelcagegear.renderBase();
			modelcagegear.renderPegs();
		
			GL11.glPushMatrix();			
				GL11.glTranslatef(0.5f, -0.225f, 0.005f);
				GL11.glRotatef(45, 0, 0, 0.5f);
				modelcagegear.renderBase();
				modelcagegear.renderPegs();
			GL11.glPopMatrix();
			
		GL11.glPopMatrix();
	}

	
}

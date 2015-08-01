package at.tyron.vintagecraft.Client.Render.TESR;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Matrix4f;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Quaternion;
import at.tyron.vintagecraft.Client.Model.ModelAxle;
import at.tyron.vintagecraft.Client.Model.ModelGearComponents;
import at.tyron.vintagecraft.Client.Model.ModelStonePot;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;

public abstract class TESRMechanicalBase extends TESRBase {
	ModelGearComponents modelcagegear = new ModelGearComponents();
	
	
	
	void setupRotationsAndPos(EnumFacing base, EnumFacing desired, float angle, float posX, float posY, float posZ) {
		if (desired == null) {
			desired = EnumFacing.NORTH;
		}
		
		float[] angles = getAnglesBetween(base, desired);
		GL11.glTranslatef(posX + 0.5f, posY + 0.5f, posZ + 0.5f);
		
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
	

	float[] getAnglesBetween(EnumFacing base, EnumFacing desired) {
		if (base == EnumFacing.UP || base == EnumFacing.DOWN) throw new RuntimeException("Not coded for vertical base");
		
		return new float[]{
			// Horizontal angle
			desired.getAxis().isVertical() ? 0 : 90f * (base.getHorizontalIndex() - desired.getHorizontalIndex()), 
			// Vertical angle
			desired.getAxis().isHorizontal() ? 0 : 90f * (desired == EnumFacing.UP ? 1 : -1) 
		};
	}
	
	float[] axis2GLFloats(EnumFacing facing) {
		float[] floats = new float[]{
			(facing.getAxis() == Axis.X ? 1f : 0f),
			(facing.getAxis() == Axis.Y ? 1f : 0f),
			(facing.getAxis() == Axis.Z ? 1f : 0f)
		};
		
		return floats;
	}
	
	
	public void renderAngledGearBox(TEAngledGearBox te, float angle, float posX, float posY, float posZ) {
		if (te.refreshModel) {
			te.refreshModel = false;
			modelcagegear.initComponents();
		}
		
		
		// Peg Gear
		GL11.glPushMatrix();
			setupRotationsAndPos(EnumFacing.NORTH, te.orientation, te.getAngle(), posX, posY, posZ);
			renderPegGear(te.texture);
		GL11.glPopMatrix();
		
		// Cage Gear
		if (te.cagegearOrientation != null) {
			GL11.glPushMatrix();
				setupRotationsAndPos(EnumFacing.NORTH, te.cagegearOrientation, 360 - te.getAngle(), posX, posY, posZ);
				renderCageGear(te.texture);
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

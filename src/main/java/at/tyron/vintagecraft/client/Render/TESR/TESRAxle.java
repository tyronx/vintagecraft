package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;

public class TESRAxle extends TESRBase {
	ModelAxle axle = new ModelAxle();
	
	public void renderAt(TEAxle te, float posX, float posY, float posZ, float f) {
		if (te.getWorld() == null) return;
		if (te.refreshModel) {
			te.refreshModel = false;
			System.out.println("refreshed " + te.orientation);
			axle.initComponents();
		}

		//te.angle += te.getNetworkSpeed(null) / 5f;

		// 0 = S, 1 = W, 2 = N, 3 = E
		// =>
		// 2 = S, 3 = W, 0 = N, 1 = E
		int facing = (te.orientation.getHorizontalIndex() + 2) % 4;
		float xAxis = (facing == 3 || facing == 1) ? 1f : 0;
		float zAxis = (facing == 2 || facing == 0) ? 1f : 0;
		
		float facingAngle = -facing * 90f;
		
		float angle = te.getAngle();
		
		
		
		GL11.glPushMatrix();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/planks/oak.png"));
		
			GL11.glTranslatef(posX + 0.5f, posY + 0.49f, posZ + 0.5f);
			GL11.glRotatef(angle, xAxis, 0f, zAxis);
			GL11.glRotatef(facingAngle, 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, -0.49f, -0.5f);
			axle.renderAxle();
			
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			GL11.glTranslatef(posX + 0.5f, posY + 0.49f, posZ + 0.5f);
			GL11.glRotatef(facingAngle, 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, -0.49f, -0.5f);
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

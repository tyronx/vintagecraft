package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelWindmillRotor;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWindmillRotor extends TESRMechanicalBase {

	ModelWindmillRotor rotor = new ModelWindmillRotor();
	
	public void renderAt(TEWindmillRotor te, float posX, float posY, float posZ, float f) {
		
		if (te.getWorld() == null || te.texture == null) return;
		//if (te.getBladeSize() == 4) System.out.println(te.getNetwork(null).getAngle());
		if (te.refreshModel) {
			te.refreshModel = false;
			rotor.initComponents();
		}
		
		
		// 0 = S, 1 = W, 2 = N, 3 = E
		// =>
		// 0 = S, 3 = W, 2 = N, 1 = E
		/*int facing = te.getOrientation().getHorizontalIndex();
		float facingAngle = -facing * 90f;


		float xAxis = (facing == 3 || facing == 1) ? 1f : 0;
		float zAxis = (facing == 2 || facing == 0) ? 1f : 0;*/
		
		GL11.glPushMatrix();
			setupRotationsAndPos(EnumFacing.SOUTH, te.getOrientation(), te.getAngle(), posX, posY, posZ);
		
		
			Minecraft.getMinecraft().getTextureManager().bindTexture(te.texture);
		
			/*GL11.glTranslatef(posX + 0.5f, posY + 0.49f, posZ + 0.5f);
			GL11.glRotatef(te.getAngle(), xAxis, 0f, zAxis);
			GL11.glRotatef(facingAngle, 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, -0.49f, -0.5f);*/
			
			rotor.renderRotor(te.getBladeSize());
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("vintagecraft:textures/blocks/linen.png"));
			
			rotor.renderWings(te.getBladeSize());
			
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEWindmillRotor)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}


}

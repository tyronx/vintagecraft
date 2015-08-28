package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Model.ModelWindmillRotor;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWindmillRotor extends TESRMechanicalBase {

	ModelWindmillRotor rotor = new ModelWindmillRotor();
	
	public void renderAt(TEWindmillRotor te, float posX, float posY, float posZ, float f) {
		
		if (te.getWorld() == null || te.texture == null) return;
		
		if (te.refreshModel) {
			te.refreshModel = false;
			rotor.initComponents();
		}
		
				
		GL11.glPushMatrix();
			setupRotationsAndPos(EnumFacing.SOUTH, te.getOrientation(), te.getAngle(), posX, posY, posZ);
		
			Minecraft.getMinecraft().getTextureManager().bindTexture(te.texture);
			
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

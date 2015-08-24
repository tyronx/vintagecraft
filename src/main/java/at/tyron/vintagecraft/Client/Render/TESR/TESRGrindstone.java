package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class TESRGrindstone extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		renderAtPos((TEGrindStone)tileentity, posX, posY, posZ);
	}

	private void renderAtPos(TEGrindStone tileentity, double posX, double posY, double posZ) {
		if (tileentity.getWorld() != null) {
			GL11.glPushMatrix();
			GL11.glTranslated(posX, posY, posZ);
			
			renderBase(tileentity);
			renderRotatingStone(tileentity);
			
			GL11.glPopMatrix();
		}

	}

	private void renderBase(TEGrindStone tileentity) {
				
	}
	
	private void renderRotatingStone(TEGrindStone tileentity) {
		
	}

}

package at.tyron.vintagecraft.Client.Render.TESR;

import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGears;
import net.minecraft.tileentity.TileEntity;

public class TESRAngledGearBox extends TESRMechanicalBase  {

	
	public void renderAt(TEAngledGears te, float posX, float posY, float posZ, float f) {
		if (te.getWorld() == null || te.woodTexture == null) return;
		renderAngledGearBox(te, te.getAngle(), posX, posY, posZ);		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEAngledGears)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}

}

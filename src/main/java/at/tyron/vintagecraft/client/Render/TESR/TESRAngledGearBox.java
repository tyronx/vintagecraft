package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import net.minecraft.tileentity.TileEntity;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;

public class TESRAngledGearBox extends TESRMechanicalBase {

	
	public void renderAt(TEAngledGearBox te, float posX, float posY, float posZ, float f) {
		if (te.getWorld() == null) return;
		
		//te.angle += te.getNetworkSpeed(null) / 5f;
		//System.out.println(te.getAngle());
		renderAngledGearBox(te, te.getAngle(), posX, posY, posZ);		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEAngledGearBox)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}

}

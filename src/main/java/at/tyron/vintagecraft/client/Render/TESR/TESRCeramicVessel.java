package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.Block.Utility.BlockClayVessel;
import at.tyron.vintagecraft.Client.Model.ModelVessel;

public class TESRCeramicVessel extends TESRBase {
	ModelVessel modelvessel = new ModelVessel();

	
	private void renderAt(TEVessel tileentity, float posX, float posY, float posZ, float par8) {
		if (tileentity.getWorld() != null) {
			if (tileentity.getBlockType() instanceof BlockClayVessel) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/rawclay.png"));
			} else {
				Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/ceramic.png"));
			}
			 
			GL11.glPushMatrix();
			GL11.glTranslatef(posX, posY, posZ);
			modelvessel.render();
			GL11.glPopMatrix();
		}
	}

	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEVessel)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}

}

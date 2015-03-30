package at.tyron.vintagecraft.client.Render.TESR;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.block.BlockIngotPile;
import at.tyron.vintagecraft.client.Model.ModelIngotPile;

public class TESRIngotPile extends TESRBase 
{
	private final ModelIngotPile ingotModel = new ModelIngotPile();

	public void renderTileEntityIngotPileAt(TEIngotPile tep, double d, double d1, double d2, float f) {
		Block block = tep.getBlockType();
		
		if (tep.getWorld() != null && tep.getStackInSlot(0) != null && block == BlocksVC.ingotPile) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/" + tep.getMetal().getName().toLowerCase() + ".png")); //texture

			GL11.glPushMatrix();
			GL11.glTranslatef((float)d + 0.0F, (float)d1 + 0F, (float)d2 + 0.0F);
			ingotModel.renderIngots(tep.getStackSize());
			GL11.glPopMatrix();
		}
	}

	
	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posZ, double par6, float par8, int p_180535_9_) {
		this.renderTileEntityIngotPileAt((TEIngotPile) te, posX, posZ, par6, par8);
	}
}
package at.tyron.vintagecraft.Client.Render.TESR;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelBucketVC;
import at.tyron.vintagecraft.TileEntity.TEWoodBucket;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;
import at.tyron.vintagecraft.WorldProperties.EnumBucketContents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRWoodBucket extends TESRBase {
	ResourceLocation ironTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/iron_darkened.png");
	ResourceLocation linenTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/linen.png");
	ResourceLocation waterTex = new ResourceLocation("textures/blocks/water_still.png");
	
	ModelBucketVC bucketmodel = new ModelBucketVC();
	
	void renderAt(TEWoodBucket te, float posX, float posY, float posZ, float partialTicks) {
		if (te.getWorld() == null) return;
		
		if (te.refreshModel) {
			bucketmodel.initComponents();
			te.refreshModel = false;
		}
		
		GL11.glPushMatrix();
			GL11.glTranslatef(posX, posY, posZ);
			GL11.glTranslatef(+0.5f, +0.5f, +0.5f);
			float []angles = getAnglesBetween(EnumFacing.NORTH, te.orientation);
			GL11.glRotatef(angles[0], 0, 1f, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/planks/" + te.treetype.getName() + ".png"));
			bucketmodel.renderWood();
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(linenTex);
			bucketmodel.renderRope();
			
			if (te.contents == EnumBucketContents.WATER) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(waterTex);
				bucketmodel.renderContents();
			}
		GL11.glPopMatrix();
		
	}
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEWoodBucket)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}


}

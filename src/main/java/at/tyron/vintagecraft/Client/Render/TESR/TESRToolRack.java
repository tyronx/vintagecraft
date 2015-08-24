package at.tyron.vintagecraft.Client.Render.TESR;

import java.util.Locale;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelToolRack;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class TESRToolRack extends TESRBase {
	ModelToolRack modeltoolrack = new ModelToolRack(); 
	
	public TESRToolRack() {
		
	}

	public void renderAt(TEToolRack te, float posX, float posY, float posZ, float f) {
		if (te.getWorld() != null) {
			EnumFacing facing = te.facing;
			
			// Crash prevention  
			if (facing == null) {
				facing = EnumFacing.NORTH;
			}
			
			// Why? O_o
			if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) facing = facing.getOpposite();
			
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/planks/" + te.woodtype.getStateName().toLowerCase(Locale.ROOT) + ".png")); 
			GL11.glPushMatrix();
			GL11.glTranslatef(posX + 0.5f, posY, posZ + 0.5f);
			GL11.glRotatef((float) (facing.getHorizontalIndex() * 90), 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, 0, -0.5f);
			modeltoolrack.renderWood();
			GL11.glPopMatrix();

			
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/nailtexture.png")); 
			GL11.glPushMatrix();
			GL11.glTranslatef(posX + 0.5f, posY, posZ + 0.5f);
			GL11.glRotatef((float) (facing.getHorizontalIndex() * 90), 0f, 1f, 0f);
			GL11.glTranslatef(-0.5f, 0, -0.5f);
			modeltoolrack.renderNails();
			GL11.glPopMatrix();

			
			

			EntityItem customitem = new EntityItem(te.getWorld());
			customitem.hoverStart = 0f;
			float blockScale = 0.45F;
			
			float left = 0.2f;
			float right = 0.7f;
			
			if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
				left = 0.7f;
				right = 0.2f;
			}

			for(int i = 0; i < 4; i++) {
				if (te.getStackInSlot(i) != null) {
					
					GL11.glPushMatrix();
					
					GL11.glTranslatef(posX + 0.5f, posY, posZ + 0.5f);
					GL11.glRotatef((float) (facing.getHorizontalIndex() * 90), 0f, 1f, 0f);
					GL11.glTranslatef(
						((i % 2 == 0) ? right : left) - 0.5f, 
						(i > 1 ? 0f : 0.5f) + 0.3f, 
						0.08f - 0.5f
					);
					GL11.glScalef(blockScale, blockScale, blockScale);
					
					
					customitem.setEntityItemStack(te.getStackInSlot(i));
					Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().thePlayer, customitem.getEntityItem(), TransformType.GUI);
					
					GL11.glPopMatrix();
				}
			}
		}
	}

	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		this.renderAt((TEToolRack)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}
}
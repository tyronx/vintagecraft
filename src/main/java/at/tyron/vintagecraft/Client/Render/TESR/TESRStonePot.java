package at.tyron.vintagecraft.Client.Render.TESR;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Client.Model.ModelStonePot;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumStonePotUtilization;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;

public class TESRStonePot extends TESRBase {
	ResourceLocation whiteTex = new ResourceLocation(ModInfo.ModID, "textures/blocks/white.png");
	
	ModelStonePot modelstonepot = new ModelStonePot();


	private void renderAtPos(TEStonePot tileentity, float posX, float posY, float posZ, float par8) {
		if (tileentity.getWorld() != null) {
			GL11.glPushMatrix();
			GL11.glTranslatef(posX, posY, posZ);
			
			render(tileentity);
			
			GL11.glPopMatrix();
		}
	}
	
	private void render(TEStonePot tileentity) {
		renderWalls(tileentity.rocktype);
	
		if (tileentity.utilization == EnumStonePotUtilization.FORGE) {
			renderFillmaterial(tileentity.getFillHeight(), tileentity.burning);
			
			renderContents(tileentity, tileentity.getHeatableItemStack());
		}
	}
	
	
	
	private void renderWalls(EnumRockType rocktype) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/cobblestone/"+rocktype.getName()+".png"));
		GL11.glPushMatrix();			
		modelstonepot.renderWalls();
		GL11.glPopMatrix();
	}
	
	
	
	private void renderFillmaterial(int fillheight, boolean burning) {
		String texturename = burning ? "ember.png" : "coal.png";
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/" + texturename));
		
		GL11.glPushMatrix();
		if (fillheight > 0) {
			if (burning) OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240 / 1.0F, 240 / 1.0F);
			modelstonepot.renderFillmaterial(fillheight);
		}
		GL11.glPopMatrix();
	}
			
			

	
	
	
	
	private void renderContents(TEStonePot tileentity, ItemStack heatableItemStack) {
		if (heatableItemStack == null) return;
		
		int ingotTemp = heatableItemStack.getTagCompound().getInteger("forgetemp") / 10;
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0f, tileentity.getFillHeight() / 16f - 0.15f, 0f);
		
		int light = tileentity.getWorld().getCombinedLight(tileentity.getPos().up(), 0); 
		int i1 = Math.min(240, light % 65536 + Math.max(0, (ingotTemp - 525)/2));
		int k1 = light / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / 1.0F, k1 / 1.0F);

		float[]colors = EnumMetal.getIncandescenceColorAsColor4f(ingotTemp);
		
		if (heatableItemStack.getItem() instanceof ItemIngot) {
			renderHotIngots(
				tileentity,
				tileentity.getIngotMetal(), 
				tileentity.getHeatableQuantity(), 
				ingotTemp / 6,
				colors
			);
		} else {
			renderHotItemstack(
				heatableItemStack, 
				ingotTemp,
				colors
			);
			
		}

		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (light % 65536) / 1.0F, k1 / 1.0F);
		GL11.glPopMatrix();
	}
	
	
	
	
	void renderHotItemstack(ItemStack itemstack, int ingotTemp, float[] colors) {
		float blockScale = 0.5F;
		float left = 0.2f;
		float right = 0.7f;
		
		
		GL11.glTranslatef(0.5f, 0.5f, 0.5f);
		GL11.glScalef(blockScale, blockScale, blockScale);
		
		Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		
        int color = EnumMetal.getIncandescenceColorAsInt(ingotTemp);

		for (int i = 0; i < itemstack.stackSize; i++) {
			GL11.glTranslatef(
				(i > 1 ? -1f : 1f) * ((i % 2 > 0) ? 0 : 0.03125f), 
				0.03125f, 
				(i > 1 ? -1f : 1f) * (((i+1) % 2 > 0) ? 0 : 0.03125f)
			);
			
			GL11.glPushMatrix();
				renderItemStack(itemstack, Minecraft.getMinecraft().thePlayer, TransformType.GUI, TextureMap.locationBlocksTexture, 0xffffffff);
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
		        GlStateManager.disableLighting();
		        GlStateManager.disableCull();
		        GlStateManager.enableBlend();
		        GlStateManager.depthMask(false);
		        GlStateManager.disableTexture2D();
		        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
		        
				renderItemStack(itemstack, Minecraft.getMinecraft().thePlayer, TransformType.GUI, whiteTex, color);
				
		        GlStateManager.enableLighting();
		        GlStateManager.enableTexture2D();
		        GlStateManager.depthMask(true);
		        GlStateManager.disableBlend();
		        GlStateManager.enableCull();
				GL11.glPopMatrix();
				
				GL11.glPushMatrix();
    
		        // Cheap glow effect
		        GlStateManager.enableBlend();
				//GL11.glColor4f(0.5F + ingotTemp / 400f, 0.1F + ingotTemp / 800f, 0.1F, Math.min(0.8f, ingotTemp / 200F));
		        GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
				GL11.glScalef(1.1f, 1.1f, 1.1f);
				GL11.glTranslatef(0f, 0.05f, 0f);
				renderItemStack(itemstack, Minecraft.getMinecraft().thePlayer, TransformType.GUI, whiteTex, color);
		        GlStateManager.disableBlend();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
		        
		        
		    GL11.glPopMatrix();

		}	
	}
	
	
	void renderHotIngots(TEStonePot tileentity, EnumMetal metal, int ingotQuantity, int ingotTemp, float[] colors) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/" + metal.getName() + ".png"));
		modelstonepot.renderIngots(ingotQuantity);
		
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
        
		GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
		GL11.glScalef(1.01f, 1.01f, 1.01f);
		GL11.glTranslatef(-0.005f, 0, -0.005f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(whiteTex);
		modelstonepot.renderIngots(ingotQuantity);
		
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        
        // Cheap glow effect
        GlStateManager.enableBlend();
		//GL11.glColor4f(0.5F + ingotTemp / 400f, 0.1F + ingotTemp / 800f, 0.1F, Math.min(0.8f, ingotTemp / 200F));
        GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
		GL11.glScalef(1.1f, 1.1f, 1.1f);
		modelstonepot.renderIngotsGlow(ingotQuantity);
        GlStateManager.disableBlend();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
	}
	
	
	
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		renderAtPos((TEStonePot)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	void renderItemStack(ItemStack itemstack, EntityPlayer player, ItemCameraTransforms.TransformType cameraTransformType, ResourceLocation texture, int color) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		IBakedModel ibakedmodel = mesher.getItemModel(itemstack);
		
		TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
		
        textureManager.bindTexture(texture);
        //textureManager.getTexture(texture).setBlurMipmap(false, false);
        
        
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.setVertexFormat(DefaultVertexFormats.ITEM);
        EnumFacing[] aenumfacing = EnumFacing.values();
        int j = aenumfacing.length;
        
        for (int k = 0; k < j; ++k) {
            EnumFacing enumfacing = aenumfacing[k];
            renderQuads(worldrenderer, ibakedmodel.getFaceQuads(enumfacing), itemstack, 1, color);
        }

        renderQuads(worldrenderer, ibakedmodel.getGeneralQuads(), itemstack, 1, color);
        tessellator.draw();
        
        

        //textureManager.bindTexture(texture);
        //textureManager.getTexture(texture).restoreLastBlurMipmap();
	}
	
	
	
	
    private void putQuadNormal(WorldRenderer renderer, BakedQuad quad) {
        Vec3i vec3i = quad.getFace().getDirectionVec();
        renderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
    }

	
    private void renderQuad(WorldRenderer renderer, BakedQuad quad, int color) {
        renderer.addVertexData(quad.getVertexData());
        
        /*if(quad instanceof net.minecraftforge.client.model.IColoredBakedQuad) {
            net.minecraftforge.client.ForgeHooksClient.putQuadColor(renderer, quad, color);
        } else {
        	renderer.putColor4(color);
        }*/
        
        renderer.putColor4(color);
        
        this.putQuadNormal(renderer, quad);
    }
    
    

	private void renderQuads(WorldRenderer renderer, List quads, ItemStack stack, int renderpass, int color) {
		BakedQuad bakedquad;
		
/*		int color = 0x44ffffff; //stack.getItem().getColorFromItemStack(stack, renderpass);
		if (EntityRenderer.anaglyphEnable) {
			color = TextureUtil.anaglyphColor(color);
		}
*/
		for (Iterator iterator = quads.iterator(); iterator.hasNext(); this.renderQuad(renderer, bakedquad, color)) {
			bakedquad = (BakedQuad)iterator.next();
		}
	}

	
}


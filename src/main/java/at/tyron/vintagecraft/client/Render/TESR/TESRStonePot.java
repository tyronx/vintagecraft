package at.tyron.vintagecraft.Client.Render.TESR;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
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
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Block.Utility.BlockClayVessel;
import at.tyron.vintagecraft.Client.Model.ModelStonePot;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumStonePotUtilization;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;

public class TESRStonePot extends TESRBase {
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
			
			renderHeatableItemStack(tileentity, tileentity.getHeatableItemStack());
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
			
			

	
	
	
	
	private void renderHeatableItemStack(TEStonePot tileentity, ItemStack heatableItemStack) {
		if (heatableItemStack == null) return;
		
		int ingotTemp = heatableItemStack.getTagCompound().getInteger("forgetemp");
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0f, tileentity.getFillHeight() / 16f - 0.15f, 0f);
		
		if (heatableItemStack.getItem() instanceof ItemIngot) {
			renderIngots(
				tileentity,
				tileentity.getIngotMetal(), 
				tileentity.getHeatableQuantity(), 
				ingotTemp / 60
			);
		} else {
			EntityItem customitem = new EntityItem(tileentity.getWorld());
			customitem.hoverStart = 0f;
			
			float blockScale = 1F;
			float left = 0.2f;
			float right = 0.7f;
			
			
			GL11.glTranslatef(0.5f, 0.3f, 0.5f);
			GL11.glScalef(blockScale, blockScale, blockScale);
			
			int light = tileentity.getWorld().getCombinedLight(tileentity.getPos().up(), 0); 
			int i1 = Math.min(240, light % 65536 + ingotTemp);
			int k1 = light / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / 1.0F, k1 / 1.0F);
			
			for (int i = 0; i < heatableItemStack.stackSize; i++) {
				Minecraft.getMinecraft().getRenderItem().renderItemModelForEntity(heatableItemStack, Minecraft.getMinecraft().thePlayer, TransformType.GUI);
				
				GL11.glTranslatef((i > 1 ? -1f : 1f) * ((i % 2 > 0) ? 0 : 0.03125f), 0.03125f, (i > 1 ? -1f : 1f) * (((i+1) % 2 > 0) ? 0 : 0.03125f));
			}
			
			//renderItemStack(heatableItemStack, Minecraft.getMinecraft().thePlayer, TransformType.GUI);
			
			//TileEntityItemStackRenderer.instance.renderByItem(heatableItemStack);
			
			//customitem.setEntityItemStack(heatableItemStack);
			//Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().thePlayer, customitem.getEntityItem(), TransformType.GUI);
			
			
			/*GL11.glEnable(GL11.GL_BLEND);
			GL11.glScalef(0.7f, 0.7f, 0.7f);
			//Minecraft.getMinecraft().getRenderItem().renderItemModelForEntity(heatableItemStack, Minecraft.getMinecraft().thePlayer, TransformType.GUI);
			renderItemStack(heatableItemStack, Minecraft.getMinecraft().thePlayer, TransformType.GUI);
			GL11.glDisable(GL11.GL_BLEND);
			*/
			
			
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (light % 65536) / 1.0F, k1 / 1.0F);
			
			
		}

		GL11.glPopMatrix();
	}
	
	
	
	
	void renderItemStack(ItemStack itemstack, EntityPlayer player, ItemCameraTransforms.TransformType cameraTransformType) {
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		IBakedModel ibakedmodel = mesher.getItemModel(itemstack);
		
		TextureManager textureManager = Minecraft.getMinecraft().renderEngine;
		
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        
        
        GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
        
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.setVertexFormat(DefaultVertexFormats.ITEM);
        EnumFacing[] aenumfacing = EnumFacing.values();
        int j = aenumfacing.length;
        
        for (int k = 0; k < j; ++k) {
            EnumFacing enumfacing = aenumfacing[k];
            renderQuads(worldrenderer, ibakedmodel.getFaceQuads(enumfacing), itemstack, 1);
        }

        renderQuads(worldrenderer, ibakedmodel.getGeneralQuads(), itemstack, 1);
        tessellator.draw();
        
        

        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
	}
	
	
	
	
    private void putQuadNormal(WorldRenderer renderer, BakedQuad quad) {
        Vec3i vec3i = quad.getFace().getDirectionVec();
        renderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
    }

	
    private void renderQuad(WorldRenderer renderer, BakedQuad quad, int color) {
        renderer.addVertexData(quad.getVertexData());
        
        if(quad instanceof net.minecraftforge.client.model.IColoredBakedQuad) {
            net.minecraftforge.client.ForgeHooksClient.putQuadColor(renderer, quad, color);
        } else {
        	renderer.putColor4(color);
        }
        
        this.putQuadNormal(renderer, quad);
    }
    
    

	private void renderQuads(WorldRenderer renderer, List quads, ItemStack stack, int renderpass) {
		BakedQuad bakedquad;
		
		GL11.glEnable(GL11.GL_BLEND);
		
		int color = stack.getItem().getColorFromItemStack(stack, renderpass);
		if (EntityRenderer.anaglyphEnable) {
			color = TextureUtil.anaglyphColor(color);
		}

		for (Iterator iterator = quads.iterator(); iterator.hasNext(); this.renderQuad(renderer, bakedquad, color)) {
			bakedquad = (BakedQuad)iterator.next();
		}
	}



	
	// ingotTemp currently between 0 and 240  (240 = bright red)
	void renderIngots(TEStonePot tileentity, EnumMetal metal, int ingotQuantity, int ingotTemp) {
		//System.out.println(metal);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModInfo.ModID, "textures/blocks/ingot/" + metal.getName() + ".png"));
		
		int light = tileentity.getWorld().getCombinedLight(tileentity.getPos().up(), 0); 
		int i1 = Math.min(240, light % 65536 + ingotTemp);
		int k1 = light / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1 / 1.0F, k1 / 1.0F);

		
		// Ingots with temperature coloring
		GL11.glColor4f(1f, 1f - ingotTemp / 250f, 1f - ingotTemp / 200f, 1f);
		modelstonepot.renderIngots(ingotQuantity);
		
		
		// Cheap glow effect 
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(0.5F + ingotTemp / 400f, 0.1F + ingotTemp / 800f, 0.1F, Math.min(0.8f, ingotTemp / 200F));
		GL11.glScalef(1.1f, 1.1f, 1.1f);
		modelstonepot.renderIngotsGlow(ingotQuantity);
		GL11.glDisable(GL11.GL_BLEND);		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (light % 65536) / 1.0F, k1 / 1.0F);
		
	}
	
	
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double posX, double posY, double posZ, float par8, int p_180535_9_) {
		renderAtPos((TEStonePot)tileentity, (float)posX, (float)posY, (float)posZ, par8);
	}
	
}


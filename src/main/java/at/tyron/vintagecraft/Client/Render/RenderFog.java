package at.tyron.vintagecraft.Client.Render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Client.VCraftModelLoader;
import at.tyron.vintagecraft.World.VCraftWorld;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.RenderFogEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderFog {
	
	public static int fogStart = 0;
	public static int fogEnd = 1000;
	public static boolean customFogRange = false;
	
	
	@SubscribeEvent
	public void RenderFogColorHandler(FogColors event) {
	}
	
	@SubscribeEvent
	public void RenderFogHandler(RenderFogEvent event) {
		if(customFogRange) {
			fogStart = 0;
			fogEnd = 1000;
								
			GL11.glFogf(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
			GL11.glFogf(GL11.GL_FOG_START, fogStart);
			GL11.glFogf(GL11.GL_FOG_END, fogEnd);
		}
	}
}

package at.tyron.vintagecraft.Client.Render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

public class RenderUtils {
    public static Tessellator tessellator = Tessellator.getInstance();
    public static WorldRenderer worldrenderer = tessellator.getWorldRenderer();
    public static void startDrawing(int type) {
    	 worldrenderer.begin(type,worldrenderer.getVertexFormat());
    }
	public static void startDrawingQuads() {
		startDrawing(7);
	}
	public static void addVertex(double x, double y, double z) {
		int l = 255 / (0 + 1);
//		worldrenderer.pos(x, y, z).color(255, 255, 255, l).endVertex();
	}
	public static void addVertexWithUV(double x, double y, double z, double u, double v) {
		int l = 255 / (0 + 1);
		
//		worldrenderer.pos(x, y, z).tex(u,v).color(255, 255, 255, l).endVertex();
		
	}
	
	
}

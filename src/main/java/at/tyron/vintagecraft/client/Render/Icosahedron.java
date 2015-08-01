package at.tyron.vintagecraft.Client.Render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;

import org.lwjgl.opengl.GL11;

public class Icosahedron {

	  public static double X = 0.525731112119133606f;
	  public static double Z = 0.850650808352039932f;

	  public static double[][] vdata = {{-X, +0, +Z}, {+X, +0, +Z}, {-X, +0, -Z}, {+X, +0, -Z}, {+0, +Z, +X}, {+0, +Z, -X},
	      {+0, -Z, +X}, {+0, -Z, -X}, {+Z, +X, +0}, {-Z, +X, +0}, {+Z, -X, +0}, {-Z, -X, +0}};

	  public static int[][] tindx = {{0, 4, 1}, {0, 9, 4}, {9, 5, 4}, {4, 5, 8}, {4, 8, 1}, {8, 10, 1}, {8, 3, 10},
	      {5, 3, 8}, {5, 2, 3}, {2, 7, 3}, {7, 10, 3}, {7, 6, 10}, {7, 11, 6}, {11, 0, 6}, {0, 1, 6}, {6, 1, 10},
	      {9, 0, 11}, {9, 11, 2}, {9, 2, 5}, {7, 2, 11}};

	
	  public static void drawIcosahedron(int depth, float radius) {

	    for (int i = 0; i < tindx.length; i++) {
	      subdivide(
	          vdata[tindx[i][0]], //
	          vdata[tindx[i][1]], //
	          vdata[tindx[i][2]], depth, radius);
	    }
	  }

	  private static void subdivide(double[] vA0, double[] vB1, double[] vC2, int depth, float radius) {

	    double[] vAB = new double[3];
	    double[] vBC = new double[3];
	    double[] vCA = new double[3];

	    int i;

	    if (depth == 0) {
	      drawTriangle(vA0, vB1, vC2, radius);

	      return;
	    }

	    for (i = 0; i < 3; i++) {
	      vAB[i] = (vA0[i] + vB1[i]) / 2;
	      vBC[i] = (vB1[i] + vC2[i]) / 2;
	      vCA[i] = (vC2[i] + vA0[i]) / 2;
	    }

	    double modAB = mod(vAB);
	    double modBC = mod(vBC);
	    double modCA = mod(vCA);

	    for (i = 0; i < 3; i++) {
	      vAB[i] /= modAB;
	      vBC[i] /= modBC;
	      vCA[i] /= modCA;
	    }

	    subdivide(vA0, vAB, vCA, depth - 1, radius);
	    subdivide(vB1, vBC, vAB, depth - 1, radius);
	    subdivide(vC2, vCA, vBC, depth - 1, radius);
	    subdivide(vAB, vBC, vCA, depth - 1, radius);
	  }
	  
	  
	  public static double mod(double[] v) {
	    return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
	  }

	  // --------------------------------------------------------------------------------

	  private static double[] calcTextureMap(double[] vtx) {
	    double[] ret = new double[3];

	    ret[0] = Math.sqrt(vtx[0] * vtx[0] + vtx[1] * vtx[1] + vtx[2] * vtx[2]);
	    ret[1] = Math.acos(vtx[2] / ret[0]);
	    ret[2] = Math.atan2(vtx[1], vtx[0]);

	    ret[1] += Math.PI;
	    ret[1] /= (2 * Math.PI);
	    ret[2] += Math.PI;
	    ret[2] /= (2 * Math.PI);
	    
	  /*  if (Math.abs(vtx[1]) < 1.0f ) {
	        ret[1] = 0.5f + 0.5f * Math.atan2(vtx[2], vtx[0]) / Math.PI;
	    } else {
	        ret[1] = 0; // Actually undefined.
	    }
	    
	    ret[2] = 2 * Math.asin(vtx[1]) / Math.PI;
	    */

	    return ret;
	  }

	  private static void drawTriangle(double[] v1, double[] v2, double[] v3, float radius) {
	    double[] spherical;

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawing(GL11.GL_TRIANGLES);
        
	    GL11.glEnable(GL11.GL_TEXTURE_2D);

	    spherical = calcTextureMap(v1);
	    worldrenderer.addVertexWithUV(radius * v1[0], radius * v1[1], radius * v1[2], spherical[1], spherical[2]);

	    spherical = calcTextureMap(v2);
	    worldrenderer.addVertexWithUV(radius * v2[0], radius * v2[1], radius * v2[2], spherical[1], spherical[2]);
	    
	    spherical = calcTextureMap(v3);
	    worldrenderer.addVertexWithUV(radius * v3[0], radius * v3[1], radius * v3[2], spherical[1], spherical[2]);

	    tessellator.draw();
	  }
	}
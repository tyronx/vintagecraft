package at.tyron.vintagecraft.Client.Render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ShootingStar {
	double xPos;
	double zPos;
	
	double xDir;
	double zDir;
	
	double xOffset;
	double zOffset;

	double xOffset2;
	double zOffset2;

	public float angle;
	
	float size;
	int age;
	int maxAge;
	
	float skyRadius = 100f;
	
	

	public ShootingStar (double xPos, double zPos, float startSize) {
		maxAge = age = (int) (40*startSize);
		size = startSize;
		angle = (float) (new Random().nextFloat() * 2 * Math.PI);
		
		this.xPos = xPos;
		this.zPos = zPos;
	}
	
	public void tick() {
		age--;
		
		xOffset += 18*MathHelper.sin(angle);
		zOffset += 18*MathHelper.cos(angle);
		
		xOffset2 += 14*MathHelper.sin(angle);
		zOffset2 += 14*MathHelper.cos(angle);
	}
	
	public boolean isDead() {
		return age < 0;
	}
	
	
	public void render() {
		GL11.glPushMatrix();
			GL11.glRotatef(angle, 0, 1f, 0f);

	        RenderUtils.startDrawing(GL11.GL_LINES);
	        GL11.glLineWidth(0.5f);
	        float col = Math.min(1f, age/10f);
	        GL11.glColor3f(col, col, col);
	        
	        Vec3 p1 = new Vec3(xPos + xOffset2, skyRadius, zPos + zOffset2);
	        Vec3 p3 = new Vec3(xPos + xOffset, skyRadius, zPos + zOffset);
	        
	        RenderUtils.addVertexWithUV(p1.xCoord, p1.yCoord, p1.zCoord, 0, 0);   // bottom left
	        RenderUtils.addVertexWithUV(p3.xCoord, p3.yCoord, p3.zCoord, 1, 1);   // top right
	
	        
	        RenderUtils.tessellator.draw();
        GL11.glPopMatrix();
	}

}

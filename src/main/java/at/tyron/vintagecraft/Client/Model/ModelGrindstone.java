package at.tyron.vintagecraft.Client.Model;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;

public class ModelGrindstone extends ModelBaseVC {
	ModelRendererVC stone;
	
	public ModelGrindstone() {
		initComponents();
	}
	
	public void initComponents() {
		stone = new ModelRendererVC(this);
		
		stone.cubeList.add(newBox(stone, 1, 0, 1, 14, 6, 14));
	}
	
	public void renderGrindStone() {
		stone.render(0.0625F / resolution);
		
/*		GL11.glPushMatrix();
			GL11.glTranslatef(0, -0.001f, 0f);
			
			GL11.glTranslatef(0.5f, 0.5f, 0.5f);
			GL11.glRotatef(22.5f, 0, 1, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			
			stone.render(0.0625F / resolution);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			GL11.glTranslatef(0, -0.002f, 0f);
			GL11.glTranslatef(0.5f, 0.5f, 0.5f);
			GL11.glRotatef(-22.5f, 0, 1, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
			stone.render(0.0625F / resolution);
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
			GL11.glTranslatef(0, -0.003f, 0f);
			GL11.glTranslatef(0.5f, 0.5f, 0.5f);
			GL11.glRotatef(45f, 0, 1, 0);
			GL11.glTranslatef(-0.5f, -0.5f, -0.5f);

			stone.render(0.0625F / resolution);
		GL11.glPopMatrix();
	*/	
		
	}
}

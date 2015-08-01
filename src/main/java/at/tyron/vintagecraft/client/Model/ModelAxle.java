package at.tyron.vintagecraft.Client.Model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;

public class ModelAxle extends ModelBase {
	ModelRendererVC hori;
	//ModelRendererVC vert;
	
	ModelRendererVC support;
	
	public ModelAxle() {
		initComponents();
	}
	
	public void initComponents() {
		hori = new ModelRendererVC(this);
	//	vert = new ModelRendererVC(this);
		support = new ModelRendererVC(this);
		
		//      								            x1  y1  z1   wdt hgt depth scalechange
		hori.cubeList.add(new ModelBox(hori,        0, 0, 14f, 12f, 0F,     4, 8, 32, 0));
		hori.cubeList.add(new ModelBox(hori,        0, 0, 12f, 14f, 0.05F,     8, 4, 32, 0));
		//vert.cubeList.add(new ModelBox(hori,        0, 0, 12f, 14f, 0.05F,     8, 4, 32, 0));
		
		support.cubeList.add(new ModelBox(support,  0, 0, 10F,  0f, 4F,     12, 24, 2, 0));
	}
	
	
	public void renderAxle() {
		hori.render(0.0625F / 2f);
		///vert.render(0.0625F / 2f);
	}
	
	public void renderSupport() {
		support.render(0.0625F / 2f);
	}
}

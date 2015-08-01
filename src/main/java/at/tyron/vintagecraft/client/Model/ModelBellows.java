package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelBellows extends ModelBase {
	public ModelRendererVC woodCover;
	public ModelRendererVC leather;
	public ModelRendererVC ironTip;
	
	public ModelRendererVC gearBase;
	public ModelRendererVC gearPegs;
	
	public ModelBellows() {	
		initComponents();
	}
	
	public void initComponents() {
		//      								          x1  y1  z1   wdt hgt depth scalechange
		
		woodCover = new ModelRendererVC(this);
		leather = new ModelRendererVC(this);
		ironTip = new ModelRendererVC(this);
		gearBase = new ModelRendererVC(this);
		gearPegs = new ModelRendererVC(this);
		
		woodCover.cubeList.add(new ModelBox(woodCover, 0, 0, 2f, 0f, 2f, 28, 2, 28, 0));
		
		// Leather Base
		leather.cubeList.add(new ModelBox(leather, 0, 0, 4f, 2f, 4f, 24, 24, 24, 0));
		// Leather lower fold
		leather.cubeList.add(new ModelBox(leather, 0, 0, 2f, 10f, 2f, 28, 2, 28, 0));
		// Leather upper fold
		leather.cubeList.add(new ModelBox(leather, 0, 0, 2f, 20f, 2f, 28, 2, 28, 0));
		
		
		//      								    x1  y1  z1   wdt hgt detph scalechange
		gearBase.cubeList.add(new ModelBox(gearBase, 0, 0,  5F, 5f, 0F,    22, 22, 2, 0));
		gearPegs.cubeList.add(new ModelBox(gearPegs, 0, 0,  9F + 0.5f, 9f + 0.5f, 1F, 2, 2, 4, 0));
		/*gearPegs.cubeList.add(new ModelBox(gearPegs, 0, 0,  4F + 21.5f, 4f + 0.5f, 1F, 2, 2, 8, 0));
		gearPegs.cubeList.add(new ModelBox(gearPegs, 0, 0,  4F + 0.5f, 4f + 21.5f, 1F, 2, 2, 8, 0));
		gearPegs.cubeList.add(new ModelBox(gearPegs, 0, 0,  4F + 21.5f, 4f + 21.5f, 1F, 2, 2, 8, 0));*/		
		
		
	}
		
}

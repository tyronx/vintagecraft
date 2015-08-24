package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelVessel extends ModelBase {
	ModelRendererVC []boxes;
	
	float[][] boxSizes = new float[][]{
		new float[]{6, 2},
		new float[]{8, 4},
		new float[]{10, 2},
		new float[]{12, 2},
		new float[]{14, 8},
		new float[]{12, 2},
		new float[]{10, 1},
		new float[]{6, 1}
	};
	
	public ModelVessel() {
		boxes = new ModelRendererVC[boxSizes.length];
		int bottom = 0;
		
		for (int i = 0; i < boxSizes.length; i++) {
			boxes[i] = new ModelRendererVC(this);
			
			float wdt = boxSizes[i][0];
			float hgt = boxSizes[i][1];
			float left = 32 - wdt;
			
			boxes[i].cubeList.add(new ModelBox(boxes[i], 0, (int) hgt, left / 2, 22 - bottom - hgt, left/ 2, (int)wdt, (int)hgt, (int)wdt, 0));
			bottom += hgt;
		}
	}
	
	
	public void render() {
		for (int i = 0; i < boxes.length; i++) {
			boxes[i].render(0.0625F / 2F);
		}
	}
	
}

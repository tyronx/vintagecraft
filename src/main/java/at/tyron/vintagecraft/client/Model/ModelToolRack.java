package at.tyron.vintagecraft.client.Model;

import at.tyron.vintagecraft.client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelToolRack extends ModelBase {
	ModelRendererVC upperStrip;
	ModelRendererVC lowerStrip;
	
	ModelRendererVC[] nails = new ModelRendererVC[4];
	
	public ModelToolRack() {
		upperStrip = new ModelRendererVC(this);
		lowerStrip = new ModelRendererVC(this);
		
		upperStrip.cubeList.add(new ModelBox(upperStrip, 0, 0, 0F, 8, 0.1F, 32, 3, 2, 0));
		lowerStrip.cubeList.add(new ModelBox(upperStrip, 0, 4, 0F, 24, 0.1F, 32, 3, 2, 0));
		
		for (int i = 0; i < nails.length; i++) {
			nails[i] = new ModelRendererVC(this);
			
			nails[i].cubeList.add(new ModelBox(upperStrip, 0, 0, 
				(i % 2 > 0) ? 3 : 27,					// x 
				(i > 1) ? 9 : 25, 						// y
				2, 										// z
				1, 1, 1, 0));
		}
	}
	
	
	public void renderWood() {
		upperStrip.render(0.0625F / 2F);
		lowerStrip.render(0.0625F / 2F);
	}
	
	public void renderNails() {
		for (int i = 0; i < nails.length; i++) {
			nails[i].render(0.0625F / 2F);
		}
	}
}

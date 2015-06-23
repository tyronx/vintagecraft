package at.tyron.vintagecraft.Client.Model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;

public class ModelGearComponents extends ModelBase {
	ModelRendererVC base;
	ModelRendererVC[] pegs = new ModelRendererVC[4];
	
	public ModelGearComponents() {
		initComponents();
	}
	
	public void initComponents() {
		base = new ModelRendererVC(this);
		//      								    x1  y1  z1   wdt hgt detph scalechange
		base.cubeList.add(new ModelBox(base, 0, 0,  8F, 8f, 0F,    16, 16, 2, 0));
		
		for (int i = 0; i < pegs.length; i++) {
			pegs[i] = new ModelRendererVC(this);
		}
		
		pegs[0].cubeList.add(new ModelBox(pegs[0], 0, 0,  8F + 0.5f, 8f + 0.5f, 1F, 2, 2, 8, 0));
		pegs[1].cubeList.add(new ModelBox(pegs[1], 0, 0,  8F + 13.5f, 8f + 0.5f, 1F, 2, 2, 8, 0));
		pegs[2].cubeList.add(new ModelBox(pegs[2], 0, 0,  8F + 0.5f, 8f + 13.5f, 1F, 2, 2, 8, 0));
		pegs[3].cubeList.add(new ModelBox(pegs[3], 0, 0,  8F + 13.5f, 8f + 13.5f, 1F, 2, 2, 8, 0));		
	}
	
	
	public void renderBase() {
		base.render(0.0625F / 2f);
	}
	
	public void renderPegs() {
		for (int i = 0; i < pegs.length; i++) {
			pegs[i].render(0.0625F / 2f);
		}
	}

	
	
	
}


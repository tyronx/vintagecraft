package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelWindmillRotor extends ModelBase {
	ModelRendererVC axleHoriPart;
	ModelRendererVC axleVertiPart;
	
	ModelRendererVC horiBlade;
	ModelRendererVC vertiBlade;
	
	
	public ModelWindmillRotor() {
		initComponents();
	}
	
	public void initComponents() {
		axleHoriPart = new ModelRendererVC(this);
		axleVertiPart = new ModelRendererVC(this);
		
		horiBlade = new ModelRendererVC(this);
		vertiBlade = new ModelRendererVC(this);
		
		//      								               x1  y1  z1     wdt hgt depth scalechange
		axleHoriPart.cubeList.add(new ModelBox(axleHoriPart,            0, 0, 13f, 10f, 0F,     6, 12, 32,    0));
		axleVertiPart.cubeList.add(new ModelBox(axleVertiPart,            0, 0, 10f, 13f, 0.05F,     12, 6, 32,    0));
		
		horiBlade.cubeList.add(new ModelBox(horiBlade,  0, 0,  0f, 14.5f, 20F, 32, 4, 4,    0));
		vertiBlade.cubeList.add(new ModelBox(vertiBlade,0, 0,  14f, 0f,  20F,  4,32, 4,    0));
		
	}
	
	
	public void renderRotor() {
		axleHoriPart.render(0.0625F / 2f);
		axleVertiPart.render(0.0625F / 2f);
		horiBlade.render(0.0625F / 2f);
		vertiBlade.render(0.0625F / 2f);
	}
	
}

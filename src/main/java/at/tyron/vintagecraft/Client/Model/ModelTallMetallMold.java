package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelTallMetallMold extends ModelBase {
	public ModelRendererVC fireclayMolds;
	
	// Molten Metal renderers
	public ModelRendererVC ingots;
	public ModelRendererVC canal;
	public ModelRendererVC furnaceHole;
	
	public ModelTallMetallMold() {
		initComponents();
	}
	
	public void initComponents() {
		fireclayMolds = new ModelRendererVC(this);
		ingots = new ModelRendererVC(this);
		canal = new ModelRendererVC(this);
		furnaceHole = new ModelRendererVC(this);
		//      								            x1  y1  z1   wdt hgt depth scalechange
//		hori.cubeList.add(new ModelBox(hori,        0, 0, 14f, 12f, 0F,     4, 8, 32, 0));

		// Base
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 0f, 0f, 0f, 32, 24, 32, 0));
		// Left border
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 0f, 24f, 0f, 4, 6, 32, 0));
		// Right border
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 28f, 24f, 0f, 4, 6, 32, 0));
		// Bottom border
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 4f, 24f, 28f, 24, 6, 4, 0));
		// Topleft border
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 4f, 24f, 0f, 10, 6, 4, 0));
		// Topright border
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 18f, 24f, 0f, 10, 6, 4, 0));
		
		// Seperator 1 right
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 18f, 24f, 10.5f, 10, 6, 2, 0));
		// Seperator 2 right
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 18f, 24f, 19f, 10, 6, 2, 0));
		
		// Seperator 1 left
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 4f, 24f, 10.5f, 10, 6, 2, 0));
		// Seperator 2 left
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 4f, 24f, 19f, 10, 6, 2, 0));
		
		// Canal
		fireclayMolds.cubeList.add(new ModelBox(fireclayMolds, 0, 0, 14f, 24f, 0f, 4, 5, 28, 0));
		
		
		canal.cubeList.add(new ModelBox(canal, 0, 0, 14f, 29f, 0f, 4, 1, 28, 0));
		furnaceHole.cubeList.add(new ModelBox(furnaceHole, 0, 0, 12f, 32f, -6f, 8, 8, 7, 0));
		ingots.cubeList.add(new ModelBox(ingots, 0, 0, 4f, 24f, 4f, 24, 5, 24, 0));
	}
	
	
}


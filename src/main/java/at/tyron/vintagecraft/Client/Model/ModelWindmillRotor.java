package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelWindmillRotor extends ModelBase {
	int maxSize = 4;
	
	ModelRendererVC axleHoriPart;
	ModelRendererVC axleVertiPart;
	
	ModelRendererVC horiBlade[];
	ModelRendererVC vertiBlade[];
	
	
	ModelRendererVC horiWingFirst[];
	ModelRendererVC vertiWingFirst[];
	
	ModelRendererVC horiWingSecond[];
	ModelRendererVC vertiWingSecond[];

	
	public ModelWindmillRotor() {
		initComponents();
	}
	
	public void initComponents() {
		axleHoriPart = new ModelRendererVC(this);
		axleVertiPart = new ModelRendererVC(this);
		
		horiBlade = new ModelRendererVC[maxSize+1];
		vertiBlade = new ModelRendererVC[maxSize+1];

		horiWingFirst = new ModelRendererVC[maxSize];
		vertiWingFirst = new ModelRendererVC[maxSize];
		horiWingSecond = new ModelRendererVC[maxSize];
		vertiWingSecond = new ModelRendererVC[maxSize];

		
		//      								               x1  y1  z1     wdt hgt depth scalechange
		axleHoriPart.cubeList.add(new ModelBox(axleHoriPart,            0, 0, 13f, 10f, 0F,     6, 12, 32,    0));
		axleVertiPart.cubeList.add(new ModelBox(axleVertiPart,            0, 0, 10f, 13f, 0.05F,     12, 6, 32,    0));
		
		for (int i = 0; i < maxSize+1; i++) {
			horiBlade[i] = new ModelRendererVC(this);
			vertiBlade[i] = new ModelRendererVC(this);
					
			horiBlade[i].cubeList.add(new ModelBox(  horiBlade[i],  0, 0,  -32*i, 14.5f, 20F, 32  + 64 * i, 4, 4,   0));
			vertiBlade[i].cubeList.add(new ModelBox(vertiBlade[i],  0, 0,  14f, -32*i,  20F,  4, 32 + 64 * i, 4,    0));
		}
		
		for (int i = 0; i < maxSize; i++) {
			horiWingFirst[i] = new ModelRendererVC(this);
			vertiWingFirst[i] = new ModelRendererVC(this);

			horiWingSecond[i] = new ModelRendererVC(this);
			vertiWingSecond[i] = new ModelRendererVC(this);

			int ip = i+1;
			
			horiWingFirst[i].cubeList.add(new ModelBox(  horiWingFirst[i],  0, 0,  -32*ip, 14.5f - 24, 20F + 2, 32 * ip - 8, 24, 1,   0));
			vertiWingFirst[i].cubeList.add(new ModelBox(vertiWingFirst[i],  0, 0,  14f + 4, -32*ip,  20F + 2,  24, 32 * ip - 8, 1,    0));

			horiWingSecond[i].cubeList.add(new ModelBox(  horiWingSecond[i],  0, 0,  32 , 14.5f + 4, 20F + 2, 32 * ip, 24, 1,   0));
			vertiWingSecond[i].cubeList.add(new ModelBox(vertiWingSecond[i],  0, 0,  14f - 24, 32,  20F + 2,  24, 32 * ip, 1,    0));
		}
		
		

	}
	
	
	// bladeSize is in blocks 0-3
	public void renderRotor(int bladeSize) {
		axleHoriPart.render(0.0625F / 2f);
		axleVertiPart.render(0.0625F / 2f);
		
		horiBlade[bladeSize].render(0.0625F / 2f);
		vertiBlade[bladeSize].render(0.0625F / 2f);
		
	}
	
	public void renderWings(int bladeSize) {
		if (bladeSize > 0) {
			horiWingFirst[bladeSize - 1].render(0.0625F / 2f);
			vertiWingFirst[bladeSize - 1].render(0.0625F / 2f);
			
			horiWingSecond[bladeSize - 1].render(0.0625F / 2f);
			vertiWingSecond[bladeSize - 1].render(0.0625F / 2f);

		}		
	}
	
}

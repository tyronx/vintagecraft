package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelStonePot extends ModelBase {
	ModelRendererVC[] stonewalls = new ModelRendererVC[5];
	ModelRendererVC[] ingots = new ModelRendererVC[8];
	ModelRendererVC[] ingotsGlow = new ModelRendererVC[8];
	
	ModelRendererVC fillmaterial;
	int fillmaterialheight = 1;
	
	int scale = 2;
	
	int stoneWallWidth = 12 * scale;
	int stoneWallHeight = 14 * scale;
	int stoneWallThickness = 2 * scale;
	int stoneWallPadding = 1 * scale;

	
	public ModelStonePot() {
		initStoneWalls();
		initIngots();
		initFillMaterial(fillmaterialheight);
	}
	

	
	void initFillMaterial(int height) {
		fillmaterial = new ModelRendererVC(this);
		fillmaterial.cubeList.add(new ModelBox(fillmaterial, 
			0, 
			0, 
			stoneWallPadding + stoneWallThickness,							// x 
			stoneWallThickness, 											// y
			stoneWallPadding + stoneWallThickness,							// z
			stoneWallWidth - stoneWallThickness,							// width 
			height * scale,		 											// height
			stoneWallWidth - stoneWallThickness,							// length
			0
		));
		
		fillmaterialheight = height;
	}
	
	
	
	
	void initStoneWalls() {
		
		int i = 0;
		
		// Left wall
		stonewalls[i] = new ModelRendererVC(this);
		stonewalls[i].cubeList.add(new ModelBox(stonewalls[i], 
			0, 
			0, 
			stoneWallPadding,										// x 
			0, 											// y
			stoneWallPadding,	 									// z
			stoneWallThickness,									// width 
			stoneWallHeight, 									// height
			stoneWallWidth,										// length
			0
		));
		
		// Right wall
		i++;
		stonewalls[i] = new ModelRendererVC(this);
		stonewalls[i].cubeList.add(new ModelBox(stonewalls[i], 
			8, 
			8, 
			stoneWallWidth + stoneWallPadding,								// x 
			0, 										// y
			stoneWallPadding + stoneWallThickness,							// z
			stoneWallThickness,									// width 
			stoneWallHeight, 									// height
			stoneWallWidth,										// length
			0
		));

		
		// Top wall
		i++;
		stonewalls[i] = new ModelRendererVC(this);
		stonewalls[i].cubeList.add(new ModelBox(stonewalls[i], 
			4, 
			4, 
			stoneWallPadding + stoneWallThickness,							// x 
			0, 											// y
			stoneWallPadding,	 									// z
			stoneWallWidth,										// width 
			stoneWallHeight, 									// height
			stoneWallThickness,									// length
			0
		));
		
		
		// Bottom wall
		i++;
		stonewalls[i] = new ModelRendererVC(this);
		stonewalls[i].cubeList.add(new ModelBox(stonewalls[i], 
			14, 
			4, 
			stoneWallPadding,										// x 
			0, 										// y
			stoneWallPadding + stoneWallWidth,								// z
			stoneWallWidth,										// width 
			stoneWallHeight, 									// height
			stoneWallThickness,									// length
			0
		));
		
		

		// Floor
		i++;
		stonewalls[i] = new ModelRendererVC(this);
		stonewalls[i].cubeList.add(new ModelBox(stonewalls[i], 
			0, 
			0, 
			stoneWallPadding + stoneWallThickness,										// x 
			0, 											// y
			stoneWallPadding + stoneWallThickness,										// z
			stoneWallWidth - stoneWallThickness,										// width 
			stoneWallThickness, 									// height
			stoneWallWidth - stoneWallThickness,										// length
			0
		));	
	}
	
	
	
	void initIngots() {
		for (int n = 0; n < ingots.length; n++){
			this.ingots[n] = new ModelRendererVC(this, 0, 0);
			int m = (n + 2)/2;
			float x = (n % 4)*0.25f + 0.25f;
			float y = (m - 1)*0.125f + 0.25f;
			float z = 0.5f;

			//if (n % 2 >= 1) z = .5F;
			
			ingots[n].cubeList.add(new ModelIngot(ingots[n], ingots[n].textureOffsetX, ingots[n].textureOffsetY));
			ingots[n].offsetY = y;
			
			if (m % 2 == 1) {
				ingots[n].rotateAngleY = 1.56F;
				ingots[n].offsetX = x;
				ingots[n].offsetZ = z + 0.25F;
			} else {
				ingots[n].offsetX = z - 0.25f;
				ingots[n].offsetZ = x - 0.5f;
			}
			
			
			
			
			x = (n % 4)*0.22f + 0.22f;
			z = 0.45f;
			y = (m - 1)*0.11f + 0.25f - 0.015f;
			
			ingotsGlow[n] = new ModelRendererVC(this, 0, 0);
			ingotsGlow[n].cubeList.add(new ModelIngot(ingotsGlow[n], ingotsGlow[n].textureOffsetX, ingotsGlow[n].textureOffsetY));
			ingotsGlow[n].offsetY = y;

			if (m % 2 == 1) {
				ingotsGlow[n].rotateAngleY = 1.56F;
				ingotsGlow[n].offsetX = x;
				ingotsGlow[n].offsetZ = z + 0.25F;
			} else {
				ingotsGlow[n].offsetX = z - 0.25f;
				ingotsGlow[n].offsetZ = x - 0.44f;
			}
			
		}
	}
	
	
	
	public void renderWalls() {
		for (int i = 0; i < stonewalls.length; i++) {
			stonewalls[i].render(0.0625F / 2f);
		}
	}
	
	public void renderIngots(int quantity) {
//		initIngots();
		for (int i = 0; i < quantity; i++) {
			ingots[i].render(0.0625F / 2f);
		}
	}


	public void renderIngotsGlow(int quantity) {
//		initIngots();
		for (int i = 0; i < quantity; i++) {
			ingotsGlow[i].render(0.0625F / 2f);
		}
	}

	
	
	public void renderFillmaterial(int height) {
		if (height != fillmaterialheight) initFillMaterial(height);
		
		fillmaterial.render(0.0625F / 2f);
	}

}

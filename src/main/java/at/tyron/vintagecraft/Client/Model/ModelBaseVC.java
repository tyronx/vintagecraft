package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;

public class ModelBaseVC extends ModelBase {
	public static int resolution = 2;
	
	public ModelBox newBox(ModelRendererVC parent, float x, float y, float z, int width, int height, int depth) {
//		woodCover.cubeList.add(new ModelBox(woodCover, 0, 0, 2f, 0f, 2f, 28, 2, 28, 0));
		ModelBox box = new ModelBox(parent, 0, 0, resolution * x, resolution * y, resolution * z, resolution * width, resolution * height, resolution * depth, 0);
		return box;
	}
	
}

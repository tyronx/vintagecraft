package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

public class ModelBucketVC extends ModelBaseVC {
	
	public ModelRendererVC bucket;
	public ModelRendererVC metalRings;
	public ModelRendererVC rope;
	
	public ModelRendererVC contents;
	
	public ModelBucketVC() {
		initComponents();
	}
	
	
	public void initComponents() {
		bucket = new ModelRendererVC(this);
		metalRings = new ModelRendererVC(this);
		rope = new ModelRendererVC(this);
		contents = new ModelRendererVC(this);
		
		contents.cubeList.add(newBox(contents, 4, 1, 4, 8, 10, 8));
		
		// Floor
		bucket.cubeList.add(newBox(bucket, 4, 0, 4, 8, 1, 8));

		// North wall
		bucket.cubeList.add(newBox(bucket, 3, 0, 3, 10, 12, 1));
		// South wall
		bucket.cubeList.add(newBox(bucket, 3, 0, 12, 10, 12, 1));
		// West wall
		bucket.cubeList.add(newBox(bucket, 3, 0, 4, 1, 12, 8));
		// East wall
		bucket.cubeList.add(newBox(bucket, 12, 0, 4, 1, 12, 8));
		// Handle left top
		bucket.cubeList.add(newBox(bucket, 3, 12, 6, 1, 2, 1));
		// Handle left bottom
		bucket.cubeList.add(newBox(bucket, 3, 12, 9, 1, 2, 1));
		// Handle left up
		bucket.cubeList.add(newBox(bucket, 3, 13, 7, 1, 1, 2));
		
		// Handle right top
		bucket.cubeList.add(newBox(bucket, 12, 12, 6, 1, 2, 1));
		// Handle right bottom
		bucket.cubeList.add(newBox(bucket, 12, 12, 9, 1, 2, 1));
		// Handle right bottom
		bucket.cubeList.add(newBox(bucket, 12, 13, 7, 1, 1, 2));
		
		
		// Bands
		rope.cubeList.add(newBox(rope, 4, 12, 7.5f, 1, 2, 1));
		rope.cubeList.add(newBox(rope, 11, 12, 7.5f, 1, 2, 1));
		rope.cubeList.add(newBox(rope, 5, 14, 7.5f, 6, 1, 1));
		// Knots
		rope.cubeList.add(newBox(rope, 12, 12, 7.5f, 2, 1, 1));
		rope.cubeList.add(newBox(rope, 2, 12, 7.5f, 2, 1, 1));
		
			
	}
	
	public void renderWood() {
		bucket.render(0.0625F / resolution);
	}
	
	
	public void renderRope() {
		rope.render(0.0625F / resolution);
	}

	public void renderContents() {
		contents.render(0.0625F / resolution);
	}

	public void renderMetalRings() {
		metalRings.render(0.0625F / resolution);
	}

	
	
	
}

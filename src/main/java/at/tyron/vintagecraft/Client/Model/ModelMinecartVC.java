package at.tyron.vintagecraft.Client.Model;

import at.tyron.vintagecraft.Client.Render.Model.ModelRendererVC;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelMinecartVC extends ModelBaseVC {
	public ModelRenderer[] sides;
	public ModelRenderer chimney;
	
	public ModelMinecartVC() {
		initComponents();
	}
	
	public void initComponents() {
		sides = new ModelRenderer[11];
		
        this.sides[0] = new ModelRenderer(this, 0, 10);
        this.sides[1] = new ModelRenderer(this, 0, 0);
        this.sides[2] = new ModelRenderer(this, 0, 0);
        this.sides[3] = new ModelRenderer(this, 0, 0);
        this.sides[4] = new ModelRenderer(this, 0, 0);
        //this.sides[5] = new ModelRenderer(this, 44, 10);
        
        this.sides[5] = new ModelRenderer(this, 0, 0);
        this.sides[6] = new ModelRenderer(this, 0, 0);
        this.sides[7] = new ModelRenderer(this, 0, 0);
        this.sides[8] = new ModelRenderer(this, 0, 0);
        
        this.sides[9] = new ModelRenderer(this, 65, 5);
        this.sides[10] = new ModelRenderer(this, 56, 14);
        
        
        int b0 = 20;
        int b1 = 8;
        int b2 = 16;
        int rotatePosY = 4;
        
        int width = 2;
        
        // Floor
        this.sides[0].addBox(-b0 / 2, -b2 / 2, -1.0F, b0, b2, 2, 0.0F);
        this.sides[0].setRotationPoint(0.0F, rotatePosY, 0.0F);
        
        // Short sides
        this.sides[1].addBox(-b0 / 2 + 2,-b1 - 1, 0.0F, b0 - 4, b1, 1, 0.0F);
        this.sides[1].setRotationPoint(-b0 / 2 + 1, rotatePosY, 0.0F);
        
        this.sides[2].addBox(-b0 / 2 + 2,-b1 - 1, -0.0F, b0 - 4, b1, 1, 0.0F);
        this.sides[2].setRotationPoint(b0 / 2 - 1, rotatePosY, 0.0F);
        
        // Long sides 
        this.sides[3].addBox(-b0 / 2 + 1,-b1 - 1, -0.0F, b0 - 2, b1, 1, 0.0F);
        this.sides[3].setRotationPoint(0.0F, rotatePosY, -b2 / 2 + 1);
        
        this.sides[4].addBox(-b0 / 2 + 1,-b1 - 1, -0.0F, b0 - 2, b1, 1, 0.0F);
        this.sides[4].setRotationPoint(0.0F, rotatePosY, b2 / 2 - 1);

        // Front wheel
        this.sides[5].addBox(5, 4 - 11, -2.3f, 1, 14, 1, 0f);
        this.sides[5].setRotationPoint(0.0F, rotatePosY, 0.0F);

        this.sides[6].addBox(4.5f, 4 - 7, -3f, 2, 6, 2, 0f);
        this.sides[6].setRotationPoint(0.0F, rotatePosY, 0.0F);

        // Back wheel
        this.sides[7].addBox(-7, 4 - 11, -2.3f, 1, 14, 1, 0f);
        this.sides[7].setRotationPoint(0.0F, rotatePosY, 0);

        this.sides[8].addBox(-7.5f, 4 - 7, -3f, 2, 6, 2, 0f);
        this.sides[8].setRotationPoint(0.0F, rotatePosY, 0);

        // Steamery
        this.sides[9].addBox(-1, -7, 1.0F, 10, 14, 9, 0.0F);
        this.sides[9].setRotationPoint(0.0F, rotatePosY, 0.0F);

        // Chimney
        this.sides[10].addBox(2, -2f, 10.0F, 4, 4, 10, 0.0F);
        this.sides[10].setRotationPoint(0.0F, rotatePosY, 0.0F);

        
        sides[0].rotateAngleX = ((float)Math.PI / 2F);
        sides[1].rotateAngleY = ((float)Math.PI * 3F / 2F);
        sides[2].rotateAngleY = ((float)Math.PI / 2F);
        sides[3].rotateAngleY = (float)Math.PI;
        
        sides[5].rotateAngleX = (float) Math.PI / 2F;
        sides[6].rotateAngleX = (float) Math.PI / 2F;
        sides[7].rotateAngleX = (float) Math.PI / 2F;
        sides[8].rotateAngleX = (float) Math.PI / 2F;
        
        sides[9].rotateAngleX = (float) Math.PI / 2F;
        sides[10].rotateAngleX = (float) Math.PI / 2F;
	}

	
	public void renderEmptyCart(float scale) {
		for (int i = 0; i < 9; ++i) {
            this.sides[i].render(scale);
        }
	}
	
	public void renderCoalPoweredCart(float scale) {
        for (int i = 0; i < 11; ++i) {
            this.sides[i].render(scale);
        }
    }
	
}

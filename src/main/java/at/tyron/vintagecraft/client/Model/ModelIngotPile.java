package at.tyron.vintagecraft.Client.Model;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraftforge.fml.relauncher.*;
import at.tyron.vintagecraft.Client.Render.Model.*;

@SideOnly(Side.CLIENT)
public class ModelIngotPile extends ModelBase {
	public ModelRendererVC[] renderer = new ModelRendererVC[64];

	public ModelIngotPile() {
		int []offsets = new int[128];
		Random rand = new Random();
		
		for (int i = 0; i < 128; i++) {
			offsets[i] = rand.nextInt(16);
		}
		
		for (int n = 0; n < 64; n++){
			this.renderer[n] = new ModelRendererVC(this, offsets[n], offsets[n + 64]);
			int m = (n+8)/8;
			float x = (n %4)*0.25f;
			float y = (m -1)*0.125f;
			float z = 0;

			if (n%8 >=4) z = .5F;
			
			renderer[n].cubeList.add(new ModelIngot(renderer[n], renderer[n].textureOffsetX, renderer[n].textureOffsetY));
			renderer[n].offsetY = y;
			if (m %2 == 1) {
				renderer[n].rotateAngleY = 1.56F;
				renderer[n].offsetX = x;
				renderer[n].offsetZ = z+.5F;
			} else {
				renderer[n].offsetX = z;
				renderer[n].offsetZ = x;
			}
		}
	}

	public void renderIngots(int i) {
		
		for (int n = 0; n < i; n++) {
			renderer[n].render(0.0625F / 2F);
		}
	}
}
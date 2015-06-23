package at.tyron.vintagecraft.Client.Render;

import net.minecraft.client.renderer.entity.RenderCaveSpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.util.ResourceLocation;

public class RenderForestSpider extends RenderCaveSpider {
	private static final ResourceLocation forestspiderTextures = new ResourceLocation("vintagecraft:textures/entity/spider/forestspider.png");
	
	public RenderForestSpider(RenderManager p_i46139_1_) {
		super(p_i46139_1_);
	}
	
    protected ResourceLocation getEntityTexture(EntityCaveSpider p_180586_1_) {
        return forestspiderTextures;
    }

	
}

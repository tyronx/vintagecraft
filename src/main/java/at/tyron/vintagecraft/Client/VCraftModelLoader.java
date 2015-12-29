package at.tyron.vintagecraft.Client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.primitives.Ints;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;


public class VCraftModelLoader implements ICustomModelLoader {
	public static final VCraftModelLoader instance = new VCraftModelLoader();
    public static final ResourceLocation dummyTexture = new ResourceLocation("vintagecraft:blocks/stove.b3d");

    public boolean accepts(ResourceLocation modelLocation)
    {
    	if (modelLocation.getResourceDomain().equals("vintagecraft") && modelLocation.getResourcePath().equals("stove")) {
    		System.out.println("bla / " + modelLocation.getResourcePath());
    	}
        return modelLocation.getResourceDomain().equals("vintagecraft") && modelLocation.getResourcePath().equals("models/block/stove.b3d");
    }

    public IModel loadModel(ResourceLocation model)
    {
        return VCraftModel.instance;
    }

    public static enum VCraftModel implements IModel
    {
        instance;

        public Collection<ResourceLocation> getDependencies()
        {
            return Collections.emptyList();
        }

        public Collection<ResourceLocation> getTextures()
        {
            return Collections.singletonList(dummyTexture);
        }

        public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> textures)
        {
            return new VCraftBakedModel(textures.apply(dummyTexture));
        }

        public IModelState getDefaultState()
        {
            return ModelRotation.X0_Y0;
        }
    }

    public static class VCraftBakedModel implements IFlexibleBakedModel
    {
        private final TextureAtlasSprite texture;

        public VCraftBakedModel(TextureAtlasSprite texture)
        {
            this.texture = texture;
        }

        public List<BakedQuad> getFaceQuads(EnumFacing side)
        {
            return Collections.emptyList();
        }

        private int[] vertexToInts(float x, float y, float z, int color, float u, float v)
        {
            return new int[] {
                Float.floatToRawIntBits(x),
                Float.floatToRawIntBits(y),
                Float.floatToRawIntBits(z),
                color,
                Float.floatToRawIntBits(texture.getInterpolatedU(u)),
                Float.floatToRawIntBits(texture.getInterpolatedV(v)),
                0
            };
        }

        public List<BakedQuad> getGeneralQuads()
        {
            List<BakedQuad> ret = new ArrayList<BakedQuad>();
            // 1 half-way rotated quad looking UP
            ret.add(new BakedQuad(Ints.concat(
                vertexToInts(0, .5f, .5f, -1, 0, 0),
                vertexToInts(.5f, .5f, 1, -1, 0, 16),
                vertexToInts(1, .5f, .5f, -1, 16, 16),
                vertexToInts(.5f, .5f, 0, -1, 16, 0)
            ), -1, EnumFacing.UP));
            return ret;
        }

        public boolean isGui3d() { return true; }

        public boolean isAmbientOcclusion() { return true; }

        public boolean isBuiltInRenderer() { return false; }

     
        public ItemCameraTransforms getItemCameraTransforms()
        {
            return ItemCameraTransforms.DEFAULT;
        }

        public VertexFormat getFormat()
        {
            return Attributes.DEFAULT_BAKED_FORMAT;
        }

		@Override
		public TextureAtlasSprite getParticleTexture() {
			return this.texture;
		}
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {}
}

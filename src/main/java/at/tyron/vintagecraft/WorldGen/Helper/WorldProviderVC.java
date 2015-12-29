package at.tyron.vintagecraft.WorldGen.Helper;

import at.tyron.vintagecraft.Client.Render.RenderCloudsVC;
import at.tyron.vintagecraft.Client.Render.RenderSkyVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.ChunkProviderGenerateVC;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderVC extends WorldProvider {
	@Override
	protected void registerWorldChunkManager() {
		super.registerWorldChunkManager();
		
		VCraftWorld.instance = new VCraftWorld(worldObj.getSeed(), this.worldChunkMgr);
     	MinecraftForge.EVENT_BUS.register(VCraftWorld.instance);
	}
	
	
	@Override
	public float getCloudHeight() {
		return 256.0F;
	}
	
	@SideOnly(Side.CLIENT)
	IRenderHandler skyRenderer;
	
    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getSkyRenderer() {
    	if (skyRenderer == null) skyRenderer  = new RenderSkyVC();
        return this.skyRenderer;
    }

	@SideOnly(Side.CLIENT)
	IRenderHandler cloudRenderer;

	@SideOnly(Side.CLIENT)
    public IRenderHandler getCloudRenderer() {
    	if (cloudRenderer == null) cloudRenderer = new RenderCloudsVC();
    	return cloudRenderer;
    }
    
	
	
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderGenerateVC(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), null);
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z) {
		return worldObj.getHeight(new BlockPos(x, 0, z)).getY() > VCraftWorld.instance.seaLevel;
	}
	
	
	@Override
	public String getDimensionName() {
		return "DEFAULT";
	}
	@Override
	public String getInternalNameSuffix() {
		return "vcdefaultworldprovider";
	}
	
	
	
}

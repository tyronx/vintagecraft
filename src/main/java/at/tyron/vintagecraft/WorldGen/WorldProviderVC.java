package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.World.VCraftWorld;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;

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
	
	
	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderGenerateVC(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled(), null);
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z) {
		return worldObj.getHorizon(new BlockPos(x, 0, z)).getY() > VCraftWorld.instance.seaLevel;
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

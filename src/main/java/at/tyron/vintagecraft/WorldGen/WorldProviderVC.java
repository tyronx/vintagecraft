package at.tyron.vintagecraft.WorldGen;

import at.tyron.vintagecraft.VCraftWorld;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.MinecraftForge;

public class WorldProviderVC extends WorldProvider {

	private int moonPhase = 0;
	private int moonPhaseLastCalculated = 0;
	
	
	@Override
	protected void registerWorldChunkManager()
	{
		/**
		 * ChunkEventHandler.onLoadWorld gets called after the NEW World gen stuff.
		 * Trying to make a NEW World will produce a crash because the cache is empty.
		 * ..maybe this is not the best place for this, but it works :)
		 */
		//TFC_Climate.worldPair.put(worldObj, new WorldCacheManager(worldObj));
		//TFC_Core.addCDM(worldObj);
		
		VCraftWorld.instance = new VCraftWorld(worldObj.getSeed());
        // Register the Chunk Load/Save Handler
     	MinecraftForge.EVENT_BUS.register(VCraftWorld.instance);

		super.registerWorldChunkManager();
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
		BlockPos pos = worldObj.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
		
		//if(y < Global.SEALEVEL || y > Global.SEALEVEL + 25) return false;
		
		/*Block b = worldObj.getBlock(x, y, z);
		return (TFC_Core.isSand(b) || TFC_Core.isGrass(b));*/
		
		//worldObj.getBlockState(pos).getBlock() == Blocks.grass;
		return true;
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

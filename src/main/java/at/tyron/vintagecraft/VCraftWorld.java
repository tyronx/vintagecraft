package at.tyron.vintagecraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.Climate;
import at.tyron.vintagecraft.WorldGen.ChunkProviderGenerateVC;
import at.tyron.vintagecraft.WorldProperties.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockGravel;
import at.tyron.vintagecraft.block.BlockSand;
import at.tyron.vintagecraft.block.BlockTopSoil;

public class VCraftWorld {
	static final ResourceLocation grassColormap = new ResourceLocation("vintagecraft:textures/colormap/grass.png");
	private static int[] grassBuffer = new int[65536];
	
	static ArrayList<BlockPos> unpopulatedChunks;
	
	static HashMap<Long, NBTTagCompound> chunkextranbt = new HashMap<Long, NBTTagCompound>();
	static HashMap<Long, NBTTagCompound> chunkextranbt_savequeue = new HashMap<Long, NBTTagCompound>();
	
	
	public static VCraftWorld instance = new VCraftWorld(); 
	
	
	public static void loadGrassColors(IResourceManager resourceManager) {
		try {
			grassBuffer = TextureUtil.readImageData(resourceManager, grassColormap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static void setUnpopChunkList(ArrayList<BlockPos> unpopulatedChunks) {
		VCraftWorld.unpopulatedChunks = unpopulatedChunks;
	}


	
		
	static NBTTagCompound getChunkNBT(int chunkX, int chunkZ) {
		return chunkextranbt.get(ChunkPos2Index(chunkX, chunkZ));
	}
	static NBTTagCompound getChunkNBT(BlockPos blockpos) {
		return chunkextranbt.get(BlockPos2Index(blockpos));
	}
	
	
	public static void setChunkNBT(int chunkX, int chunkZ, String key, int[] data) {
		long index = ChunkPos2Index(chunkX, chunkZ);
		
		int x = 0;
		
		NBTTagCompound nbt = chunkextranbt_savequeue.get(index);
		if (nbt == null) {
			chunkextranbt.get(index);
			x = 1;
		}
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
			x = 2;
		}
		
		//System.out.println("set nbt "+key+" for chunk " + chunkX + "/" + chunkZ + " (@index " + index + ")   x="+x);
		
		nbt.setIntArray(key, data);
		
		chunkextranbt_savequeue.put(index, nbt);
		chunkextranbt.put(index, nbt);
	}
	
	
	  
	

	public static void setChunkNBT(int chunkX, int chunkZ, String key, boolean value) {
		long index = ChunkPos2Index(chunkX, chunkZ);
		
		NBTTagCompound nbt = chunkextranbt_savequeue.get(index);
		if (nbt == null) chunkextranbt.get(index);
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		
		nbt.setBoolean(key, value);
		
		chunkextranbt_savequeue.put(index, nbt);
		chunkextranbt.put(index, nbt);
	}
	

	
	//@SubscribeEvent
	public void loadChunk(ChunkDataEvent.Load event) {
		NBTTagCompound nbt = event.getData().getCompoundTag("vintagecraft");
		
		chunkextranbt.put(Chunk2Index(event.getChunk()), nbt);
		
		if (nbt.hasKey("vcraftpopulated") && !nbt.getBoolean("vcraftpopulated")) {
			unpopulatedChunks.add(new BlockPos(event.getChunk().xPosition, 0, event.getChunk().zPosition));
		}
		
		//System.out.println("loaded nbt with chunk " + event.getChunk().xPosition + "/" + event.getChunk().zPosition);
	}
	

	//@SubscribeEvent
	public void saveChunk(ChunkDataEvent.Save event) {
		long index = Chunk2Index(event.getChunk());
		NBTTagCompound nbt = chunkextranbt_savequeue.get(index);
		
		if (nbt != null) {
			event.getData().setTag("vintagecraft", nbt);
			chunkextranbt.put(index, nbt);
			chunkextranbt_savequeue.remove(index);
			System.out.println("saved nbt with chunk " + event.getChunk().xPosition + "/" + event.getChunk().zPosition);
		}
		
		
	}
	
	
	
	//@SubscribeEvent
	public void chunkUnload(ChunkEvent.Unload event) {
		chunkextranbt.remove(Chunk2Index(event.getChunk()));
		chunkextranbt_savequeue.remove(Chunk2Index(event.getChunk()));
	}
	
    
   // @SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload event) {
		chunkextranbt.clear();
		chunkextranbt_savequeue.clear();
	}


    private static int _getClimate(BlockPos pos) {
    	NBTTagCompound nbt = getChunkNBT(pos);
    	
    	if (nbt == null || !nbt.hasKey("climate")) System.out.println("climate array for chunk " + (pos.getX()>>4) + "/" + + (pos.getZ()>>4) + " missing!" + " (@index " + BlockPos2Index(pos) + ")");
    	
    	
    	return nbt.getIntArray("climate")[((pos.getZ() & 15) << 4) + (pos.getX() & 15)];
    }

    // Returns climate = int[temp, fertility, rain] 
    public static int[] getClimate(BlockPos pos) {
    	NBTTagCompound nbt = getChunkNBT(pos);
    	
    	if (!nbt.hasKey("climate")) System.out.println("climate array for chunk " + (pos.getX()>>4) + "/" + + (pos.getZ()>>4) + " missing!");
    	
    	int climate = nbt.getIntArray("climate")[((pos.getZ() & 15) << 4) + (pos.getX() & 15)];
    	
    	return new int[]{normalizeTemperature((climate >> 16) & 0xff), (climate >> 8) & 0xff, climate & 0xff};
    }
    
    public static int getTemperature(BlockPos pos) {
    	return normalizeTemperature((_getClimate(pos) >> 16) & 0xff);
    }
    
    public static int getRainfall(BlockPos pos) {
    	return (_getClimate(pos) >> 0) & 0xff;
    }
    
    public static int getFertily(BlockPos pos) {
    	return (_getClimate(pos) >> 8) & 0xff;
    }
    
    
    public static int getGrassColorAtPos(BlockPos pos) {
    	int climate = _getClimate(pos);
    	
    	int temperature = (climate >> 16) & 0xff;
    	int rainfall = climate & 0xff;
    	//System.out.println(temperature + "/" + (255-rainfall));
    	return grassBuffer[temperature + 256 * (255-rainfall)];
    }
	


    
    
    public static int normalizeTemperature(int temperature) {
    	return (int) (temperature / 4.25f) - 30;
    }
    
    
    
    public static int getForest(BlockPos pos) {
    	int forest = getChunkNBT(pos).getIntArray("forest")[((pos.getZ() & 15) << 4) + (pos.getX() & 15)];
    	
    	return 255 - (forest & 0xff);
    }
    

    
    
    
    
    
    
    public static IBlockState getTopLayerAtPos(int x, int y, int z, EnumRockType rocktype) {
    	BlockPos pos = new BlockPos(x, y, z);
		int temperature = getTemperature(pos);
		int rainfall = getRainfall(pos);
		int fertilityvalue = getFertily(pos);
		
		EnumFertility fertility = EnumFertility.fromFertilityValue(fertilityvalue);
		
		if (fertility != null) {
			EnumOrganicLayer layer = EnumOrganicLayer.fromClimate(rainfall, temperature);
			return BlocksVC.topsoil.getDefaultState().withProperty(BlockTopSoil.organicLayer, layer).withProperty(BlockTopSoil.fertility, fertility);
		} else {
			if (temperature < 10) {
				return BlocksVC.gravel.getDefaultState().withProperty(BlockGravel.STONETYPE, rocktype);
			} else {
				return BlocksVC.sand.getDefaultState().withProperty(BlockSand.STONETYPE, rocktype);
			}
		}
	}

	public static IBlockState getSubLayerAtPos(int x, int y, int z, EnumRockType rocktype) {
		int fertilityvalue = getFertily(new BlockPos(x, y, z));
		EnumFertility fertility = EnumFertility.fromFertilityValue(fertilityvalue);
		int temperature = getTemperature(new BlockPos(x, y, z));
		
		if (fertility != null) {
			return rocktype.getRockVariantForBlock(BlocksVC.subsoil);
		} else {
			if (temperature < 10) {
				return BlocksVC.gravel.getDefaultState().withProperty(BlockGravel.STONETYPE, rocktype);
			} else {
				return BlocksVC.sand.getDefaultState().withProperty(BlockSand.STONETYPE, rocktype);
			}
		}
	}


    
    
    
    
      
    static long Chunk2Index(Chunk chunk) {
    	return ChunkPos2Index(chunk.xPosition, chunk.zPosition);
    }
	static long BlockPos2Index(BlockPos pos) {
		return ChunkPos2Index(pos.getX() >> 4, pos.getZ() >> 4);
	}
	static long ChunkPos2Index(int chunkX, int chunkZ) {
		return ((long)chunkX + (long)Integer.MAX_VALUE) + (((long)chunkZ + (long)Integer.MAX_VALUE) << 32); 
	}
    
}

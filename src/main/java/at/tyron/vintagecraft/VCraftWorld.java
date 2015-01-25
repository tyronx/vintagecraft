package at.tyron.vintagecraft;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.sun.security.ntlm.Client;

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
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import at.tyron.vintagecraft.Network.ChunkPutNbt;
import at.tyron.vintagecraft.Network.ChunkRemoveNbt;
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
	
	public static VCraftWorld instance = new VCraftWorld(); 
	
	
	static HashMap<Long, HashMap<String, String>> profiling = new HashMap<Long, HashMap<String,String>>();
	
	static void mark(int chunkX, int chunkZ, String key) {
		long index = ChunkPos2Index(chunkX, chunkZ);
		
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ) {
			//System.out.println("called by client");
			return;
		}
		
		HashMap<String, String> chunk = profiling.get(index);
		if (chunk == null) {
			chunk = new HashMap<String, String>();
			chunk.put("chunkX", "" + chunkX);
			chunk.put("chunkZ", "" + chunkZ);
			chunk.put("list", "");
			chunk.put("counter", "0");
		}
		
		String str = chunk.get(key);
		int num;
		if (str == null) {
			num = 1;
		} else {
			num = Integer.parseInt(str);
			num++;
		}
		chunk.put(key, ""+num);
		
		int counter = Integer.parseInt(chunk.get("counter")) + 1;
		chunk.put("counter", "" + counter);
		chunk.put("list", chunk.get("list") + "\r\n" + counter + " "  + key);
		
		profiling.put(index, chunk);
	}
	
	
	static void printProfiling(String reason) {
		System.out.println("writing chunknbt.txt");

		Writer writer;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("chunknbt.txt"), "utf-8"));

			writer.write("Crash at " + FMLCommonHandler.instance().getEffectiveSide());
			writer.write(reason + "\r\n");
			
			Set<Long> keys = profiling.keySet();
			
			for (Long index : keys) {
				HashMap<String, String> chunk = profiling.get(index);
				
				writer.write("=======================\r\n");
				writer.write("chunk @ " + chunk.get("chunkX") + "/" + chunk.get("chunkZ") + "\r\n");
				writer.write("index = " + index + "\r\n");
				
				Set<String> chunkkeys = chunk.keySet();
				for (String key : chunkkeys) {
					if (!key.equals("chunkX") && !key.equals("chunkZ") && !key.equals("list") && !key.equals("counter")) {
						writer.write(key + ": " + chunk.get(key) + "\r\n");
					}
				}
				
				writer.write("order:\r\n");
				writer.write(chunk.get("list") + "\r\n");
			}
			

			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("chunknbt.txt written.");
		
	}
	

	

	public static void setUnpopChunkList(ArrayList<BlockPos> unpopulatedChunks) {
		VCraftWorld.unpopulatedChunks = unpopulatedChunks;
	}



	static NBTTagCompound getChunkNBT(BlockPos blockpos) {
		return VintageCraft.proxy.getChunkNbt(BlockPos2Index(blockpos));
	}
	
	
	public static void setChunkNBT(int chunkX, int chunkZ, String key, int[] data) {
		long index = ChunkPos2Index(chunkX, chunkZ);
		
		int x = 2;
		
		NBTTagCompound nbt = VintageCraft.proxy.getChunkNbt(index);
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
			x = 0;
		}
		
		nbt.setIntArray(key, data);
		VintageCraft.proxy.putChunkNbt(index, nbt);
		
		mark(chunkX, chunkZ, "setchunknbt-" + key + " x-" + x);
	}
	
	
	  
	

	public static void setChunkNBT(int chunkX, int chunkZ, String key, boolean value) {
		long index = ChunkPos2Index(chunkX, chunkZ);
		
		NBTTagCompound nbt = VintageCraft.proxy.getChunkNbt(index);
		
		if (nbt == null) {
			nbt = new NBTTagCompound();
		}
		
		nbt.setBoolean(key, value);
		
		VintageCraft.proxy.putChunkNbt(index, nbt);
		
		mark(chunkX, chunkZ, "setchunknbt-" + key + " (" + value +")");
	}
	
	
	

	
	@SubscribeEvent
	public void loadChunk(ChunkDataEvent.Load event) {
		NBTTagCompound nbt = event.getData().getCompoundTag("vintagecraft");
		
		VintageCraft.proxy.putChunkNbt(Chunk2Index(event.getChunk()), nbt);
		
		if (nbt.hasKey("vcraftpopulated") && !nbt.getBoolean("vcraftpopulated")) {
			unpopulatedChunks.add(new BlockPos(event.getChunk().xPosition, 0, event.getChunk().zPosition));
		}
		
		mark(event.getChunk().xPosition, event.getChunk().zPosition, "load " + nbt.hasKey("climate"));		
	}
	

	@SubscribeEvent
	public void saveChunk(ChunkDataEvent.Save event) {	
		long index = Chunk2Index(event.getChunk());
		NBTTagCompound nbt = VintageCraft.proxy.getChunkNbt(index); // chunkextranbt_savequeue.get(index);
		
		
		if (nbt != null) {
			event.getData().setTag("vintagecraft", nbt);
			mark(event.getChunk().xPosition, event.getChunk().zPosition, "save " + nbt.hasKey("climate"));
		} else {
			mark(event.getChunk().xPosition, event.getChunk().zPosition, "save-no nbt?");
		}
		
		
		
		if (!event.getChunk().isLoaded()) {
			mark(event.getChunk().xPosition, event.getChunk().zPosition, "removed from list");
			// TODO
			//VintageCraft.proxy.removeChunkNbt(Chunk2Index(event.getChunk()));
		}
	}
	
	
	
    @SubscribeEvent
    public void onChunkWatch(ChunkWatchEvent.Watch event) {
    	long index = ChunkPos2Index(event.chunk.chunkXPos, event.chunk.chunkZPos);
    	VintageCraft.packetPipeline.sendTo(new ChunkPutNbt(index, VintageCraft.proxy.getChunkNbt(index)), event.player);
    }
    
    @SubscribeEvent
    public void onChunkUnWatch(ChunkWatchEvent.UnWatch event) {
    	long index = ChunkPos2Index(event.chunk.chunkXPos, event.chunk.chunkZPos);
    	VintageCraft.packetPipeline.sendTo(new ChunkRemoveNbt(index), event.player);
    }


    private static int _getClimate(BlockPos pos) {
    	NBTTagCompound nbt = getChunkNBT(pos);
    	mark(pos.getX() >> 4, pos.getZ() >> 4, "getnbt-_climate " + (nbt == null));
    	
    	if (nbt == null || !nbt.hasKey("climate")) {
    		printProfiling("_climate array for chunk " + (pos.getX()>>4) + "/" + + (pos.getZ()>>4) + " at coord " + pos + " missing!" + " (@index " + BlockPos2Index(pos) + ")");
    	}
    	
    	
    	return nbt.getIntArray("climate")[((pos.getZ() & 15) << 4) + (pos.getX() & 15)];
    }

    // Returns climate = int[temp, fertility, rain] 
    public static int[] getClimate(BlockPos pos) {
    	NBTTagCompound nbt = getChunkNBT(pos);
    	mark(pos.getX() >> 4, pos.getZ() >> 4, "getnbt-climate");
    	
    	if (!nbt.hasKey("climate")) {
    		printProfiling("climate array for chunk " + (pos.getX()>>4) + "/" + + (pos.getZ()>>4) + " missing!");
    	}
    	
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
    
    
    

	


    
    
    public static int normalizeTemperature(int temperature) {
    	return (int) (temperature / 4.25f) - 30;
    }
    
    
    
    public static int getForest(BlockPos pos) {
    	int forest = getChunkNBT(pos).getIntArray("forest")[((pos.getZ() & 15) << 4) + (pos.getX() & 15)];
    	mark(pos.getX() >> 4, pos.getZ() >> 4, "getnbt-forest");
    	
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


    
    
    
	
	@SideOnly(Side.CLIENT)
	public static void loadGrassColors(IResourceManager resourceManager) {
		try {
			grassBuffer = TextureUtil.readImageData(resourceManager, grassColormap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    @SideOnly(Side.CLIENT)
    public static int getGrassColorAtPos(BlockPos pos) {
    	int climate = _getClimate(pos);
    	
    	int temperature = (climate >> 16) & 0xff;
    	int rainfall = climate & 0xff;
    	//System.out.println(temperature + "/" + (255-rainfall));
    	return grassBuffer[temperature + 256 * (255-rainfall)];
    }
    
      
    static long Chunk2Index(Chunk chunk) {
    	return ChunkPos2Index(chunk.xPosition, chunk.zPosition);
    }
	static long BlockPos2Index(BlockPos pos) {
		return ChunkPos2Index(pos.getX() >> 4, pos.getZ() >> 4);
	}
	static long ChunkPos2Index(int chunkX, int chunkZ) {
		return ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ);
		//return ((long)chunkX + (long)Integer.MAX_VALUE) + (((long)chunkZ + (long)Integer.MAX_VALUE) << 32); 
	}
    
}

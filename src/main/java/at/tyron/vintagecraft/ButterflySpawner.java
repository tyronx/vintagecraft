package at.tyron.vintagecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import at.tyron.vintagecraft.Block.BlockRock;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTallGrassVC;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.EnumButterflySpawn;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage.ModList;

public class ButterflySpawner {

	int totalAliveButterflies = 0;
	long lastWorldTime = 0;	
	
	public boolean shouldSpawnMore(WorldServer world, int newlySpawned) {
		return totalAliveButterflies + newlySpawned < 40 * (world.isDaytime() ? 1 : 0.5f);
	}
	
	ArrayList<ChunkCoordIntPair> chunks = new ArrayList<ChunkCoordIntPair>();
	
    public int doButterflySpawning(WorldServer world) {      	
    	int spawnedButterflies = 0;
    	int playerIndex;
    	
    	chunks.clear();
    	
        for (playerIndex = 0; playerIndex < world.playerEntities.size(); ++playerIndex) {
        	EntityPlayer player = (EntityPlayer)world.playerEntities.get(playerIndex);
        	
            int playerChunkPosX = MathHelper.floor_double(player.posX / 16.0D);
            int playerChunkPosZ = MathHelper.floor_double(player.posZ / 16.0D);
            int chunkRange = 5;
            
            for (int chunkX = -chunkRange; chunkX <= chunkRange; ++chunkX) {
                for (int chunkZPos = -chunkRange; chunkZPos <= chunkRange; ++chunkZPos) {
                	ChunkCoordIntPair pair = new ChunkCoordIntPair(chunkX + playerChunkPosX, chunkZPos + playerChunkPosZ);
                	if (!chunks.contains(pair)) {
                		chunks.add(pair);	
                	}
                }
            }
        }
        
        Collections.shuffle(chunks);
        
        
        Iterator eligibleChunksForSpawningIterator = chunks.iterator();
        while (eligibleChunksForSpawningIterator.hasNext() && shouldSpawnMore(world, spawnedButterflies)) {
            ChunkCoordIntPair chunkCoordinate = (ChunkCoordIntPair)eligibleChunksForSpawningIterator.next();
            BlockPos randomChunkSpawnPoint = this.getRandomSpawningPointInChunk(world, chunkCoordinate.chunkXPos * 16, chunkCoordinate.chunkZPos * 16);
            
            int xPos = randomChunkSpawnPoint.getX();
            int yPos = randomChunkSpawnPoint.getY();
            int zPos = randomChunkSpawnPoint.getZ();
            

            int spawnedEntities = 0;
            
            for (int tries = 0; tries < 3; ++tries) {
                int rndxPos = xPos;
                int rndyPos = yPos;
                int rndzPos = zPos;
                short posVariation = 6;
                
                for (int attempt = 0; attempt < 4; ++attempt) {
                    rndxPos += world.rand.nextInt(posVariation) - world.rand.nextInt(posVariation);
                    rndyPos += world.rand.nextInt(posVariation) - world.rand.nextInt(posVariation);
                    rndzPos += world.rand.nextInt(posVariation) - world.rand.nextInt(posVariation);
                    
                    if (world.getClosestPlayer(rndxPos, rndyPos, rndzPos, 7.0D) != null) {
                    	continue;
                    }
                    
                    if (!nearSurface(world, rndxPos, rndyPos, rndzPos)) continue;
                    
                    int climate[] = VCraftWorld.instance.getClimate(new BlockPos(rndxPos, rndyPos, rndzPos));
                    int forest = VCraftWorld.instance.getForest(new BlockPos(rndxPos, rndyPos, rndzPos));
                    
                    int index = EnumButterflySpawn.getRandomButterfly(climate[2], climate[0], forest, rndzPos, world.rand);
                    
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setInteger("xpos", rndxPos);
                    nbt.setInteger("ypos", rndyPos);
                    nbt.setInteger("zpos", rndzPos);
                    nbt.setInteger("butterflyindex", index);
                    
                    if (index > 0) {
                    	FMLInterModComms.sendRuntimeMessage(VintageCraft.instance, "butterflymania", "butterflymania-spawnbutterfly", nbt);
                    	spawnedButterflies++;
                    }
                    
                    if (spawnedButterflies > 6) return spawnedButterflies;
                    
                }
            }
        }
        return spawnedButterflies;
    }
    
    
    
    BlockPos getRandomSpawningPointInChunk(World world, int posX, int posZ) {
        return 
        	new BlockPos(posX + world.rand.nextInt(16), VCraftWorld.seaLevel + world.rand.nextInt(256 - VCraftWorld.seaLevel), posZ + world.rand.nextInt(16));
    }
    
    
    boolean nearSurface(World world, int posX, int posY, int posZ) {
    	if (!world.isAirBlock(new BlockPos(posX, posY, posZ))) return false;
    	
    	int maxHeight = 5;
    	
    	while (maxHeight > 0) {
    		Block block = world.getBlockState(new BlockPos(posX, --posY, posZ)).getBlock();
    		
    		if (block == Blocks.air || 
    			block == Blocks.snow ||
    			block == Blocks.ice ||
				block instanceof BlockTopSoil || 
				block instanceof BlockSubSoil ||
				block instanceof BlockLeaves ||
				block instanceof BlockTallGrassVC || 
				BlocksVC.rock.containsBlock(block) || 
				BlocksVC.gravel.containsBlock(block) || 
				BlocksVC.sand.containsBlock(block)
			) {

    			return true;
    		}
    		
    		maxHeight--;
    	}
    	
    	return false;
    }
    
    
	@SubscribeEvent
	public void onTick(TickEvent.ServerTickEvent event) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance() == null) return;
		WorldServer[] worlds = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;
		
		if (worlds[0] != null && worlds[0].getWorldTime() % 40L == 0L && lastWorldTime != worlds[0].getWorldTime()) {
			lastWorldTime = worlds[0].getWorldTime();
			
			if (!Loader.isModLoaded("butterflymania")) return;
			
			for (int i=0; i < worlds.length; i++) {
				totalAliveButterflies = 0;
				for (int j = 0; j < worlds[i].loadedEntityList.size(); ++j) {
	                Entity entity = (Entity)worlds[i].loadedEntityList.get(j);
	                if (entity instanceof EntityLiving) {
	                	EntityLiving living = (EntityLiving)entity;
	                	
	            		if (living.getName().equals("entity.butterflymania.butterflymania:butterfly.name") && !living.isDead) {
	            			totalAliveButterflies++;
	            		}
	                }
	            }
				
				if (worlds[i].provider.getDimensionId() == 0) {
					if (shouldSpawnMore(worlds[i], 0)) {
						doButterflySpawning(worlds[i]);	
					}
					
				}
			}
		}		
	}

    
}

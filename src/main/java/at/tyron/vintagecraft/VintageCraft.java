package at.tyron.vintagecraft;

import at.tyron.vintagecraft.Client.CreativeTabsVC;
import at.tyron.vintagecraft.Item.ItemArmorVC;
import at.tyron.vintagecraft.Network.AnvilTechniquePacket;
import at.tyron.vintagecraft.Network.CarpentryTechniquePacket;
import at.tyron.vintagecraft.Network.ChunkPutNbtPacket;
import at.tyron.vintagecraft.Network.ChunkRemoveNbtPacket;
import at.tyron.vintagecraft.Network.MechanicalNetworkNBTPacket;
import at.tyron.vintagecraft.Network.SoundEffectToServerPacket;
import at.tyron.vintagecraft.Network.StartMeteorShowerPacket;
import at.tyron.vintagecraft.Network.WorldDataPacket;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import at.tyron.vintagecraft.World.VCraftWorldSavedData;
import at.tyron.vintagecraft.World.WindGen;
import at.tyron.vintagecraft.World.Crafting.EnumAnvilRecipe;
import at.tyron.vintagecraft.World.Crafting.EnumCarpentryRecipes;
import at.tyron.vintagecraft.World.Crafting.Recipes;
import at.tyron.vintagecraft.WorldGen.DynTreeGenerators;
import at.tyron.vintagecraft.WorldGen.WorldGenDeposits;
import at.tyron.vintagecraft.WorldGen.Helper.WorldProviderVC;
import at.tyron.vintagecraft.WorldGen.Helper.WorldTypeVC;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.SpecialSpawn;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

/*
 * Things to add before for a "Update requires new world":
 * - Wood type chests
 * - New trees, plants, rocktypes, etc.
 */

@Mod(modid = ModInfo.ModID, version = ModInfo.ModVersion)
public class VintageCraft {
	@Instance("vintagecraft")
	public static VintageCraft instance;
	
	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	    
 	public static final SimpleNetworkWrapper packetPipeline = NetworkRegistry.INSTANCE.newSimpleChannel("vintagecraft");
 	
 	
 	public static CreativeTabsVC terrainTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "terrain");
 	public static CreativeTabsVC floraTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "flora");
 	public static CreativeTabsVC resourcesTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "resources");
 	public static CreativeTabsVC craftedBlocksTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "craftedblocks");
 	public static CreativeTabsVC toolsarmorTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "toolsandarmor");
 	public static CreativeTabsVC mechanicsTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "mechanics");
 	
 	@EventHandler
 	public static void preInit(FMLPreInitializationEvent event) {
 		VintageCraftConfig.loadConfig(event);
 	}
 	
    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception {
    	packetPipeline.registerMessage(AnvilTechniquePacket.Handler.class, AnvilTechniquePacket.class, 0, Side.SERVER);
    	packetPipeline.registerMessage(ChunkPutNbtPacket.Handler.class, ChunkPutNbtPacket.class, 1, Side.CLIENT);
    	packetPipeline.registerMessage(ChunkRemoveNbtPacket.Handler.class, ChunkRemoveNbtPacket.class, 2, Side.CLIENT);
    	packetPipeline.registerMessage(SoundEffectToServerPacket.Handler.class, SoundEffectToServerPacket.class, 3, Side.SERVER);
    	packetPipeline.registerMessage(MechanicalNetworkNBTPacket.ClientHandler.class, MechanicalNetworkNBTPacket.class, 4, Side.CLIENT);
    	packetPipeline.registerMessage(StartMeteorShowerPacket.ClientHandler.class, StartMeteorShowerPacket.class, 5, Side.CLIENT);
    	packetPipeline.registerMessage(WorldDataPacket.ClientHandler.class, WorldDataPacket.class, 6, Side.CLIENT);
    	packetPipeline.registerMessage(CarpentryTechniquePacket.Handler.class, CarpentryTechniquePacket.class, 7, Side.SERVER);
    	
    	
    	BlocksVC.init();
    	ItemsVC.init();
    	AchievementsVC.init();
    	
    	FMLCommonHandler.instance().bus().register(this);
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	proxy.registerTileEntities();
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    	
		System.out.println("vcraft has init.");
		
       
     	
     	
     	
     	GameRegistry.registerWorldGenerator(new WorldGenDeposits(), 4);
     	
        
        WorldType.DEFAULT = WorldTypeVC.DEFAULT;
        WorldType.FLAT = WorldTypeVC.FLAT;
        
       // DimensionManager.unregisterDimension(-1);
		DimensionManager.unregisterDimension(0);
		DimensionManager.unregisterDimension(1);

		//DimensionManager.unregisterProviderType(-1);
		DimensionManager.unregisterProviderType(0);
		DimensionManager.unregisterProviderType(1);
        DimensionManager.registerProviderType(0, WorldProviderVC.class, true);
        DimensionManager.registerProviderType(1, WorldProviderVC.class, false);

		//DimensionManager.registerDimension(-1, -1);
		DimensionManager.registerDimension(0, 0);
		DimensionManager.registerDimension(1, 1);
		
		
		proxy.init(event);
    }
    
    

    
    
	
    	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { 
		proxy.postInit(event);
		
		ItemsVC.initTabIcons();
		DynTreeGenerators.initGenerators();
		
		Recipes.addRecipes();
		EnumAnvilRecipe.registerRecipes();
		EnumCarpentryRecipes.registerRecipes();
	}
	
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent evt) {
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager command = server.getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		manager.registerCommand(new ServerCommandsVC());
	}
		

	@SubscribeEvent
    public void entityJoin(EntityJoinWorldEvent evt) {
		if (!evt.world.isRemote && evt.entity instanceof EntityPlayerMP) {
			
			packetPipeline.sendTo(new WorldDataPacket(evt.world.getSeed()), (EntityPlayerMP)evt.entity);
		}
		
		if (evt.entity instanceof EntityZombie) {
			if (((EntityZombie)evt.entity).isChild()) {
				((EntityZombie)evt.entity).setChild(false);
			}
		}
    }

	
	@SubscribeEvent
	public void entitySpawn(SpecialSpawn evt) {
		if (evt.world.isRemote) return;
    	VintageCraftMobTweaker.entitySpawn(evt);
	}
	
	@SubscribeEvent
	public void entityDeath(LivingDeathEvent evt) {
		if (evt.entityLiving instanceof EntityZombie && evt.source.getEntity() instanceof EntityPlayer) {
			ItemStack[]inventory = evt.entityLiving.getInventory();
			int numArmorItems = 0;
			for (ItemStack stack : inventory) {
				if (stack != null && stack.getItem() instanceof ItemArmorVC) {
					ItemArmorVC armoritem = (ItemArmorVC)stack.getItem();
					if (armoritem.getArmorMaterial().name().equals("IRONVC")) numArmorItems++;
				}
			}
			
			if (numArmorItems == 4) {
				((EntityPlayer)evt.source.getEntity()).triggerAchievement(AchievementsVC.killIronArmorZombie);
			}
		}
	}
	
	
	public VCraftWorldSavedData getOrCreateWorldData(World world) {
		VCraftWorldSavedData worlddata;
		
		worlddata = (VCraftWorldSavedData) world.getPerWorldStorage().loadData(VCraftWorldSavedData.class, "vcraft");
		
		if (worlddata == null) {
			worlddata = new VCraftWorldSavedData("vcraft");
			world.getPerWorldStorage().setData("vcraft", worlddata);
		}
		
		return worlddata;
	}
	
	@SubscribeEvent
	public void loadWorld(WorldEvent.Load evt) {
		VintageCraftMobTweaker.setSpawnCap(EnumCreatureType.MONSTER, VintageCraftMobTweaker.spawnCapByDay(evt.world.getTotalWorldTime() / 24000L, evt.world.getDifficulty()));
		
		WindGen.registerWorld(evt.world);
	}
	
	@SubscribeEvent
	public void unloadWorld(WorldEvent.Unload evt) {
		WindGen.unregisterWorld(evt.world);
		
		VCraftWorldSavedData worlddata = getOrCreateWorldData(evt.world);
		
		
		System.out.println("unload networks");
		MechnicalNetworkManager.unloadManagers();
	}
	
	
	@SubscribeEvent
	public void onServerTick(TickEvent.WorldTickEvent event) {
		if (
			FMLCommonHandler.instance().getMinecraftServerInstance() == null || 
			event.phase == TickEvent.Phase.END || 
			event.world.provider.getDimensionId() != 0
		) return;
		
		
		if (MechnicalNetworkManager.getNetworkManagerForWorld(event.world) == null) {
			
			VCraftWorldSavedData worlddata = getOrCreateWorldData(event.world);
			worlddata.setWorld(event.world);
			MechnicalNetworkManager.addManager(event.world, worlddata.getNetworks());
		}		

		
		
		long worldtime = event.world.getTotalWorldTime();
		long timeofday = event.world.getWorldTime() / 24000L;
		
		if (worldtime % 6000L == 0) {
			VintageCraftMobTweaker.setSpawnCap(EnumCreatureType.MONSTER, VintageCraftMobTweaker.spawnCapByDay(worldtime / 24000L, event.world.getDifficulty()));
        	VintageCraftConfig.saveConfig();
		}
		
		
		int moonphase = event.world.provider.getMoonPhase(event.world.getWorldTime());
		boolean cannotSleeptonight =
			moonphase == 0 ||
			(event.world.getDifficulty() == EnumDifficulty.HARD && (moonphase == 7 || moonphase == 1))
		;
		
		
		if (cannotSleeptonight) {
			 for (Object obj : event.world.playerEntities) {
				 EntityPlayer player = (EntityPlayer)obj;		 
				 if (player.isPlayerSleeping() && getSleepTimer(player) > 80) {
					player.wakeUpPlayer(true, true, true);
					player.addChatMessage(new ChatComponentText("You tried to fall sleep, but something is keeping you awake tonight."));
				 }	 
			 }			
		}

		
		
		long day = worldtime / 24000L;
		if (day > 0 && day % 20 == 0 && timeofday % 24000L == 14000) {
			packetPipeline.sendToAll(new StartMeteorShowerPacket(10000));
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Something strange is happening in the night sky"));
		}
		

		
	}
	
	
	public int getSleepTimer(EntityPlayer player) {
		int timer = ReflectionHelper.getPrivateValue(EntityPlayer.class, player, 20);
		return timer;
	}
	
		
	
	@SubscribeEvent
	public void onEvent(LivingDropsEvent event) {
		VintageCraftMobTweaker.tweakDrops(event);
	}

	
	
}

package at.tyron.vintagecraft;

import net.minecraft.world.WorldType;

import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.Client.ClientProxy;
import at.tyron.vintagecraft.Network.AnvilTechniquePacket;
import at.tyron.vintagecraft.Network.ChunkPutNbt;
import at.tyron.vintagecraft.Network.ChunkRemoveNbt;
import at.tyron.vintagecraft.Network.SoundEffectToServerPacket;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.Recipes;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.DynTreeGenerators;
import at.tyron.vintagecraft.WorldGen.WorldGenDeposits;
import at.tyron.vintagecraft.WorldGen.MapGenFlora;
import at.tyron.vintagecraft.WorldGen.Helper.DynTreeGen;
import at.tyron.vintagecraft.WorldGen.Helper.WorldProviderVC;
import at.tyron.vintagecraft.WorldGen.Helper.WorldTypeVC;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilRecipe;
import at.tyron.vintagecraft.WorldProperties.MobInventoryItems;
//import at.tyron.vintagecraft.client.Model.BlockOreVCModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRedSandstone;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLootBonus;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.server.FMLServerHandler;

@Mod(modid = ModInfo.ModID, version = ModInfo.ModVersion)
public class VintageCraft {
	@Instance("vintagecraft")
	public static VintageCraft instance;
	
	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	
	    
    // The packet pipeline
 	//public static final PacketPipeline packetPipeline = new PacketPipeline();
 	public static final SimpleNetworkWrapper packetPipeline = NetworkRegistry.INSTANCE.newSimpleChannel("vintagecraft");
 	
 	
 	public static CreativeTabsVC terrainTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "terrain");
 	public static CreativeTabsVC floraTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "flora");
 	public static CreativeTabsVC resourcesTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "resources");
 	public static CreativeTabsVC craftedBlocksTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "craftedblocks");
 	public static CreativeTabsVC toolsarmorTab = new CreativeTabsVC(CreativeTabsVC.getNextID(), "toolsandarmor");
 	
 	@EventHandler
 	public static void preInit(FMLPreInitializationEvent event) {
 		VintageCraftConfig.loadConfig(event);
 	}
 	
    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception {
    	packetPipeline.registerMessage(AnvilTechniquePacket.Handler.class, AnvilTechniquePacket.class, 0, Side.SERVER);
    	packetPipeline.registerMessage(ChunkPutNbt.Handler.class, ChunkPutNbt.class, 1, Side.CLIENT);
    	packetPipeline.registerMessage(ChunkRemoveNbt.Handler.class, ChunkRemoveNbt.class, 2, Side.CLIENT);
    	packetPipeline.registerMessage(SoundEffectToServerPacket.Handler.class, SoundEffectToServerPacket.class, 3, Side.SERVER);
    	
    	BlocksVC.init();
    	ItemsVC.init();
    	
    	
    	
    	//packetPipeline.initalise();
    	
    	FMLCommonHandler.instance().bus().register(this);
    	MinecraftForge.EVENT_BUS.register(this);
    	
    	proxy.registerRenderInformation();
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    	
		System.out.println("vcraft has init.");
		
       
     	
     	
     	
     	GameRegistry.registerWorldGenerator(new WorldGenDeposits(), 4);
     	//GameRegistry.registerWorldGenerator(new WorldGenFlora(), 5);
     	
        
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
	}
	
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent evt) {
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager command = server.getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		manager.registerCommand(new VintageCraftCommands());
	}
		

	@SubscribeEvent
    public void entityJoin(EntityJoinWorldEvent evt) {
    	VintageCraftMobTweaker.entityJoin(evt);
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
		long worldtime = getOrCreateWorldData(evt.world).getWorldTime();
		
		VintageCraftMobTweaker.setSpawnCap(EnumCreatureType.MONSTER, VintageCraftMobTweaker.spawnCapByDay(worldtime / 24000L, evt.world.getDifficulty()));
	}
	
	
	@SubscribeEvent
	public void onServerTick(TickEvent.WorldTickEvent event) {
		if (
			FMLCommonHandler.instance().getMinecraftServerInstance() == null || 
			event.phase == TickEvent.Phase.END || 
			event.world.provider.getDimensionId() != 0
		) return;
		
		
		
		long worldtime = getOrCreateWorldData(event.world).getWorldTime();
		getOrCreateWorldData(event.world).setWorldTime(worldtime + 1);
		
		
		if (worldtime % 6000L == 0) {
			VintageCraftMobTweaker.setSpawnCap(EnumCreatureType.MONSTER, VintageCraftMobTweaker.spawnCapByDay(worldtime / 24000L, event.world.getDifficulty()));
        	VintageCraftConfig.saveConfig();
		}
	}
	
	

	public long daysPassed(World world) {
		return world.getWorldTime() / 24000;
	}
	
	
	@SubscribeEvent
	public void onEvent(LivingDropsEvent event) {
		VintageCraftMobTweaker.tweakDrops(event);
	}
	
	
}

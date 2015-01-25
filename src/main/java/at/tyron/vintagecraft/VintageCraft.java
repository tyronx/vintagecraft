package at.tyron.vintagecraft;

import net.minecraft.world.WorldType;

import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.Network.PacketPipeline;
import at.tyron.vintagecraft.World.Climate;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.Recipes;
import at.tyron.vintagecraft.WorldGen.WorldProviderVC;
import at.tyron.vintagecraft.WorldGen.WorldTypeVC;
import at.tyron.vintagecraft.WorldGen.WorldGenDeposits;
import at.tyron.vintagecraft.WorldGen.MapGenFlora;
import at.tyron.vintagecraft.client.ClientProxy;
import at.tyron.vintagecraft.client.Model.BlockOreVCModel;
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
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLootBonus;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
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
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
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
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.server.FMLServerHandler;

@Mod(modid = ModInfo.ModID, version = ModInfo.ModVersion)
public class VintageCraft {
	@Instance("vintagecraft")
	public static VintageCraft instance;

	
	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;
	    
    // The packet pipeline
 	public static final PacketPipeline packetPipeline = new PacketPipeline();

 	
 	
    @EventHandler
    public void init(FMLInitializationEvent event) throws Exception {
    	BlocksVC.init();
    	ItemsVC.init();
    	
    	packetPipeline.initalise();
    	
    	FMLCommonHandler.instance().bus().register(this);
    	MinecraftForge.EVENT_BUS.register(this);
		
    	
    	proxy.registerRenderInformation();
    	//proxy.registerTileEntities();
    	
    	NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
    	
		System.out.println("vcraft has init.");
		
       
        // Register the Chunk Load/Save Handler
     	MinecraftForge.EVENT_BUS.register(VCraftWorld.instance);
     	
     	
     	
     	GameRegistry.registerWorldGenerator(new WorldGenDeposits(), 4);
     	//GameRegistry.registerWorldGenerator(new WorldGenFlora(), 5);
     	
        
        WorldType.DEFAULT = new WorldTypeVC(0, "VCDefault");
        
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
		
		Recipes.addRecipes();
    }
    
    
	
    	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) { proxy.postInit(event); }
	
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent evt) {
		MinecraftServer server = MinecraftServer.getServer();
		ICommandManager command = server.getCommandManager();
		ServerCommandManager manager = (ServerCommandManager) command;
		manager.registerCommand(new VintageCraftCommands());
	}
		
	/*@SubscribeEvent
	public void onServerPostTick(ServerTickEvent evt) {
		//System.out.println("server post tick: " + evt.type);
		MinecraftServer.getServer().theProfiler.profilingEnabled = true;
		MinecraftServer.getServer().theProfiler.getProfilingData("root");
	}*/

	
	
	@SubscribeEvent
	public void onEvent(LivingDropsEvent event) {
	    if (event.entity instanceof EntityPig) {
	        event.drops.clear();
	        ItemStack itemStackToDrop = new ItemStack(ItemsVC.porkchopRaw, 3 + event.entityLiving.getRNG().nextInt(8));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	    }
	    if (event.entity instanceof EntityCow) {
	        event.drops.clear();
	        ItemStack itemStackToDrop = new ItemStack(ItemsVC.beefRaw, 5 + event.entityLiving.getRNG().nextInt(12));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	        
	        itemStackToDrop = new ItemStack(Items.leather, 3 + event.entityLiving.getRNG().nextInt(3));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	    }
	    if (event.entity instanceof EntityChicken) {
	        event.drops.clear();
	        ItemStack itemStackToDrop = new ItemStack(ItemsVC.chickenRaw, 1 + event.entityLiving.getRNG().nextInt(3));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	    }
	} 
}

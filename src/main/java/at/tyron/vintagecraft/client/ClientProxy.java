package at.tyron.vintagecraft.Client;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import at.tyron.vintagecraft.CommonProxy;
import at.tyron.vintagecraft.CreativeTabsVC;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.BlockOreVC;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Client.Render.RenderCloudsVC;
import at.tyron.vintagecraft.Client.Render.RenderForestSpider;
import at.tyron.vintagecraft.Client.Render.RenderSkyVC;
import at.tyron.vintagecraft.Client.Render.ShootingStar;
import at.tyron.vintagecraft.Client.Render.Model.RenderEntityStone;
import at.tyron.vintagecraft.Client.Render.TESR.*;
import at.tyron.vintagecraft.Entity.EntityForestSpider;
import at.tyron.vintagecraft.Entity.EntityMobHorse;
import at.tyron.vintagecraft.Entity.EntityStone;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.*;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.TETallMetalMold;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityHorse;

public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {
	
	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
		
		IReloadableResourceManager IRRM = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
		IRRM.registerReloadListener(this);
		
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class, new RenderEntityStone());
		RenderingRegistry.registerEntityRenderingHandler(EntityMobHorse.class, new RenderHorse(Minecraft.getMinecraft().getRenderManager(), new ModelHorse(), 0.75f));		
		RenderingRegistry.registerEntityRenderingHandler(EntityForestSpider.class, new RenderForestSpider(Minecraft.getMinecraft().getRenderManager()));
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		VCraftWorld.loadTextures(resourceManager);
	}
	
	
	
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
		
		if (VintageCraftConfig.rearrangeCreativeTabs) {
			int i = 0;
			
			CreativeTabs [] tabs = new CreativeTabs[CreativeTabs.creativeTabArray.length];
			
			for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
				if (tab instanceof CreativeTabsVC) {
					if (i == 5 || i == 11) i++; // don't touch search tab or inventory tab
					tab.tabIndex = i;
					tabs[i++] = tab;
				}
			}
			
			tabs[5] = CreativeTabs.tabAllSearch;
			tabs[11] = CreativeTabs.tabInventory;
			i = 7;
			
			for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
				if (!(tab instanceof CreativeTabsVC) && !tab.getTabLabel().equals("search") && !tab.getTabLabel().equals("inventory")) {
					if (i == 11) i++;
					tab.tabIndex = i;
					tabs[i++] = tab;
				}
			}
			
			CreativeTabs.creativeTabArray = tabs;

			/*for (CreativeTabs tab : CreativeTabs.creativeTabArray) {
				System.out.println(tab.getTabLabel() + ": " + tab.tabIndex);
			}*/
		}
	}
	
	
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        
        registerModelLocation(Item.getItemFromBlock(BlocksVC.tallmetalmolds), "tallmetalmolds", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.stonepot), "vintagecraft:stonepot/", EnumRockType.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.stonepot), "stonepot", "inventory");

        registerModelLocation(Item.getItemFromBlock(BlocksVC.toolrack), "toolrack", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.axle), "vintagecraft:axle/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.axle), "axle", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.angledgears), "vintagecraft:angledgearbox/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.angledgears), "angledgearbox", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.windmillrotor), "vintagecraft:windmillrotor/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.windmillrotor), "windmillrotor", "inventory");

        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.bellows), "vintagecraft:bellows/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.bellows), "bellows", "inventory");

        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.grindstone), "vintagecraft:grindstone/", EnumRockType.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.grindstone), "grindstone", "inventory");
        
        for (EnumTree treetype : EnumTree.values()) {
        	if (treetype.jankahardness > 800) {
        		ModelBakery.addVariantName(Item.getItemFromBlock(BlocksVC.carpenterTable), "vintagecraft:carpentertable/" + treetype.getName());
        	}
        }
        registerModelLocation(Item.getItemFromBlock(BlocksVC.carpenterTable), "carpentertable", "inventory");
        
        

        registerModelLocation(ItemsVC.stone, "stone", "inventory");
        registerModelLocation(ItemsVC.seeds, "seeds", "inventory");
        
    	registerModelLocation(ItemsVC.fireclay_ball, "fireclay_ball", "inventory");
    	registerModelLocation(ItemsVC.fireclay_brick_raw, "fireclay_brick_raw", "inventory");
    	registerModelLocation(ItemsVC.fireclay_brick, "fireclay_brick", "inventory");
    	
    	registerModelLocation(ItemsVC.ore, "ore", "inventory");
    	registerModelLocation(ItemsVC.metalingot, "ingot", "inventory");
    	
    	
    	registerModelLocation(ItemsVC.seeds, "seeds", "inventory");    	
    	registerModelLocation(ItemsVC.dryGrass, "drygrass", "inventory");
    	registerModelLocation(ItemsVC.flaxFibers, "flaxfibers", "inventory");
    	registerModelLocation(ItemsVC.linenCloth, "linencloth", "inventory");
    	registerModelLocation(ItemsVC.firestarter, "firestarter", "inventory");
    	registerModelLocation(ItemsVC.ironTuyere, "irontuyere", "inventory");
    	registerModelLocation(ItemsVC.flaxTwine, "flaxtwine", "inventory");
    	registerModelLocation(ItemsVC.stitchedleather, "stitchedleather", "inventory");
    	registerModelLocation(ItemsVC.sail, "sail", "inventory");
    	registerModelLocation(ItemsVC.peatbrick, "peatbrick", "inventory");
    	
    	registerModelLocation(ItemsVC.tools.values().toArray(new Item[0]), "tool", "inventory");
    	registerModelLocation(ItemsVC.toolheads.values().toArray(new Item[0]), "toolhead", "inventory");
    	
    	registerModelLocation(ItemsVC.anvilbase, "anvilbase", "inventory");
    	registerModelLocation(ItemsVC.anvilsurface, "anvilsurface", "inventory");
    	
    	registerModelLocation(ItemsVC.armor.values().toArray(new Item[0]), "armor", "inventory");
    	
    	
    	registerModelLocation(new Item[]{ItemsVC.porkchopRaw, ItemsVC.porkchopCooked, ItemsVC.beefRaw, ItemsVC.beefCooked, ItemsVC.chickenRaw, ItemsVC.chickenCooked}, "food", "inventory");
    	
    	
    	
		ClientRegistry.registerTileEntity(TEIngotPile.class, "ingotpile", new TESRIngotPile());
		ClientRegistry.registerTileEntity(TEToolRack.class, "toolrack", new TESRToolRack());
		ClientRegistry.registerTileEntity(TEVessel.class, "ceramicvessel2", new TESRCeramicVessel());
		ClientRegistry.registerTileEntity(TEStonePot.class, "stonepot", new TESRStonePot());
		ClientRegistry.registerTileEntity(TEAxle.class, "axle", new TESRAxle());
		ClientRegistry.registerTileEntity(TEAngledGearBox.class, "angledgearbox", new TESRAngledGearBox());
		ClientRegistry.registerTileEntity(TEWindmillRotor.class, "windmillrotor", new TESRWindmillRotor());
		ClientRegistry.registerTileEntity(TEGrindStone.class, "grindstone", new TESRGrindstone());
		ClientRegistry.registerTileEntity(TEBellows.class, "bellows", new TESRBellows());
		ClientRegistry.registerTileEntity(TETallMetalMold.class, "tallmetalmold", new TESRTallMetalMold());
    }
    
    
    
    @SubscribeEvent
    public void onClientTicket(ClientTickEvent evt) {
    	
    	if (evt.phase == TickEvent.Phase.END && Minecraft.getMinecraft().theWorld != null) {
    		Random rnd = Minecraft.getMinecraft().theWorld.rand;
    		float chance = 0.004f;
    		if (VintageCraft.proxy.meteorShowerDuration > 0) {
    			VintageCraft.proxy.meteorShowerDuration--;
    			chance = 0.11f;
    		}
    		
    		
    		if (rnd.nextFloat() < chance && RenderSkyVC.shootingStars.size() < 35) {
    			RenderSkyVC.shootingStars.add(new ShootingStar(
    				rnd.nextFloat()*300 - 150,
    				rnd.nextFloat()*300 - 150,
    				rnd.nextFloat()
    			));
    		}
    		
    		for (Iterator<ShootingStar> iterator = RenderSkyVC.shootingStars.iterator(); iterator.hasNext();) {
    			ShootingStar star = iterator.next();
    			star.tick();
    			if (star.isDead()) {
    		        // Remove the current element from the iterator and the list.
    		        iterator.remove();
    		    }
    		}
    		
    	}
    }
    
	
    private void registerModelLocation(final Item[] items, final String name, final String type) {
    	for (Item item : items) {
    		registerModelLocation(item, name, type);
    	}
    }
	
	private void registerModelLocation(final Item item, final String name, final String type) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//System.out.println("registerModelLocation for " + name);
		if (renderItem != null) {
	        renderItem.getItemModelMesher().register(item, new ItemMeshDefinition() {
	            @Override
	            public ModelResourceLocation getModelLocation(ItemStack stack) {
	            	//System.out.println(name + "/" + stack.getUnlocalizedName());
	            	if (item instanceof ISubtypeFromStackPovider && ((ISubtypeFromStackPovider)item).getSubType(stack) != null) {
	            		//System.out.println(ModInfo.ModID + ":" + name + "/" + ((ISubtypeFromStackPovider)item).getSubType(stack));
	            		return new ModelResourceLocation(ModInfo.ModID + ":" + name + "/" + ((ISubtypeFromStackPovider)item).getSubType(stack), type);
	            	} else {
	            		return new ModelResourceLocation(ModInfo.ModID + ":" + name, type);
	            	}
	            }
	        });
		}	
	}
	
	
	public boolean isFancyGraphics() {
		return Minecraft.getMinecraft().isFancyGraphicsEnabled();
	}
	
	
	@Override
	public void registerItemBlockTexture(Block block, String blockclassname, String subtype, int meta) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation("vintagecraft:" + blockclassname + "/" + subtype, "inventory"));
		ModelBakery.addVariantName(Item.getItemFromBlock(block), "vintagecraft:" + blockclassname + "/" + subtype);
	}
	
	@Override
	public void registerItemBlockTexture(Block block, String blockclassname) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("vintagecraft:" + blockclassname, "inventory"));
	}
	
	@Override
	public void registerItemBlockTextureVanilla(Block block, String blockclassname) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(blockclassname, "inventory"));
	}
	
	public void addVariantName(Item item, String... names) {
		ModelBakery.addVariantName(item, names);
	}
	
	public void addVariantNamesFromEnum(Item item, String prefix, IStateEnum[] names) {
		for (IStateEnum state : names) {
			ModelBakery.addVariantName(item, prefix + state.getStateName());
		}
	}
	
	
	
	public void ignoreProperties(Block block, IProperty[] properties) {
		BlockModelShapes bms = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
		bms.registerBlockWithStateMapper(block, (new StateMap.Builder()).addPropertiesToIgnore(properties).build());
	}
	
	
	public void playLoopingSound(String resourcelocation, IPitchAndVolumProvider pitchandvolumneprovider) {
		SoundHandler sndh = Minecraft.getMinecraft().getSoundHandler();
		
		sndh.playSound(new LoopingSound(new ResourceLocation(resourcelocation), pitchandvolumneprovider));
	}

	public World getClientWorld() {
		return Minecraft.getMinecraft().theWorld;
	}
	
}

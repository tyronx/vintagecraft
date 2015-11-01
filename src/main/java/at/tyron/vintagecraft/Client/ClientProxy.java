package at.tyron.vintagecraft.Client;

import java.util.Iterator;
import java.util.Random;

import at.tyron.vintagecraft.CommonProxy;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Client.Render.RenderFog;
import at.tyron.vintagecraft.Client.Render.RenderSkyVC;
import at.tyron.vintagecraft.Client.Render.ShootingStar;
import at.tyron.vintagecraft.Client.Render.Entity.RenderMinecart;
import at.tyron.vintagecraft.Client.Render.Entity.RenderForestSpider;
import at.tyron.vintagecraft.Client.Render.Model.ModelCowVC;
import at.tyron.vintagecraft.Client.Render.Model.RenderEntityStone;
import at.tyron.vintagecraft.Client.Render.TESR.TESRAngledGearBox;
import at.tyron.vintagecraft.Client.Render.TESR.TESRAxle;
import at.tyron.vintagecraft.Client.Render.TESR.TESRBellows;
import at.tyron.vintagecraft.Client.Render.TESR.TESRCeramicVessel;
import at.tyron.vintagecraft.Client.Render.TESR.TESRGrindstone;
import at.tyron.vintagecraft.Client.Render.TESR.TESRIngotPile;
import at.tyron.vintagecraft.Client.Render.TESR.TESRStonePot;
import at.tyron.vintagecraft.Client.Render.TESR.TESRTallMetalMold;
import at.tyron.vintagecraft.Client.Render.TESR.TESRToolRack;
import at.tyron.vintagecraft.Client.Render.TESR.TESRWindmillRotor;
import at.tyron.vintagecraft.Client.Render.TESR.TESRWoodBucket;
import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecartVC;
import at.tyron.vintagecraft.Entity.EntityEmptyMinecartVC;
import at.tyron.vintagecraft.Entity.EntityGunpowderSparkFX;
import at.tyron.vintagecraft.Entity.EntityMinecartVC;
import at.tyron.vintagecraft.Entity.EntityStone;
import at.tyron.vintagecraft.Entity.Animal.EntityCowVC;
import at.tyron.vintagecraft.Entity.Animal.EntityForestSpider;
import at.tyron.vintagecraft.Entity.Animal.EntityMobHorse;
import at.tyron.vintagecraft.Entity.Animal.EntitySheepVC;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TEWoodBucket;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.TETallMetalMold;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGears;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.EnumBucketContents;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.Interfaces.*;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.model.ModelCow;
import net.minecraft.client.model.ModelHorse;
import net.minecraft.client.model.ModelSheep2;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderCow;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {
	@Override
	public void generateGundPowderSpark(World world, BlockPos pos, float offsetX, float offsetY, float offsetZ) {
		double motionX = 0.08 + world.rand.nextGaussian() * 0.05D;
	    double motionY = 0.1 + world.rand.nextGaussian() * 0.1D;
	    double motionZ = 0.05 + world.rand.nextGaussian() * 0.05D;
	    
		EntityFX particle = new EntityGunpowderSparkFX(
			world, 
			pos.getX() + offsetX, 
			pos.getY() + offsetY, 
			pos.getZ() + offsetZ, 
			motionX, 
			motionY, 
			motionZ
		);
		
		particle.motionX = motionX;
		particle.motionY = motionY;
		particle.motionZ = motionZ;
		
		Minecraft.getMinecraft().effectRenderer.addEffect(particle);	
	}
	
	
	@Override
	public void registerTileEntities() {
		super.registerTileEntities();
		
		IReloadableResourceManager IRRM = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
		IRRM.registerReloadListener(this);
		
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class, new RenderEntityStone());
		RenderingRegistry.registerEntityRenderingHandler(EntityMobHorse.class, new RenderHorse(rm, new ModelHorse(), 0.75f));		
		RenderingRegistry.registerEntityRenderingHandler(EntityForestSpider.class, new RenderForestSpider(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityCoalPoweredMinecartVC.class, new RenderMinecart(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityEmptyMinecartVC.class, new RenderMinecart(rm));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCowVC.class, new RenderCow(rm, new ModelCowVC(), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySheepVC.class, new RenderSheep(rm, new ModelSheep2(), 0.7F));
	}
	

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		VCraftWorld.loadTextures(resourceManager);
	}
	
	
	
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		
		ClientCommandHandler.instance.registerCommand(new ClientCommandsVC());
		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(new RenderFog());
		
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
        
        String mechFolder = EnumObjectCategory.Mechanics.getFolderPart();
        String metalFolder = EnumObjectCategory.Metalworking.getFolderPart();
        
        registerModelLocation(Item.getItemFromBlock(BlocksVC.woodenrail), "woodenrail", "inventory");
        
        registerModelLocation(Item.getItemFromBlock(BlocksVC.saltlamp), "saltlamp", "inventory");
        
        registerModelLocation(Item.getItemFromBlock(BlocksVC.tallmetalmolds), "tallmetalmolds", "inventory");
        
        registerModelLocation(Item.getItemFromBlock(BlocksVC.blastpowdersack), "blastpowdersack", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.stonepot), ModInfo.AssetPrefix + metalFolder + "stonepot/", EnumRockType.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.stonepot), "stonepot", "inventory");

        registerModelLocation(Item.getItemFromBlock(BlocksVC.toolrack), "toolrack", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.axle), ModInfo.AssetPrefix + mechFolder + "axle/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.axle), "axle", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.angledgears), ModInfo.AssetPrefix + mechFolder + "angledgearbox/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.angledgears), "angledgearbox", "inventory");
        
        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.windmillrotor), ModInfo.AssetPrefix + mechFolder + "windmillrotor/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.windmillrotor), "windmillrotor", "inventory");

        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.bellows), ModInfo.AssetPrefix + mechFolder + "bellows/", EnumTree.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.bellows), "bellows", "inventory");

        addVariantNamesFromEnum(Item.getItemFromBlock(BlocksVC.grindstone), ModInfo.AssetPrefix + mechFolder + "grindstone/", EnumRockType.values());
        registerModelLocation(Item.getItemFromBlock(BlocksVC.grindstone), "grindstone", "inventory");
        
        for (EnumTree treetype : EnumTree.values()) {
        	if (treetype.jankahardness > 800) {
        		ModelBakery.addVariantName(Item.getItemFromBlock(BlocksVC.carpenterTable), ModInfo.AssetPrefix + mechFolder + "carpentertable/" + treetype.getName());
        		
	        	for (EnumBucketContents cnt : EnumBucketContents.values()) {
	        		ModelBakery.addVariantName(Item.getItemFromBlock(BlocksVC.woodbucket), ModInfo.AssetPrefix + mechFolder + "woodbucket/" + treetype.getName() + "-" + cnt.getName());
	        	}
        	}
        }
        registerModelLocation(Item.getItemFromBlock(BlocksVC.woodbucket), "woodbucket", "inventory");
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
    	
    	registerModelLocation(ItemsVC.coalpoweredMinecart, "minecart/coalpowered", "inventory");
    	registerModelLocation(ItemsVC.emptyMinecart, "minecart/empty", "inventory");
    	
    	registerModelLocation(ItemsVC.blastingPowder, "blastingpowder", "inventory");
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
    	
    	
    	registerModelLocation(new Item[]{ItemsVC.bread, ItemsVC.porkchopRaw, ItemsVC.porkchopCooked, ItemsVC.beefRaw, ItemsVC.beefCooked, ItemsVC.chickenRaw, ItemsVC.chickenCooked}, "food", "inventory");
    	
    	
    	
		ClientRegistry.registerTileEntity(TEIngotPile.class, "ingotpile", new TESRIngotPile());
		ClientRegistry.registerTileEntity(TEToolRack.class, "toolrack", new TESRToolRack());
		ClientRegistry.registerTileEntity(TEVessel.class, "ceramicvessel2", new TESRCeramicVessel());
		ClientRegistry.registerTileEntity(TEStonePot.class, "stonepot", new TESRStonePot());
		ClientRegistry.registerTileEntity(TEAxle.class, "axle", new TESRAxle());
		ClientRegistry.registerTileEntity(TEAngledGears.class, "angledgearbox", new TESRAngledGearBox());
		ClientRegistry.registerTileEntity(TEWindmillRotor.class, "windmillrotor", new TESRWindmillRotor());
		ClientRegistry.registerTileEntity(TEGrindStone.class, "grindstone", new TESRGrindstone());
		ClientRegistry.registerTileEntity(TEBellows.class, "bellows", new TESRBellows());
		ClientRegistry.registerTileEntity(TETallMetalMold.class, "tallmetalmold", new TESRTallMetalMold());
		ClientRegistry.registerTileEntity(TEWoodBucket.class, "woodbucket", new TESRWoodBucket());
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
	            	EnumObjectCategory cat = ((ICategorizedBlockOrItem)stack.getItem()).getCategory();
	            	
	            	//System.out.println(name + "/" + stack.getUnlocalizedName());
	            	if (item instanceof ISubtypeFromStackPovider && ((ISubtypeFromStackPovider)item).getSubType(stack) != null) {
	            		//System.out.println(ModInfo.ModID + ":" + name + "/" + ((ISubtypeFromStackPovider)item).getSubType(stack));
	            		return new ModelResourceLocation(ModInfo.AssetPrefix + cat.getFolderPart() + name + "/" + ((ISubtypeFromStackPovider)item).getSubType(stack), type);
	            	} else {
	            		return new ModelResourceLocation(ModInfo.AssetPrefix + cat.getFolderPart() + name, type);
	            	}
	            }
	        });
		}	
	}
	
	
	public boolean isFancyGraphics() {
		return Minecraft.isFancyGraphicsEnabled();
	}
	
	
	@Override
	public void registerItemBlockTexture(Block block, String blockclassname, String subtype, int meta) {
		EnumObjectCategory cat = ((ICategorizedBlockOrItem)block).getCategory();
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
			Item.getItemFromBlock(block), meta, new ModelResourceLocation(ModInfo.AssetPrefix + cat.getFolderPart() + blockclassname + "/" + subtype, "inventory")
		);
		ModelBakery.addVariantName(Item.getItemFromBlock(block), ModInfo.AssetPrefix + cat.getFolderPart() + blockclassname + "/" + subtype);
	}
	
	@Override
	public void registerItemBlockTexture(Block block, String blockclassname) {
		EnumObjectCategory cat = ((ICategorizedBlockOrItem)block).getCategory();
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
			Item.getItemFromBlock(block), 0, new ModelResourceLocation(ModInfo.AssetPrefix + cat.getFolderPart() + blockclassname, "inventory")
		);
	}
	
	@Override
	public void registerItemBlockTextureVanilla(Block block, String blockclassname) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(
			Item.getItemFromBlock(block), 0, new ModelResourceLocation(blockclassname, "inventory")
		);
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

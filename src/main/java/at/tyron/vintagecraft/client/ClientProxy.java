package at.tyron.vintagecraft.Client;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderItem;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import at.tyron.vintagecraft.CommonProxy;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.Block.BlockOreVC;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Client.Render.TESR.*;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.Item.*;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;

public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {
	
	public static ModelResourceLocation oremodelLocation = new ModelResourceLocation(ModInfo.ModID + ":" + BlocksVC.raworeName, null);
	
	

	
	@Override
	public void registerRenderInformation() {
		
		IReloadableResourceManager IRRM = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
		IRRM.registerReloadListener(this);	
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		VCraftWorld.loadGrassColors(resourceManager);
	}
	
	
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        
        registerModelLocation(Item.getItemFromBlock(BlocksVC.toolrack), "toolrack", "inventory"); //ItemsVC.toolrack
        registerModelLocation(ItemsVC.stone, "stone", "inventory");
        
    	registerModelLocation(ItemsVC.fireclay_ball, "fireclay_ball", "inventory");
    	registerModelLocation(ItemsVC.fireclay_brick_raw, "fireclay_brick_raw", "inventory");
    	registerModelLocation(ItemsVC.fireclay_brick, "fireclay_brick", "inventory");
    	
    	registerModelLocation(ItemsVC.ore, "ore", "inventory");
    	registerModelLocation(ItemsVC.ingot, "ingot", "inventory");
    	registerModelLocation(ItemsVC.wheatSeeds, "wheatseeds", "inventory");
    	
    	registerModelLocation(ItemsVC.peatbrick, "peatbrick", "inventory");
    	//registerModelLocation(ItemsVC.clayVessel, "clayvessel", "inventory");
    	//registerModelLocation(ItemsVC.ceramicVessel, "ceramicvessel", "inventory");
    	
    	registerModelLocation(new Item[]{ItemsVC.ironAxe, ItemsVC.ironHoe, ItemsVC.ironPickaxe, ItemsVC.ironShovel, ItemsVC.ironSword, ItemsVC.ironSaw, ItemsVC.ironShears}, "tool", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.bismuthbronzeAxe, ItemsVC.bismuthbronzeHoe, ItemsVC.bismuthbronzePickaxe, ItemsVC.bismuthbronzeShovel, ItemsVC.bismuthbronzeSword, ItemsVC.bismuthbronzeSaw, ItemsVC.bismuthbronzeShears}, "tool", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.tinbronzeAxe, ItemsVC.tinbronzeHoe, ItemsVC.tinbronzePickaxe, ItemsVC.tinbronzeShovel, ItemsVC.tinbronzeSword, ItemsVC.tinbronzeSaw, ItemsVC.tinbronzeShears}, "tool", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.copperAxe, ItemsVC.copperHoe, ItemsVC.copperPickaxe, ItemsVC.copperShovel, ItemsVC.copperSword, ItemsVC.copperSaw, ItemsVC.copperShears}, "tool", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.stoneAxe, ItemsVC.stoneHoe, ItemsVC.stonePickaxe, ItemsVC.stoneShovel, ItemsVC.stoneSword}, "tool", "inventory");
    	
    	
    	registerModelLocation(new Item[]{ItemsVC.copperHelmet, ItemsVC.copperChestplate, ItemsVC.copperLeggings, ItemsVC.copperBoots}, "armor", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.tinbronzeHelmet, ItemsVC.tinbronzeChestplate, ItemsVC.tinbronzeLeggings, ItemsVC.tinbronzeBoots}, "armor", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.bismuthbronzeHelmet, ItemsVC.bismuthbronzeChestplate, ItemsVC.bismuthbronzeLeggings, ItemsVC.bismuthbronzeBoots}, "armor", "inventory");
    	
    	
    	registerModelLocation(new Item[]{ItemsVC.porkchopRaw, ItemsVC.porkchopCooked, ItemsVC.beefRaw, ItemsVC.beefCooked, ItemsVC.chickenRaw, ItemsVC.chickenCooked}, "food", "inventory");
    	
    	
    	
		ClientRegistry.registerTileEntity(TEIngotPile.class, "ingotpile", new TESRIngotPile());
		ClientRegistry.registerTileEntity(TEToolRack.class, "ToolRack", new TESRToolRack());
		ClientRegistry.registerTileEntity(TEVessel.class, "ceramicvessel2", new TESRCeramicVessel());

    }
	
    private void registerModelLocation(final Item[] items, final String name, final String type) {
    	for (Item item : items) {
    		registerModelLocation(item, name, type);
    	}
    }
	
	private void registerModelLocation(final Item item, final String name, final String type) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		//System.out.println("registerModelLocation");
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
	
	
	public void ignoreProperties(Block block, IProperty[] properties) {
		BlockModelShapes bms = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
		bms.registerBlockWithStateMapper(block, (new StateMap.Builder()).addPropertiesToIgnore(properties).build());
	}

	
}

package at.tyron.vintagecraft.client;

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
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import at.tyron.vintagecraft.CommonProxy;
import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockOreVC;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.block.BlockVC;
//import at.tyron.vintagecraft.client.Model.BlockOreVCModel;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.item.*;
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
        
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.peat), "peat", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.topsoil), "topsoil", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.sand), "sand", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.gravel), "gravel", "inventory");
    	//registerModelLocation(Item.getItemFromBlock(BlocksVC.cobblestone), "cobblestone", "inventory");
    	
    	registerModelLocation(ItemsVC.stone, "stone", "inventory");
    	registerModelLocation(ItemsVC.ore, "ore", "inventory");
    	registerModelLocation(ItemsVC.ingot, "ingot", "inventory");
    	registerModelLocation(ItemsVC.wheatSeeds, "wheatseeds", "inventory");
    	
    	registerModelLocation(ItemsVC.peatbrick, "peatbrick", "inventory");
    	
    	registerModelLocation(new Item[]{ItemsVC.copperAxe, ItemsVC.copperHoe, ItemsVC.copperPickaxe, ItemsVC.copperShovel, ItemsVC.copperSword, ItemsVC.copperSaw}, "tool", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.stoneAxe, ItemsVC.stoneHoe, ItemsVC.stonePickaxe, ItemsVC.stoneShovel, ItemsVC.stoneSword}, "tool", "inventory");
    	registerModelLocation(new Item[]{ItemsVC.porkchopRaw, ItemsVC.porkchopCooked, ItemsVC.beefRaw, ItemsVC.beefCooked, ItemsVC.chickenRaw, ItemsVC.chickenCooked}, "food", "inventory");
    }
	
    private void registerModelLocation(final Item[] items, final String name, final String type) {
    	for (Item item : items) {
    		registerModelLocation(item, name, type);
    	}
    }
	
	private void registerModelLocation(final Item item, final String name, final String type) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
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

package at.tyron.vintagecraft.client;

import java.io.IOException;

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
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockOreVC;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.client.Model.BlockOreVCModel;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.item.*;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener {
	private static final ResourceLocation grassColormap = new ResourceLocation("textures/colormap/grass.png");
	
	
	

	
	@Override
	public void registerRenderInformation() {
		
		IReloadableResourceManager IRRM = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
		IRRM.registerReloadListener(this);	
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
	/*	try {
			BlockGrass.setGrassBiomeColorizer(TextureUtil.readImageData(resourceManager, grassColormap));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.rawore), BlocksVC.raworeName, null);
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.log), "log", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.planks), "planks", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.leaves), "leaves", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.leavesbranchy), "leavesbranchy", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.doubleplant), "doubleplant", "inventory");
    	
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.peat), "peat", "inventory");
    	registerModelLocation(Item.getItemFromBlock(BlocksVC.topsoil), "topsoil", "inventory");
    	
    	registerModelLocation(ItemsVC.stone, "stone", "inventory");
    	registerModelLocation(ItemsVC.ore, "ore", "inventory");
    	registerModelLocation(ItemsVC.ingot, "ingot", "inventory");
    	
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
	            	if (item instanceof ISubtypeFromStackPovider) {
	            		//System.out.println(ModInfo.ModID + ":" + name + "/" + ((ISubtypeFromStackPovider)item).getSubType(stack));
	            		return new ModelResourceLocation(ModInfo.ModID + ":" + name + "/" + ((ISubtypeFromStackPovider)item).getSubType(stack), type);
	            	} else {
	            		return new ModelResourceLocation(ModInfo.ModID + ":" + name, type);
	            	}
	            }
	        });
		}	
	}

	@SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
        TextureAtlasSprite base = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/slime");
//        TextureAtlasSprite overlay = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/redstone_block");
        
        //event.modelRegistry.putObject(BlocksVC.oremodelLocation, new BlockOreVCModel(base, overlay));
        event.modelRegistry.putObject(BlocksVC.oremodelLocation, new BlockOreVCModel(base));
    }
	
	
	@SubscribeEvent
	public void registerTextures(TextureStitchEvent.Pre event) {
        TextureMap map = event.map;
        for (EnumMaterialDeposit deposit : EnumMaterialDeposit.values()) {
        	if (deposit.block == BlocksVC.rawore) {
        		for (EnumRockType rocktype: EnumRockType.values()) {
        			event.map.registerSprite(new ResourceLocation(ModInfo.ModID + ":blocks/ore/" + deposit.getName() + "-" + rocktype.getName()));
        		}
        	}
        }
	}
	
	
	
	public boolean isFancyGraphics() {
		return Minecraft.getMinecraft().isFancyGraphicsEnabled();
	}

}

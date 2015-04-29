package at.tyron.vintagecraft;

import java.util.HashMap;

import at.tyron.vintagecraft.Block.BlockOreVC;
import at.tyron.vintagecraft.Entity.EntityStone;
import at.tyron.vintagecraft.Gui.GuiAnvil;
import at.tyron.vintagecraft.Gui.GuiStove;
import at.tyron.vintagecraft.Gui.GuiVessel;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerStove;
import at.tyron.vintagecraft.Inventory.ContainerVessel;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEVessel;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {
	HashMap<Long, NBTTagCompound> chunkextranbt = new HashMap<Long, NBTTagCompound>();
	
	public void putChunkNbt(long index, NBTTagCompound nbt) {
		chunkextranbt.put(index, nbt);
	}
	
	public NBTTagCompound getChunkNbt(long index) {
		return chunkextranbt.get(index);
	}
	
	public void removeChunkNbt(long index) {
		//chunkextranbt.remove(index);
	}
	
	public void clearChunkNbt() {
		chunkextranbt.clear();
	}
	
	public void registerRenderInformation() {
		//RenderingRegistry.registerEntityRenderingHandler(BlockOreVC.class, new RenderOre());
		
	}
	
	
	public void registerTileEntities() {
		
	}


	public void init(FMLInitializationEvent event) {
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		 
		EntityRegistry.registerModEntity(EntityStone.class, "stonethrown", entityId, VintageCraft.instance, 64, 1, true);  
		EntityList.addMapping(EntityStone.class, "stonethrown", entityId);
		
	}


	public void postInit(FMLPostInitializationEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isFancyGraphics() {
		return false;
	}

	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new ContainerStove(player.inventory, (TEHeatSourceWithGUI) world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == 1) {
			return new ContainerVessel(player.inventory, player.getHeldItem().getTagCompound());
		}
		if (ID == 2) {
			return new ContainerVessel(player.inventory, (TEVessel) world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == 4) {
			return new ContainerAnvil(player.inventory, (TEAnvil) world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new GuiStove(player.inventory, world, (TEHeatSourceWithGUI) world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == 1) {
			return new GuiVessel(player.inventory, world, player.getHeldItem().getTagCompound());
		}
		if (ID == 2) {
			return new GuiVessel(player.inventory, (TEVessel) world.getTileEntity(new BlockPos(x, y, z)));
		}

		if (ID == 4) {
			return new GuiAnvil(player.inventory, (TEAnvil) world.getTileEntity(new BlockPos(x, y, z)));
		}

		
			
			
		return null;
	}
	
	
	
	
	public void registerItemBlockTexture(Block block, String blockclassname, String subtype, int meta) {}
	
	public void registerItemBlockTexture(Block block, String blockclassname) {}
	
	public void registerItemBlockTextureVanilla(Block block, String blockclassname) {}
	
	public void addVariantName(Item item, String... names) {}
	
	public void ignoreProperties(Block block, IProperty[] properties) {};
	
	public void playLoopingSound(String resourcelocation, IPitchAndVolumProvider pitchandvolumneprovider) {};
}

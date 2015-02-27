package at.tyron.vintagecraft;

import java.util.HashMap;

import at.tyron.vintagecraft.Inventory.ContainerStove;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TileEntityStove;
import at.tyron.vintagecraft.block.BlockOreVC;
import at.tyron.vintagecraft.gui.GuiStove;
import net.minecraft.block.Block;
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
		// TODO Auto-generated method stub
		
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
			return new ContainerStove(player.inventory, (TileEntityStove) world.getTileEntity(new BlockPos(x, y, z)));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (ID == 0) {
			return new GuiStove(player.inventory, world, (TileEntityStove) world.getTileEntity(new BlockPos(x, y, z)));
		}
			
			
		return null;
	}
	
	
	/*public void registerBlockTexture(Block block, String folderprefix, String blockclassname, String subtype) {
		
	}*/
	
	
	public void registerItemBlockTexture(Block block, String blockclassname, String subtype, int meta) {
		
	}
	
	public void registerItemBlockTexture(Block block, String blockclassname) {
		
	}
	
	public void addVariantName(Item item, String... names) {
		
	}
	
	
}

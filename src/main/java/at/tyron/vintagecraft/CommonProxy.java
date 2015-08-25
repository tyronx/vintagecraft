package at.tyron.vintagecraft;

import java.util.HashMap;
import java.util.Hashtable;

import at.tyron.vintagecraft.Entity.EntityForestSpider;
import at.tyron.vintagecraft.Entity.EntityMobHorse;
import at.tyron.vintagecraft.Entity.EntityStone;
import at.tyron.vintagecraft.Gui.GuiAnvil;
import at.tyron.vintagecraft.Gui.GuiCarpenterTable;
import at.tyron.vintagecraft.Gui.GuiForge;
import at.tyron.vintagecraft.Gui.GuiStove;
import at.tyron.vintagecraft.Gui.GuiVessel;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerCarpenterTable;
import at.tyron.vintagecraft.Inventory.ContainerForge;
import at.tyron.vintagecraft.Inventory.ContainerStove;
import at.tyron.vintagecraft.Inventory.ContainerVessel;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TECarpenterTable;
import at.tyron.vintagecraft.TileEntity.TECokeOvenDoor;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
import at.tyron.vintagecraft.TileEntity.TEFurnaceSection;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.TileEntity.TEOrePile;
import at.tyron.vintagecraft.TileEntity.TESapling;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import at.tyron.vintagecraft.TileEntity.TETallMetalMold;
import at.tyron.vintagecraft.TileEntity.TEToolRack;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.TileEntity.TileEntityForestSpiderSpawner;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAngledGearBox;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEAxle;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEBellows;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEGrindStone;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IGuiHandler {
	public Hashtable<Integer, MechnicalNetworkManager> serverNetworkManagers = new Hashtable<Integer, MechnicalNetworkManager>();
	public Hashtable<Integer, MechnicalNetworkManager> clientNetworkManagers = new Hashtable<Integer, MechnicalNetworkManager>();
	
	HashMap<Long, NBTTagCompound> chunkextranbt = new HashMap<Long, NBTTagCompound>();
	public int meteorShowerDuration = 0;
	public long worldSeed;
	
	
	public int getNightSkyType() {
		return (int) (Math.abs(worldSeed) % 4);
	}
	
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
	
	public void registerTileEntities() {
		GameRegistry.registerTileEntity(TEHeatSourceWithGUI.class, ModInfo.ModID + ":stove");
		GameRegistry.registerTileEntity(TEFarmland.class, ModInfo.ModID + ":farmlandte");
		GameRegistry.registerTileEntity(TESapling.class, ModInfo.ModID + ":saplingte");
		GameRegistry.registerTileEntity(TEIngotPile.class, ModInfo.ModID + ":ingotpile");
		GameRegistry.registerTileEntity(TEToolRack.class, ModInfo.ModID + ":toolrack");
		GameRegistry.registerTileEntity(TEVessel.class, ModInfo.ModID + ":ceramicvessel2");
		GameRegistry.registerTileEntity(TEFurnaceSection.class, ModInfo.ModID + ":furnacesection");
		GameRegistry.registerTileEntity(TEAnvil.class, ModInfo.ModID + ":anvilvc");
		GameRegistry.registerTileEntity(TEStonePot.class, ModInfo.ModID + ":forge");
		
		GameRegistry.registerTileEntity(TileEntityForestSpiderSpawner.class, ModInfo.ModID + ":nonburningmobspawner");
		GameRegistry.registerTileEntity(TEAngledGearBox.class, ModInfo.ModID + ":angledgearbox");
		GameRegistry.registerTileEntity(TEAxle.class, ModInfo.ModID + ":axle");
		GameRegistry.registerTileEntity(TEWindmillRotor.class, ModInfo.ModID + ":windmillrotor");
		GameRegistry.registerTileEntity(TEGrindStone.class, ModInfo.ModID + ":grindstone");
		GameRegistry.registerTileEntity(TEBellows.class, ModInfo.ModID + ":bellows");
		GameRegistry.registerTileEntity(TETallMetalMold.class, ModInfo.ModID + ":tallmetalmold");
		GameRegistry.registerTileEntity(TECokeOvenDoor.class, ModInfo.ModID + ":cokeoven");
		GameRegistry.registerTileEntity(TEOrePile.class, ModInfo.ModID + ":orepile");
		
		GameRegistry.registerTileEntity(TECarpenterTable.class, ModInfo.ModID + ":carpentertable");
	}
	
	

	public void init(FMLInitializationEvent event) {
		EntityRegistry.registerModEntity(EntityStone.class, "stonethrown", 3, VintageCraft.instance, 64, 1, true);  
		EntityRegistry.registerModEntity(EntityMobHorse.class, "mobhorse", 4, VintageCraft.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityForestSpider.class, "VCForestSpider", 5, VintageCraft.instance, 64, 1, true);
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
		if (ID == 5) {
			return new ContainerForge(player.inventory, (TEStonePot) world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == 6) {
			return new ContainerCarpenterTable(player.inventory, (TECarpenterTable) world.getTileEntity(new BlockPos(x, y, z)));
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
		if (ID == 5) {
			return new GuiForge(player.inventory, (TEStonePot) world.getTileEntity(new BlockPos(x, y, z)));
		}
		if (ID == 6) {
			return new GuiCarpenterTable(player.inventory, (TECarpenterTable) world.getTileEntity(new BlockPos(x, y, z)));
		}

		
			
			
		return null;
	}
	
	
	
	
	public void registerItemBlockTexture(Block block, String blockclassname, String subtype, int meta) {}
	
	public void registerItemBlockTexture(Block block, String blockclassname) {}
	
	public void registerItemBlockTextureVanilla(Block block, String blockclassname) {}
	
	public void addVariantName(Item item, String... names) {}
	
	public void addVariantNamesFromEnum(Item item, IStateEnum names) {}
	
	public void ignoreProperties(Block block, IProperty[] properties) {};
	
	public void playLoopingSound(String resourcelocation, IPitchAndVolumProvider pitchandvolumneprovider) {};
	
	public World getClientWorld() {
		return null;
	}
}

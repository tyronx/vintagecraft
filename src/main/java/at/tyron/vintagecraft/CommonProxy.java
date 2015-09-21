package at.tyron.vintagecraft;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import at.tyron.vintagecraft.Client.Gui.GuiAnvil;
import at.tyron.vintagecraft.Client.Gui.GuiCarpenterTable;
import at.tyron.vintagecraft.Client.Gui.GuiCoalPoweredMinecart;
import at.tyron.vintagecraft.Client.Gui.GuiForge;
import at.tyron.vintagecraft.Client.Gui.GuiStove;
import at.tyron.vintagecraft.Client.Gui.GuiVessel;
import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecartVC;
import at.tyron.vintagecraft.Entity.EntityEmptyMinecartVC;
import at.tyron.vintagecraft.Entity.EntityMinecartVC;
import at.tyron.vintagecraft.Entity.EntityStone;
import at.tyron.vintagecraft.Entity.Animal.EntityCowVC;
import at.tyron.vintagecraft.Entity.Animal.EntityForestSpider;
import at.tyron.vintagecraft.Entity.Animal.EntityMobHorse;
import at.tyron.vintagecraft.Entity.Animal.EntitySheepVC;
import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerCarpenterTable;
import at.tyron.vintagecraft.Inventory.ContainerCoalPoweredMinecart;
import at.tyron.vintagecraft.Inventory.ContainerForge;
import at.tyron.vintagecraft.Inventory.ContainerStove;
import at.tyron.vintagecraft.Inventory.ContainerVessel;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEBlastPowderSack;
import at.tyron.vintagecraft.TileEntity.TEWoodBucket;
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
import net.minecraft.client.particle.EntityFX;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.event.entity.player.AchievementEvent;
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
	public int nightskytypeOffset = 0;
	
	
	public int getNightSkyType() {
		return (int) (Math.abs(worldSeed + nightskytypeOffset) % 4);
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
		GameRegistry.registerTileEntity(TEBlastPowderSack.class, ModInfo.ModID + ":blastpowdersack");
		GameRegistry.registerTileEntity(TEWoodBucket.class, ModInfo.ModID + ":bucket");
	}
	
	

	public void init(FMLInitializationEvent event) {
		EntityRegistry.registerModEntity(EntityStone.class, "stonethrown", 3, VintageCraft.instance, 64, 1, true);  
		EntityRegistry.registerModEntity(EntityMobHorse.class, "mobhorse", 4, VintageCraft.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityForestSpider.class, "VCForestSpider", 5, VintageCraft.instance, 64, 1, true);
		EntityRegistry.registerModEntity(EntityCoalPoweredMinecartVC.class, "VCCoalPoweredMinecart", 6, VintageCraft.instance, 80, 1, true);
		EntityRegistry.registerModEntity(EntityEmptyMinecartVC.class, "VCMinecart", 7, VintageCraft.instance, 80, 1, true);
		
		EntityRegistry.registerModEntity(EntityCowVC.class, "CowVC", 8, VintageCraft.instance, 80, 1, true);
		EntityRegistry.registerModEntity(EntitySheepVC.class, "SheepVC", 9, VintageCraft.instance, 80, 1, true);
		
		
		for (Achievement ach : AchievementsVC.achievements) {
			ach.registerStat();
		}
		
		AchievementPage.registerAchievementPage(new AchievementPage(
			"Vintagecraft", 
		    AchievementsVC.achievements.toArray(new Achievement[0])
		));

	}


	public void postInit(FMLPostInitializationEvent event) {		
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
		if (ID == 7) {
			double centerX = x + 0.5;
			double centerY = y + 0.5;
			double centerZ = z + 0.5;
			AxisAlignedBB bb = new AxisAlignedBB(x - 1, y - 1, z - 1, x+1.5, y+1.5, z+1.5);
			
			EntityCoalPoweredMinecartVC cart = (EntityCoalPoweredMinecartVC)findNearestEntityWithinAABB(world, EntityCoalPoweredMinecartVC.class, bb, centerX, centerY, centerZ);
			if (cart == null) return null;

			return new ContainerCoalPoweredMinecart(player.inventory, cart);
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
		if (ID == 7) {
			double centerX = x + 0.5;
			double centerY = y + 0.5;
			double centerZ = z + 0.5;
			AxisAlignedBB bb = new AxisAlignedBB(x - 1, y - 1, z - 1, x+1.5, y+1.5, z+1.5);
			
			EntityCoalPoweredMinecartVC cart = (EntityCoalPoweredMinecartVC)findNearestEntityWithinAABB(world, EntityCoalPoweredMinecartVC.class, bb, centerX, centerY, centerZ);
			if (cart == null) return null;
			return new GuiCoalPoweredMinecart(player.inventory, cart);
		}
		
			
			
		return null;
	}
	
	
	
	
	
	   public Entity findNearestEntityWithinAABB(World world, Class entityType, AxisAlignedBB aabb, double centerX, double centerY, double centerZ) {
	        List list = world.getEntitiesWithinAABB(entityType, aabb);
	        Entity entity1 = null;
	        double d0 = Double.MAX_VALUE;

	        for (int i = 0; i < list.size(); ++i) {
	            Entity entity2 = (Entity)list.get(i);

                double d1 = entity2.getDistance(centerX, centerY, centerZ);
                if (d1 <= d0) {
                    entity1 = entity2;
                    d0 = d1;
                }
	        }

	        return entity1;
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

	public void generateGundPowderSpark(World world, BlockPos pos, float offsetX, float offsetY, float offsetZ) {
	}
}

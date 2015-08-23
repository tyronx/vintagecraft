package at.tyron.vintagecraft;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Network.StartMeteorShowerPacket;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.DynTreeGenerators;
import at.tyron.vintagecraft.WorldGen.GenLayerVC;
import at.tyron.vintagecraft.WorldGen.Helper.BlockStateSerializer;
import at.tyron.vintagecraft.WorldGen.Helper.DynTreeBranch;
import at.tyron.vintagecraft.WorldGen.Helper.DynTreeGen;
import at.tyron.vintagecraft.WorldGen.Helper.DynTreeRoot;
import at.tyron.vintagecraft.WorldGen.Helper.DynTreeTrunk;
import at.tyron.vintagecraft.WorldGen.Helper.NatFloat;
import at.tyron.vintagecraft.WorldGen.Layer.*;
import at.tyron.vintagecraft.WorldGen.Village.DynVillageGenerators;
import at.tyron.vintagecraft.WorldGen.Village.EnumVillage;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrustLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrustLayerGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFlowerGroup;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class VintageCraftCommands extends CommandBase {

	@Override
	public String getName() {
		return "vcraft";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/vcraft genlayer [forest|continents|rock]";
	}

	
	// /vcraft genlayer forest
	
	void replaceBlocks(BlockPos center, ICommandSender sender, int wdt, int hgt, boolean ignoremeta) {
		ItemStack searchstack = ((EntityPlayer)sender.getCommandSenderEntity()).inventory.mainInventory[0];
		ItemStack replacestack = ((EntityPlayer)sender.getCommandSenderEntity()).inventory.mainInventory[1];

		if (!(searchstack.getItem() instanceof ItemBlock)) {
			sender.addChatMessage(new ChatComponentText("Search item is not a block"));
			return;
		}

		
		if (!(replacestack.getItem() instanceof ItemBlock)) {
			sender.addChatMessage(new ChatComponentText("Replace item is not a block"));
			return;
		}
		
		Block searchblock = ((ItemBlock)searchstack.getItem()).block;
		Block replaceblock = ((ItemBlock)replacestack.getItem()).block;
		IBlockState replaceblockstate = replaceblock.getStateFromMeta(replacestack.getItemDamage());
		
		int replaced = 0;
		
		for (int x = -wdt/2; x < wdt; x++) {
			for (int z = -wdt/2; z < wdt; z++) {
				for (int y = 0; y < hgt; y++) {
					IBlockState blockstate = sender.getEntityWorld().getBlockState(center.add(x, y, z));
						
					
					if (blockstate.getBlock() == searchblock && 
						(ignoremeta || blockstate.getBlock().getMetaFromState(blockstate) == searchstack.getItemDamage())) { 
					
						sender.getEntityWorld().setBlockState(center.add(x, y, z), replaceblockstate);
						replaced++;
					}
				}
			}
		}
		
		sender.addChatMessage(new ChatComponentText("Replaced " + replaced + " blocks"));
		
	}
	
	void clearArea(World world, BlockPos center, int wdt, int hgt) {
		for (int x = -wdt/2; x < wdt; x++) {
			for (int z = -wdt/2; z < wdt; z++) {
				for (int y = 0; y < hgt; y++) {
					world.setBlockState(center.add(x, y, z), Blocks.air.getDefaultState());
				}
			}
		}
	}
	
	void createPlane(World world, BlockPos center, int wdt, IBlockState state) {
		for (int x = -wdt/2; x < wdt; x++) {
			for (int z = -wdt/2; z < wdt; z++) {
				world.setBlockState(center.add(x, -1, z), state);
			}
		}
	}
	
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		if (args.length == 0) {
			sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
			return;
		}
		
		
		if (args[0].equals("meteorshower")) {
			VintageCraft.instance.packetPipeline.sendToAll(new StartMeteorShowerPacket(10000));
			MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("Something strange is happening in the night sky"));
		}
		
		if (args[0].equals("moonphase")) {
			int moonphase = sender.getEntityWorld().provider.getMoonPhase(sender.getEntityWorld().getWorldTime());
			
			sender.addChatMessage(new ChatComponentText("" + moonphase));
		}

		if (args[0].equals("time")) {
			VCraftWorldSavedData worlddata = VintageCraft.instance.getOrCreateWorldData(sender.getEntityWorld());
			sender.addChatMessage(new ChatComponentText((Math.floor(worlddata.getWorldTime() / 2400F) / 10f) + " days passed"));
		}

		
		if (args[0].equals("mobcap")) {
			VCraftWorldSavedData worlddata = VintageCraft.instance.getOrCreateWorldData(sender.getEntityWorld());
			
			
			sender.addChatMessage(new ChatComponentText(
				EnumCreatureType.MONSTER.getMaxNumberOfCreature() + " max mobs / " + (Math.floor(worlddata.getWorldTime() / 2400F) / 10f) + " days passed"
			));
		}
		
		if (args[0].equals("clear")) {
			int wdt = 30;
			int hgt = 80;
			
			clearArea(sender.getEntityWorld(), sender.getPosition(), wdt, hgt);
		}

		if (args[0].equals("replace")) {
			int wdt = 35;
			int hgt = 35;
			
			replaceBlocks(sender.getPosition(), sender, wdt, hgt, false);
		}
		if (args[0].equals("replaceignoremeta")) {
			int wdt = 35;
			int hgt = 35;
			
			replaceBlocks(sender.getPosition(), sender, wdt, hgt, true);
		}

		
		
		if (args[0].equals("plane")) {
			int wdt = 30;
			int hgt = 30;
			
			clearArea(sender.getEntityWorld(), sender.getPosition(), wdt, hgt);
			
			wdt = 80;
			createPlane(sender.getEntityWorld(), sender.getPosition(), 80, BlocksVC.topsoil.getDefaultState().withProperty(BlockTopSoil.organicLayer, EnumOrganicLayer.NORMALGRASS));
		}
		
		if (args[0].equals("gen")) {
			GsonBuilder builder = new GsonBuilder();
	        builder.registerTypeAdapter(IBlockState.class, new BlockStateSerializer());
	        
	        Gson gson = builder.create();
	        
			DynTreeGen gen = gson.fromJson(args[1], DynTreeGen.class); 
			
			gen.growTree(sender.getEntityWorld(), sender.getPosition().north());
		}
		
		
		if (args[0].equals("cgentree") || args[0].equals("cgenltree") || args[0].equals("cgenptree")) {
			args[0] = args[0].substring(1);
			int wdt = 30;
			int hgt = 80;
			
			clearArea(sender.getEntityWorld(), sender.getPosition(), wdt, hgt);	
		}
		
		
		
		if (args[0].equals("genvillage")) {
			DynVillageGenerators.initGenerators();
			
			float size = 1f;
			if (args.length >= 2) {
				size = (float)parseDouble(args[1]);
			}
			
			EnumVillage.DEFAULT.getGenerator().generate(sender.getEntityWorld(), sender.getPosition(), size);
		}
		
		
		if (args[0].equals("gentree") || args[0].equals("genltree") || args[0].equals("genptree")) {
			float size = 1f;
			float bend = 0f;
			EnumTree tree = EnumTree.SCOTSPINE;
			
			if (args.length >= 2) {
				tree = tree.valueOf(args[1].toUpperCase(Locale.ROOT));
			}
			
			if (args.length >= 3) {
				size = (float)parseDouble(args[2]);
			}
			
			if (args.length == 4) {
				bend = (float)parseDouble(args[3]);
			}
			
			DynTreeGenerators.initGenerators();
			
			DynTreeGen gen = tree.defaultGenerator;
			if (args[0].equals("genltree")) gen = tree.lushGenerator;
			if (args[0].equals("genptree")) gen = tree.poorGenerator;
			
			gen.growTree(sender.getEntityWorld(), sender.getPosition().down().east(3), size);
		}
		
		
		
		if (args[0].equals("genheightmap")) {
			/*GenLayerVC.shouldDraw = true;
			
			long seed = sender.getEntityWorld().rand.nextInt(50000);
			GenLayerTerrain normalTerrainGen = new GenLayerTerrain(seed + 0);
			
			ChunkPrimer primer = new ChunkPrimer();
			normalTerrainGen.generateTerrain(0, 0, primer, sender.getEntityWorld());

			GenLayerVC.drawImageGrayScale(256, genlayer, name);
			
			GenLayerVC.shouldDraw = false;*/
		}
		
		if (args[0].equals("genlayer")) {
			if (args.length == 1) {
				sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
				return;
			}
			
			long seed = sender.getEntityWorld().rand.nextInt(50000);
			
			//seed = 1L;
			
			GenLayerVC.shouldDraw = true;
			
			if (args[1].equals("climate")) {	
				GenLayerVC.genClimate(seed);
			}
			if (args[1].equals("forest")) {	
				GenLayerVC.genForest(seed);
			}
			if (args[1].equals("biomes")) {	
				GenLayerVC.genErosion(seed);
			}
			if (args[1].equals("deposits")) {
				GenLayerVC.genDeposits(seed);
			}
			if (args[1].equals("age")) {
				GenLayerVC.genAgemap(seed);
			}
			if (args[1].equals("heightmap")) {	
				GenLayerVC.genHeightmap(seed);
			}
			if (args[1].equals("noisemod")) {
				GenLayerVC.genNoiseFieldModifier(seed, -70);
			}
			if (args[1].equals("noisemod2")) {
				GenLayerVC.genNoiseFieldModifier(seed, 0);
			}
			if (args[1].equals("rockoffset")) {
				GenLayerVC.genHorizontalRockOffsetMap(seed);
			}
			
			
			GenLayerVC.shouldDraw = false;
			

			sender.addChatMessage(new ChatComponentText("Layers generated with seed " + seed + " to " + getCleanPath()));
			
		}

		
		if (args[0].equals("climate")) {
			int climate[] = VCraftWorld.instance.getClimate(sender.getPosition());
			int forest = VCraftWorld.instance.getForest(sender.getPosition());
			
			sender.addChatMessage(new ChatComponentText(
				"Temperature " + climate[0] + 
				", Rainfall " + climate[2] + 
				", Fertility " + climate[1] + 
				", Forest " + forest + 
				", mod forest " + EnumTree.getForestDensity(255-forest, climate[2], climate[0]) + 
				", descaled temp " + VCraftWorld.instance.deScaleTemperature(climate[0])
			));
			
			EnumFlowerGroup flora = EnumFlowerGroup.getRandomFlowerForClimate(climate[2], climate[0], forest, sender.getEntityWorld().rand);
			System.out.println("chosen flower " + flora);
			
			EnumTree tree = EnumTree.getRandomTreeForClimate(climate[2], climate[0], forest, sender.getPosition().getY(), sender.getEntityWorld().rand);
			System.out.println("chosen tree " + tree);
			/*if (flora != null) {
				sender.getEntityWorld().setBlockState(sender.getPosition(), flora.variants[0].getBlockState());
			}*/
			//System.out.println("tree size " + (0.66f - Math.max(0, (sender.getPosition().getY() * 1f / EnumTree.SCOTSPINE.maxy) - 0.5f)));   //sender.getEntityWorld().rand.nextFloat()/3 - 
		}
		
		if (args[0].equals("reloadgrass")) {
			VCraftWorld.instance.loadTextures(Minecraft.getMinecraft().getResourceManager());
			sender.addChatMessage(new ChatComponentText("reloaded."));
		}
		
		if (args[0].equals("grasscolor")) {
			sender.addChatMessage(new ChatComponentText("#" + Integer.toHexString(VCraftWorld.instance.getGrassColorAtPos(sender.getPosition()))));
		}
		
		if (args[0].equals("toplayers")) {
			EnumRockType rocktype = EnumRockType.CHERT;
			
			IBlockState[]states = EnumCrustLayerGroup.getTopLayers(rocktype, sender.getPosition().down(), sender.getEntityWorld().rand);
			
			System.out.println(states.length);
			
			for (IBlockState state : states) {
				System.out.println(state);
			}
		}
		
		if (args[0].equals("noisegen")) {
			BlockPos pos = sender.getPosition();
			NoiseGeneratorOctaves noiseGen1 = new NoiseGeneratorOctaves(sender.getEntityWorld().rand, 4);
			int xSize = 5;
			int ySize = 17;
			int zSize = 5;
			
			double horizontalScale = 300D;
			double verticalScale = 300D;      // probably horizontal scale
			
			double noise1[] = new double[0];
			noise1 = noiseGen1.generateNoiseOctaves(null, pos.getX(), 0, pos.getZ(), xSize, ySize, zSize, horizontalScale, verticalScale, horizontalScale);
			
			//File outFile = new File(name+".bmp");
			
			
			for (double num : noise1) {
				System.out.println(num);
			}
			
		}
		
	}
	
	
	public static String getCleanPath() {
	   File sdf = new File("");
	   return sdf.getAbsolutePath();
	}

}

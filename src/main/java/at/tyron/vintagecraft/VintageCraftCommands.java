package at.tyron.vintagecraft;

import java.io.File;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.WorldGen.DynTreeGen;
import at.tyron.vintagecraft.WorldGen.GenLayers.*;
import at.tyron.vintagecraft.WorldProperties.EnumCrustLayer;
import at.tyron.vintagecraft.WorldProperties.EnumFlora;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
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
	
	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		World world = sender.getEntityWorld();
		
		if (args.length == 0) {
			sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
			return;
		}
		
		if (args[0].equals("clear")) {
			int wdt = 20;
			int hgt = 30;
			
			for (int x = -wdt/2; x < wdt; x++) {
				for (int z = -wdt/2; z < wdt; z++) {
					for (int y = 0; y < hgt; y++) {
						sender.getEntityWorld().setBlockState(sender.getPosition().add(x, y, z), Blocks.air.getDefaultState());
					}
				}
			}
		}
		
		if (args[0].equals("gentree")) {
			int wdt = 5;
			int hgt = 15;
			
			if (args.length == 2) {
				wdt = parseInt(args[1]);
			}
			if (args.length == 3) {
				hgt = parseInt(args[2]);
			}
			
			DynTreeGen bla     = new DynTreeGen(0.9f, 0.1f, 0.1f, 0.1f, 0, 0, 0, 0, 3, 6, (float)(25*Math.PI/180), (float)(40*Math.PI/180), 0, (float)(2*Math.PI), 3, 2);
			
			
			bla.gen(sender.getEntityWorld(), sender.getPosition().east(3), wdt, hgt);
		}
		
		if (args[0].equals("genlayer")) {
			if (args.length == 1) {
				sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
				return;
			}
			
			long seed = sender.getEntityWorld().rand.nextInt(5000);
			
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
			
			if (args[1].equals("rocks")) {	
				GenLayerVC.genRockLayer(seed, EnumRockType.getRockTypesForCrustLayer(EnumCrustLayer.ROCK_1));
			}
			
			if (args[1].equals("rockdeform")) {	
				GenLayerVC.genRockDeformation(seed);
			}
			
			
			GenLayerVC.shouldDraw = false;

			sender.addChatMessage(new ChatComponentText("Layers generated with seed " + seed + " to " + getCleanPath()));
			
		}

		
		if (args[0].equals("climate")) {
			
			int temp = VCraftWorld.getTemperature(sender.getPosition());
			int rainfall = VCraftWorld.getRainfall(sender.getPosition());
			int fertility = VCraftWorld.getFertily(sender.getPosition());
			int forest =  VCraftWorld.getForest(sender.getPosition());
			
			sender.addChatMessage(new ChatComponentText("Temperature " + temp + ", Rainfall " + rainfall + ", Fertility " + fertility + ", Forest " + forest));
			
			EnumFlora flora = EnumFlora.getRandomFlowerForClimate(rainfall, temp, forest, sender.getEntityWorld().rand);
			
			System.out.println("chosen " + flora);
			/*if (flora != null) {
				sender.getEntityWorld().setBlockState(sender.getPosition(), flora.variants[0].getBlockState());
			}*/
		}
		
		if (args[0].equals("reloadgrass")) {
			VCraftWorld.loadGrassColors(Minecraft.getMinecraft().getResourceManager());
			sender.addChatMessage(new ChatComponentText("reloaded."));
		}
		
		if (args[0].equals("grasscolor")) {
			sender.addChatMessage(new ChatComponentText("#" + Integer.toHexString(VCraftWorld.getGrassColorAtPos(sender.getPosition()))));
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

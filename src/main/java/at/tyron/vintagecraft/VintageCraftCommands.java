package at.tyron.vintagecraft;

import java.io.File;
import java.util.List;
import java.util.Random;

import at.tyron.vintagecraft.WorldGen.DynTreeBranch;
import at.tyron.vintagecraft.WorldGen.DynTreeGen;
import at.tyron.vintagecraft.WorldGen.DynTreeRoot;
import at.tyron.vintagecraft.WorldGen.DynTreeTrunk;
import at.tyron.vintagecraft.WorldGen.GenLayers.*;
import at.tyron.vintagecraft.WorldProperties.EnumCrustLayer;
import at.tyron.vintagecraft.WorldProperties.EnumFlora;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
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
	
	void clearArea(World world, BlockPos center, int wdt, int hgt) {
		for (int x = -wdt/2; x < wdt; x++) {
			for (int z = -wdt/2; z < wdt; z++) {
				for (int y = 0; y < hgt; y++) {
					world.setBlockState(center.add(x, y, z), Blocks.air.getDefaultState());
				}
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
		
		if (args[0].equals("clear")) {
			int wdt = 30;
			int hgt = 80;
			
			clearArea(sender.getEntityWorld(), sender.getPosition(), wdt, hgt);
		}
		
		
		
		if (args[0].equals("genspruce")) {
			float size = 1f;
			
			if (args.length == 2) {
				size = (float)parseDouble(args[1]);
			}
			
			
			/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier),
			 *	new DynTreeBranch(anglevert, varianceAnglevert, anglehori, varianceAnglehori, spacing, varianceSpacing, widthloss)
	   		 */
			DynTreeGen.spruce.gen(sender.getEntityWorld(), sender.getPosition().down().east(3), size, 0f);
		}
		
		
		if (args[0].equals("genbirch")) {
			float size = 1f;
			float bend = 0f;
			
			if (args.length == 2) {
				size = (float)parseDouble(args[1]);
			}

			if (args.length == 3) {
				bend = (float)parseDouble(args[2]);
			}

			DynTreeGen.birch.gen(sender.getEntityWorld(), sender.getPosition().down().east(3), size, bend);
		}
		
		
		
		
		if (args[0].equals("gendogw")) {
			float size = 1f;
			float bend = 0f;
			
			if (args.length == 2) {
				size = (float)parseDouble(args[1]);
			}

			if (args.length == 3) {
				bend = (float)parseDouble(args[2]);
			}

			 DynTreeGen.mountaindogwood.gen(sender.getEntityWorld(), sender.getPosition().down().east(3), size, bend);
		}
		

		if (args[0].equals("genscotspine")) {
			float size = 1f;
			float bend = 0.07f;
			
			if (args.length == 2) {
				size = (float)parseDouble(args[1]);
			}

			if (args.length == 3) {
				bend = (float)parseDouble(args[2]);
			}
			
			// Reference: http://upload.wikimedia.org/wikipedia/commons/3/31/A_trio_of_Scots_Pine_-_geograph.org.uk_-_1750728.jpg
			
			/* new DynTreeTrunk(avgHeight, width, widthloss, branchStart, branchSpacing, branchVarianceSpacing, variance, numBranching, branchWidthMultiplier),
			 *	new DynTreeBranch(anglevert, varianceAnglevert, anglehori, varianceAnglehori, spacing, varianceSpacing, widthloss, gravityDrag)
	   		 */
			DynTreeGen scotspine = new DynTreeGen(
				EnumTree.SCOTSPINE, 
				null,
				new DynTreeTrunk(0.8f, 1f, 0.05f, 0.5f, 0.02f, 0f, 0.1f, 3, 0.4f),
				new DynTreeBranch(Math.PI / 2 - 0.9f, 0f, 0, 2*Math.PI, 0.25f, 0f, 0.02f, 0.2f)
			);

			 scotspine.gen(sender.getEntityWorld(), sender.getPosition().down().east(3), size, bend);
		}
		
		
		
		
		
	/*	if (args[0].equals("gentrees")) {
			DynTreeGen birch = new DynTreeGen(
				EnumTree.BIRCH, 
				null,
				new DynTreeTrunk(0.8f, 1f, 0.05f, 0.08f, 0.35f, 0.17f, 0.3f, 0.9f, 0.2f, 0.1f),
				new DynTreeBranch(Math.PI / 4, Math.PI / 8, 0, 2*Math.PI, 5f, 2f)
			);
			
			float size = 0;
			int width = 5;
			for (int i = 0; i < 15; i++) {
				size += 0.2f;
				
				clearArea(sender.getEntityWorld(), sender.getPosition().east(width), 50, 80);
				
				width += Math.max(10, size * 10);
			}
			
			
			size = 0;
			width = 5;
			for (int i = 0; i < 15; i++) {
				size += 0.2f;
				
				birch.gen(sender.getEntityWorld(), sender.getPosition().east(width), size);
				
				width += (int) Math.max(10, size * 10);
			}
			
		}*/
		
		
		
		
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

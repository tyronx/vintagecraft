package at.tyron.vintagecraft;

import java.io.File;
import java.util.List;

import at.tyron.vintagecraft.WorldGen.GenLayers.*;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerAddIsland;
import at.tyron.vintagecraft.WorldGen.GenLayers.Continent.GenLayerIsland;


import at.tyron.vintagecraft.WorldProperties.EnumCrustLayer;
import at.tyron.vintagecraft.WorldProperties.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

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
		
		if (args[0].equals("genlayer")) {
			if (args.length == 1) {
				sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
				return;
			}
			
			long seed = sender.getEntityWorld().rand.nextInt(5000);
			
			//seed = 1L;
			
			GenLayerVC.shouldDraw = true;
			
			if (args[1].equals("forest")) {	
				GenLayerVC.genForest(seed);
			}
			if (args[1].equals("biomes")) {	
				GenLayerVC.genBiomes(seed);
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

		
	}
	
	
	public static String getCleanPath() {
	   File sdf = new File("");
	   return sdf.getAbsolutePath();
	}

}

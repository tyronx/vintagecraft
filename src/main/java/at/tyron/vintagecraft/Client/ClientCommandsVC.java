package at.tyron.vintagecraft.Client;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Client.Render.RenderSkyVC;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class ClientCommandsVC extends CommandBase {

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	@Override
	public String getName() {
		return "vcraft";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/vcraft command params";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		
		if (args[0].equals("nst")) {
			
			if (args.length >= 2) {
				VintageCraft.proxy.worldSeed = parseInt(args[1]);
			}
			
			RenderSkyVC.nightSkyTex = null;
		}
		

		if (args[0].equals("nsbc")) {
			if (args.length >= 2) {
				VintageCraftConfig.nightSkyBrightness = parseInt(args[1]);
			}
			
			if (args.length >= 3) {
				VintageCraftConfig.nightSkyContrast = (float) parseDouble(args[2]);
			}
			
			RenderSkyVC.nightSkyTex = null;
		}
		

		if (args[0].equals("fog")) {
			int start = 2;
			int end = 5;
			float density = 0.3f;

			if (args.length >= 2) {
				start = parseInt(args[1]);
			}
			
			if (args.length >= 3) {
				end = parseInt(args[2]);
			}
			
			at.tyron.vintagecraft.Client.Render.RenderFog.customFogRange = start != -1;
			at.tyron.vintagecraft.Client.Render.RenderFog.fogStart = start;
			at.tyron.vintagecraft.Client.Render.RenderFog.fogEnd = end;
		}
		
	}
}


package at.tyron.vintagecraft;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class VintageCraftConfig {
	public static final String ModChannel = "VintageCraft";
	public static boolean rearrangeCreativeTabs = true;
	public static boolean debugBlockRegistration = false;
	
	public static long worldTime = 0L;
	
	static Configuration cfg;

	public static void loadConfig(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile());
		worldTime = Long.parseLong(cfg.get("internal", "worldtime", "0").getString());
 	}
	
	public static void saveConfig() {
		cfg.get("internal", "worldtime", "0").set(worldTime + "");
		cfg.save();
	}

}

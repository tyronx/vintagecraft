package at.tyron.vintagecraft;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class VintageCraftConfig {
	public static final String ModChannel = "VintageCraft";
	public static boolean rearrangeCreativeTabs = true;
	public static boolean debugBlockRegistration = false;
	
	// Configurable stuff
	public static int mobFreeDays = 1;
	public static int easyMobCap = 70;
	public static int mediumMobCap = 80;
	public static int hardMobCap = 90;
	
	static Configuration cfg;

	public static void loadConfig(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile());
		
	//	worldTime = Long.parseLong(cfg.get("internal", "worldtime", "0").getString());
		mobFreeDays = cfg.get("difficulty", "mobFreeDays", "1", "Amount of days in a new world where mob cap is 0, after that in increases by 15 each day until the default cap is reached. Set to 0 to have no mob cap reduction at all.").getInt();
		easyMobCap = cfg.get("difficulty", "easyMobCap", "70", "Mob cap when on easy difficulty").getInt();
		mediumMobCap = cfg.get("difficulty", "mediumMobCap", "80", "Mob cap when on medium difficulty").getInt();
		hardMobCap = cfg.get("difficulty", "hardMobCap", "90", "Mob cap when on hard difficulty").getInt();
 	}
	
	public static void saveConfig() {
		cfg.save();
	}

}

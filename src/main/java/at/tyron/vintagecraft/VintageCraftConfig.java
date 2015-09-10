package at.tyron.vintagecraft;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class VintageCraftConfig {
	// Terraingeneration
	/*public static float coordinateScale = 1000f;
	public static float heightScale = 1000f;
	public static float depthNoiseScaleX = 200f;
    public static float depthNoiseScaleZ = 200f;
    public static float depthNoiseScaleExponent = 0.5f;
    public static float mainNoiseScaleX = 80f;
    public static float mainNoiseScaleY = 160f;
    public static float mainNoiseScaleZ = 80f;
    public static float biomeDepthWeight = 1.0F;
    public static float biomeDepthOffset = 0.0F;
    public static float biomeScaleWeight = 1.0F;
    public static float biomeScaleOffset = 0.0F;
    public static float baseSize = 8.5F;
    public static float stretchY = 12.0F;
    public static float upperLimitScale = 512.0F;
    public static float lowerLimitScale = 512.0F;*/

	
	
	
	
	public static final String ModChannel = "VintageCraft";







	
	
	public static boolean rearrangeCreativeTabs = true;
	public static boolean debugBlockRegistration = false;
	
	// Configurable stuff
	public static int mobFreeDays = 2;
	public static int easyMobCap = 70;
	public static int mediumMobCap = 80;
	public static int hardMobCap = 90;
	
	public static int nightSkyBrightness = -20;
	public static float nightSkyContrast = 0.8f;
	
	
	static Configuration cfg;

	public static void loadConfig(FMLPreInitializationEvent event) {
		cfg = new Configuration(event.getSuggestedConfigurationFile());
		
	//	worldTime = Long.parseLong(cfg.get("internal", "worldtime", "0").getString());
		mobFreeDays = cfg.get("difficulty", "mobFreeDays", "1", "Amount of days in a new world where mob cap is 0, after that in increases by 15 each day until the default cap is reached. Set to 0 to have no mob cap reduction at all.").getInt();
		easyMobCap = cfg.get("difficulty", "easyMobCap", "70", "Mob cap when on easy difficulty").getInt();
		mediumMobCap = cfg.get("difficulty", "mediumMobCap", "80", "Mob cap when on medium difficulty").getInt();
		hardMobCap = cfg.get("difficulty", "hardMobCap", "90", "Mob cap when on hard difficulty").getInt();
		
		nightSkyBrightness = cfg.get("client", "nightskyBrightness", -20).getInt();
		nightSkyContrast = (float) cfg.get("client", "nightskyContrast", 0.8).getDouble();
 	}
	
	public static void saveConfig() {
		cfg.save();
	}

}

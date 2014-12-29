package at.tyron.vintagecraft.World;

import net.minecraft.world.biome.BiomeGenBase;

public class VCBiome extends BiomeGenBase {
	static float riverDepthMin = -0.5F;
	public static float riverDepthMax = -0.3F;
	
	public static VCBiome[] biomeList = new VCBiome[256];
	
	public static final VCBiome ocean = new VCBiome(0).setBiomeName("Ocean").setMinMaxHeight(-0.9F, 0.00001F).setBiomeColor(0x0000ff);
	public static final VCBiome plains = new VCBiome(1).setBiomeName("Plains").setBiomeColor(0x69dfa0);
	public static final VCBiome lake = new VCBiome(2).setBiomeName("Lake").setMinMaxHeight(-0.5F, 0.001F).setBiomeColor(0x4a8e9e);
	public static final VCBiome HighHills = (new VCBiome(3)).setBiomeName("High Hills").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0x044f27);
	
		
	public static final VCBiome swampland = (new VCBiome(6)).setBiomeName("Swamp").setMinMaxHeight(-0.1F, 0.1F).setBiomeColor(0x1f392b);
	public static final VCBiome river = new VCBiome(7).setBiomeName("River").setMinMaxHeight(riverDepthMin, riverDepthMax).setBiomeColor(0xffffff);
	
	public static final VCBiome beach = (new VCBiome(16)).setColor(0xfade55).setBiomeName("Beach").setMinMaxHeight(0.01F, 0.02F).setBiomeColor(0xffb873);
	public static final VCBiome gravelbeach = (new VCBiome(17)).setColor(0xfade55).setBiomeName("Gravel Beach").setMinMaxHeight(0.01F, 0.02F).setBiomeColor(0x8f7963);
	
	
	public static final VCBiome HighHillsEdge = (new VCBiome(20)).setBiomeName("High Hills Edge").setMinMaxHeight(0.2F, 0.4F).setBiomeColor(0x30a767);
	public static final VCBiome rollingHills = (new VCBiome(30)).setBiomeName("Rolling Hills").setMinMaxHeight(0.1F, 0.4F).setBiomeColor(0x87b434);
	public static final VCBiome Mountains = (new VCBiome(31)).setBiomeName("Mountains").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0x707960);
	public static final VCBiome MountainsEdge = (new VCBiome(32)).setBiomeName("Mountains Edge").setMinMaxHeight(0.4F, 0.8F).setBiomeColor(0xb2bc9f);
	public static final VCBiome HighPlains = (new VCBiome(35)).setBiomeName("High Plains").setMinMaxHeight(0.4F, 0.43F).setBiomeColor(0xa6a41c);
	public static final VCBiome DeepOcean = new VCBiome(36).setBiomeName("Deep Ocean").setMinMaxHeight(-1.5F, 0.00001F).setBiomeColor(0x0e055a);
	
	
	
	
	protected int biomeColor = 0x000000;
	
	
	public static boolean isOceanic(int id) {
		return id == VCBiome.ocean.biomeID || id == VCBiome.DeepOcean.biomeID;
	}
	
	public static boolean isMountainous(int id) {
		return id == VCBiome.Mountains.biomeID || id == VCBiome.MountainsEdge.biomeID;
	}
	
	public static boolean isShore(int id) {
		return id == VCBiome.beach.biomeID || VCBiome.MountainsEdge.biomeID == id; 
	}


	
	public boolean isOceanic() {
		return isOceanic(this.biomeID);
	}
	
	public boolean isMountainous() {
		return isMountainous(this.biomeID);
	}
	
	public boolean isShore() {
		return isShore(this.biomeID); 
	}

	
	
	
	
	
	public VCBiome(int par1) {
		super(par1);

		this.topBlock = BlocksVC.topsoil.getDefaultState();
		this.minHeight = 0.1F;

		biomeList[par1] = this;
	}
	
	
	public int getBiomeColor() {
		return biomeColor;
	}

	public VCBiome setBiomeColor(int c) {
		biomeColor = c;
		return this;
	}
	
	

	@Override
	public VCBiome setBiomeName(String par1Str) {
		this.biomeName = par1Str;
		return this;
	}
	
	
	
	/**
	 * Sets the minimum and maximum height of this biome. Seems to go from -2.0 to 2.0.
	 */
	//@Override
	public VCBiome setMinMaxHeight(float par1, float par2) {
		this.minHeight = par1;
		this.maxHeight = par2;
		
		/*this.minHeight = par1-2.7F;
		this.maxHeight = par2-2.7F;*/
		return this;
	}
	
	
	
	/**
	 * return the biome specified by biomeID, or 0 (ocean) if out of bounds
	 */
	public static VCBiome getBiome(int id)
	{
		if(biomeList[id] == null)
		{
			System.out.println("Biome ID is null: " + id);
		}
		if (id >= 0 && id <= biomeList.length && biomeList[id] != null)
		{
			return biomeList[id];
		}
		else
		{
			System.out.println("Biome ID is out of bounds: " + id + ", defaulting to 0 (Ocean)");
			return ocean;
		}
	}


	@Override
	public VCBiome setColor(int par1) {
		this.color = par1;
		return this;
	}


	public static VCBiome[] getBiomeGenArray() {
		return biomeList;
	}
	
	
	/*@Override
	public float getSpawningChance() {
		return 1f;
	}*/
}

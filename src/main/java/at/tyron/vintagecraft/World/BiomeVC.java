package at.tyron.vintagecraft.World;

import java.util.ArrayList;

import at.tyron.vintagecraft.Interfaces.IGenLayerSupplier;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeVC extends BiomeGenBase implements IGenLayerSupplier {
	static float riverDepthMin = -0.5F;
	public static float riverDepthMax = -0.3F;
	int weight = 100;
	
	public static BiomeVC[] biomeList = new BiomeVC[256];
	
	/*public static final BiomeVC ocean = new BiomeVC(0).setBiomeName("Ocean").setMinMaxHeight(-0.9F, 0.00001F).setBiomeColor(0x0000ff);
	public static final BiomeVC plains = new BiomeVC(1).setBiomeName("Plains").setBiomeColor(0x69dfa0);
	public static final BiomeVC lake = new BiomeVC(2).setBiomeName("Lake").setMinMaxHeight(-0.5F, 0.001F).setBiomeColor(0x4a8e9e);
	public static final BiomeVC HighHills = (new BiomeVC(3)).setBiomeName("High Hills").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0x044f27);
	
		
	public static final BiomeVC swampland = (new BiomeVC(6)).setBiomeName("Swamp").setMinMaxHeight(-0.1F, 0.1F).setBiomeColor(0x1f392b);
	public static final BiomeVC river = new BiomeVC(7).setBiomeName("River").setMinMaxHeight(riverDepthMin, riverDepthMax).setBiomeColor(0xffffff);
	
	public static final BiomeVC beach = (new BiomeVC(16)).setColor(0xfade55).setBiomeName("Beach").setMinMaxHeight(0.01F, 0.02F).setBiomeColor(0xffb873);
	public static final BiomeVC gravelbeach = (new BiomeVC(17)).setColor(0xfade55).setBiomeName("Gravel Beach").setMinMaxHeight(0.01F, 0.02F).setBiomeColor(0x8f7963);
	
	
	public static final BiomeVC HighHillsEdge = (new BiomeVC(20)).setBiomeName("High Hills Edge").setMinMaxHeight(0.2F, 0.4F).setBiomeColor(0x30a767);
	public static final BiomeVC rollingHills = (new BiomeVC(30)).setBiomeName("Rolling Hills").setMinMaxHeight(0.1F, 0.4F).setBiomeColor(0x87b434);
	public static final BiomeVC Mountains = (new BiomeVC(31)).setBiomeName("Mountains").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0x707960);
	public static final BiomeVC MountainsEdge = (new BiomeVC(32)).setBiomeName("Mountains Edge").setMinMaxHeight(0.4F, 0.8F).setBiomeColor(0xb2bc9f);
	public static final BiomeVC HighPlains = (new BiomeVC(35)).setBiomeName("High Plains").setMinMaxHeight(0.4F, 0.43F).setBiomeColor(0xa6a41c);
	public static final BiomeVC DeepOcean = new BiomeVC(36).setBiomeName("Deep Ocean").setMinMaxHeight(-1.5F, 0.00001F).setBiomeColor(0x0e055a);
	*/
	
	/*
	public static final BiomeVC Submerged = new BiomeVC(0).setBiomeName("Submerged").setMinMaxHeight(-0.9F, 0.00001F).setBiomeColor(0x0000ff);
	public static final BiomeVC Plains = new BiomeVC(1).setBiomeName("Flat").setBiomeColor(0x69dfa0);
	public static final BiomeVC Lake = new BiomeVC(2).setBiomeName("Lake").setMinMaxHeight(-0.5F, 0.001F).setBiomeColor(0x4a8e9e);
	public static final BiomeVC HighHills = (new BiomeVC(3)).setBiomeName("High Hills").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0x044f27);
	public static final BiomeVC Mountains = (new BiomeVC(31)).setBiomeName("Mountains").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0x707960);
	
	*/
	
	
	public static final BiomeVC Fjords = new BiomeVC(0).setBiomeName("Fjords").setMinMaxHeight(-0.9F, 0.00001F).setBiomeColor(0x000000).setWeight(5);
	public static final BiomeVC Flat = new BiomeVC(1).setBiomeName("Flat").setBiomeColor(0x666666);
	public static final BiomeVC Lake = new BiomeVC(2).setBiomeName("Lake").setMinMaxHeight(-0.5F, 0.001F).setBiomeColor(0x333333);
	public static final BiomeVC HighHills = (new BiomeVC(3)).setBiomeName("High Hills").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0xaaaaaa);
	public static final BiomeVC Mountains = (new BiomeVC(4)).setBiomeName("Mountains").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0xcccccc);
	
	
	
	
	protected int biomeColor = 0x000000;
	
	
/*	public static boolean isOceanic(int id) {
		return id == BiomeVC.ocean.biomeID || id == BiomeVC.DeepOcean.biomeID;
	}
	
	public static boolean isMountainous(int id) {
		return id == BiomeVC.Mountains.biomeID || id == BiomeVC.MountainsEdge.biomeID;
	}
	
	public static boolean isShore(int id) {
		return id == BiomeVC.beach.biomeID || BiomeVC.MountainsEdge.biomeID == id; 
	}


	
	public boolean isOceanic() {
		return isOceanic(this.biomeID);
	}
	
	public boolean isMountainous() {
		return isMountainous(this.biomeID);
	}
	
	public boolean isShore() {
		return isShore(this.biomeID); 
	}*/

	
	
	
	
	
	public BiomeVC(int par1) {
		super(par1);

		this.topBlock = BlocksVC.topsoil.getDefaultState();
		this.minHeight = 0.1F;

		biomeList[par1] = this;
	}
	
	
	public int getBiomeColor() {
		return biomeColor;
	}

	public BiomeVC setBiomeColor(int c) {
		biomeColor = c;
		return this;
	}
	
	

	@Override
	public BiomeVC setBiomeName(String par1Str) {
		this.biomeName = par1Str;
		return this;
	}
	
	
	
	/**
	 * Sets the minimum and maximum height of this biome. Seems to go from -2.0 to 2.0.
	 */
	//@Override
	public BiomeVC setMinMaxHeight(float par1, float par2) {
		this.minHeight = par1;
		this.maxHeight = par2;
		
		/*this.minHeight = par1-2.7F;
		this.maxHeight = par2-2.7F;*/
		return this;
	}
	
	
	
	/**
	 * return the biome specified by biomeID, or 0 (ocean) if out of bounds
	 */
	public static BiomeVC getBiome(int id) {
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
			//return ocean;
			return Flat;
		}
	}

	
	public static IGenLayerSupplier[] getBiomes() {
		ArrayList<BiomeVC> biomes = new ArrayList<BiomeVC>();
		
		for (BiomeVC biome : biomeList) {
			if (biome != null) biomes.add(biome);
		}
		
		return biomes.toArray(new BiomeVC[0]);
	}
	

	

	@Override
	public BiomeVC setColor(int par1) {
		this.color = par1;
		return this;
	}


	public static BiomeVC[] getBiomeGenArray() {
		return biomeList;
	}


	@Override
	public int getDepthMin() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getDepthMax() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getColor() {
		return biomeID;
	}


	@Override
	public int getWeight() {
		return weight;
	}
	
	public BiomeVC setWeight(int weight) {
		this.weight = weight;
		return this;
	}


	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}


	
	/*@Override
	public float getSpawningChance() {
		return 1f;
	}*/
}

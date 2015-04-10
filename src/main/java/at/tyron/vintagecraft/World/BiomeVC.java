package at.tyron.vintagecraft.World;

import java.util.ArrayList;

import at.tyron.vintagecraft.Interfaces.IGenLayerSupplier;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeVC extends BiomeGenBase implements IGenLayerSupplier {
	static float riverDepthMin = -0.5F;
	public static float riverDepthMax = -0.3F;
	int weight = 100;
	
	public static BiomeVC[] biomeList = new BiomeVC[256];
	
	public static final BiomeVC Fjords = new BiomeVC(0).setBiomeName("Fjords").setMinMaxHeight(-0.9F, 0.00001F).setBiomeColor(0x000000).setWeight(5);
	public static final BiomeVC Flat = new BiomeVC(1).setBiomeName("Flat").setBiomeColor(0x666666);
	public static final BiomeVC Lake = new BiomeVC(2).setBiomeName("Lake").setMinMaxHeight(-0.5F, 0.001F).setBiomeColor(0x333333);
	public static final BiomeVC HighHills = (new BiomeVC(3)).setBiomeName("High Hills").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0xaaaaaa);
	public static final BiomeVC Mountains = (new BiomeVC(4)).setBiomeName("Mountains").setMinMaxHeight(0.8F, 1.6F).setBiomeColor(0xcccccc);
	
	
	
	
	protected int biomeColor = 0x000000;
	
	
	
	
	
	
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
}

package at.tyron.vintagecraft.World;

import java.util.ArrayList;

import at.tyron.vintagecraft.Interfaces.IGenLayerSupplier;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeVC extends BiomeGenBase implements IGenLayerSupplier {
	static float riverDepthMin = -0.5F;
	public static float riverDepthMax = -0.3F;
	int weight = 100;
	
	public static BiomeVC[] biomeList = new BiomeVC[256];
	
	public static final BiomeVC Fjords = new BiomeVC(0).setBiomeName("Rolling Hills").setMinMaxHeight(0F, 0.2F).setBiomeColor(0x000000).setWeight(5);
	public static final BiomeVC Flat = new BiomeVC(1).setBiomeName("Flat").setMinMaxHeight(-0.3F, -0.1F).setBiomeColor(0x666666);
	public static final BiomeVC Lake = new BiomeVC(2).setBiomeName("Lake").setMinMaxHeight(-1F, -0.5F).setBiomeColor(0x333333).setWeight(10);
	public static final BiomeVC HighHills = (new BiomeVC(3)).setBiomeName("High Hills").setMinMaxHeight(0.7F, 1.5F).setBiomeColor(0xaaaaaa);
	public static final BiomeVC Mountains = (new BiomeVC(4)).setBiomeName("Mountains").setMinMaxHeight(1.8F, 2.5F).setBiomeColor(0xcccccc);
	
	
	
	
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
	public BiomeVC setMinMaxHeight(float minheight, float maxheight) {
		this.minHeight = minheight - 0.2f;
		this.maxHeight = maxheight - 0.2f;
		
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

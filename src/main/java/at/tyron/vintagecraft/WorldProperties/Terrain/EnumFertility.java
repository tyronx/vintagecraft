package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IStateEnum;

public enum EnumFertility implements IStateEnum, IStringSerializable {
	VERYLOW (3, 1, 0.00002f, false),
	LOW (0, 4, 0.5f, true),
	MEDIUM (1, 9, 1f, true),
	HIGH (2, 21, 1.35f, true) 
	;

	int meta;
	int minfertility; // value between 0 - 25 for it to be classified as this type fertilty
	public boolean topsoil;
	public float growthspeedmultiplier;
	
	private static EnumFertility[] FERTILITY_LOOKUP = new EnumFertility[26];
	
	
	private EnumFertility(int meta, int minfertility, float growthspeedmultiplier, boolean topsoil) {
		this.meta = meta;
		this.minfertility = minfertility;
		this.growthspeedmultiplier = growthspeedmultiplier;
		this.topsoil = topsoil;
	}
	
	
	public int getAsNumber() {
		if (this == VERYLOW) return 20;
		if (this == LOW) return 65;
		if (this == MEDIUM) return 150;
		return 235;
	}
	
	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}
	@Override
	public String getStateName() {
		return name().toLowerCase(Locale.ROOT);
	}
	
	
	
	@Override
	public int getMetaData(Block block) {
		return meta;
	}

	public int getMetaData() {
		return meta;
	}

	@Override
	public int getId() {
		return meta;
	}
	
	public int getMinFertility() {
		return minfertility;
	}
	
	
	public static EnumFertility fromMeta(int meta) {
		for (EnumFertility fertility : EnumFertility.values()) {
			if (fertility.meta == meta) return fertility;
		}
		return EnumFertility.LOW;
	}
	
	
	public static EnumFertility fromFertilityValue(int fertility) {
		return FERTILITY_LOOKUP[fertility/10];
	}
	
	

	@Override
	public void init(Block block, int meta) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static EnumFertility[] valuesForTopsoil() {
		return new EnumFertility[] {LOW, MEDIUM, HIGH};
	}
	
	
	static {
		EnumFertility fertility = null, nextfertility = null;
        for (int i = 0; i < FERTILITY_LOOKUP.length; i++) {
        	if (fertility != HIGH) nextfertility = fromMeta(fertility == null ? 0 : fertility.meta + 1);
        	
        	if (nextfertility.minfertility <= i) fertility = nextfertility;
        	
        	FERTILITY_LOOKUP[i] = fertility;
        }
        
        FERTILITY_LOOKUP[0] = VERYLOW;
        FERTILITY_LOOKUP[1] = VERYLOW;
        FERTILITY_LOOKUP[2] = VERYLOW;
        FERTILITY_LOOKUP[3] = VERYLOW;        
	}

}

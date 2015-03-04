package at.tyron.vintagecraft.WorldProperties;

import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;

public enum EnumFertility implements IEnumState, IStringSerializable {
	LOW (0, "lowf", 4),
	MEDIUM (1, "medf", 10),
	HIGH (2, "hif", 21)
	;

	int meta;
	int minfertility; // value between 0 - 25 for it to be classified as this type fertilty
	String shortname;
	
	private static EnumFertility[] FERTILITY_LOOKUP = new EnumFertility[26];
	
	
	private EnumFertility(int meta, String shortname, int minfertility) {
		this.meta = meta;
		this.shortname = shortname;
		this.minfertility = minfertility;
	}
	
	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	public String shortName() {
		return shortname;
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
	
	@Override
	public String getStateName() {
		return getName();
	}
	
	public int getMinFertility() {
		return minfertility;
	}
	
	
	public static EnumFertility fromMeta(int meta) {
		for (EnumFertility fertility : EnumFertility.values()) {
			if (fertility.meta == meta) return fertility;
		}
		return null;
	}
	
	
	public static EnumFertility fromFertilityValue(int fertility) {
		return FERTILITY_LOOKUP[fertility/10];
	}
	
	

	@Override
	public void init(Block block, int meta) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	static {
		EnumFertility fertility = null, nextfertility = null;
        for (int i = 0; i < FERTILITY_LOOKUP.length; i++) {
        	if (fertility != HIGH) nextfertility = fromMeta(fertility == null ? 0 : fertility.meta + 1);
        	
        	if (nextfertility.minfertility <= i) fertility = nextfertility;
        	
        	FERTILITY_LOOKUP[i] = fertility;
        }
        
        
        
	}

}

package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.util.IStringSerializable;

public enum EnumDoublePlantTypeVC implements IStringSerializable, IEnumState {
    GOLDENROD(0, "goldenrod", -1),
    GOLDENROD2(1, "goldenrod2", 0),
    GOLDENROD3(2, "goldenrod3", 1),
    
    BUTTERFLYMILKWEED(3, "butterflymilkweed", -1),
    BUTTERFLYMILKWEED2(4, "butterflymilkweed2", 3),
    ORANGEBUTTERFLYBUSH(5, "orangebutterflybush", -1),
    
    LATANA(6, "latana", -1)
    ;
    
    static EnumDoublePlantTypeVC[] META_LOOKUP = new EnumDoublePlantTypeVC[values().length];
    int meta;
    String name;
    String unlocalizedName;
    int variantof;

    private EnumDoublePlantTypeVC(int meta, String name, int variantof) {
    	this.meta = meta;
        this.name = name;
        this.unlocalizedName = name;
    }


    public int getMeta() {
        return this.meta;
    }

    public String toString() {
        return this.name;
    }

    public static EnumDoublePlantTypeVC fromMeta(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }

        return META_LOOKUP[meta];
    }

    public String getName() {
        return this.name;
    }

    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }

    static {
        EnumDoublePlantTypeVC[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            EnumDoublePlantTypeVC var3 = var0[var2];
            META_LOOKUP[var3.getMeta()] = var3;
        }
    }

	@Override
	public int getMetaData() {
		return meta;
	}


	@Override
	public String getStateName() {
		return getName();
	}
}
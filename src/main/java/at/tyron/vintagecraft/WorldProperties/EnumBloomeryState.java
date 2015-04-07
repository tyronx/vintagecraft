package at.tyron.vintagecraft.WorldProperties;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IStringSerializable;

public enum EnumBloomeryState implements IStringSerializable {
	UNLIT_EMPTY (0, 0),
	UNLIT_2 (0, 2),
	UNLIT_4 (0, 4),
	UNLIT_6 (0, 6),
	UNLIT_8 (0, 8),
	UNLIT_10 (0, 10),
	UNLIT_12 (0, 12),
	UNLIT_14 (0, 14),
	UNLIT_16 (0, 16),
	LIT (1, 0),
	MELTED_4 (2, 4),
	MELTED_6 (2, 6),
	MELTED_8 (2, 8),
	MELTED_10 (2, 10),
	MELTED_12 (2, 12),
	MELTED_14 (2, 14),
	MELTED_16 (2, 16)
	
	;
	
	
	int fillheight;
	// 0 = unlit, 1 = lit, 2 = melted
	int mode;
	
	private EnumBloomeryState(int mode, int fillheight) {
		this.mode = mode;
		this.fillheight = fillheight;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	

	
	public static EnumBloomeryState stateFor(int mode, int fillheight) {
		for (EnumBloomeryState state : values()) {
			if (state.mode == mode && (state.fillheight == fillheight || mode==1)) {
				return state;
			}
		}
		
		System.out.println("fill height for mode="+ mode + ", fillheight = " + fillheight + " is null!");
		
		return null;
	}
	
}

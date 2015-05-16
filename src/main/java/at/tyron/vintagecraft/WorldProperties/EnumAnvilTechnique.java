package at.tyron.vintagecraft.WorldProperties;

public enum EnumAnvilTechnique {
	LIGHTHIT (1),
	MEDIUMHIT (2),
	HEAVYHIT (3),
	DRAW (4),
	PUNCH (5),
	BEND (6), 
	UPSET (7),
	SHRINK (8)
	;
	
	int id;
	static EnumAnvilTechnique[] TECHNIQUESBYID; 
	
	private EnumAnvilTechnique(int id) {
		this.id = id;
	}
	
	
	public static EnumAnvilTechnique fromId(int id) {
		return TECHNIQUESBYID[id];
	}
	
	public int getId() {
		return id;
	}
	
	
	static {
		TECHNIQUESBYID = new EnumAnvilTechnique[values().length + 1];
		
		EnumAnvilTechnique[] techniques = values();
		
		for (int i = 0; i < values().length; i++) {
			TECHNIQUESBYID[techniques[i].getId()] = techniques[i];
		}
	}
}

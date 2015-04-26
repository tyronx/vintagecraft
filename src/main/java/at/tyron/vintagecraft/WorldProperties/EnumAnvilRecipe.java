package at.tyron.vintagecraft.WorldProperties;

public enum EnumAnvilRecipe {

	PICKAXE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.BEND
	}),
	
	AXE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.LIGHTHIT
	}),
	
	SWORD (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.SHRINK
	}),
	
	SHEARS (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT
	}),
	
	SHOVEL (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT
	}),
	
	HOE (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.SHRINK
	}),
	
	SAW (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT
	}),
	
	
	ANVIL_BASE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.HEAVYHIT
	}),

	
	ANVIL_SURFACE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.MEDIUMHIT
	}),
	
	
	ANVIL (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT		
	}),


	
	/*HELMET (new EnumAnvilTechnique[] { }),
	CHESTPLATE (new EnumAnvilTechnique[] { }),
	LEGGINGS (new EnumAnvilTechnique[] { }),
	BOOTS (new EnumAnvilTechnique[] { }),*/
	;
	
	EnumAnvilTechnique []steps;
	
	private EnumAnvilRecipe(EnumAnvilTechnique []steps) {
		this.steps = steps;
	}
}

package at.tyron.vintagecraft.World.Crafting;

import java.util.HashMap;
import java.util.Locale;

import at.tyron.vintagecraft.Item.ItemAnvilPart;
import at.tyron.vintagecraft.Item.ItemAnvilVC;
import at.tyron.vintagecraft.Item.ItemArmorVC;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemMetalPlate;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.item.ItemStack;


// This here is just a helper class that makes it easier to write down and register 
// the anvil recipes of vintagecraft. You do not need it if you were to add your own anvil recipes

public enum EnumAnvilRecipe {

	PICKAXE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.BEND
	}, 3, EnumTool.PICKAXE),
	
	AXE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.LIGHTHIT
	}, 3, EnumTool.AXE),
	
	SWORD (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.SHRINK
	}, 2, EnumTool.SWORD),
	
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
	}, 4, EnumTool.SHEARS),
	
	SHOVEL (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT
	}, 1, EnumTool.SHOVEL),
	
	HOE (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.SHRINK
	}, 1, EnumTool.HOE),
	
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
		EnumAnvilTechnique.LIGHTHIT
	}, 3, EnumTool.SAW),
	
	
	HAMMER (new EnumAnvilTechnique[] {
			EnumAnvilTechnique.HEAVYHIT,
			EnumAnvilTechnique.HEAVYHIT,
			EnumAnvilTechnique.MEDIUMHIT,
			EnumAnvilTechnique.MEDIUMHIT
	}, 4, EnumTool.HAMMER),
	

	CARPENTERSTOOLSET (new EnumAnvilTechnique[] {
			// Hand Saw
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.PUNCH,
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.PUNCH,
			EnumAnvilTechnique.LIGHTHIT,
			// Hand Axe
			EnumAnvilTechnique.MEDIUMHIT,
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.LIGHTHIT,
			// Hand Drill
			EnumAnvilTechnique.DRAW,
			EnumAnvilTechnique.DRAW,
			EnumAnvilTechnique.DRAW,
			// Plane
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.UPSET,
			// Chisel
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.UPSET,
			EnumAnvilTechnique.MEDIUMHIT
	
	}, 5, EnumTool.CARPENTERSTOOLSET),
	

	
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
	}, 10),

	
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
	}, 8),
	
	
	ANVIL (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT		
	}, 0),
	
	
	
	PLATE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.MEDIUMHIT,		
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.UPSET,
		EnumAnvilTechnique.LIGHTHIT,
	}, 2),

	
	HELMET (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.PUNCH
	}, 2, 0),
	
	CHESTPLATE (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.SHRINK,
		EnumAnvilTechnique.UPSET,
	}, 4, 1),
	
	LEGGINGS (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,			
	}, 3, 2),
	
	BOOTS (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.PUNCH,
	}, 2, 3),
	
	
	COKEOVENDOOR (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.MEDIUMHIT,
	}),

	
	FIX_INGOT (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.LIGHTHIT,		
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.LIGHTHIT		
	}, 1),
			
	FIX_PLATE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.LIGHTHIT,
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.LIGHTHIT,		
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.LIGHTHIT		
	}, 1),
				
	/*	
	COPPER_WIRE (new EnumAnvilTechnique[] {
		EnumAnvilTechnique.HEAVYHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.MEDIUMHIT,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,		
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW,
		EnumAnvilTechnique.DRAW, 
		EnumAnvilTechnique.DRAW
	}, 1),
		*/		
		


	
	// Scharniere
	/*HINGES (new EnumAnvilTechnique[] { 
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.BEND,
		EnumAnvilTechnique.PUNCH,
		EnumAnvilTechnique.PUNCH,
	}, 1),
	*/
	;
	
	public EnumAnvilTechnique []steps;
	public EnumTool tool = null;
	public int armorpiece = -1;
	public int ingots = 0;
	public int plates = 0;
	
	private EnumAnvilRecipe(EnumAnvilTechnique []steps) {
		this.steps = steps;
	}
	
	private EnumAnvilRecipe(EnumAnvilTechnique []steps, int ingots) {
		this.steps = steps;
		tool = null;
		this.ingots = ingots;
	}
	
	private EnumAnvilRecipe(EnumAnvilTechnique []steps, int ingots, EnumTool tool) {
		this.steps = steps;
		this.tool = tool;
		this.ingots = ingots;
	}


	private EnumAnvilRecipe(EnumAnvilTechnique []steps, int plates, int armorpiece) {
		this.steps = steps;
		this.armorpiece = armorpiece;
		this.plates = plates;
	}
	
	
	

	
	public static void registerRecipes() {
		HashMap<EnumTool, EnumAnvilRecipe> toolrecipes = new HashMap<EnumTool, EnumAnvilRecipe>();
		for (EnumAnvilRecipe recipe : values()) {
			if (recipe.tool == null) continue;
			toolrecipes.put(recipe.tool, recipe);
		}

		
		HashMap<Integer, EnumAnvilRecipe> armorrecipes = new HashMap<Integer, EnumAnvilRecipe>();
		for (EnumAnvilRecipe recipe : values()) {
			if (recipe.armorpiece == -1) continue;

			armorrecipes.put(recipe.armorpiece, recipe);
		}
		
		
		
		ItemStack output;
		for (EnumMetal metal : EnumMetal.values()) {
			ItemStack metalingot = ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), metal);
			ItemStack metalplate = BlocksVC.metalplate.getItemStackFor(metal);
	
			/**** 1. Tool recipes ****/
			if (metal.hasTools) {
				for (EnumTool tool : EnumTool.values()) {
					if (tool.requiresWoodenHandle) {
						output = new ItemStack(ItemsVC.toolheads.get(metal.getName() + "_" + tool.getName()+"toolhead"));
					} else {
						output = new ItemStack(ItemsVC.tools.get(metal.getName() + "_" + tool.getName()));
					}
					
					//System.out.println("output = " + output);
					metalingot.stackSize = toolrecipes.get(tool).ingots; 
							
					WorkableRecipeBase recipe = toolrecipes.get(tool).registerRecipe(output.copy(), metalingot.copy());
					
					if (tool == EnumTool.CARPENTERSTOOLSET) {
						recipe.setIngredientText("5ingotstier2plus");
					}
				}
			}
			
			/**** 2. Anvil recipes ****/
			if (metal.hasAnvil) {
				ItemStack anvil = ItemAnvilVC.getItemStack(metal);
				ItemStack anvilbase = ItemAnvilPart.setMetal(new ItemStack(ItemsVC.anvilbase), metal);
				ItemStack anvilsurface = ItemAnvilPart.setMetal(new ItemStack(ItemsVC.anvilsurface), metal);
				
				metalingot.stackSize = EnumAnvilRecipe.ANVIL_BASE.ingots;
				EnumAnvilRecipe.ANVIL_BASE.registerRecipe(anvilbase, metalingot.copy());
				metalingot.stackSize = EnumAnvilRecipe.ANVIL_SURFACE.ingots;
				EnumAnvilRecipe.ANVIL_SURFACE.registerRecipe(anvilsurface, metalingot.copy());
				
				EnumAnvilRecipe.ANVIL.registerRecipe(anvil, anvilbase.copy(), anvilsurface.copy()).setIngredientText("anvilbaseplussurface");
			}
			
			
			
			/**** 2. Fix ingot and plate recipes ****/
			metalingot.stackSize = 1;
			FIX_INGOT.registerRecipe(
				metalingot.copy(), 
				((ItemIngot)ItemsVC.metalingot).markOddlyShaped(metalingot.copy(), true)
			);

			
			metalplate.stackSize = 1;
			FIX_PLATE.registerRecipe(
				metalplate.copy(), 
				((ItemMetalPlate)metalplate.getItem()).markOddlyShaped(metalplate.copy(), true)			
			);

			
			
			/***** 3. Metal sheet, and armor *****/
			if (metal.hasArmor) {
				metalplate.stackSize = 1;
				metalingot.stackSize = 2;
				EnumAnvilRecipe.PLATE.registerRecipe(
					metalplate.copy(), 
					metalingot.copy()
				);
			
				for (int i = 0; i < ItemArmorVC.armorTypes.length; i++) {
					String armorpiece = ItemArmorVC.armorTypes[i];
			
					metalplate.stackSize = armorrecipes.get(i).plates;
					
					armorrecipes.get(i).registerRecipe(
						new ItemStack(ItemsVC.armor.get(metal.getName() + "_" + armorpiece)), 
						metalplate.copy()
					);
				}
			}
		}
		
		
		// Coke Oven Door
		ItemStack ironplate  = BlocksVC.metalplate.getItemStackFor(EnumMetal.IRON);
		ironplate.stackSize = 2;
		COKEOVENDOOR.registerRecipe(new ItemStack(BlocksVC.cokeovendoor), ironplate).setIngredientText("2ironplates");
	}
	
	
	WorkableRecipeBase registerRecipe(ItemStack output, ItemStack input, ItemStack input2) {
		AnvilRecipe recipe;
		
		if (input2 != null) {
			recipe = new AnvilRecipe(output, input, input2, steps);
		} else {
			recipe = new AnvilRecipe(output, input, steps);
			if (ingots > 0) {
				recipe.setIngredientText(ingots+"ingots");
			}
			if (plates > 0) {
				recipe.setIngredientText(plates+"plates");
			}
		}
		
		if (this == FIX_PLATE || this == EnumAnvilRecipe.FIX_INGOT) {
			recipe.wildcardMatch = true;
			
		}
		
		if (input.getItem() instanceof ItemIngot || input.getItem() instanceof ItemMetalPlate || input.getItem() instanceof ItemAnvilPart) {
			
			if (input.getItem() instanceof ItemIngot) {
				recipe.setDisplayInRecipeHelper(ItemIngot.getMetal(input) == EnumMetal.IRON);
			}
			if (input.getItem() instanceof ItemMetalPlate) {
				recipe.setDisplayInRecipeHelper(ItemMetalPlate.getMetal(input) == EnumMetal.IRON);
			}
			if (input.getItem() instanceof ItemAnvilPart) {
				recipe.setDisplayInRecipeHelper(ItemAnvilPart.getMetal(input) == EnumMetal.IRON);
			}
			
		} else {
			recipe.setDisplayInRecipeHelper(true);
		}
		
		
		recipe.setUnlocalizedName(name().toLowerCase(Locale.ROOT));
		recipe.setIcon(getOutputForDisplay());
		WorkableRecipeManager.smithing.registerRecipe(recipe);
		return recipe;
	}
	
	WorkableRecipeBase registerRecipe(ItemStack output, ItemStack input) {
		return registerRecipe(output, input, null);
	}
	

	
	
	public ItemStack getOutputForDisplay() {
		EnumMetal metal = EnumMetal.IRON;
		
		switch (this) {
			case ANVIL: return ItemAnvilVC.getItemStack(metal);
			case ANVIL_BASE: return ItemAnvilPart.setMetal(new ItemStack(ItemsVC.anvilbase), metal);
			case ANVIL_SURFACE: return ItemAnvilPart.setMetal(new ItemStack(ItemsVC.anvilsurface), metal);
			case AXE: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_axe"));
			case PICKAXE: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_pickaxe"));
			case HAMMER: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_hammer"));
			case HOE: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_hoe"));
			case SAW: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_saw"));
			case SHOVEL: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_shovel"));
			case SWORD: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_sword"));
			case SHEARS: return new ItemStack(ItemsVC.tools.get(metal.getName() + "_" + "shears"));
			
			case PLATE: return BlocksVC.metalplate.getItemStackFor(metal); //ItemMetalPlate.setMetal(new ItemStack(ItemsVC.metalplate), metal);
			case HELMET: return new ItemStack(ItemsVC.armor.get(metal.getName() + "_helmet"));
			case CHESTPLATE: return new ItemStack(ItemsVC.armor.get(metal.getName() + "_chestplate"));
			case LEGGINGS: return new ItemStack(ItemsVC.armor.get(metal.getName() + "_leggings"));
			case BOOTS: return new ItemStack(ItemsVC.armor.get(metal.getName() + "_boots"));
//			case COPPER_WIRE: return new ItemStack(Items.redstone);
			case FIX_INGOT: return ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), metal);
			case FIX_PLATE: return BlocksVC.metalplate.getItemStackFor(metal); //return ItemMetalPlate.setMetal(new ItemStack(ItemsVC.metalplate), metal);
			case COKEOVENDOOR: return new ItemStack(BlocksVC.cokeovendoor);
			default:
		}
		
		return null;
	}	
}

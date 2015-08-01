package at.tyron.vintagecraft.WorldProperties;

import java.util.HashMap;

import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import at.tyron.vintagecraft.Item.ItemAnvilPart;
import at.tyron.vintagecraft.Item.ItemAnvilVC;
import at.tyron.vintagecraft.Item.ItemArmorVC;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemMetalPlate;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.World.AnvilRecipes;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;

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
					if (tool == EnumTool.SHEARS) {
						output = new ItemStack(ItemsVC.tools.get(metal.getName() + "_" + tool.getName()));
					} else {
						output = new ItemStack(ItemsVC.toolheads.get(metal.getName() + "_" + tool.getName()+"toolhead"));
					}
					
					
					//System.out.println("output = " + output);
					metalingot.stackSize = toolrecipes.get(tool).ingots; 
							
					AnvilRecipes.registerRecipe(new AnvilRecipes(
						output.copy(), 
						metalingot.copy(), 
						toolrecipes.get(tool).steps)
					);
				}
			}
			
			/**** 2. Anvil recipes ****/
			if (metal.hasAnvil) {
				ItemStack anvil = ItemAnvilVC.getItemStack(metal);
				ItemStack anvilbase = ItemAnvilPart.setMetal(new ItemStack(ItemsVC.anvilbase), metal);
				ItemStack anvilsurface = ItemAnvilPart.setMetal(new ItemStack(ItemsVC.anvilsurface), metal);
				
				metalingot.stackSize = EnumAnvilRecipe.ANVIL_BASE.ingots;
				AnvilRecipes.registerRecipe(new AnvilRecipes(anvilbase, metalingot.copy(), EnumAnvilRecipe.ANVIL_BASE.steps));
				metalingot.stackSize = EnumAnvilRecipe.ANVIL_SURFACE.ingots;
				AnvilRecipes.registerRecipe(new AnvilRecipes(anvilsurface, metalingot.copy(), EnumAnvilRecipe.ANVIL_SURFACE.steps));
				
				AnvilRecipes.registerRecipe(new AnvilRecipes(anvil, anvilbase.copy(), anvilsurface.copy(), EnumAnvilRecipe.ANVIL.steps));
			}
			
			
			/**** 2. Create Ingot recipes ****/
			metalingot.stackSize = 1;
			AnvilRecipes.registerRecipe(new AnvilRecipes(
				metalingot.copy(), 
				((ItemIngot)ItemsVC.metalingot).markOddlyShaped(metalingot.copy(), true), 
				FIX_INGOT.steps,
				true
			));

			
			metalplate.stackSize = 1;
			AnvilRecipes.registerRecipe(new AnvilRecipes(
				metalplate.copy(), 
				((ItemMetalPlate)metalplate.getItem()).markOddlyShaped(metalplate.copy(), true), 
				FIX_PLATE.steps,
				true
			));

			
			
			/***** 3. Create Metal sheet recipes *****/
			if (metal.hasArmor) {
				metalplate.stackSize = 1;
				metalingot.stackSize = 2;
				AnvilRecipes.registerRecipe(new AnvilRecipes(
					metalplate.copy(), 
					metalingot.copy(),
					EnumAnvilRecipe.PLATE.steps
				));
			
				for (int i = 0; i < ItemArmorVC.armorTypes.length; i++) {
					String armorpiece = ItemArmorVC.armorTypes[i];
			
					metalplate.stackSize = armorrecipes.get(i).plates;
					
					AnvilRecipes.registerRecipe(new AnvilRecipes(
						new ItemStack(ItemsVC.armor.get(metal.getName() + "_" + armorpiece)),
						metalplate.copy(), 
						armorrecipes.get(i).steps
					));
				}
			}
		}
		
		
		/***** 4. Copper wire ****/
/*		AnvilRecipes.registerRecipe(new AnvilRecipes(
			new ItemStack(Items.redstone, 16), 
			ItemIngot.getItemStack(EnumMetal.COPPER, 1), 
			COPPER_WIRE.steps
		));
	*/	
		
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
			default:
		}
		
		return null;
	}
	

	
	
}

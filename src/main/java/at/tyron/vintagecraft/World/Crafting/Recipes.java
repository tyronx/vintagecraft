
package at.tyron.vintagecraft.World.Crafting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.Item.ItemArmorVC;
import at.tyron.vintagecraft.Item.ItemCarpenterTable;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemLogVC;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemPlanksVC;
import at.tyron.vintagecraft.Item.ItemSeedVC;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.Item.ItemStonePot;
import at.tyron.vintagecraft.Item.ItemToolHead;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrop;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class Recipes {
	static  List recipes = CraftingManager.getInstance().getRecipeList();
	
	static void registerToSorter() {
		RecipeSorter.register("vintagecraft:toolsupportedrecipe", ToolSupportedRecipe.class, Category.SHAPED, "after:minecraft:shaped");
		RecipeSorter.register("vintagecraft:shapelessrecipesvc", ShapelessRecipesVC.class, Category.SHAPELESS, "after:minecraft:shaped");
		RecipeSorter.register("vintagecraft:shapedrecipesvc", ShapedRecipesVC.class, Category.SHAPED, "after:minecraft:shaped");
	}
	
	public static void addRecipes() {
		registerToSorter();
		addInterModRecipes();
		
		removeRecipe(new ItemStack(Items.item_frame));
		removeRecipe(new ItemStack(Items.arrow, 4));
		
		
		
		
		ItemStack stick = new ItemStack(Items.stick);
		ItemStack copperingot = ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.COPPER);
		ItemStack tinbronzeingot = ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.TINBRONZE);
		ItemStack bismuthbronzeingot = ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.BISMUTHBRONZE);
		ItemStack ironingot = ItemIngot.setMetal(new ItemStack(ItemsVC.metalingot), EnumMetal.IRON);
		

		for (EnumTool tool : EnumTool.values()) {
			if (!tool.requiresWoodenHandle) continue;
			
			for (EnumMetal metal : EnumMetal.values()) {
				if (!metal.hasTools) continue;
				
				Item toolitem = ItemsVC.tools.get(metal.getName() + "_" + tool.getName());
				Item dmdtoolitem = ItemsVC.tools.get(metal.getName() + "_" + tool.getName() + "_dmd");
				
				
				Item toolheaditem = ItemsVC.toolheads.get(metal.getName() + "_" + tool.getName() + "toolhead");
				ItemStack toolheadstack = new ItemStack(toolheaditem);
				ItemStack dmdtoolheadstack = ItemToolHead.getDiamondEncrustedVariantOf(new ItemStack(toolheaditem)); 
				
				 
				
				addShapedRecipeVC(new ItemStack(dmdtoolitem), new Object[]{" T", " S", 'T', dmdtoolheadstack, 'S', Items.stick});
				addShapedRecipeVC(new ItemStack(toolitem), new Object[]{" T", " S", 'T', toolheadstack, 'S', Items.stick});

			}
		}
		
		
		for (BlockClassEntry rock : BlocksVC.rock.values()) {
			EnumRockType rocktype = (EnumRockType) rock.getKey();
			ItemStack stone = ItemStone.setRockType(new ItemStack(ItemsVC.stone), rocktype);
			
			GameRegistry.addShapedRecipe(BlocksVC.cobblestone.getItemStackFor(rocktype, 2), new Object[] { "SSS", "SCS", "SSS", 'C', Items.clay_ball, 'S', stone});
			GameRegistry.addShapedRecipe(ItemStonePot.setRockType(new ItemStack(BlocksVC.stonepot), rocktype), new Object[] { "S S", "SCS", "SSS", 'C', Items.clay_ball, 'S', stone});
			
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.tools.get("stone_axe")), new Object[] { "SS ", "SW ", " W ", 'S', stone, 'W', stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.tools.get("stone_hoe")), new Object[] { "SS ", " W ", " W ", 'S', stone, 'W',  Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.tools.get("stone_pickaxe")), new Object[] { "SSS", " W ", " W ", 'S', stone, 'W',  Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.tools.get("stone_shovel")), new Object[] { " S ", " W ", " W ", 'S', stone, 'W', Items.stick});	
			GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.tools.get("stone_hammer")), new Object[] { "SSS", "SWS", " W ", 'S', stone, 'W', Items.stick});

		}

		
		

		
		for (BlockClassEntry log : BlocksVC.log.values()) {
			ItemStack logstack = new ItemStack(log.block);
			ItemLogVC.withTreeType(logstack, log);
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.crafting_table), new Object[] { "LL", "LL", 'L', logstack});
		}
	
		for (BlockClassEntry planks : BlocksVC.planks.values()) {
			ItemStack planksstack = new ItemStack(planks.block);
			ItemPlanksVC.withTreeType(planksstack, planks);
			
			ItemStack slabstack = BlocksVC.singleslab.getItemStackFor(planks.getKey());
			ItemStack logstack = BlocksVC.log.getItemStackFor(planks.getKey());
			
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.chest), new Object[] { "LLL", "LCL", "LLL", 'L', planksstack, 'C', copperingot});
			GameRegistry.addShapedRecipe(new ItemStack(Items.boat, 1), new Object[] { "   ", "W W", "WWW", 'W', planks.block});

			GameRegistry.addShapedRecipe(BlocksVC.fence.getItemStackFor(planks.getKey(), 2), new Object[] { "   ", "WSW", "WSW", 'W', planksstack, 'S', Items.stick});
			GameRegistry.addShapedRecipe(BlocksVC.fencegate.getItemStackFor(planks.getKey()), new Object[] { "   ", "WSW", "WSW", 'S', planksstack, 'W', Items.stick});
			
			
			ItemStack quartzcrystal = ItemOreVC.getItemStackFor(EnumOreType.QUARTZCRYSTAL, 1);
			GameRegistry.addShapedRecipe(BlocksVC.quartzglass.getItemStackFor(planks.getKey()), new Object[] { " S ", "SQS", " S ", 'S', planksstack, 'Q', quartzcrystal});
			
			recipes.add(new ToolSupportedRecipe(BlocksVC.plankstairs.getItemStackFor(planks.getKey()), new Object[] { "S ", " W", 'S', ItemsVC.tools.get("copper_saw"), 'W', planksstack}));
			recipes.add(new ToolSupportedRecipe(BlocksVC.plankstairs.getItemStackFor(planks.getKey()), new Object[] { " S", "W ", 'S', ItemsVC.tools.get("copper_saw"), 'W', planksstack}));

			ItemStack fourplanks = planksstack.copy();
			fourplanks.stackSize = 4;
			recipes.add(new ToolSupportedRecipe(fourplanks, new Object[] { "S ", "W ", 'S', ItemsVC.tools.get("copper_saw"), 'W', logstack}));
			recipes.add(new ToolSupportedRecipe(fourplanks, new Object[] { "W ", "S ", 'S', ItemsVC.tools.get("copper_saw"), 'W', logstack}));

			recipes.add(new ToolSupportedRecipe(BlocksVC.singleslab.getItemStackFor(planks.getKey(), 2), new Object[] { "SW", "  ", 'S', ItemsVC.tools.get("copper_saw"), 'W', planksstack}));
			recipes.add(new ToolSupportedRecipe(BlocksVC.singleslab.getItemStackFor(planks.getKey(), 2), new Object[] { "  ", "SW", 'S', ItemsVC.tools.get("copper_saw"), 'W', planksstack}));
			
			recipes.add(new ToolSupportedRecipe(BlocksVC.plankstairs.getItemStackFor(planks.getKey()), new Object[] { "S ", " W", 'S', ItemsVC.tools.get("copper_saw"), 'W', planksstack}));
			recipes.add(new ToolSupportedRecipe(BlocksVC.plankstairs.getItemStackFor(planks.getKey()), new Object[] { " S", "W ", 'S', ItemsVC.tools.get("copper_saw"), 'W', planksstack}));
				
			
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.wooden_button), new Object[] {"#", '#', planksstack});
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.jukebox, 1), new Object[] { "WWW", "WDW", "WWW", 'W', planksstack, 'D', Items.diamond});
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.wooden_pressure_plate, 1), new Object[] { "WW", 'W', planksstack});
			GameRegistry.addShapedRecipe(new ItemStack(Items.sign, 1), new Object[] { " W", " S", 'W', planksstack, 'S', Items.stick});
			GameRegistry.addShapedRecipe(new ItemStack(Blocks.trapdoor, 1), new Object[] { "   ", "WWW", "WWW", 'W', planksstack});	
			GameRegistry.addShapedRecipe(new ItemStack(Items.bed, 1), new Object[] { "   ", "WWW", "PPP", 'W', Blocks.wool, 'P', planksstack});
			GameRegistry.addShapedRecipe(new ItemStack(Items.oak_door, 1), new Object[] { "WW ", "WW ", "WW ", 'W', planksstack});
			
			
			EnumTree tree = (EnumTree) planks.getKey();
			ItemCarpenterTable item = (ItemCarpenterTable) Item.getItemFromBlock(BlocksVC.carpenterTable);
			if (tree.jankahardness > 800) {
				
				recipes.add(new ToolSupportedRecipe(item.withTreeType(tree), new Object[] { "PPP", "P P", "PSP", 'S', ItemsVC.tools.get("copper_saw"), 'P', planksstack}));
				recipes.add(new ToolSupportedRecipe(item.withTreeType(tree), new Object[] { "PPP", "PSP", "P P", 'S', ItemsVC.tools.get("copper_saw"), 'P', planksstack}));
			}
			
			
		}
		
		
		
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.fireclay_brick_raw, 1), new Object[] { "CC", "CC", 'C', ItemsVC.fireclay_ball});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.fireclaybricks, 1), new Object[] { "CCC", "CCC", "CCC", 'C', ItemsVC.fireclay_brick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.fireclaystairs, 1), new Object[] { "CC ", "CCC", "CCC", 'C', ItemsVC.fireclay_brick});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.furnaceSection, 1), new Object[] { "CCC", "CCC", "C C", 'C', ItemsVC.fireclay_brick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.furnaceChimney, 1), new Object[] { " C ", "CCC", "   ", 'C', ItemsVC.fireclay_brick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.furnaceChimney, 1), new Object[] { "   ", " C ", "CCC", 'C', ItemsVC.fireclay_brick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.tallmetalmolds, 1), new Object[] { "C C", "CCC", "CCC", 'C', ItemsVC.fireclay_brick});
		
		GameRegistry.addShapedRecipe(new ItemStack(Item.getItemFromBlock(BlocksVC.clayVessel)), new Object[] { " C ", "C C", "CCC", 'C', Items.clay_ball});

		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.stove), new Object[] { "BIB", "B B", "BBB", 'B', ItemsVC.fireclay_brick, 'I', BlocksVC.metalplate.getItemStackFor(EnumMetal.IRON)});  //ItemMetalPlate.getItemStack(EnumMetal.IRON, 1)
		
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.firestarter), new Object[] { "SW", "W ", 'S', ItemsVC.dryGrass, 'W', Items.stick});
		
		
		
		
		ItemStack lignite = new ItemStack(ItemsVC.ore);
		ItemOreVC.setOreType(lignite, EnumOreType.LIGNITE);
		
		ItemStack bituminouscoal = new ItemStack(ItemsVC.ore);
		ItemOreVC.setOreType(lignite, EnumOreType.BITUMINOUSCOAL);
		
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.torch, 4), new Object[] { "C", "S", 'C', lignite, 'S', stick});
		GameRegistry.addShapedRecipe(new ItemStack(BlocksVC.torch, 4), new Object[] { "C", "S", 'C', bituminouscoal, 'S', stick});
		
		GameRegistry.addShapelessRecipe(new ItemStack(BlocksVC.torch), new Object[] { Blocks.torch });
		
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.arrow, 2), new Object[] { " T ", " S ", " F ", 'T', Items.flint, 'S', Items.stick, 'F', Items.feather});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.armor_stand), new Object[] {"SSS", " S ", "SCS", 'S', Items.stick, 'C', Items.clay_ball});
		
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.saddle), new Object[] {"LLL", "L L", " B ", 'L', ItemsVC.stitchedleather, 'B', tinbronzeingot});
		GameRegistry.addShapedRecipe(new ItemStack(Items.saddle), new Object[] {"LLL", "L L", " B ", 'L', ItemsVC.stitchedleather, 'B', bismuthbronzeingot});
		
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsVC.flaxTwine), new Object[]{ItemsVC.flaxFibers, ItemsVC.flaxFibers, ItemsVC.flaxFibers, ItemsVC.flaxFibers});
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsVC.stitchedleather), new Object[]{Items.leather, Items.leather, Items.leather, Items.leather, ItemsVC.flaxTwine});
		
		GameRegistry.addShapedRecipe(new ItemStack(ItemsVC.linenCloth), new Object[] {"FFF", "FFF", "FFF", 'F', ItemsVC.flaxTwine});
		
		GameRegistry.addShapedRecipe(new ItemStack(Items.lead), new Object[] {"FF ", "FF ", "  F", 'F', ItemsVC.flaxTwine});
		
		
		ItemStack wheat = ItemSeedVC.withCropType(EnumCrop.WHEAT);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsVC.bread), new Object[]{wheat, wheat, wheat, wheat, wheat});
		ItemStack flaxSeeds = ItemSeedVC.withCropType(EnumCrop.FLAX);
		GameRegistry.addShapelessRecipe(new ItemStack(ItemsVC.bread), new Object[]{flaxSeeds, flaxSeeds, flaxSeeds, flaxSeeds, flaxSeeds});
		
		addShapelessRecipeVC(new ItemStack(ItemsVC.blastingPowder, 3), new Object[] {
			ItemOreVC.getItemStackFor(EnumOreType.SULFUR, 1), 
			ItemOreVC.getItemStackFor(EnumOreType.BITUMINOUSCOAL, 1), 
			ItemOreVC.getItemStackFor(EnumOreType.SALTPETER, 1),
			ItemOreVC.getItemStackFor(EnumOreType.SALTPETER, 1),
			ItemOreVC.getItemStackFor(EnumOreType.SALTPETER, 1),
			ItemOreVC.getItemStackFor(EnumOreType.SALTPETER, 1),
		});
		
		GameRegistry.addShapelessRecipe(new ItemStack(BlocksVC.blastpowdersack, 8), new Object[] {
			ItemsVC.blastingPowder,
			ItemsVC.blastingPowder,
			ItemsVC.blastingPowder,
			ItemsVC.blastingPowder,
			ItemsVC.blastingPowder,
			ItemsVC.blastingPowder,
			ItemsVC.linenCloth,
			ItemsVC.flaxTwine
		});
		
		
		addShapedRecipeVC(new ItemStack(BlocksVC.saltlamp), new Object[] {"SSS", "S S", "STS", 'S', ItemOreVC.getItemStackFor(EnumOreType.ROCKSALT, 1), 'T', new ItemStack(Blocks.torch) });
		
		
	}
	
	
	// Also checks the item NBT Tags and input quantity
	public static void addShapelessRecipeVC(ItemStack stack, Object ... recipeComponents) {
        ArrayList arraylist = Lists.newArrayList();
        Object[] aobject = recipeComponents;
        int i = recipeComponents.length;

        for (int j = 0; j < i; ++j) {
            Object object1 = aobject[j];

            if (object1 instanceof ItemStack) {
                arraylist.add(((ItemStack)object1).copy());
            }
            else if (object1 instanceof Item) {
                arraylist.add(new ItemStack((Item)object1));
            }
            else {
                if (!(object1 instanceof Block)) {
                    throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object1.getClass().getName() + "!");
                }

                arraylist.add(new ItemStack((Block)object1));
            }
        }

        recipes.add(new ShapelessRecipesVC(stack, arraylist));
    }


	
	
	// Also checks the item NBT Tags and input quantity
    public static ShapedRecipesVC addShapedRecipeVC(ItemStack stack, Object ... recipeComponents) {
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (recipeComponents[i] instanceof String[]) {
            String[] astring = (String[])((String[])recipeComponents[i++]);

            for (int l = 0; l < astring.length; ++l) {
                String s1 = astring[l];
                ++k;
                j = s1.length();
                s = s + s1;
            }
        }
        else {
            while (recipeComponents[i] instanceof String)
            {
                String s2 = (String)recipeComponents[i++];
                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = Maps.newHashMap(); i < recipeComponents.length; i += 2) {
            Character character = (Character)recipeComponents[i];
            ItemStack itemstack1 = null;

            if (recipeComponents[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item)recipeComponents[i + 1]);
            }
            else if (recipeComponents[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block)recipeComponents[i + 1], 1, 32767);
            }
            else if (recipeComponents[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack)recipeComponents[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c0))).copy();
            }
            else {
                aitemstack[i1] = null;
            }
        }

        ShapedRecipesVC shapedrecipes = new ShapedRecipesVC(j, k, aitemstack, stack);
        recipes.add(shapedrecipes);
        return shapedrecipes;
    }

    
    static void addInterModRecipes() {
    	sendJarLidMaterial(ItemOreVC.getItemStackFor(EnumOreType.LAPISLAZULI, 1), 40, 2);
    	sendJarLidMaterial(ItemOreVC.getItemStackFor(EnumOreType.EMERALD, 1), 200, 3);
    	sendJarLidMaterial(ItemOreVC.getItemStackFor(EnumOreType.DIAMOND, 1), 400, 4);
    	
    	sendJarLidMaterial(ItemIngot.getItemStack(EnumMetal.LEAD, 1), 80, 0);
    	sendJarLidMaterial(ItemIngot.getItemStack(EnumMetal.GOLD, 1), 300, 1);
    }
    
    static void sendJarLidMaterial(ItemStack itemstack, int uses, int jartype) {
    	NBTTagCompound nbt = new NBTTagCompound();
    	nbt.setTag("liditemstack", itemstack.writeToNBT(new NBTTagCompound()));
    	nbt.setInteger("uses", uses);
    	nbt.setInteger("jartype", jartype);
    	nbt.setBoolean("checknbt", true);
    	
    	FMLInterModComms.sendRuntimeMessage(VintageCraft.instance, "butterflymania", "butterflymania-regjarlidmaterial", nbt);
    }
    

	
	static void removeRecipe(ItemStack resultItem) {
	    ItemStack recipeResult = null;
	    ArrayList recipes = (ArrayList) CraftingManager.getInstance().getRecipeList();

	    for (int scan = 0; scan < recipes.size(); scan++) {
	        IRecipe tmpRecipe = (IRecipe) recipes.get(scan);
	        recipeResult = tmpRecipe.getRecipeOutput();
	        
	        
	        //System.out.println(resultItem + " == " + recipeResult);
	        if (ItemStack.areItemStacksEqual(resultItem, recipeResult)) {
	            System.out.println("[Vintagecraft] Removed Recipe: " + recipes.get(scan) + " -> " + recipeResult);
	            recipes.remove(scan);
	            return;
	        }
	    }
	    
	    System.out.println("failed removing recipe, no matching recipe found for  " + resultItem);
	}
}

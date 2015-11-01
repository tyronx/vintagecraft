package at.tyron.vintagecraft.WorldProperties;

import java.util.Locale;
import java.util.Random;

import at.tyron.vintagecraft.Item.Metalworking.ItemArmorVC;
import at.tyron.vintagecraft.World.ItemsVC;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;

public class MobInventoryItems {
	static float[] weaponChance = new float[]{0.3f, 0.6f, 0.9f};  
	
	static float[][] armorTypeChances = new float[][]{
	/* all armor items */		new float[]{0.1f, 0.2f, 0.4f},
	/* chest+leggings+boots */  new float[]{0.1f, 0.2f, 0.3f},
	/* chest+leggings */		new float[]{0.1f, 0.1f, 0.2f}
	};
	
	
	
	static float[][] metalTierChances = new float[][]{
		/* Iron   */ new float[] { 0.001f, 0.1f, 0.2f },
		/* Bronze */ new float[] { 0.05f, 0.25f, 0.3f },
		/* Copper */ new float[] { 0.2f,  0.15f, 0.2f }
	};
		
	static EnumMetal[] tiers = new EnumMetal[]{EnumMetal.IRON, EnumMetal.TINBRONZE, EnumMetal.COPPER};
	
	
	static float[] horseArmorTypeChances =  /* Iron */ new float[] { 0f, 0.5f, 0.85f };

	
	
	public static ItemStack getDifficultyBasedHorseArmor(EnumDifficulty difficulty, Random rand) {
		if (difficulty == EnumDifficulty.PEACEFUL) return null;
		
		int difficultyIndex = difficulty.ordinal() - 1;
		
		if (rand.nextFloat() < horseArmorTypeChances[difficultyIndex]) {
			return new ItemStack(Items.iron_horse_armor);
		}
		
		return null;
		
	}
	
	/*
	 * stone axe
	 * all metal swords
	 * leather armor (4 variants: chestplate & leggings, full, chestplate leggings boots, leggings boots)
	 * metal armor (same variants)
	 * 
	 */
	
	public static ItemStack[] getDifficultyBasedMobInventory(EnumDifficulty difficulty, float chanceModifier, Random rand) {
		if (difficulty == EnumDifficulty.PEACEFUL) return null;
		
		ItemStack[] inventory = new ItemStack[5];
		
		int difficultyIndex = difficulty.ordinal() - 1;
		
		// Metal tier
		EnumMetal metal = null;
		float rnd = rand.nextFloat() - chanceModifier;
		int i = 0;
		for (float[] tierchances : metalTierChances) {
			rnd -= tierchances[difficultyIndex];
			if (rnd <= 0) {
				metal = tiers[i];
				break;
			}
			i++;
		}
		
		
		// Weapon
		if (rand.nextFloat() - chanceModifier < weaponChance[difficultyIndex]) {
			if (metal == null) {
				inventory[0] = new ItemStack(ItemsVC.tools.get("stone_axe"));
			} else {
				if (rand.nextInt(4) == 0) {
					inventory[0] = new ItemStack(ItemsVC.tools.get(metal.getName() + "_axe"));
				} else {
					inventory[0] = new ItemStack(ItemsVC.tools.get(metal.getName() + "_sword"));					
				}
			}
		}
		
		
		
		// Armor type
		int armortype = -1;
		rnd = rand.nextFloat() - chanceModifier;
		i = 0;
		for (float[] armortypechance : armorTypeChances) {
			rnd -= armortypechance[difficultyIndex];
			if (rnd <= 0) {
				armortype = i;
				break;
			}
			i++;
		}
		

		
		switch (armortype) {
			case 2:
				inventory[4] = getArmorItem(0, metal);
			case 1:
				inventory[1] = getArmorItem(3, metal);
			case 0:
				inventory[3] = getArmorItem(1, metal);
				inventory[2] = getArmorItem(2, metal);
		}
		
		/*if (armortype == 2) {
			System.out.println("boosting mob with full gear of metal " + metal);
		}
		if (armortype == 1) {
			System.out.println("boosting mob with almost full gear of metal " + metal);
		}
		if (armortype == 0) {
			System.out.println("boosting mob with half gear of metal " + metal);
		}
		if (armortype == -1) {
			System.out.println("no armor for mob");
		}*/
		
		
		return inventory;
		
	}
	
	
	static ItemStack getArmorItem(int armorpiece, EnumMetal metal) {
		String armorpiecename = ItemArmorVC.armorTypes[armorpiece];
		
		if (metal == null) {
			switch (armorpiece) {
				case 0: return new ItemStack(Items.leather_helmet);
				case 1: return new ItemStack(Items.leather_chestplate);
				case 2: return new ItemStack(Items.leather_leggings);
				case 3: return new ItemStack(Items.leather_boots);
			}
		} else {

			return new ItemStack(ItemsVC.armor.get(metal.getName() + "_" + armorpiecename));
		}
		
		return null;
	}
	
	
	
	public static int getArmorExtraHealthBoost(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemArmor) {
			ArmorMaterial armmat = ((ItemArmor)stack.getItem()).getArmorMaterial();
			String name = armmat.name().toLowerCase(Locale.ROOT);
			
			if (name.equals("leather")) {
				return 2;
			}
			if (name.equals("coppervc")) {
				return 3; 
			}
			if (name.equals("tinbronzevc")) {
				return 4;
			}
			if (name.equals("bismuthbronzevc")) {
				return 4;
			}
			if (name.equals("ironvc")) {
				return 5;
			}
			
			//System.out.println("armor material " + name + " not found");
			
		}
		return 0;
	}

}

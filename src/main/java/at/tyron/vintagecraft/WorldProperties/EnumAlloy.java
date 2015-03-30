package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.Arrays;

import at.tyron.vintagecraft.item.ItemIngot;
import net.minecraft.item.ItemStack;

public enum EnumAlloy {

	BRONZE (EnumMetal.TINBRONZE, new EnumMetal[]{EnumMetal.COPPER, EnumMetal.TIN}, new int[]{88, 8}, new int[]{92, 12}, EnumMetal.COPPER.meltingpoint),
	BISMUTHBRONZE (EnumMetal.BISMUTHBRONZE, new EnumMetal[]{EnumMetal.COPPER, EnumMetal.ZINC, EnumMetal.BISMUTH}, new int[]{50, 20, 10}, new int[]{70, 30, 20}, EnumMetal.COPPER.meltingpoint)
	
	
	;
	
	
	EnumMetal[] fromMetals;
	int[] fromMetalsMinRatio; // in %
	int[] fromMetalsMaxRatio; // in %
	public EnumMetal toMetal;
	public int meltingpoint;
	
	private EnumAlloy(EnumMetal toMetal, EnumMetal[] fromMetals, int[] fromMetalsMinRatio, int[] fromMetalsMaxRatio, int meltingpoint) {
		this.meltingpoint = meltingpoint;
		this.toMetal = toMetal;
		
		sortIngredients(fromMetals, fromMetalsMinRatio, fromMetalsMaxRatio, meltingpoint);
	}
	
	void sortIngredients(EnumMetal[] fromMetals, int[] fromMetalsMinRatio, int[] fromMetalsMaxRatio, int meltingpoint) {
		this.fromMetals = fromMetals.clone();
		
		Arrays.sort(this.fromMetals);
		
		// Sort ratios too
		this.fromMetalsMinRatio = new int[fromMetalsMinRatio.length];
		this.fromMetalsMaxRatio = new int[fromMetalsMaxRatio.length];
		
		for (int newidx = 0; newidx < this.fromMetals.length; newidx++) {
			int oldidx = 0;
			for (EnumMetal unsortedmetal : fromMetals) {
				if (unsortedmetal == this.fromMetals[newidx]) break;
				oldidx++;
			}
			
			this.fromMetalsMinRatio[newidx] = fromMetalsMinRatio[oldidx];
			this.fromMetalsMaxRatio[newidx] = fromMetalsMaxRatio[oldidx];
			
		}
		
		/*System.out.println(toMetal.getName() + " Ingredients ");
		for (int i = 0; i < fromMetals.length; i++) {
			System.out.println( fromMetals[i] + " " + fromMetalsMinRatio[i] + "% - " + fromMetalsMaxRatio[i] + "%");
		}*/
	}
	

	
	public static ItemStack getSmeltedItemStack(ItemStack []stacks) {
		EnumAlloy alloy = getSuitableAlloy(stacks);
		if (alloy == null) return null;
		
		int quantity = 0 ;
		for (ItemStack stack : stacks) {
			if (stack != null) quantity += stack.stackSize;
		}
		
		return ItemIngot.getItemStack(alloy.toMetal, quantity);
	}
	
	
	public static EnumAlloy getSuitableAlloy(ItemStack []stacks) {
		if (stacks == null) return null;
		
		EnumMetal []metals = getMetals(stacks);
		
		if (metals == null) return null;
		
		int []ratios = getRatios(stacks, metals);
		
		EnumMetal []metalssorted = metals.clone();
		Arrays.sort(metalssorted);
		
		int []ratiossorted = new int[ratios.length];
		
		for (int newidx = 0; newidx < metalssorted.length; newidx++) {
			int oldidx = 0;
			for (EnumMetal unsortedmetal : metals) {
				if (unsortedmetal == metalssorted[newidx]) break;
				oldidx++;
			}
			ratiossorted[newidx] = ratios[oldidx];
		}
		
		

		int sumratios = 0;
		
		for (EnumAlloy alloy : values()) {
			if (Arrays.equals(alloy.fromMetals, metalssorted)) {
				for (int idx = 0; idx < alloy.fromMetals.length; idx++) {
					if (ratiossorted[idx] < alloy.fromMetalsMinRatio[idx] || ratiossorted[idx] > alloy.fromMetalsMaxRatio[idx]) return null;
					
					sumratios += ratiossorted[idx];
				}
				
				return sumratios == 100 ? alloy : null;
			}
		}
		
		return null;
	}


	
	
	

	private static EnumMetal[] getMetals(ItemStack[] stacks) {
		ArrayList<EnumMetal> metals = new ArrayList<EnumMetal>();
		
		for (ItemStack stack : stacks) {
			if(stack == null) continue;
			
			if (stack.stackSize >= 1 && stack.getItem() instanceof ItemIngot) {
				if (!metals.contains(ItemIngot.getMetal(stack))) {
					metals.add(ItemIngot.getMetal(stack));
				}
				
			} else {
				return null;
			}
		}
		
		return metals.toArray(new EnumMetal[0]);
	}



	private static int[] getRatios(ItemStack[] stacks, EnumMetal[] metals) {
		int[] ratios = new int[metals.length];
		
		int sum = 0;
		
		for (ItemStack stack : stacks) {
			if(stack == null) continue;
			
			int idx = 0;
			for (EnumMetal metal : metals) {
				if (ItemIngot.getMetal(stack) == metal) break;
				idx++;
			}
				
			ratios[idx] += stack.stackSize;
			sum += stack.stackSize;
		}
				
		for (int i = 0; i < ratios.length; i++) {
			ratios[i] = (100 * ratios[i]) / sum;
		}
		
		return ratios;
	}
	
	
}

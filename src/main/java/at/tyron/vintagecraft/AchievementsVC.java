package at.tyron.vintagecraft;

import java.util.ArrayList;

import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemSeedVC;
import at.tyron.vintagecraft.Item.ItemVC;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrop;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementsVC {
	public static ArrayList<Achievement> achievements = new ArrayList<Achievement>();
	
	public static Achievement copperAge;
	public static Achievement bronzeAge;
	public static Achievement ironAge;
	public static Achievement steelAge;
	
	public static Achievement acquireMechanicalPower;
	public static Achievement acquireCoke;
	public static Achievement createOddlyShapedIngot;
	
	public static Achievement findFlax;
	
	public static Achievement killUndeadHorse;
	public static Achievement killIronArmorZombie;
	public static Achievement killForestSpider;
	
	
	
	
	//public static Achievement acquireIron = (new Achievement("achievement.acquireIron", "acquireIron", 1, 4, Items.iron_ingot, buildFurnace)).func_180788_c();
	public static void init() {
		ItemStack copperIngot = ItemIngot.getItemStack(EnumMetal.COPPER, 1);
		ItemStack bronzeIngot = ItemIngot.getItemStack(EnumMetal.TINBRONZE, 1);
		ItemStack ironIngot = ItemIngot.getItemStack(EnumMetal.IRON, 1);
		ItemStack steelIngot = ItemIngot.getItemStack(EnumMetal.STEEL, 1);
		
		ItemStack coke = ItemOreVC.getItemStackFor(EnumOreType.COKE, 1);
		ItemStack toolhammer = new ItemStack(ItemsVC.tools.get("copper_hammer"));
		ItemStack flaxseeds = ItemSeedVC.withCropType(EnumCrop.FLAX);
		ItemStack windmillrotor = new ItemStack(BlocksVC.windmillrotor);
		
		ItemStack copperSword = new ItemStack(ItemsVC.tools.get("copper_sword"));
		ItemStack bronzeSword = new ItemStack(ItemsVC.tools.get("tinbronze_sword"));
		ItemStack ironSword = new ItemStack(ItemsVC.tools.get("iron_sword"));
		
		copperAge = newAchievement("copperAge", 0, 0, copperIngot, null);
		bronzeAge = newAchievement("bronzeAge", 1, 2, bronzeIngot, copperAge);
		ironAge = newAchievement("ironAge", 0, 4, ironIngot, bronzeAge).setSpecial();
		steelAge = newAchievement("steelAge", 1, 6, steelIngot, ironAge).setSpecial();
		
		acquireCoke = newAchievement("acquireCoke", -2, 4, coke, ironAge);
		createOddlyShapedIngot = newAchievement("createOddlyShapedIngot", 2, 0, toolhammer, copperAge);
		
		findFlax = newAchievement("findFlax", -5, 0, flaxseeds, null);
		acquireMechanicalPower = newAchievement("acquireMechanicalPower", -5, 2, windmillrotor, findFlax);
		
		killUndeadHorse = newAchievement("killUndeadHorse", 5, 2, copperSword, null);
		killForestSpider = newAchievement("killForestSpider", 5, 4, bronzeSword, killUndeadHorse);
		killIronArmorZombie = newAchievement("killIronArmorMob", 5, 6, ironSword, killForestSpider);
	}
	
	public static Achievement newAchievement(String code, int col, int row, ItemStack itemstack, Achievement parent) {
		Achievement achievement = new Achievement("achievement." + code, code, col, row, itemstack, parent);
		achievements.add(achievement);
		return achievement;
	}
}



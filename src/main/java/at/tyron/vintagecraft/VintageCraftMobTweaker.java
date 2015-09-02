package at.tyron.vintagecraft;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Random;

import at.tyron.vintagecraft.Entity.EntityMobHorse;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.MobInventoryItems;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

public class VintageCraftMobTweaker {
	// Called from Vintagecraft.java
    public static void entitySpawn(LivingSpawnEvent evt) {
    	if (! (evt.entity.worldObj instanceof WorldServer)) return;
    
		if (evt.entity instanceof EntityZombie) {
			// Completely Disable baby zombies for now
			// EDIT: This is not working whatsoever o.O Baby zombies still spawn
			((EntityZombie)evt.entity).setChild(false);
		}
    	
    	if (evt.entity instanceof EntityMob) {
    		int spawncap = VintageCraftMobTweaker.spawnCapByDay(evt.world.getTotalWorldTime() / 24000L, evt.world.getDifficulty());
    		if (spawncap == 0) {
    			evt.setCanceled(true);
    			return;
    		}
    	}

    	if (evt.entity instanceof EntityZombie) {
    		gearUpMob((EntityZombie)evt.entity);
    	}

    	if (evt.entity instanceof EntitySkeleton) {
    		gearUpMob((EntitySkeleton)evt.entity); 
    	}
    	

    }

	
    // Called from Vintagecraft.java
	public static void tweakDrops(LivingDropsEvent event) {
		float growth = 1f;
		float dropquantity;
		Random rand = null;
		
		if (event.entity instanceof EntityAgeable) {
			int age = ((EntityAgeable)event.entity).getGrowingAge();
			if (age < 0) {
				growth = (1 - age / 24000f);
			}
			rand = event.entityLiving.getRNG();
		}
		
		if (event.entity instanceof EntityCreeper || event.entity instanceof EntityZombie) {
			event.drops.clear();
		}
		
		if (event.entity instanceof EntitySheep) {
			event.drops.clear();
			event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, new ItemStack(Blocks.wool)));
		}
		
	    if (event.entity instanceof EntityPig) {
	        event.drops.clear();
	        
	        ItemStack itemStackToDrop = new ItemStack(ItemsVC.porkchopRaw, getDropQuantity(3, growth, rand) + rand.nextInt(2));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	    }
	    
	    if (event.entity instanceof EntityCow) {
	        event.drops.clear();
	        ItemStack itemStackToDrop = new ItemStack(ItemsVC.beefRaw, getDropQuantity(5, growth, rand) + rand.nextInt(2));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	        
	        itemStackToDrop = new ItemStack(Items.leather, getDropQuantity(2, growth, rand) + rand.nextInt(2));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	    }
	    
	    if (event.entity instanceof EntityChicken) {
	        event.drops.clear();
	        ItemStack itemStackToDrop = new ItemStack(ItemsVC.chickenRaw, getDropQuantity(1, growth, rand));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	        
	        itemStackToDrop = new ItemStack(Items.feather, getDropQuantity(3, growth, rand));
	        event.drops.add(new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY, event.entity.posZ, itemStackToDrop));
	    }
	}
	
    
	static int getDropQuantity(int max, float factor, Random rand) {
		int q = (int) (max * factor);
		return q + (rand.nextFloat() < max*factor - q ? 1 : 0);
	}

    
	public static void setSpawnCap(EnumCreatureType type, int value) {
		try {
			Field field = EnumCreatureType.class.getDeclaredFields()[5];
			field.setAccessible(true);
			Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(type, value);

		}
		catch(Exception e){
			throw new RuntimeException("couldnt set mob cap");
		}
	}
	
	
	public static int spawnCapByDay(long days, EnumDifficulty difficulty) {
		int cap = 0;
		switch (difficulty) {
			case EASY: cap = VintageCraftConfig.easyMobCap; break;
			case NORMAL: cap = VintageCraftConfig.mediumMobCap; break;
			case HARD: cap = VintageCraftConfig.hardMobCap; break;
			default: cap = 0; break;
		}
		
		if (VintageCraftConfig.mobFreeDays == 0) return cap;
		if (VintageCraftConfig.mobFreeDays > days) return 0;
		
		//System.out.println(days + " / " + VintageCraftConfig.mobFreeDays);
		return (int) Math.min(cap, (days - VintageCraftConfig.mobFreeDays + 1) * 15);
	}
	
    
    
    
	public static void gearUpMob(EntityMob mob) {
		if (mob.worldObj.isRemote) return;
		
		// Already has armor? Skip!
		if (mob.getInventory()[2] != null || mob.getInventory()[3] != null || mob.getInventory()[1] != null) return;
		
		EnumDifficulty difficulty = mob.worldObj.getDifficulty();
		
		// The deeper the mob spawns, the better equipment it receives
		float difficultyModifier = 0.45f * Math.max(0, VCraftWorld.seaLevel - mob.getPosition().getY()) / VCraftWorld.seaLevel;
		
		//System.out.println("gear up mob at y = "+mob.getPosition().getY()+", diff mod = " + difficultyModifier);
		
		ItemStack[] inventory = MobInventoryItems.getDifficultyBasedMobInventory(difficulty, difficultyModifier, mob.worldObj.rand);
		if (inventory == null) return;
		
		int healthboost = 0;
		int numgearitems = 0;
		
		for (int i = 0; i < inventory.length; i++) {
			if (mob instanceof EntitySkeleton && i == 0) continue;
			
			mob.setCurrentItemOrArmor(i, inventory[i]);
			mob.setEquipmentDropChance(i, 0);
			
			healthboost += MobInventoryItems.getArmorExtraHealthBoost(inventory[i]);
			
			if (inventory[i] != null) numgearitems++;
		}

		
		if (numgearitems >= 4 && mob instanceof EntityZombie) {
		
			
		//	mob.getEntityAttribute(((EntityZombie)mob).reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", 0.95D, 0));
			
			if (healthboost > 5) {
		//		((EntityZombie)mob).multiplySize(1.1f);
			}
			
		}
		
		if (healthboost > 0) {
			//System.out.println("added health " + healthboost + " / new max h = " + mob.getMaxHealth());
			
			/*System.out.println("boosting mob. attributes: resistance=" + mob.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()
					+ " / maxhealth=" + mob.getHealth() + " / attack damage=" +  mob.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue()
			);*/

			
			mob.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(
				new AttributeModifier("Vintagecraft Gear bonus", healthboost / 12f, 1)
			);
			mob.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(
				new AttributeModifier("Vintagecraft Gear bonus", healthboost / 20f, 0)
			);
			mob.getEntityAttribute(SharedMonsterAttributes.attackDamage).applyModifier(
				new AttributeModifier("Vintagecraft Gear bonus", healthboost / 20f, 1)
			);
			
			//mob.experienceValue += healthboost;
			
			//System.out.println("now resis: " + mob.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue());
			mob.setHealth(mob.getMaxHealth());
			
			/*System.out.println("boosted mob. attributes: resistance=" + mob.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue()
					+ " / maxhealth=" + mob.getHealth() + " / attack damage=" +  mob.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue()
			);*/
			
			tryGearUpMobWithHorse(mob, numgearitems);
		}
		

		
	}
	
	
	static void tryGearUpMobWithHorse(EntityMob mob, int numgearitems) {
		// Only for skeles and zombies
		if (!(mob instanceof EntityZombie) && !(mob instanceof EntitySkeleton)) return;
		// Only for armored mobs
		if (numgearitems < 2) return;
		// Spawn only after 35 ingame days
		long daysPassed = mob.worldObj.getTotalWorldTime() / 24000L;
		
		EnumDifficulty difficulty = mob.worldObj.getDifficulty();

		//System.out.println("try horsing up " + mob.getClass());
		//new Exception().printStackTrace(System.out);
		
		// after 20,15,10 days  => 50% the amount of horses
		// increase by 10% every 10,15,20 days
		// until a max of 110% horses
		int dayModifier = (difficulty.ordinal()+1) * 5;   // returns 10,15,20
		float modifier = Math.min(1.2f, (daysPassed > dayModifier ? 0.5f : 0f) + 0.1f * ((daysPassed - dayModifier) / dayModifier));
		
		
		float horseSpawnchance = 0f;
		switch (difficulty) {
			case PEACEFUL: horseSpawnchance = 0f; break;
			case EASY: horseSpawnchance = 0.15f; break;
			case NORMAL: horseSpawnchance = 0.45f; break;
			case HARD: horseSpawnchance = 0.65f; break;
		}
		
		if (mob.worldObj.rand.nextFloat() > modifier * horseSpawnchance) return;
		
		BlockPos pos = mob.getPosition();
		World world = mob.worldObj;
		
		boolean spawnableGround =  
			world.getBlockState(pos.east()).getBlock().isPassable(world, pos.east()) &&
			world.getBlockState(pos.west()).getBlock().isPassable(world, pos.west()) &&
			world.getBlockState(pos.north()).getBlock().isPassable(world, pos.north()) &&
			world.getBlockState(pos.south()).getBlock().isPassable(world, pos.south()) &&
			world.getBlockState(pos.up().east()).getBlock().isPassable(world, pos.up().east()) &&
			world.getBlockState(pos.up().west()).getBlock().isPassable(world, pos.up().west()) &&
			world.getBlockState(pos.up().north()).getBlock().isPassable(world, pos.up().north()) &&
			world.getBlockState(pos.up().south()).getBlock().isPassable(world, pos.up().south()) &&
			pos.getY() >= VCraftWorld.seaLevel &&
			world.canBlockSeeSky(pos)
		;
		if(!spawnableGround) return;
		
		
		
		EntityMobHorse horse = new EntityMobHorse(mob.worldObj);

		// Aw pity, undead horses can't wear armor it seems
        /*ItemStack horseArmor = MobInventoryItems.getDifficultyBasedHorseArmor(mob.worldObj.getDifficulty(), mob.worldObj.rand);
        if (horseArmor != null) {
        	horse.setHorseArmorStack(horseArmor);
        	horse.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Vintagecraft Gear bonus", 1f, 1));
        	horse.setHealth(horse.getMaxHealth());
        }*/
        
        horse.setLocationAndAngles(mob.posX, mob.posY, mob.posZ, 0.0F, 0.0F);
		mob.worldObj.spawnEntityInWorld(horse);
		mob.mountEntity(horse);
		
		if (mob instanceof EntitySkeleton) {
			horse.setHorseType(4);
		} else {
			horse.setHorseType(3);
		}
		
	}
  

	
}

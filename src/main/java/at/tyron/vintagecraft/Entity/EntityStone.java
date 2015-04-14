package at.tyron.vintagecraft.Entity;

import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityStone extends EntityThrowable {
	ItemStack stack;
	
	public EntityStone(World world) {
		super(world);
		this.stack = ItemStone.setRockType(new ItemStack(ItemsVC.stone), EnumRockType.ANDESITE);
	}
	
	public EntityStone(ItemStack stack, World worldIn, EntityPlayer player) {
		super(worldIn, player);
		this.stack = stack;
	}
	

	protected void onImpact(MovingObjectPosition movingpos) {
		if (movingpos.entityHit !=  null) {
			float amount = 1;
			movingpos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), amount);
			
			Entity entity = movingpos.entityHit; 
		//	double height = entity.getBoundingBox().maxX - entity.getBoundingBox().minY;
			
			//System.out.println("entity height " + height + " / hitvec-y " + movingpos.hitVec.yCoord);
			
			if (worldObj.rand.nextInt(2) == 0) {
				if (entity instanceof EntityLivingBase) {
		//			System.out.println("add effect");
					EntityLivingBase entityliving = (EntityLivingBase)entity;
					entityliving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 125, 3));
				}
			}

		}
		
		if (!worldObj.isRemote) {
			if (worldObj.rand.nextInt(2) == 0) {
				worldObj.spawnEntityInWorld(new EntityItem(worldObj, this.posX, this.posY, this.posZ, stack));
			} else {
				for (int i = 0; i < 8; ++i) {
					this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}
		
		

		if (!this.worldObj.isRemote) {
			this.setDead();
		}
	}
	
	public ItemStack getItemstack() {
		return stack;
	}

}

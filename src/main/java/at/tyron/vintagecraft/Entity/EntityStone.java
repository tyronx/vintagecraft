package at.tyron.vintagecraft.Entity;

import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityStone extends EntityThrowable {
	ItemStack stack;
	
	public EntityStone(World world) {
		super(world);
		this.stack = ItemStone.setRockType(new ItemStack(ItemsVC.stone), EnumRockType.ANDESITE);
		this.setSize(0.25f, 0.07f);
	}
	
	public EntityStone(ItemStack stack, World worldIn, EntityPlayer player) {
		super(worldIn, player);
		this.stack = stack;
	}
	

	protected void onImpact(MovingObjectPosition movingpos) {
		
		
		if (movingpos.entityHit != null) {
			float amount = 1;
			
			EntityLivingBase thrower = this.getThrower();
			Entity entity = movingpos.entityHit; 
			
			
			if (entity instanceof EntityLivingBase) {
				float knockoutchance = 0.35f;
				
				if (entity instanceof EntityLiving) {
					ItemStack helmet = ((EntityLiving)entity).getCurrentArmor(3);
					if (helmet != null && helmet.getItem() instanceof ItemArmor) {
						int dmgReduction = ((ItemArmor)helmet.getItem()).getArmorMaterial().getDamageReductionAmount(0);
						knockoutchance /= Math.max(1, 2 * dmgReduction);
					}
				}
				
				if (worldObj.rand.nextFloat() < knockoutchance) {
					EntityLivingBase entityliving = (EntityLivingBase)entity;
					entityliving.addPotionEffect(new PotionEffect(Potion.moveSlowdown.getId(), 125, 3));
					playSound("random.successful_hit", 0.3f, 1f);
				}
			} else {
				thrower = null;
			}
			
			if (!worldObj.isRemote) {
				movingpos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, thrower), amount);
				if (thrower == null && movingpos.entityHit instanceof EntityLivingBase) {
					((EntityLivingBase)movingpos.entityHit).setRevengeTarget((EntityLivingBase)movingpos.entityHit);

				}
			}
			
			playSound("vintagecraft:thud", 0.5f, 1f);

			
		} else {
			
			double angle = Math.atan2(motionY, MathHelper.sqrt_double(motionX*motionX+motionZ*motionZ));
			
			if (movingpos.sideHit == EnumFacing.UP && Math.abs(motionY) > 0.05f) {
				motionY = -motionY;
				motionX /= 8 * Math.abs(angle);
				motionZ /= 8 * Math.abs(angle);
				motionY /= 8 * Math.abs(angle);
				
				playSound("vintagecraft:thud", (float) (0.5f * (Math.abs(motionX) + Math.abs(motionZ) + Math.abs(motionY))), 1f);
				return;
			}
		}
		
		
		if (Math.abs(motionX) + Math.abs(motionZ) < 0.9f || movingpos.entityHit != null) {
			if (!worldObj.isRemote) {
				EntityItem item = new EntityItem(worldObj, this.posX, this.posY, this.posZ, stack);
				item.motionX = item.motionZ = item.motionY = 0;
				worldObj.spawnEntityInWorld(item);
			}
			
		} else {
			for (int i = 0; i < 3; ++i) {
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
			}
			playSound("vintagecraft:thud", 0.9f, 1f);
			
            for (int var2 = 0; var2 < 10; ++var2) {
            	
                this.worldObj.spawnParticle(
                	EnumParticleTypes.BLOCK_DUST, 
                	this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width,
                	this.posY + (double)(this.rand.nextFloat() * this.height), 
                	this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, 
                	motionX / 10, 
                	-motionY / 10, 
                	motionZ / 10, 
                	new int[] {Block.getStateId(BlocksVC.rock.getBlockStateFor(ItemStone.getRockType(stack) ))}
                );
                
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

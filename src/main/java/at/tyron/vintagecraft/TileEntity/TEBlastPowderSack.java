package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.VintageCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.EnumParticleTypes;

public class TEBlastPowderSack extends NetworkTileEntity implements IUpdatePlayerListBox {
	int fuseTimer;
	boolean exploded;
	
	// If no more solid ground, wait 1-2 ticks before dropping
	// This prevents the blast powder sack from just dropping when 
	// a nearby explosion first blows up the ground below
	int dropTimer; 
	

	public TEBlastPowderSack() {
		
	}
	

	public static int getFuseDuration() {
		return 80;
	}
	
	public static float explosionStrength() {
		return 4f;
	}
	
	// When blown up by another explosive
	public static float indirectExplosionStrength() {
		return 2f;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		fuseTimer = compound.getInteger("fuseTimer");
		dropTimer = compound.getInteger("dropTimer");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("fuseTimer", fuseTimer);
		compound.setInteger("dropTimer", dropTimer);
	}
	

	@Override
	public void update() {
		if (fuseTimer > 0) {
			fuseTimer--;
			
			VintageCraft.proxy.generateGundPowderSpark(worldObj, getPos(), 0.5f, 0.6f, 0.5f);
			
			if (fuseTimer <= 0) {
				explode(explosionStrength());
			}
			
			dropTimer = 0;
		}
		
		if (dropTimer > 0) {
			dropTimer--;
			if (dropTimer <= 0) {
				worldObj.destroyBlock(pos, true);
			}
		}
	}
	
	
	public boolean tryIgnite() {
		if (fuseTimer == 0) {
			fuseTimer = getFuseDuration();
			worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "game.tnt.primed", 1.0F, 1.0F);
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}
	
	
	public void explode(float stength) {
		float strength = 4.0F;
		exploded = true;
		
		if (!this.worldObj.isRemote) {
			worldObj.destroyBlock(getPos(), false);
			worldObj.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.125, pos.getZ() + 0.5, strength, true);
		}
	}

	public void destroyedByExplosion() {
		if (!exploded) {
			explode(indirectExplosionStrength());
		}
		
	}

	public void groundRemoved() {
		if (!worldObj.isRemote) {
			dropTimer = 2;
		}
	}
	
}

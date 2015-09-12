package at.tyron.vintagecraft.Entity;

import java.util.Iterator;

import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCoalPoweredMinecartVC extends EntityMinecartVC {
	public boolean refreshModel = false;

	public int fuel;
	public int burntime;
    public double pushX;
    public double pushZ;

    public EntityCoalPoweredMinecartVC(World worldIn) {
        super(worldIn);
    }
	
	public EntityCoalPoweredMinecartVC(World worldIn, double p_i1719_2_, double p_i1719_4_, double p_i1719_6_) {
		super(worldIn, p_i1719_2_, p_i1719_4_, p_i1719_6_);
	}
	
	

    

    
    
    
    
    
    
    public EntityMinecart.EnumMinecartType getMinecartType() {
        return EntityMinecart.EnumMinecartType.FURNACE;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }

    public void onUpdate() {
        super.onUpdate();

        if (this.fuel > 0) {
            --this.fuel;
            burntime++;
        }

        if (this.fuel <= 0) {
            this.pushX = this.pushZ = 0.0D;
            burntime = 0;
        }

        this.setMinecartPowered(this.fuel > 0);

        if (this.isMinecartPowered() && this.rand.nextInt(3) == 0) {
        	worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 1.4D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    protected double func_174898_m() {
        return 0.2D;
    }

    public void killMinecart(DamageSource p_94095_1_) {
        super.killMinecart(p_94095_1_);
    }

    protected void func_180460_a(BlockPos p_180460_1_, IBlockState p_180460_2_) {
        super.func_180460_a(p_180460_1_, p_180460_2_);
        double pushForce = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (pushForce > 1.0E-4D && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001D) {
            pushForce = (double)MathHelper.sqrt_double(pushForce);
            double motionForce = (double) MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            
            this.pushX = 0.8f * (motionX / motionForce) * pushForce;
            this.pushZ = 0.8f * (motionZ / motionForce) * pushForce;
        }
    }
	
    protected void applyDrag() {
        double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;

        if (d0 > 1.0E-4D) {
            d0 = (double)MathHelper.sqrt_double(d0);
            this.pushX /= d0;
            this.pushZ /= d0;
            double d1 = 1.0D;
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.800000011920929D;
            
            float drag = Math.min(0.5f, burntime / 800f);
            
            this.motionX += this.pushX * d1 * drag;
            this.motionZ += this.pushZ * d1 * drag;
        } else {
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.0D;
            this.motionZ *= 0.9800000190734863D;
        }

        super.applyDrag();
    }
    

    public boolean interactFirst(EntityPlayer playerIn) {
        ItemStack itemstack = playerIn.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() instanceof ItemOreVC) {
        	EnumOreType oretype = ItemOreVC.getOreType(itemstack);
        	
        	if (oretype == EnumOreType.LIGNITE || oretype == EnumOreType.BITUMINOUSCOAL || oretype == EnumOreType.COKE) {
        	
	            if (!playerIn.capabilities.isCreativeMode && --itemstack.stackSize == 0) {
	                playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, (ItemStack)null);
	            }
	            
	            fuel += oretype == EnumOreType.LIGNITE ? 2400 : 3600;
	            
	            this.pushX = MathHelper.clamp_double(playerIn.posX - posX, -0.2f, 0.2f);
	            this.pushZ = MathHelper.clamp_double(playerIn.posZ - posZ, -0.2f, 0.2f);
        	}
        }

        return super.interactFirst(playerIn);
    }
    
    

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setDouble("PushX", this.pushX);
        tagCompound.setDouble("PushZ", this.pushZ);
        tagCompound.setShort("Fuel", (short)this.fuel);
    }

	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.pushX = tagCompund.getDouble("PushX");
        this.pushZ = tagCompund.getDouble("PushZ");
        this.fuel = tagCompund.getShort("Fuel");
    }

    protected boolean isMinecartPowered() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    protected void setMinecartPowered(boolean p_94107_1_) {
        if (p_94107_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(this.dataWatcher.getWatchableObjectByte(16) & -2)));
        }
    }

    

	
}

package at.tyron.vintagecraft.Entity;

import java.util.Iterator;

import at.tyron.vintagecraft.Inventory.ContainerCoalPoweredMinecart;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemPeatBrick;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
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

public class EntityCoalPoweredMinecartVC extends EntityMinecartContainerVC {
	public boolean refreshModel = false;

	// Amount of ticks how long the current fuel has been burning
	public int fuelBurnTime;
	
	// Amount of ticks the current fuel burns 
	public int maxFuelBurnTime;
	
	// Amount of ticks passed since the cart is burning fuel
	public int totalBurntime;  
	
	
    public double pushX;
    public double pushZ;
    
    // Last player how opened the inventory
    public double plannedPushX;
    public double plannedPushZ;
    

    public EntityCoalPoweredMinecartVC(World worldIn) {
        super(worldIn);
    }
	
	public EntityCoalPoweredMinecartVC(World worldIn, double p_i1719_2_, double p_i1719_4_, double p_i1719_6_) {
		super(worldIn, p_i1719_2_, p_i1719_4_, p_i1719_6_);
	}
	
	

    

    public boolean isOnRail() {
        return
        	BlockRailBase.isRailBlock(this.worldObj, new BlockPos(posX, posY - 1, posZ)) ||
        	BlockRailBase.isRailBlock(this.worldObj, new BlockPos(posX, posY, posZ))
        ;
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
        
        if (canBurnFuel()) {
        	igniteFuel();
        }
        
        if (canConsumeFuel()) {
            --this.fuelBurnTime;
            totalBurntime++;
            this.setMinecartPowered(this.fuelBurnTime > 0);
            
        } else {
        	if (totalBurntime <= 0) {
        		totalBurntime = 0;
        		this.pushX = this.pushZ = 0.0D;
        	}
        }

        

        if (this.isMinecartPowered() && isOnRail() && this.rand.nextInt(3) == 0) {
        	worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 1.4D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    
    private void igniteFuel() {
		int fuelvalue = getFuelValue(minecartContainerItems[0]);
		minecartContainerItems[0].stackSize--;
		if (minecartContainerItems[0].stackSize == 0) {
			minecartContainerItems[0] = null;
		}
		
		fuelBurnTime = maxFuelBurnTime = fuelvalue;
		
		pushX = plannedPushX;
		pushZ = plannedPushZ;     
	}

	private boolean canBurnFuel() {
		return 
			fuelBurnTime == 0 && 
			getFuelValue(minecartContainerItems[0]) > 0
		;
	}

	private boolean canConsumeFuel() {
		return 
			this.fuelBurnTime > 0 &&
			isOnRail()
		;
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
            
            float drag = Math.min(0.5f, totalBurntime / 800f);
            
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
		plannedPushX = MathHelper.clamp_double(playerIn.posX - posX, -0.2f, 0.2f);
		plannedPushZ = MathHelper.clamp_double(playerIn.posZ - posZ, -0.2f, 0.2f);
		
        return super.interactFirst(playerIn);
    }
    
    
    
    public static int getFuelValue(ItemStack itemstack) {
    	if (itemstack == null) return 0;
    	
    	if (itemstack.getItem() instanceof ItemOreVC) {
        	EnumOreType oretype = ItemOreVC.getOreType(itemstack);
        	
        	if (oretype == EnumOreType.LIGNITE || oretype == EnumOreType.BITUMINOUSCOAL || oretype == EnumOreType.COKE) {
        	
	            return oretype == EnumOreType.LIGNITE ? 2400 : 3600;
        	}
    	}
    	
    	if (itemstack.getItem() instanceof ItemPeatBrick) {
    		return 800;
    	}
    	
    	return 0;
    }
    
    

    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setDouble("PlannedPushX", this.plannedPushX);
        tagCompound.setDouble("PlannedPushZ", this.plannedPushZ);
        tagCompound.setInteger("fuelBurnTime", this.fuelBurnTime);
        tagCompound.setInteger("maxFuelBurnTime", this.maxFuelBurnTime);
        tagCompound.setInteger("totalBurntime", this.totalBurntime);
        
        tagCompound.setDouble("PushX", this.pushX);
        tagCompound.setDouble("PushZ", this.pushZ);
        
    }

	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        
        this.pushX = tagCompund.getDouble("PushX");
        this.pushZ = tagCompund.getDouble("PushZ");
        this.plannedPushX = tagCompund.getDouble("PlannedPushX");
        this.plannedPushZ = tagCompund.getDouble("PlannedPushZ");
        
        this.fuelBurnTime = tagCompund.getInteger("fuelBurnTime");
        this.maxFuelBurnTime = tagCompund.getInteger("maxFuelBurnTime");
        this.totalBurntime = tagCompund.getInteger("totalBurntime");
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

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerCoalPoweredMinecart(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return "vintagecraft:coalpoweredminecart";
	}

    
    public int getField(int id) {
        switch (id) {
        	case 0: return fuelBurnTime;
        	case 1: return maxFuelBurnTime;
        	default: return 0;
        }
    }

    public void setField(int id, int value) {
        switch (id) {
    		case 0: fuelBurnTime = value; break;
    		case 1: maxFuelBurnTime = value; break;
    		default: return;
        }
    	
    }

    public int getFieldCount() {
        return 2;
    }

    
	
}

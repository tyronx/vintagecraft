package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Block.BlockBloomeryChimney;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;

public class TEBloomery extends TENoGUIInventory implements IUpdatePlayerListBox {
	int mode;
	int burntime;
	
	// storage[0] == coal
	// storage[1] == iron ore
	// storage[2] == ingots
	
	// bloomery 
	
	public TEBloomery() {
		mode = 0;
		
		storage = new ItemStack[getSizeInventory()];
		storage[0] = ItemOreVC.getItemStackFor(EnumOreType.BITUMINOUSCOAL, 0);
		storage[1] = ItemOreVC.getItemStackFor(EnumOreType.LIMONITE, 0);
		storage[2] = ItemIngot.getItemStack(EnumMetal.IRON, 0);
	}
	
	
	
	
	
    @Override
    public void update() {
    	if (burntime == 1) {
    		finishMelt();
    	}
    	
    	if (burntime > 0) burntime--;
    	
    	
    	if (!worldObj.isRemote || mode != 1) return;
    	
    	
		if (!(worldObj.getBlockState(pos.up()).getBlock() instanceof BlockBloomeryChimney) || ! worldObj.isAirBlock(pos.up(2))) {
			burntime = 0;
			mode = 0;
			worldObj.markBlockForUpdate(pos);
			markDirty();

		}

    	
    	if (worldObj.rand.nextInt(100) == 0) {
    		worldObj.playSound(
    			(double)((float)pos.getX() + 0.5F), 
    			(double)((float)pos.getY() + 0.5F), 
    			(double)((float)pos.getZ() + 0.5F), 
    			"fire.fire", 
    			0.5F + worldObj.rand.nextFloat(), 
    			worldObj.rand.nextFloat() * 0.5F + 0.3F, 
    			false
    		);
        }
    	
    	
    	
    	float quantitysmoke = (2.5f * burntime * burntime) / (getTotalBurnTime() * getTotalBurnTime()); 
    	while (quantitysmoke > 0.001f) {
    		if (quantitysmoke < 1f && worldObj.rand.nextFloat() > quantitysmoke) break;
    		
            double x = (double)pos.getX() + 0.3f + worldObj.rand.nextDouble() * 0.4f;
            double y = (double)pos.getY() + 0.7f + worldObj.rand.nextDouble() * 0.3D + 0.7D;
            double z = (double)pos.getZ() + 0.3f + worldObj.rand.nextDouble() * 0.4f;
            
            worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
            
            /*if (worldObj.rand.nextFloat() < 0.3f) {
            	worldObj.spawnParticle(EnumParticleTypes.FLAME, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
            }*/
            
            quantitysmoke--;
    	}
    }
	
	

	@Override
	public int getSizeInventory() {
		return 3;
	}

	@Override
	public String getName() {
		return "bloomery";
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Bloomery");
	}
	
	
	
	public void finishMelt() {
		mode = 2;
		storage[0].stackSize = 0; // Coal
		storage[2].stackSize = storage[1].stackSize / 4;  // Iron ingots 
		storage[1].stackSize = 0; // Iron ore
		
		
		if (storage[2].stackSize == 0) mode = 0;
		
		worldObj.markBlockForUpdate(pos);
		markDirty();
	}
	
	
	
	
	public int getMode() {
		return mode;
	}
	
	
	public int getFillHeight() {
		return Math.max(
			2 * ((storage[0].stackSize + storage[1].stackSize) / 4),
			storage[2].stackSize * 4
		);
	}

	
	public boolean canIgnite() {
		return 
			worldObj.getBlockState(pos.up()).getBlock() instanceof BlockBloomeryChimney &&
			worldObj.isAirBlock(pos.up(2)) &&
			mode == 0 &&
			storage[0].stackSize >= storage[1].stackSize &&
			getFillHeight() >= 2
		;
	}
	
	
	public int getTotalBurnTime() {
		return 20 * 60 * 5; // 5 Minutes
	}
	
	
	public boolean tryIgnite() {
		if (!canIgnite()) return false;
				
		burntime = getTotalBurnTime();
		
		// Consume coal right away
		storage[0].stackSize = 0; 
		mode = 1;
		
		worldObj.markBlockForUpdate(pos);
		return true;
	}
	

	public boolean tryPutItemStack(ItemStack itemstack) {
		if (mode != 0) return false;
		
		EnumOreType oretype = ItemOreVC.getOreType(itemstack);
		if (oretype == null) return false;
		
		for (ItemStack ownstack : storage) {
			if (ItemOreVC.getOreType(ownstack) == oretype  && ownstack.stackSize < 16) {
				ownstack.stackSize++;
				itemstack.stackSize--;
				worldObj.markBlockForUpdate(pos);
				return true;
			}
			
		}
		
		return false;	
	}


	
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		mode = nbttagcompound.getInteger("mode");
		burntime = nbttagcompound.getInteger("burntime");
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("mode", mode);
		nbttagcompound.setInteger("burntime", burntime);
		super.writeToNBT(nbttagcompound);
	}

}

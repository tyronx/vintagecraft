package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Block.BlockOrePile;
import at.tyron.vintagecraft.Block.Metalworking.BlockCokeOvenDoor;
import at.tyron.vintagecraft.Interfaces.IBlockIgniteable;
import at.tyron.vintagecraft.Item.Metalworking.ItemIngot;
import at.tyron.vintagecraft.Item.Terrafirma.ItemOreVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;

public class TEOrePile extends TENoGUIInventory implements IUpdatePlayerListBox {
	int burnTime;
	
	public int getMaxBurntime() {
		return 10 * 20 * blockHeight();
	}
	
	public TEOrePile() {
		storage = new ItemStack[1];
	}
	public void setItemStack(ItemStack copy) {
		storage[0] = copy;
	}
	public int getStackSize() {
		if (storage[0] == null) return 0;
		return storage[0].stackSize;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		
		burnTime = nbttagcompound.getInteger("burnTime");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		super.writeToNBT(nbttagcompound);
		
		nbttagcompound.setInteger("burnTime", burnTime);
	}
	

	public boolean tryGrabOre(EntityPlayer player) {
		if (burnTime > 0) return false;
		boolean success = false;
		
		TEOrePile pile = getTopmostOrePile();
		
		if (pile.storage[0].stackSize > 0) {
			ItemStack ejectedstack = pile.storage[0].splitStack(1);
			
			if (!player.inventory.addItemStackToInventory(ejectedstack)) {
				if (!getWorld().isRemote) {
					getWorld().spawnEntityInWorld(
						new EntityItem(getWorld(),
							pile.getPos().getX(),
							pile.getPos().getY() + 1, 
							pile.getPos().getZ(), 
							ejectedstack
					));
				}
			}
			success = true;
			
		}
		
		if (pile.storage[0].stackSize <= 0) {
			getWorld().setBlockToAir(pile.getPos());
		}
		
		getWorld().markBlockForUpdate(pile.getPos());
		
		return success;
	}

	public boolean tryTransferOre(ItemStack stack) {
		TEOrePile pile = getTopmostOrePile();
		
		if (stack.getItem() != pile.storage[0].getItem() || 
			ItemOreVC.getOreType(stack) != getOreType() ||
			!((ItemOreVC)stack.getItem()).isPlaceable(stack)
		) {
			return false;
		}
		
		if (pile.storage[0].stackSize < ItemIngot.maxpilesize) {
			
			if (pile.storage[0].stackSize + 4 <= ItemIngot.maxpilesize && stack.stackSize >= 4) {
				pile.storage[0].stackSize += 4;
				stack.stackSize -= 4;
			} else {
				pile.storage[0].stackSize++;
				stack.stackSize--;				
			}
			
				
			
			getWorld().markBlockForUpdate(pile.getPos());
			
			return true;
		} else {
			return BlockOrePile.tryCreatePile(stack, getWorld(), pile.getPos().up());
		}
	}


	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getName() {
		return "orepile";
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Ore Pile");
	}

	public EnumOreType getOreType() {
		EnumOreType ore = null;
		
		if (storage[0] != null) {
			ore = ItemOreVC.getOreType(storage[0]); 
		}
		return ore == null ? EnumOreType.BISMUTHINITE : ore;
	}

	public TEOrePile getTopmostOrePile() {
		BlockPos pos = getPos();
		TEOrePile pile = this;
		
		while (getWorld().getBlockState(pos).getBlock() instanceof BlockOrePile) {
			pile = (TEOrePile)getWorld().getTileEntity(pos);
			pos = pos.up();
		}
		
		return pile;
	}
	
	public int blockHeight() {
		return Math.min(15, storage[0] == null ? 0 : storage[0].stackSize / 4);
	}
	
	public boolean isBurning() {
		return burnTime > 0;
	}
	
	public boolean tryIgnite() {
		boolean burnable = getOreType() == EnumOreType.LIGNITE || getOreType() == EnumOreType.BITUMINOUSCOAL || getOreType() == EnumOreType.COKE;
		
		if (burnable && burnTime == 0) {
			burnTime = getMaxBurntime();
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}
	

	@Override
	public void update() {
		if (burnTime > 0 && worldObj.isRemote) {

			
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
	    	
	    	
	    	
	    	float quantitysmoke = (0.2f * burnTime * burnTime) / (getMaxBurntime() * getMaxBurntime()); 
	    	while (quantitysmoke > 0.001f) {
	    		if (quantitysmoke < 1f && worldObj.rand.nextFloat() > quantitysmoke) break;
	    		
	            double x = (double)pos.getX() + 0.1f + worldObj.rand.nextDouble() * 0.8f;
	            double y = (double)pos.getY() + blockHeight() / 16.0 + 0.125D;
	            double z = (double)pos.getZ() + 0.1f + worldObj.rand.nextDouble() * 0.8f;
	            
	            worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
	            
	            quantitysmoke--;
	    	}
			
		}
		
		if (burnTime > 0 && !worldObj.isRemote) {
			burnTime--;
			
			if (worldObj.rand.nextFloat() < 0.3f && getMaxBurntime() - burnTime > 60) {
				BlockPos rndpos = pos.add(worldObj.rand.nextInt(3) - 1, worldObj.rand.nextInt(3) - 1, worldObj.rand.nextInt(3) - 1);
				IBlockState state = worldObj.getBlockState(rndpos);
				if (!rndpos.equals(pos) && state.getBlock() instanceof IBlockIgniteable) {
					((IBlockIgniteable)state.getBlock()).ignite(worldObj, rndpos, null);
				}
			}
			
			
			if (burnTime <= 1) {
				if (!handleByCokeOvenDoor()) {
					worldObj.setBlockToAir(pos);
				} else {
					worldObj.markBlockForUpdate(pos);
					burnTime = 0;
				}
			}
			
			
		}
		
	}

	boolean handleByCokeOvenDoor() {
		IBlockState state = null;
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) {
			state = worldObj.getBlockState(pos.offset(facing));
			if (state.getBlock() instanceof BlockCokeOvenDoor) {
				TECokeOvenDoor te = (TECokeOvenDoor) worldObj.getTileEntity(pos.offset(facing));
				if (te.canCreateCoke(storage[0], pos)) {
					storage[0] = te.getCokedOutput(storage[0]);
					return true;
				}
			}
		}
		

		
		return false;
	}
	
	
	

}

package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Block.BlockFireBrick;
import at.tyron.vintagecraft.Block.Utility.BlockFurnaceChimney;
import at.tyron.vintagecraft.Block.Utility.BlockFurnaceSection;
import at.tyron.vintagecraft.Block.Utility.BlockMetalPlate;
import at.tyron.vintagecraft.Block.Utility.BlockTallMetalMolds;
import at.tyron.vintagecraft.Interfaces.Item.IItemFuel;
import at.tyron.vintagecraft.Interfaces.Item.IItemSmeltable;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnaceType;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;

public class TEFurnaceSection extends TENoGUIInventory implements IUpdatePlayerListBox {
	int state;
	int burntime;
	EnumFurnaceType furnacetype;
	int receivedAirBlows;
	
	int airblowtimer;
	
	// The furnace section has 4 inventory slots
	// storage[0] == coal
	// storage[1] == input ore 
	// storage[2] == output metal
	// storage[3] == input metal
	
	
	public TEFurnaceSection() {
		state = 0;
		receivedAirBlows = 0;
		
		storage = new ItemStack[getSizeInventory()];
		// Input fuel
		storage[0] = null;
		// Input ore
		storage[1] = null;
		// Output metal 
		storage[2] = null;
		// Input metal
		storage[3] = ItemIngot.getItemStack(EnumMetal.IRON, 0);
	}
	

	public int getTotalBurnTime() {
		if (furnacetype == EnumFurnaceType.BLASTFURNACE) {
			return 20 * 60 * 7; // 7 Minutes
		}
		return 20 * 60 * 5; // 5 Minutes
	}
	
	public int getRequiredBlowsForSteel() { 
		return 30;
	}

	public EnumFurnaceType getFurnaceType() {
		checkFurnaceType();
		return furnacetype;
	}
	
	
	public void checkFurnaceType() {
		furnacetype = null;
		if (isValidBlastFurnace()) {
			furnacetype = EnumFurnaceType.BLASTFURNACE;
		} else {
			if (isValidBloomery()) {
				furnacetype = EnumFurnaceType.BLOOMERY;
			}
		}
		
	}
	
	public boolean viableFuel(ItemStack stack) {
		if (!(stack.getItem() instanceof IItemFuel)) return false;
		IItemFuel fuelitem = (IItemFuel)stack.getItem();
		
		if (furnacetype != null) {
			switch (furnacetype) {
				case BLASTFURNACE:
					return fuelitem.isMetalWorkingFuel(stack) && fuelitem.getBurningHeat(stack) >= 1300;
				case BLOOMERY:
					return fuelitem.isMetalWorkingFuel(stack) && fuelitem.getBurningHeat(stack) >= 1200;
			}
		}
		
		return false;
	}
	
	
	public boolean isValidBloomery() {
		return
			worldObj.getBlockState(pos.up()).getBlock() instanceof BlockFurnaceChimney &&
			!(worldObj.getBlockState(pos.down()).getBlock() instanceof BlockFurnaceSection) &&
			worldObj.isAirBlock(pos.up(2))
		;
	}
	
	public boolean isValidBlastFurnace() {
		IBlockState self = worldObj.getBlockState(pos);
		EnumFacing facing = (EnumFacing) self.getValue(BlockFurnaceSection.FACING); 
		
		
		return 
			worldObj.getBlockState(pos.up()).getBlock() instanceof BlockFurnaceSection &&
			worldObj.getBlockState(pos.up(2)).getBlock() instanceof BlockFurnaceChimney &&
			worldObj.isAirBlock(pos.up(3)) &&
			worldObj.getBlockState(pos.down()).getBlock() instanceof BlockFireBrick &&
			worldObj.getBlockState(pos.offset(facing).down()).getBlock() instanceof BlockTallMetalMolds &&
			((TETallMetalMold)worldObj.getTileEntity(pos.offset(facing).down())).orientation == facing.getOpposite() &&
			coveredInIronSheets(pos) &&
			coveredInIronSheets(pos.up())
		;
	}
	
	public boolean coveredInIronSheets(BlockPos pos) {
		for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) {
			IBlockState state = worldObj.getBlockState(getPos().offset(facing));
			if (!(state.getBlock() instanceof BlockMetalPlate) || BlockMetalPlate.getFacing(state) != facing.getOpposite()) {
				return false;
			}
			EnumMetal metal = BlockMetalPlate.getMetal(state);
			if (metal != EnumMetal.IRON && metal != EnumMetal.STEEL) return false;
		}
		
		return true;
	}
	
	
    @Override
    public void update() {
    	if (airblowtimer > 0) {
    		airblowtimer--;
    	}
    	
    	if (burntime > 0) {
    		burntime--;
    	} else {
    		return;
    	}
    	
    	if (state != 1) return;
    	
    	
    	if (worldObj.isRemote) {
    		// Client updates
    		
        	if (worldObj.rand.nextInt(100) == 0) {
        		worldObj.playSound(
        			(double)((float)pos.getX() + 0.5F), 
        			(double)((float)pos.getY() + 0.5F), 
        			(double)((float)pos.getZ() + 0.5F), 
        			"fire.fire", 
        			0.5F, 
        			worldObj.rand.nextFloat() * 0.5F + 0.3F, 
        			false
        		);
            }
        	
        	
        	
        	float quantitysmoke = (2.5f * burntime * burntime) / (getTotalBurnTime() * getTotalBurnTime());
        	
        	if (furnacetype == EnumFurnaceType.BLASTFURNACE) quantitysmoke /= 10;
        	quantitysmoke += Math.min(4, airblowtimer / 2);
        	
        	while (quantitysmoke > 0.001f) {
        		if (quantitysmoke < 1f && worldObj.rand.nextFloat() > quantitysmoke) break;
        		
                double x = (double)pos.getX() + 0.3f + worldObj.rand.nextDouble() * 0.4f;
                double y = (double)pos.getY() + 0.7f + worldObj.rand.nextDouble() * 0.3D + 0.7D + (furnacetype == EnumFurnaceType.BLASTFURNACE ? 1D : 0D);
                double z = (double)pos.getZ() + 0.3f + worldObj.rand.nextDouble() * 0.4f;
                
                worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, x, y, z, 0.0D, 0.0D, 0.0D, new int[0]);
                
                quantitysmoke--;
        	}
        	
        	
        	
    	} else {
    		// Server updates
    	
	    	if (burntime - receivedAirBlows*20 <= 1) {
	    		finishMelt();
	    		return;
	    	}
	    	
			if (burntime % 30 == 0 && (furnacetype == EnumFurnaceType.BLOOMERY && !isValidBloomery()) || (furnacetype == EnumFurnaceType.BLASTFURNACE && !isValidBlastFurnace())) {
				burntime = 0;
				state = 0;
				worldObj.markBlockForUpdate(pos);
				receivedAirBlows = 0;
				if (storage[0] != null) {
					storage[0].stackSize = 0;
				}
				
				markDirty();
	
			}
    	}	
    	
    }
	
	

	@Override
	public int getSizeInventory() {
		return 4;
	}

	@Override
	public String getName() {
		return "furnacesection";
	}

	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Furnacesection");
	}
	
	
	
	public void finishMelt() {
		if (furnacetype == EnumFurnaceType.BLASTFURNACE) {
			IBlockState self = worldObj.getBlockState(pos);
			EnumFacing facing = (EnumFacing) self.getValue(BlockFurnaceSection.FACING);
			
			TETallMetalMold te = (TETallMetalMold) worldObj.getTileEntity(pos.offset(facing).down());
			if (te != null) {
				int quantityIngots = Math.min(storage[0].stackSize / 6, storage[1].stackSize / getSmeltedRatio(storage[1]));

				EnumMetal smeltedMetal = getSmeltedMetalType(storage[1]);
				
				if (smeltedMetal != EnumMetal.IRON) {
					te.receiveMoltenMetal(quantityIngots, smeltedMetal);
					
				} else {
					// Below 75% blows -> no steel
					// Between 75% and 100% blows -> between 0 and fullsteel 
					float steelRatio = Math.min(1f, 4f * Math.max(0, receivedAirBlows*1f / getRequiredBlowsForSteel() - 0.75f));
										
					int quantitySteel = (int) (steelRatio * quantityIngots);
					int quantityIron = quantityIngots - quantitySteel;
					
					System.out.println("received " + receivedAirBlows + " blows, resulting in " + quantitySteel + " steel ingots and " + quantityIron + " iron ingots");
					
					if (quantityIron > 0) {
						te.receiveMoltenMetalMix(quantityIron, EnumMetal.IRON, quantitySteel, EnumMetal.STEEL);					
					} else {
						te.receiveMoltenMetal(quantitySteel, EnumMetal.STEEL);
					}
					
				}
				
			}
			
			TEFurnaceSection tefs = (TEFurnaceSection) worldObj.getTileEntity(pos.up());
			if (tefs != null) {
				tefs.state = 0;
				worldObj.markBlockForUpdate(pos.up());
			}

			state = 0;
			clearContent();			
		} else {
			state = 2;
			
			storage[0] = null;
			storage[2] = getSmeltedOre(storage[1]);
			if (storage[2] != null) {
				storage[2].stackSize = storage[1].stackSize / getSmeltedRatio(storage[1]);  // ingots	
			}
			
			storage[1].stackSize = 0; // ore
			
			if (storage[2].stackSize == 0) state = 0;
		}
		
		burntime = 0;
		receivedAirBlows = 0;
		worldObj.markBlockForUpdate(pos);
		markDirty();
	}
	
	
	
	
	public int getState() {
		return state;
	}
	
	
	public int getFillHeight() {
		return Math.min(16, getUnboundFillHeight());
	}
	
	public int getUnboundFillHeight() {
		if (furnacetype == null) {
			return Math.max(0, getFillHeightFromBelow());
		}
		
		int quantityFuel = storage[0] != null ? storage[0].stackSize : 0; 
		int quantityOre = storage[1] != null ? storage[1].stackSize : 0;
		int quantityOutput = storage[2] != null ? storage[2].stackSize * 4 : 0;
		
		return Math.max(
			2 * ((quantityFuel + quantityOre) / 4 + storage[3].stackSize),
			quantityOutput
		);
	}
	
	
	public int getFillHeightFromBelow() {
		TileEntity te = worldObj.getTileEntity(pos.down());
		if (te instanceof TEFurnaceSection) {
			TEFurnaceSection fste = (TEFurnaceSection)te;
			return fste.getUnboundFillHeight() - 16;
		}
		return -1;
	}

	
	public boolean canIgnite() {
		checkFurnaceType();
		
		return 
			furnacetype != null &&
			state == 0 &&
			storage[0] != null &&
			storage[0].stackSize >= storage[1].stackSize &&
			getFillHeight() >= 2
		;
	}
	
	
	
	
	public int maxStackSize(ItemStack ore) {
		// 16 coal and 16 limonite ore for bloomeries
		if (furnacetype != EnumFurnaceType.BLASTFURNACE) return 16;

		// 36 coal and 24 limonite ore for blast furnaces
		if (ore != null && getSmeltedOre(ore) != null) return 24;
		return 36;
	}
	
	
	public ItemStack getSmeltedOre(ItemStack ore) {
		if (ore.getItem() instanceof IItemSmeltable) {
			ItemStack smelted = ((IItemSmeltable)ore.getItem()).getSmelted(ore);
			
			if (((IItemSmeltable)ore.getItem()).getMeltingPoint(ore) <= furnacetype.maxTemperature) {
				return smelted;
			}
			
			return null;
		}
		
		return null;
	}
	
	public int getSmeltedRatio(ItemStack ore) {
		return ((IItemSmeltable)ore.getItem()).getRaw2SmeltedRatio(ore);
	}
	
	public EnumMetal getSmeltedMetalType(ItemStack ore) {
		if (ore.getItem() instanceof IItemSmeltable) {
			return ItemIngot.getMetal(getSmeltedOre(ore));
		}
		return null;
	}
	
	
	public boolean tryIgnite() {
		if (!canIgnite()) return false;
		
		burntime = getTotalBurnTime();
		state = 1;
		
		if (furnacetype == EnumFurnaceType.BLASTFURNACE) {
			TEFurnaceSection te = (TEFurnaceSection) worldObj.getTileEntity(pos.up());
			te.state = 1;
			worldObj.markBlockForUpdate(pos.up());
		}
		
		worldObj.markBlockForUpdate(pos);
		return true;
	}
	
	// :D
	public boolean canTheoreticallyPutItemStack(ItemStack itemstack) {
		return 
			viableFuel(itemstack) ||
			getSmeltedOre(itemstack) != null;			
	}

	public boolean tryPutItemStack(ItemStack itemstack) {
		checkFurnaceType();

		if (state != 0) return false;
		
		if (storage[0] == null && viableFuel(itemstack)) {

			storage[0] = itemstack.splitStack(1);
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		
		
		
		if (storage[0] != null && ItemStack.areItemsEqual(storage[0], itemstack) && ItemStack.areItemStackTagsEqual(storage[0], itemstack)) {
			if (storage[0].stackSize >= maxStackSize(null)) return false;

			storage[0].stackSize++;
			itemstack.stackSize--;
			worldObj.markBlockForUpdate(pos);
			return true;
		}

		
		ItemStack smelted = getSmeltedOre(itemstack);

		if (smelted != null) {

			if (storage[1] != null) {
				if (ItemOreVC.getOreType(storage[1]) != ItemOreVC.getOreType(itemstack)) return false;
				
				if (storage[1].stackSize >= maxStackSize(itemstack)) return false;

				storage[1].stackSize++;
				itemstack.stackSize--;
			} else {
				storage[1] = itemstack.splitStack(1);
			}
			
			worldObj.markBlockForUpdate(pos);
			return true;
		}
		
		//  TODO: Make this work (i.e. being able to smelt iron ingots back into steel)
		/*if (itemstack.getItem() instanceof ItemIngot && ItemIngot.getMetal(itemstack) == EnumMetal.IRON) {
			if (storage[1].stackSize + storage[3].stackSize*4 >= maxStackSize(EnumOreType.LIMONITE)) return false;
			
			storage[3].stackSize++;
			itemstack.stackSize--;
			worldObj.markBlockForUpdate(pos);
		}*/
		
		return false;
	}


	
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		state = nbttagcompound.getInteger("mode");
		burntime = nbttagcompound.getInteger("burntime");
		int furnacetypeindex = nbttagcompound.getInteger("furnacetype");
		if (furnacetypeindex == -1) {
			furnacetype = null;
		} else {
			furnacetype = EnumFurnaceType.values()[furnacetypeindex];
		}
		
		receivedAirBlows = nbttagcompound.getInteger("receivedAirBlows");
		
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("mode", state);
		nbttagcompound.setInteger("burntime", burntime);
		if (furnacetype == null) {
			nbttagcompound.setInteger("furnacetype", -1);
		} else {
			nbttagcompound.setInteger("furnacetype", furnacetype.ordinal());
		}
		
		nbttagcompound.setInteger("receivedAirBlows", receivedAirBlows);
		
		super.writeToNBT(nbttagcompound);
	}


	public boolean tryTakeItemStack(EntityPlayer player) {
		if (state != 0) return false;
		
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = storage[i];
			
			if (stack != null && stack.stackSize > 0) {
				if (!player.inventory.addItemStackToInventory(stack)) {
					if (!getWorld().isRemote) {
						getWorld().spawnEntityInWorld(
							new EntityItem(getWorld(),
								getPos().getX() + 0.5f,
								getPos().getY() + 0.5f, 
								getPos().getZ() + 0.5f, 
								stack
						));
					}
				}
				
				stack.stackSize--;

				return true;
			}
		}
		return false;
	}


	public void receiveAirBlowFromBellows() {
		if (state == 1) {
			if (!worldObj.isRemote) System.out.println(receivedAirBlows + ", blows. Time left: " + (burntime - receivedAirBlows*20)/20 + " s");
			receivedAirBlows++;
			airblowtimer = 15;
		}
	}


	public void clearFuel() {
		storage[0] = null;
	}
	
	public void clearContent() {
		storage[0] = null;
		storage[1].stackSize = 0;
		storage[2].stackSize = 0;
		storage[3].stackSize = 0;
	}

}

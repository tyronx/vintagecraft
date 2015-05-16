package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.AnvilRecipes;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Interfaces.ISmithable;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

public class TEAnvil extends TENoGUIInventory {
	private EnumMetal metal;
	int uses = -1;
	public int currentTemplate = -1;
	
	public TEAnvil() {
		metal = null;
		storage = new ItemStack[getSizeInventory()];
	}
	
	public void setUses(int uses) {
		this.uses = uses;
	}
	
	public void onAnvilUse(EntityPlayer entityplayer) {
		if (uses > 0) uses--;
		if (uses == 0) {
			worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:anvilbreak", 1f, 1f);
			ejectContents();
			worldObj.destroyBlock(pos, true);
			entityplayer.closeScreen();
		}
	}
	
	
	public EnumMetal getMetal() {
		return metal;
	} 
	
	@Override
	public void createInitNBT(NBTTagCompound nbt) {
		writeToNBT(nbt);
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt) {
		readFromNBT(nbt);
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		metal = EnumMetal.byId(nbttagcompound.getInteger("metal"));
		currentTemplate = nbttagcompound.getInteger("currenttemplate");
		super.readFromNBT(nbttagcompound);
	}

	
	@Override
	public void writeToNBT(NBTTagCompound nbttagcompound) {
		nbttagcompound.setInteger("metal", metal != null ? metal.getId() : -1);
		nbttagcompound.setInteger("currenttemplate", currentTemplate);
		super.writeToNBT(nbttagcompound);
	}



	@Override
	public int getSizeInventory() {
		return 4;
	}



	@Override
	public String getName() {
		return "Anvil";
	}



	@Override
	public IChatComponent getDisplayName() {
		return new ChatComponentText("Anvil");
	}



	public void checkCraftable(ContainerAnvil containeranvil) {
		//System.out.println("check craftable");
		
		ItemStack itemstack = containeranvil.getSlot(0).getStack();
		if (itemstack == null || !(itemstack.getItem() instanceof ISmithable)) return;
		
		
		if (itemstack.getItem() instanceof IItemHeatable) {
			((IItemHeatable)itemstack.getItem()).updateTemperature(itemstack, worldObj);
		}
		
        ISmithable smithable = (ISmithable)itemstack.getItem();	        
        if (!smithable.workableOn(metal != null ? metal.tier : 0, itemstack, containeranvil.getSlot(1).getStack())) return;

        
        EnumAnvilTechnique []techniques = smithable.getAppliedAnvilTechniques(itemstack);
        if (techniques.length == 0) return;
        
        
        
        ItemStack []input;
        if (containeranvil.getSlot(1).getStack() != null) {
        	input = new ItemStack[]{itemstack, containeranvil.getSlot(1).getStack()};
        } else {
        	input = new ItemStack[]{itemstack};
        }
        
		if (AnvilRecipes.isInvalidRecipe(techniques, input)) {
			//System.out.println("is oddly shaped");
			smithable.markOddlyShaped(itemstack, true);
			containeranvil.getSlot(2).putStack(itemstack);
			containeranvil.getSlot(0).putStack(null);
		} else {
			AnvilRecipes recipe = AnvilRecipes.getMatchingRecipe(techniques, input);

			if (recipe != null) {
				//System.out.println("created item  " + recipe.output);
				containeranvil.getSlot(2).putStack(recipe.output.copy());
				containeranvil.getSlot(1).putStack(null);
				containeranvil.getSlot(0).putStack(null);
			}
			
			
				
			
		}
		
		containeranvil.detectAndSendChanges();
		
	}


	public void setMetal(EnumMetal metal) {
		this.metal = metal;
	}


	public int getTier() {
		if (metal != null) return metal.tier;
		return 0;
	}


}

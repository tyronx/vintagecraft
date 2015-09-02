package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.AchievementsVC;
import at.tyron.vintagecraft.Interfaces.IItemHeatable;
import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeManager;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
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
	
	public boolean onAnvilUse(EntityPlayer entityplayer) {
		if (uses > 0) uses--;
		if (uses == 0) {
			worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:anvilbreak", 1f, 1f);
			ejectContents();
			worldObj.destroyBlock(pos, true);
			entityplayer.closeScreen();
			return false;
		}
		return true;
	}
	
	
	public EnumMetal getMetal() {
		return metal;
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
		ItemStack itemstack = containeranvil.getSlot(0).getStack();
		if (itemstack == null || !(itemstack.getItem() instanceof IItemSmithable)) return;
		
        IItemSmithable smithable = (IItemSmithable)itemstack.getItem();	        
        if (!smithable.workableOn(metal != null ? metal.tier : 0, itemstack, containeranvil.getSlot(1).getStack())) return;

        
        EnumWorkableTechnique []techniques = smithable.getAppliedTechniques(itemstack);
        if (techniques.length == 0) return;
        
        ItemStack []input;
        if (containeranvil.getSlot(1).getStack() != null) {
        	input = new ItemStack[]{itemstack, containeranvil.getSlot(1).getStack()};
        } else {
        	input = new ItemStack[]{itemstack};
        }
        
		if (WorkableRecipeManager.smithing.isInvalidRecipe(techniques, input)) {
			containeranvil.playerInventory.player.triggerAchievement(AchievementsVC.createOddlyShapedIngot);
			
			smithable.markOddlyShaped(itemstack, true);
			containeranvil.getSlot(2).putStack(itemstack);
			containeranvil.getSlot(0).putStack(null);			
		} else {
			WorkableRecipeBase recipe = WorkableRecipeManager.smithing.getMatchingRecipe(techniques, input);

			if (recipe != null) {
				moveToOutput(containeranvil, input[0], recipe.output.copy());
				containeranvil.getSlot(1).putStack(null);
			}
		}
		
		containeranvil.detectAndSendChanges();
	}
	
	
	public void moveToOutput(ContainerAnvil containeranvil, ItemStack firstinput, ItemStack output) {
		if (firstinput.getItem() instanceof IItemHeatable && output.getItem() instanceof IItemHeatable) {
			
			IItemHeatable heatableInput = (IItemHeatable)firstinput.getItem();
			IItemHeatable heatableOutput = (IItemHeatable)output.getItem();
			
			heatableOutput.setTemperatureM10(
				output,
				heatableInput.getTemperatureM10(firstinput), 
				worldObj.getTotalWorldTime()
			);
			heatableOutput.setStartCoolingAt(output, heatableInput.getStartCoolingAt(firstinput));
		}
		
		containeranvil.getSlot(2).putStack(output);
		containeranvil.getSlot(0).putStack(null);
	}


	public void setMetal(EnumMetal metal) {
		this.metal = metal;
	}


	public int getTier() {
		if (metal != null) return metal.tier;
		return 0;
	}


}

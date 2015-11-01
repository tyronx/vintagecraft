package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import at.tyron.vintagecraft.Interfaces.ISizedItem;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumItemSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemFoodVC extends ItemFood implements IItemSmeltable, ISubtypeFromStackPovider, ISizedItem {
	String internalname;
	
	public ItemFoodVC(int amount, boolean isWolfFood) {
		super(amount, isWolfFood);
		setCreativeTab(VintageCraft.resourcesTab);
	}
	
    public ItemFoodVC(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setCreativeTab(VintageCraft.resourcesTab);
    }

	public Item register(String internalname) {
		setUnlocalizedName(internalname);
		GameRegistry.registerItem(this, internalname);
		VintageCraft.proxy.addVariantName(this, ModInfo.ModID + ":food/" + internalname);
		this.internalname = internalname;
		return this;
	}
    
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (internalname.equals("porkchopRaw")) {
			return new ItemStack(ItemsVC.porkchopCooked);
		}
		if (internalname.equals("beefRaw")) {
			return new ItemStack(ItemsVC.beefCooked);
		}
		if (internalname.equals("chickenRaw")) {
			return new ItemStack(ItemsVC.chickenCooked);
		}
		return null;
	}

	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		
		tooltip.add("+" + (getHealAmount(stack)/2f) + " food");
	}
	
	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		return 1;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		return 100;
	}
	
	@Override
	public String getSubType(ItemStack stack) {
		return internalname;
	}

	@Override
	public float getSmeltingSpeedModifier(ItemStack raw) {
		return 0.9f;
	}

	@Override
	public EnumItemSize getItemSize() {
		return EnumItemSize.SMALL;
	}

	
}

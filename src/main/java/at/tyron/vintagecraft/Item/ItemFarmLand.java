package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Interfaces.Item.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFarmLand extends ItemBlockVC implements ISubtypeFromStackPovider {

	public ItemFarmLand(Block block) {
		super(block);
		setHasSubtypes(true);
	}

	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	for (EnumFertility fertility : EnumFertility.values()) {
    		subItems.add(setFertility(new ItemStack(Item.getItemFromBlock(BlocksVC.farmland)), fertility));
    	}
    }
    
    
    @Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(StatCollector.translateToLocal(getFertility(itemstack).getName() + ".name"));
	}
	
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return "farmland";
	}
	
	
	
	public EnumFertility getFertility(ItemStack itemstack) {
		return EnumFertility.fromMeta(itemstack.getItemDamage());
	}



	public ItemStack setFertility(ItemStack itemStack, EnumFertility fertility) {
		itemStack.setItemDamage(fertility.getMetaData());
		return itemStack;
	}



	@Override
	public String getSubType(ItemStack stack) {
		EnumFertility fertility = EnumFertility.fromMeta((stack.getItemDamage() >> 2) & 3);		
		return fertility.getStateName();
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	

}

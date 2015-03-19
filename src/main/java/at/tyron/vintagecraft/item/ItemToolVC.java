package at.tyron.vintagecraft.item;

import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemToolVC extends ItemVC implements ISubtypeFromStackPovider {	
	public EnumTool tooltype;
	
	public ItemToolVC() {
		setCreativeTab(CreativeTabs.tabTools);
		maxStackSize = 1;
		setMaxDamage(getMaxUses());
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (tooltype == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName();
	}

	
	
	@Override
	public float getDigSpeed(ItemStack itemstack, IBlockState state) {
		return getEfficiencyOnMaterial(itemstack, state.getBlock().getMaterial());
	}
	

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(1, playerIn);
        }

        return true;
    }
    
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", (double)this.getDamageGainOnEntities(), 0));
        return multimap;
    }
    
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	
	@Override
	public boolean canHarvestBlock(Block blockIn) {
		return blockIn.getBlockHardness(null, null) - getHarvestLevel() < 2;
    }

	 
    
    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

	
	public abstract int getHarvestLevel();
	public abstract int getMaxUses();
	public abstract float getEfficiencyOnMaterial(ItemStack itemstack, Material material);
	public abstract float getDamageGainOnEntities();
	
	public int getEnchantability() {
		return 0;
	}
	
	
	
	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		stack.setItemDamage(stack.getItemDamage()+2);
		return stack;
	}
	
	@Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }
	
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

		if (!playerIn.canPlayerEdit(pos.offset(side), side, stack) || tooltype != EnumTool.HOE) {
	        return false;
	    } else {
	        return net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, playerIn, worldIn, pos) > 0;
	    }
    }
    
    public boolean isRepairable() {
    	return false;
    }
}

package at.tyron.vintagecraft.Item;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.Interfaces.IItemSmeltable;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRock extends ItemBlockVC implements IItemSmeltable {

	public ItemRock(Block block) {
		super(block);
		this.setMaxDamage(0);
        this.setHasSubtypes(true);
	}


	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (BlocksVC.rock.containsBlock(item.getBlock())) {
			return "rock." + getRockType(itemstack).getUnlocalizedName();
		} else {
			return "rock";
		}
	}
	
	
	public static EnumRockType getRockType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumRockType) getBlockClass(block).getEntryFromMeta(block, itemstack.getItemDamage()).getKey();
	}
	
	public static BaseBlockClass getBlockClass(Block block) {
		return BlocksVC.rock;
	}

	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		ItemRock item = (ItemRock) itemstack.getItem();
		
		if (getBlockClass(item.getBlock()).containsBlock(item.getBlock())) {
			EnumRockType rocktype = getRockType(itemstack);
			tooltip.add(StatCollector.translateToLocal(BlocksVC.rock.getCommandSenderName() + "." + rocktype.getUnlocalizedName() + ".name"));
			tooltip.add(StatCollector.translateToLocal("rocktype." + rocktype.group.name().toLowerCase(Locale.ROOT) + ".name"));
		}
	}
	
	
	
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
    	if (((ItemRock)stack.getItem()).getBlock() == BlocksVC.topsoil) {
    		return BlocksVC.topsoil.getRenderColor(BlocksVC.topsoil.getStateFromMeta(stack.getMetadata()));
    	}
    	return 16777215;
    }
	
	

	@Override
    public int getMetadata(int damage) {
        return damage;
    }
	
	
	@Override
	public ItemStack getSmelted(ItemStack raw) {
		if (getRockType(raw) == EnumRockType.QUARTZITE) {
			return new ItemStack(Blocks.glass);
		}
		return null;
	}

	@Override
	public int getRaw2SmeltedRatio(ItemStack raw) {
		if (getRockType(raw) == EnumRockType.QUARTZITE) {
			return 1;
		}
		return 0;
	}

	@Override
	public int getMeltingPoint(ItemStack raw) {
		if (getRockType(raw) == EnumRockType.QUARTZITE) {
			return 1800;
		}
		return 0;
	}
	
	@Override
	public float getSmeltingSpeedModifier(ItemStack raw) {
		return 1f;
	}


}

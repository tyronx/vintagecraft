package at.tyron.vintagecraft.Item;

import java.util.HashMap;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.Organic.BlockLeavesVC;
import at.tyron.vintagecraft.Block.Utility.BlockStoneAnvil;
import at.tyron.vintagecraft.Interfaces.IItemRackable;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemToolVC extends ItemVC implements ISubtypeFromStackPovider, IItemRackable {	
	public EnumTool tooltype;
	public boolean diamondencrusted; 
	
	public ItemToolVC(EnumTool tooltype, boolean diamondencrusted) {
		this.tooltype = tooltype;
		if (!diamondencrusted) {
			setCreativeTab(VintageCraft.toolsarmorTab);
		}
		maxStackSize = 1;
		this.diamondencrusted = diamondencrusted;

		int maxUses = (int) (getMaxUses() * (diamondencrusted ? 1.5f : 1));
		setMaxDamage(maxUses);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (tooltype == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		
		return super.getUnlocalizedName();
	}
	
	
	public static ItemStack getDiamondEncrustedVarianOf(ItemStack toolstack) {
		if (!(toolstack.getItem() instanceof ItemToolVC)) return null;
		if (((ItemToolVC)toolstack.getItem()).diamondencrusted) return toolstack;
		
		ItemToolVC item = ItemsVC.tools.get(toolstack.getItem().getUnlocalizedName(toolstack) + "_dmd");
		
		return new ItemStack(item);
	}
	
	public String getVariant(ItemStack toolstack) {
		return diamondencrusted ? "_dmd" : "";
	}
	
	
	@Override
	public float getDigSpeed(ItemStack itemstack, IBlockState state) {
		boolean harvestable = canHarvestBlock(state);
		
		float f = getEfficiencyOnMaterial(itemstack, state.getBlock().getMaterial());
		
		if (diamondencrusted) {
			f *= 1.3;
		}
		
		if (state.getBlock() instanceof BlockVC) {
			f /= ((BlockVC)state.getBlock()).getBlockHardnessMultiplier(state);
		}
		
		return f / (harvestable ? 1 : 20f);
	}
	

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }
    
    


    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    	if (tooltype == EnumTool.SHEARS) {
    		stack.damageItem(1, playerIn);
    		
    		int uses = destroyBlocksOfClass(worldIn, playerIn.getPosition(), pos, Math.min(5, stack.getMaxDamage() - stack.getItemDamage()), BlockLeavesVC.class);
    		
    		stack.damageItem(uses, playerIn);
    		
    	} else {
    	
	        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
	            stack.damageItem(1, playerIn);
	        }
    	}
        

        return true;
    }
    
    public Multimap getItemAttributeModifiers() {
        Multimap multimap = super.getItemAttributeModifiers();
        
        float dmg = this.getDamageGainOnEntities();
		if (diamondencrusted) {
			dmg *= 1.3;
		}

        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", (double)dmg, 0));
        return multimap;
    }
    
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	
	@Override
	public boolean canHarvestBlock(Block blockIn) {
		return true;
    }
	
	
	public boolean canHarvestBlock(IBlockState state) {
		if (state.getBlock() instanceof BlockVC) {
			BlockVC block = (BlockVC)state.getBlock();
			return block.getHarvetLevel(state) <= getHarvestLevel();
		}
		return true;
	}

	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Condition: " + Math.round(100 - 100f*itemstack.getItemDamage() / getMaxUses()) + "%");
	}

	 
	@Override
	public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
		return !canHarvestBlock(player.worldObj.getBlockState(pos));
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
    	if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) return false;
    	
		if (tooltype == EnumTool.HOE) {
	        return net.minecraftforge.event.ForgeEventFactory.onHoeUse(stack, playerIn, worldIn, pos) > 0;
	    }
		
		if (tooltype == EnumTool.HAMMER) {
			
			IBlockState state = worldIn.getBlockState(pos);
			
			
			boolean unfinishedanvil = BlocksVC.stoneanvil.containsBlock(state.getBlock()) && (Integer) state.getValue(((BlockStoneAnvil)state.getBlock()).STAGE) > 0;
			
			if (
				(BlocksVC.rock.containsBlock(state.getBlock()) || unfinishedanvil) && 
				worldIn.isAirBlock(pos.up())
			) {

				if (unfinishedanvil) {
					int stage = (Integer) state.getValue(((BlockStoneAnvil)state.getBlock()).STAGE) - 1;
					((BlockStoneAnvil)state.getBlock()).setStage(worldIn, pos, state, stage);
				} else {
					IBlockState anvilstate = BlocksVC.stoneanvil.getBlockStateFor(BlocksVC.rock.getEntryFromState(state).getKey());
					worldIn.setBlockState(pos, anvilstate.withProperty(BlockStoneAnvil.STAGE, 2));
				}
				
				if (!worldIn.isRemote) {
					worldIn.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:stonehit", 1f, 1f);
				}
				
				stack.damageItem(2, playerIn);
				playerIn.swingItem();
			}
			
		}
		
		return false;
    }
    
    public boolean isRepairable() {
    	return false;
    }
    
    
    
    public int destroyBlocksOfClass(World world, BlockPos playerpos, BlockPos centerpos, int quantity, Class blockclass) {
    	HashMap<BlockPos, Double> positions = new HashMap<BlockPos, Double>(); 
		BlockPos pos;
		
		for (int dx = -1; dx <= 1; dx++) {
			for (int dy = -1; dy <= 1; dy++) {
				for (int dz = -1; dz <= 1; dz++) {
					if (dx != 0 || dz != 0 || dy != 0) {
						pos = centerpos.add(dx, dy, dz);
						if (blockclass.isInstance(world.getBlockState(pos).getBlock())) {
							positions.put(pos, playerpos.distanceSq(pos.getX(), pos.getY(), pos.getZ()));
						}
					}
				}
			}
		}
		
		ImmutableList<BlockPos> nearestblocks = Ordering.natural().onResultOf(Functions.forMap(positions)).immutableSortedCopy(positions.keySet());
		int destroyed = 0;
		
		
		for (int i = 0; i < quantity; i++) {
			if (nearestblocks.size() <= i) break;
			if (!world.isRemote) world.destroyBlock(nearestblocks.get(i), true);
			destroyed++;
		}
		
		return destroyed;
    }
    
}

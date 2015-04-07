package at.tyron.vintagecraft.Item;

import java.util.List;

import at.tyron.vintagecraft.Block.BlockFirepit;
import at.tyron.vintagecraft.Block.BlockLeavesBranchy;
import at.tyron.vintagecraft.Block.BlockLeavesVC;
import at.tyron.vintagecraft.Block.BlockPlanksVC;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Block.BlockFirepit.EnumBuildStage;
import at.tyron.vintagecraft.BlockClass.BlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.TreeClass;
import at.tyron.vintagecraft.Interfaces.IFuel;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumMaterialDeposit;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemLogVC extends ItemBlock implements ISubtypeFromStackPovider, IFuel {

	public ItemLogVC(Block block) {
		super(block);
        this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + getTreeType(stack).getStateName();
	}
	
	
	public static ItemStack withTreeType(ItemStack itemstack, BlockClassEntry treetype) {
		itemstack.setItemDamage(treetype.metadata);
		return itemstack;
	}
	
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	
	
	public static EnumTree getTreeType(ItemStack itemstack) {
		Block block = ((ItemBlock)itemstack.getItem()).block;
		return (EnumTree) getBlockClass(block).getBlockClassfromMeta(block, itemstack.getItemDamage()).getKey();
	}

	@Override
	public String getSubType(ItemStack stack) {
		if (getTreeType(stack) == null) return EnumTree.ASH.getStateName();
		return getTreeType(stack).getStateName();
	}
	
	

	@Override
	public int getBurningHeat(ItemStack stack) {
		return 800;
	}

	@Override
	public float getBurnDurationMultiplier(ItemStack stack) {
		return 2f;
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		EnumStrongHeatSource.addItemStackInformation(itemstack, tooltip);
	}
	
	
	public static BlockClass getBlockClass(Block block) {
		// Workaround for Java being too fail to allow overriding static methods
		if (block instanceof BlockLeavesBranchy) return BlocksVC.leavesbranchy;
		if (block instanceof BlockLeavesVC) return BlocksVC.leaves;
		if (block instanceof BlockPlanksVC) return BlocksVC.planks;
		
		return BlocksVC.log;
	}
	
	
	
	public BlockFirepit.EnumBuildStage getNextFirepitStage(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side) {
		IBlockState state = world.getBlockState(pos);
		
		if(state.getBlock() instanceof BlockFirepit) {
			BlockFirepit.EnumBuildStage stage = (EnumBuildStage) state.getValue(BlockFirepit.buildstage);
			return stage.getNextStage();
		} else {
			return null;
		}
	}
	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		BlockFirepit.EnumBuildStage stage = getNextFirepitStage(itemstack, entityplayer, world, pos, side);
		
		if (stage != null) {
			world.setBlockState(pos, BlocksVC.firepit.getDefaultState().withProperty(BlockFirepit.buildstage, stage));
			itemstack.stackSize -=3;
			return true;
		}
		
		
    	if (entityplayer.getCurrentEquippedItem() != null && entityplayer.isSneaking()) {
    		IBlockState state = world.getBlockState(pos);
    		if(state.getBlock() instanceof BlockFirepit) {
    			TEHeatSourceWithGUI teheatsource = (TEHeatSourceWithGUI) world.getTileEntity(pos);
        		if (teheatsource.tryPutItemStack(itemstack)) {
        			return true;
        		}
    		}
    	}
    	

    	
		return super.onItemUse(itemstack, entityplayer, world, pos, side, hitX, hitY, hitZ);
	}

	
	

}

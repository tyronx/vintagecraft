
package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Block.Metalworking.BlockCokeOvenDoor;
import at.tyron.vintagecraft.Interfaces.IItemFuel;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class TECokeOvenDoor extends NetworkTileEntity {
	public TECokeOvenDoor() {
	}

	public boolean isValidCokeOven() {
		IBlockState state = worldObj.getBlockState(pos); 
		if (state.getBlock() != BlocksVC.cokeovendoor) return false;
		
		EnumFacing back = ((EnumFacing) state.getValue(BlockCokeOvenDoor.FACING)).getOpposite();
		BlockPos backPos = pos.offset(back);
		BlockPos backbackPos = backPos.offset(back);
		
		EnumFacing left = back.rotateYCCW();
		EnumFacing right = back.rotateY();
		
		return
			// 1st Vertical Layer
			fireClayBricks(pos.down()) && 
			fireClayBricks(pos.down().offset(left)) &&
			fireClayBricks(pos.down().offset(right)) &&
			fireClayBricks(pos.offset(left)) &&
			fireClayBricks(pos.offset(right)) &&
			fireClayStairs(pos.up()) &&
			fireClayStairs(pos.up().offset(left)) &&
			fireClayStairs(pos.up().offset(right)) &&
			
			// 2nd Vertical Layer
			fireClayBricks(backPos.down()) && 
			fireClayBricks(backPos.down().offset(left)) &&
			fireClayBricks(backPos.down().offset(right)) &&
			fireClayBricks(backPos.offset(left)) &&
			fireClayBricks(backPos.offset(right)) &&
			fireClayStairs(backPos.up()) &&
			fireClayStairs(backPos.up().offset(left)) &&
			fireClayStairs(backPos.up().offset(right)) &&

			// 3rd Vertical Layer
			fireClayBricks(backbackPos.down()) && 
			fireClayBricks(backbackPos.down().offset(left)) &&
			fireClayBricks(backbackPos.down().offset(right)) &&
			fireClayBricks(backbackPos.offset(left)) &&
			fireClayBricks(backbackPos.offset(right)) &&
			fireClayStairs(backbackPos.up())
		;
			
		
		
	}
	
	public boolean fireClayBricks(BlockPos pos) {
		return worldObj.getBlockState(pos).getBlock() == BlocksVC.fireclaybricks;
	}
	public boolean fireClayStairs(BlockPos pos) {
		return worldObj.getBlockState(pos).getBlock() == BlocksVC.fireclaystairs || worldObj.getBlockState(pos).getBlock() == BlocksVC.fireclaybricks;
	}

	public ItemStack getCokedOutput(ItemStack stack) {
		if (stack == null) return null;
		
		if (stack.getItem() instanceof IItemFuel) {
			return ((IItemFuel)stack.getItem()).getCokedOutput(stack);
		}
		return null;
	}
	
	


	public boolean canCreateCoke(ItemStack stack, BlockPos orepos) {
		IBlockState state = worldObj.getBlockState(pos); 
		if (state.getBlock() != BlocksVC.cokeovendoor) return false;
		EnumFacing back = ((EnumFacing) state.getValue(BlockCokeOvenDoor.FACING)).getOpposite();
		BlockPos backPos = pos.offset(back);
		
		boolean opened = (Boolean)state.getValue(BlockCokeOvenDoor.OPENED);
		
/*		System.out.println(
			isValidCokeOven()+" && "+(getCokedOutput(stack)!=null)+" && "+backPos.equals(orepos)+" && "+(!opened)
		);
*/
		
		return isValidCokeOven() && getCokedOutput(stack)!=null && backPos.equals(orepos) && !opened;
	}
	


	

	
	
}

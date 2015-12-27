package at.tyron.vintagecraft.Block.Mechanics;

import at.tyron.vintagecraft.AchievementsVC;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.Block.IBlockItemSink;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEMechanicalNetworkDeviceBase;
import at.tyron.vintagecraft.TileEntity.Mechanics.TEWindmillRotor;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockWindMillRotor extends BlockMechanicalVC implements IBlockItemSink {

	public BlockWindMillRotor() {
		super(Material.wood);
		setCreativeTab(VintageCraft.mechanicsTab);
	}



	@Override
	public String getSubType(ItemStack stack) {

		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TEWindmillRotor();
	}
	
	public boolean suitableGround(World worldIn, BlockPos pos) {
		boolean hasNeighbour = false;
		for (EnumFacing facing : EnumFacing.values()) {
			if (TEMechanicalNetworkDeviceBase.getNeighbourDevice(worldIn, pos, facing, false) != null) {
				hasNeighbour = true;
				break;
			}
		}

		return hasNeighbour;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		String mcside = "C";
		if (!worldIn.isRemote) mcside = "S";
		
		TEWindmillRotor te = (TEWindmillRotor)worldIn.getTileEntity(pos);
		if (te != null) {
			//System.out.println("network: " + te.getNetwork(te.getOrientation()));
			
			
			
			te.refreshModel = true;
			if (te.getNetwork(side) != null) {
	//			if (!worldIn.isRemote) te.getNetwork(null).sendNetworkToClient(worldIn);
				boolean networkClockWise = te.getNetwork(null).isClockWise();
				System.out.println(mcside + " " + "N"+te.getNetwork(side).networkId + ": " + te.getOrientation() + " / " + (networkClockWise ? "clockwise" : "counter-clockwise")  + " " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
//				System.out.println(te.getNetwork(null));
			} else {
				System.out.println(mcside + " " + te.getOrientation() + " / " + " " + (te.clockwise ? "clockwise" : "counter-clockwise") + " from " + te.directionFromFacing);
			}
			//System.out.println(te.getWindRotatingPower());
		}
		
		return false;
		
	}



	@Override
	public boolean tryPutItemstack(World world, BlockPos pos, EntityPlayer player, EnumFacing side, ItemStack itemstack) {
		if (itemstack.getItem() == ItemsVC.sail && itemstack.stackSize == 4) {
			TEWindmillRotor te = (TEWindmillRotor)world.getTileEntity(pos);
			if (te != null && te.tryAddBlades(player)) {
				player.triggerAchievement(AchievementsVC.acquireMechanicalPower);
				
				itemstack.stackSize = 0;
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		
	}



	@Override
	public boolean isBlockedAllowedAt(World worldIn, BlockPos pos) {
		return hasConnectibleDeviceAt(worldIn, pos);
	}
	
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntity te = (TileEntity)worldIn.getTileEntity(pos);
		if (te instanceof TEWindmillRotor) {
			int bladesize = ((TEWindmillRotor)te).getBladeSize();
			if (bladesize > 0) {
				ItemStack sails = new ItemStack(ItemsVC.sail, bladesize * 4);
				Block.spawnAsEntity(worldIn, pos, sails);
			}
		}
		
		super.breakBlock(worldIn, pos, state);
	}

	
}

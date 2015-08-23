package at.tyron.vintagecraft.TileEntity;

import javax.swing.plaf.metal.MetalToolBarUI;

import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;

public class TETallMetalMold extends NetworkTileEntity implements IUpdatePlayerListBox {
	public boolean refreshModel = false;

	
	int liquidMetalFlowTimer = 0;
	int quantityIngots = 0;
	int quantityIngots2 = 0;
	EnumMetal metal = null;
	EnumMetal metal2 = null;
	public EnumFacing orientation = EnumFacing.NORTH;
	private int metalTemperature = 0;
	
	
	public int getQuantityIngots() {
		return quantityIngots;
	}
	public int getQuantityIngots2() {
		return quantityIngots2;
	}
	public int getLiquidMetalFlowTimer() {
		return liquidMetalFlowTimer;
	}
	public int getMetalTemperature() {
		return metalTemperature;
	}
	public EnumMetal getMetal() {
		return metal;
	}
	public EnumMetal getMetal2() {
		return metal2;
	}
	public void onIngotsRemoved() {
		metal = null;
		metal2 = null;
		quantityIngots = 0;
		quantityIngots2 = 0;
		liquidMetalFlowTimer = 0;
		metalTemperature = 0;
		worldObj.markBlockForUpdate(pos);
	}
	
	public void receiveMoltenMetal(int quantityIngots, EnumMetal metal) {
		this.quantityIngots = quantityIngots;
		this.metal = metal;
		this.liquidMetalFlowTimer = 20 * 5;
		this.metalTemperature  = metal.meltingpoint;
		
		worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:moltenmetal", 1f, 1f);
		worldObj.markBlockForUpdate(pos);
	}
	
	public void receiveMoltenMetalMix(int quantity1, EnumMetal metal1, int quantity2, EnumMetal metal2) {
		this.quantityIngots = quantity1;
		this.metal = metal1;
		
		this.quantityIngots2 = quantity2;
		this.metal2 = metal2;
		
		this.liquidMetalFlowTimer = 20 * 5;
		this.metalTemperature  = Math.max(metal1.meltingpoint, metal2.meltingpoint);
		
		worldObj.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "vintagecraft:moltenmetal", 1f, 1f);
		worldObj.markBlockForUpdate(pos);		
	}
	
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		liquidMetalFlowTimer = compound.getInteger("liquidMetalFlowTimer");
		metalTemperature = compound.getInteger("metalTemperature");
		metal = EnumMetal.byId(compound.getInteger("metal"));
		metal2 = EnumMetal.byId(compound.getInteger("metal2"));
		
		quantityIngots = compound.getInteger("quantityIngots");
		quantityIngots2 = compound.getInteger("quantityIngots2");
		
		int num = compound.getInteger("orientation");
		orientation = num == -1 ? null : EnumFacing.values()[num];
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		
		compound.setInteger("liquidMetalFlowTimer", liquidMetalFlowTimer);
		compound.setInteger("metalTemperature", metalTemperature);
		compound.setInteger("metal", metal == null ? -1 : metal.id);
		compound.setInteger("quantityIngots", quantityIngots);
		
		compound.setInteger("metal2", metal2 == null ? -1 : metal2.id);
		compound.setInteger("quantityIngots2", quantityIngots2);
		
		compound.setInteger("orientation", orientation == null ? -1 : orientation.ordinal());
	}

	@Override
	public void update() {
		//System.out.println(orientation);
		
		if (liquidMetalFlowTimer > 0) {
			liquidMetalFlowTimer--;
		}
		if (metalTemperature > 0) {
			metalTemperature--;
			if (metalTemperature > 1000) {
				metalTemperature-=2;
			}
		}
	}

	public void setOrientation(EnumFacing facing) {
		this.orientation = facing;
		worldObj.markBlockForUpdate(pos);
	}
	
	

/*	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expand(5f, 5f, 5f);
	}
*/

}

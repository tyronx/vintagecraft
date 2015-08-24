package at.tyron.vintagecraft.Client.Render.TESR;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

public class TESRBase extends TileEntitySpecialRenderer {
	float[] getAnglesBetween(EnumFacing base, EnumFacing desired) {
		if (base == EnumFacing.UP || base == EnumFacing.DOWN) throw new RuntimeException("Not coded for vertical base");
		
		return new float[]{
			// Horizontal angle
			desired.getAxis().isVertical() ? 0 : 90f * (base.getHorizontalIndex() - desired.getHorizontalIndex()), 
			// Vertical angle
			desired.getAxis().isHorizontal() ? 0 : 90f * (desired == EnumFacing.UP ? 1 : -1) 
		};
	}
	
	float[] axis2GLFloats(EnumFacing facing) {
		float[] floats = new float[]{
			(facing.getAxis() == Axis.X ? 1f : 0f),
			(facing.getAxis() == Axis.Y ? 1f : 0f),
			(facing.getAxis() == Axis.Z ? 1f : 0f)
		};
		
		return floats;
	}
	
	
	
	public static float toHorizontalAngle(EnumFacing facing) {
		int horIndex = facing.getHorizontalIndex();
		if (horIndex == -1) return 0;
		
		return (horIndex * 90 - 0.5f);
		//return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 3);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_) {
	}
}
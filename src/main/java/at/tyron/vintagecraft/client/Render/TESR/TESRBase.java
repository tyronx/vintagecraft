package at.tyron.vintagecraft.Client.Render.TESR;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import com.google.common.primitives.SignedBytes;

public class TESRBase extends TileEntitySpecialRenderer {

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
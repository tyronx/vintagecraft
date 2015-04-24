package at.tyron.vintagecraft.Interfaces;

import net.minecraft.client.audio.ITickableSound;
import net.minecraft.util.BlockPos;

public interface IPitchAndVolumProvider {
	float getPitch();
	float getVolumne();
	
	public boolean isDonePlaying(IPitchAndVolumProvider self);
	
	BlockPos getPos();
}

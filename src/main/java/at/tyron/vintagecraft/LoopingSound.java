package at.tyron.vintagecraft;

import at.tyron.vintagecraft.Interfaces.IPitchAndVolumProvider;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class LoopingSound extends PositionedSound implements ITickableSound {
	IPitchAndVolumProvider pitchandvolumneprovider;
	
	public LoopingSound(ResourceLocation soundResource, IPitchAndVolumProvider pitchandvolumneprovider) {
		super(soundResource);
		this.pitchandvolumneprovider = pitchandvolumneprovider;
		this.xPosF = pitchandvolumneprovider.getPos().getX() + 0.5f;
		this.yPosF = pitchandvolumneprovider.getPos().getY() + 0.5f;
		this.zPosF = pitchandvolumneprovider.getPos().getZ() + 0.5f;
		
		repeat = true;
	}
	
	@Override
	public void update() {
		volume = pitchandvolumneprovider.getVolumne();
		pitch = pitchandvolumneprovider.getPitch();
	}

	@Override
	public boolean isDonePlaying() {
		return pitchandvolumneprovider.isDonePlaying(pitchandvolumneprovider);
	}

}

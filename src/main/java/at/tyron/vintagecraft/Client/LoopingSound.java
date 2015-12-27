package at.tyron.vintagecraft.Client;

import at.tyron.vintagecraft.Interfaces.Tileentity.IPitchAndVolumProvider;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class LoopingSound extends PositionedSound implements ITickableSound {
	IPitchAndVolumProvider pitchandvolumneprovider;
	
	public LoopingSound(ResourceLocation soundResource, IPitchAndVolumProvider pitchandvolumneprovider) {
		super(soundResource);
		this.pitchandvolumneprovider = pitchandvolumneprovider;
		this.xPosF = pitchandvolumneprovider.getPosition().getX() + 0.5f;
		this.yPosF = pitchandvolumneprovider.getPosition().getY() + 0.5f;
		this.zPosF = pitchandvolumneprovider.getPosition().getZ() + 0.5f;
		
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

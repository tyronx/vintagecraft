package at.tyron.vintagecraft.BlockClass;

import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumCrop;

public class CropClassEntry<E> extends BlockClassEntry<E> {
	public EnumCrop crop;
	public int stage;
	

	public CropClassEntry(EnumStateImplementation key, EnumCrop crop, int stage) {
		super(key);
		this.crop = crop;
		this.stage = stage;
	}

}

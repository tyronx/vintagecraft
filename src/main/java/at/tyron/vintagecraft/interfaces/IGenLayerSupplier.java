package at.tyron.vintagecraft.interfaces;


/* For classes/enums that supply infos on how to generate layers */ 

public interface IGenLayerSupplier {

	public int getDepthMin();
	public int getDepthMax();
	
	public int getColor();
	
	public int getWeight();
}

package at.tyron.vintagecraft.Item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ItemVC extends Item {

	
	public Item register(String internalname) {
		setUnlocalizedName(internalname);
		GameRegistry.registerItem(this, internalname);
		return this;
	}
	
	

}

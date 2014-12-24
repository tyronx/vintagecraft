package at.tyron.vintagecraft.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemVC extends Item {

	
	public Item register(String internalname) {
		setUnlocalizedName(internalname);
		GameRegistry.registerItem(this, internalname);
		
		//Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation("swm:SteelSword", "inventory"));
		
		return this;
	}
}

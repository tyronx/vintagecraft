package at.tyron.vintagecraft;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import at.tyron.vintagecraft.World.Climate;

public class ChunkEventHandler {

	
    
    @SubscribeEvent
	public void onUnloadWorld(WorldEvent.Unload event) {
		//Climate.removeCacheManager(event.world);
		//TFC_Core.removeCDM(event.world);
		//if(event.world.provider.dimensionId == 0)
		//	AnvilManager.getInstance().clearRecipes();
	}

	@SubscribeEvent
	public void onLoadWorld(WorldEvent.Load event) {
		//System.out.println("onloadworld");
		//if(event.world.provider.dimensionId == 0 && event.world.getTotalWorldTime() < 100)
		//	createSpawn(event.world);
				
		
		//TFC_Core.addCDM(event.world);
		
		
		
	}
	
	
}

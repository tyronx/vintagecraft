package at.tyron.vintagecraft.Client.Gui;

import at.tyron.vintagecraft.Entity.EntityCoalPoweredMinecartVC;
import at.tyron.vintagecraft.Inventory.ContainerCoalPoweredMinecart;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiCoalPoweredMinecart extends GuiContainer {

	static final ResourceLocation texture = new ResourceLocation("vintagecraft:textures/gui/container/gui_coalpoweredminecart.png");
	
	EntityCoalPoweredMinecartVC cart;
	
	public GuiCoalPoweredMinecart(InventoryPlayer player, EntityCoalPoweredMinecartVC cart) {
		super(new ContainerCoalPoweredMinecart(player, cart));
		this.cart = cart;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        
        drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int pixeldiff;

        if (cart.fuelBurnTime > 0) {
            pixeldiff = burnproggress(13);
            drawTexturedModalRect(k + 84, l + 30 + 12 - pixeldiff, 176, 12 - pixeldiff, 14, pixeldiff + 1);
            
            int totalfuelTime = cart.fuelBurnTime;
            if (cart.minecartContainerItems[0] != null) {
            	totalfuelTime += cart.getFuelValue(cart.minecartContainerItems[0]) * cart.minecartContainerItems[0].stackSize;
            }
            
            String timeleft = "";
            totalfuelTime = (int)Math.ceil(totalfuelTime / 20.0);

            if (totalfuelTime > 3600) {
            	int hours = totalfuelTime / 3600;
            	totalfuelTime -= hours * 3600;
            	timeleft += hours + "h ";
            }

            if (totalfuelTime > 60) {
            	int minutes = totalfuelTime / 60;
            	totalfuelTime -= minutes * 60;
            	timeleft += minutes + "m ";
            }
            
            timeleft += totalfuelTime + "s";
            
            
    		fontRendererObj.drawString(timeleft, k + 90 - fontRendererObj.getStringWidth(timeleft)/2, l + 15, 0);
        }
	}
	
    private int burnproggress(int multiplicator) {
    	if (cart.maxFuelBurnTime == 0) return 0;
    	
        return cart.fuelBurnTime * multiplicator / cart.maxFuelBurnTime;
    }
    


}

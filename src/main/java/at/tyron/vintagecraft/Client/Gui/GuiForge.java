package at.tyron.vintagecraft.Client.Gui;

import at.tyron.vintagecraft.Inventory.ContainerForge;
import at.tyron.vintagecraft.TileEntity.TEStonePot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiForge extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation("vintagecraft:textures/gui/container/gui_forge.png");
	
	TEStonePot teforge;
	InventoryPlayer playerInventory;

	public GuiForge(InventoryPlayer player, TEStonePot teforge) {
		super(new ContainerForge(player, teforge));
		
		this.teforge = teforge;
		this.playerInventory = player;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
	}

    

}

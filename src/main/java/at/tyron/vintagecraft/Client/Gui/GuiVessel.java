package at.tyron.vintagecraft.Client.Gui;

import at.tyron.vintagecraft.Inventory.ContainerVessel;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiVessel extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation("vintagecraft:textures/gui/container/gui_vessel.png");

	InventoryPlayer playerInventory;
	
	public GuiVessel(InventoryPlayer player, World world, NBTTagCompound nbt) {
		super(new ContainerVessel(player, nbt));
		
		this.playerInventory = player;
	}
	
	public GuiVessel(InventoryPlayer player, TEVessel tevessel) {
		super(new ContainerVessel(player, tevessel));
		
		this.playerInventory = player;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
		
	}

}

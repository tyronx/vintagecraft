package at.tyron.vintagecraft.gui;

import at.tyron.vintagecraft.Inventory.ContainerStove;
import at.tyron.vintagecraft.TileEntity.TileEntityStove;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiStove extends GuiContainer {
	private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
	
	TileEntityStove stove;
	InventoryPlayer playerInventory;
	
	public GuiStove(InventoryPlayer player, World world, TileEntityStove tileEntity) {
		super(new ContainerStove(player, tileEntity));
		
		this.playerInventory = player;
		this.stove = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		
		String s = stove.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
		
        fontRendererObj.setUnicodeFlag(true);
        
		String furntemp = stove.furnaceTemperature + " deg.";
		this.fontRendererObj.drawString(furntemp, this.xSize / 2 - 10, 56, 4210752);
		
		if (stove.oreSlot() != null) {
			String itemtemp = stove.oreTemperature + " deg.";
			this.fontRendererObj.drawString(itemtemp, this.xSize / 2 - 10, 20, 4210752);
		}
		
		fontRendererObj.setUnicodeFlag(false);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int pixeldiff;

        if (stove.isBurning()) {
            pixeldiff = this.burnproggress(13);
            this.drawTexturedModalRect(k + 56, l + 36 + 12 - pixeldiff, 176, 12 - pixeldiff, 14, pixeldiff + 1);
        }

        pixeldiff = this.smeltprogress(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, pixeldiff + 1, 16);
        
	}
	
	
	
    private int smeltprogress(int multiplicator) {
        int orecookingtime = stove.oreCookingTime;
        int maxcookingtime = stove.maxCookingTime();
        
        return maxcookingtime != 0 && orecookingtime != 0 ? orecookingtime * multiplicator / maxcookingtime : 0;
    }

    private int burnproggress(int multiplicator) {
    	if (stove.maxFuelBurnTime == 0) return 0;
    	
        return stove.fuelBurnTime * multiplicator / stove.maxFuelBurnTime;
    }
    
}

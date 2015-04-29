package at.tyron.vintagecraft.Gui;

import java.io.IOException;
import java.util.ArrayList;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerVessel;
import at.tyron.vintagecraft.Network.SoundEffectToServerPacket;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiAnvil extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation("vintagecraft:textures/gui/container/gui_anvil.png");
	
	ArrayList<GuiButtonVC> buttons = new ArrayList<GuiButtonVC>();
	boolean mousedown;
	InventoryPlayer playerInventory;
	
	ArrayList<EnumAnvilTechnique> appliedtechniques = new ArrayList<EnumAnvilTechnique>();
	
	
	public GuiAnvil(InventoryPlayer player, World world, NBTTagCompound nbt) {
		super(new ContainerVessel(player, nbt));
		
		this.playerInventory = player;
	}
	
	public GuiAnvil(InventoryPlayer player, TEAnvil teanvil) {
		super(new ContainerAnvil(player, teanvil));
		
		this.playerInventory = player;
		
		int w = 13;
		int spacing = 14; 
		int basex = 56;
		int basey = 22;
		
        buttons.clear();
		buttons.add(new GuiButtonVC(156, 5, w, w, "anvilassistant", this));
		
		
		buttons.add(new GuiButtonVC(basex, basey, w, w, "lighthit", this, EnumAnvilTechnique.LIGHTHIT));
		buttons.add(new GuiButtonVC(basex + spacing, basey, w, w, "mediumhit", this, EnumAnvilTechnique.MEDIUMHIT));
		buttons.add(new GuiButtonVC(basex + 2*spacing, basey, w, w, "heavyhit", this, EnumAnvilTechnique.HEAVYHIT));
		buttons.add(new GuiButtonVC(basex + 3*spacing, basey, w, w, "draw", this, EnumAnvilTechnique.DRAW));
		buttons.add(new GuiButtonVC(basex, basey + spacing, w, w, "punch", this, EnumAnvilTechnique.PUNCH));
		buttons.add(new GuiButtonVC(basex + spacing, basey + spacing, w, w, "bend", this, EnumAnvilTechnique.BEND));
		buttons.add(new GuiButtonVC(basex + 2*spacing, basey + spacing, w, w, "upset", this, EnumAnvilTechnique.UPSET));
		buttons.add(new GuiButtonVC(basex + 3*spacing, basey + spacing, w, w, "shrink", this, EnumAnvilTechnique.SHRINK));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        
        int i = 0;
        int dy = 0;
        
        for (GuiButtonVC button : buttons) {
        	i++;
        	
        	if (mousedown && button.insideButton(mouseX - x, mouseY - y)) {
        		button.draw(x, y, 200, 13);
        		dy = 1;
        	} else {
        		button.draw(x, y, 200, 0);
        		dy = 0;
        	}
        	
    		if (button.data instanceof EnumAnvilTechnique) {
    			this.drawTexturedModalRect(x + button.x, y + button.y + dy, 200 + 13 * ((i-2) % 4), 67 + 13 * (i > 5 ? 1 : 0), 13, 13);
    		}
        }
        
        
        i = 0;
        for (EnumAnvilTechnique technique : appliedtechniques) {
        	this.drawTexturedModalRect(x + 6 + 15*i, y + 5, 200 + 13 * (technique.ordinal() % 4), 67 + 13 * (technique.ordinal() >= 4 ? 1 : 0), 13, 13);
        	
        	i++;
        }

	}

	
	
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
        	mousedown = true;
        }
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	mousedown = false;
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
    	
    	for (GuiButtonVC button : buttons) {
    		if (button.insideButton(mouseX - x, mouseY - y)) {
    			
    			if (button.data instanceof EnumAnvilTechnique) {
    				TechniqueHit((EnumAnvilTechnique)button.data);
    			}
    			
    			
    			System.out.println(button.buttonname + " pressed");
    		}
    	}
    }
    
    
    
    void TechniqueHit(EnumAnvilTechnique technique) {
    	float pitch = 1f + 0.5f * (mc.theWorld.rand.nextFloat() - 0.8f); 
		this.mc.theWorld.playSound(
			mc.thePlayer.posX,
			mc.thePlayer.posY, 
			mc.thePlayer.posZ, 
			"vintagecraft:anvil_hit", 
			1f, 
			pitch, 
			false
		);
		
		VintageCraft.packetPipeline.sendToServer(new SoundEffectToServerPacket(0,0,0,"vintagecraft:anvil_hit", pitch));
		
		appliedtechniques.add(technique);
		
		if (appliedtechniques.size() == 10) {
			appliedtechniques.clear();
		}
			
    }
}

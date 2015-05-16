package at.tyron.vintagecraft.Gui;

import java.io.IOException;
import java.util.ArrayList;

import at.tyron.vintagecraft.AnvilRecipes;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ISmithable;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerVessel;
import at.tyron.vintagecraft.Item.ItemIngot;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.Network.AnvilTechniquePacket;
import at.tyron.vintagecraft.Network.SoundEffectToServerPacket;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.TileEntity.TEVessel;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilRecipe;
import at.tyron.vintagecraft.WorldProperties.EnumAnvilTechnique;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
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
	
	//ArrayList<EnumAnvilTechnique> appliedtechniques = new ArrayList<EnumAnvilTechnique>();
	
	//int currentTemplate = -1;
	
	TEAnvil teanvil;
	
	public GuiAnvil(InventoryPlayer player, TEAnvil teanvil) {
		super(new ContainerAnvil(player, teanvil));
		
		this.teanvil = teanvil;
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
        mc.getTextureManager().bindTexture(texture);
        
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        
        drawButtons(x, y, mouseX, mouseY);
        drawTemplates(x, y);
        drawTechniques(x, y);
	}
	
	void drawButtons(int x, int y, int mouseX, int mouseY) {
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
    		
    		if (button.buttonname.equals("anvilassistant")) {
    			this.drawTexturedModalRect(x + button.x + 2, y + button.y + 2 + dy, 200, 35, 8, 8);
    		}
        }
        
	}
	
	void drawTechniques(int x, int y) {     
        EnumAnvilTechnique[] techniques = getAppliedTechniques();
        if (techniques == null) return;
        
        // Draw the last 10 techniques
        int j = 0;
        for (int i = Math.max(0, techniques.length - 10); i < techniques.length; i++) {
        	EnumAnvilTechnique technique = techniques[i];
        	
        	drawTexturedModalRect(x + 6 + 15*j, y + 5, 200 + 13 * (technique.ordinal() % 4), 67 + 13 * (technique.ordinal() >= 4 ? 1 : 0), 13, 13);
        	j++;
        }
	}
	
	
	EnumAnvilTechnique[] getAppliedTechniques() {
        ItemStack itemstack = teanvil.getStackInSlot(0);
        if (itemstack == null || !(itemstack.getItem() instanceof ISmithable)) return null;
        ISmithable smithable = (ISmithable)itemstack.getItem();
        
        return smithable.getAppliedAnvilTechniques(itemstack);
	}
	
	void drawTemplates(int x, int y) {
		if (teanvil.currentTemplate != -1) {
			int start = 0;
			EnumAnvilTechnique[] techniques = getAppliedTechniques();
	        if (techniques != null) start = Math.max(0, techniques.length - 10);
			
			EnumAnvilRecipe recipe = EnumAnvilRecipe.values()[teanvil.currentTemplate];
			int j = 0; 
			for (int i = start; i < recipe.steps.length; i++) {
				drawTexturedModalRect(x + 6 + 15*j, y + 5, 200 + 13 * (recipe.steps[i].ordinal() % 4), 93 + 13 * (recipe.steps[i].ordinal() >= 4 ? 1 : 0), 13, 13);
				j++;
			}
			
			String name = recipe.name().substring(0, 1) + recipe.name().toLowerCase().substring(1).replace('_', ' ');
			if (recipe.ingots > 0) {
				name += "\n" + recipe.ingots + " Ingots";
			}
			if (recipe.plates > 0) {
				name += "\n" + recipe.plates + " Plates";
			}
			
			fontRendererObj.drawSplitString(name, x + 5, y + 20, 50, 0xffffff);
			mc.getTextureManager().bindTexture(texture);
		}
	}

	
	
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
        	mousedown = true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
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
    			
    			if (button.buttonname.equals("anvilassistant")) {
    				teanvil.currentTemplate = ((teanvil.currentTemplate+2) % (EnumAnvilRecipe.values().length+1)) - 1;
    				
    				this.mc.theWorld.playSound(
						mc.thePlayer.posX,
						mc.thePlayer.posY, 
						mc.thePlayer.posZ, 
						"gui.button.press", 
						1f, 
						1f, 
						false
					);
    			}
    			
    			//System.out.println(button.buttonname + " pressed");
    		}
    	}
    	super.mouseReleased(mouseX, mouseY, state);
    }
    
    
    
    void TechniqueHit(EnumAnvilTechnique technique) {
    	ItemStack hammer = inventorySlots.getSlot(3).getStack();
    	if (
    		hammer == null ||
    		!(hammer.getItem() instanceof ItemToolVC) || 
    		((ItemToolVC)hammer.getItem()).tooltype != EnumTool.HAMMER
    	) {
    		return;
    	}
    	
    	
    	float pitch = 1.7f - 0.2f * (technique.ordinal() % 4);
    	
    	if (technique == EnumAnvilTechnique.LIGHTHIT || technique == EnumAnvilTechnique.MEDIUMHIT || technique == EnumAnvilTechnique.HEAVYHIT || technique == EnumAnvilTechnique.DRAW) {
    		pitch /= 2;
    	}
    	
		this.mc.theWorld.playSound(
			mc.thePlayer.posX,
			mc.thePlayer.posY, 
			mc.thePlayer.posZ, 
			"vintagecraft:anvil_hit", 
			1f, 
			pitch, 
			false
		);
		
		
		if (!inventorySlots.getSlot(2).getHasStack()) {
			VintageCraft.packetPipeline.sendToServer(new SoundEffectToServerPacket(0, 0, 0, "vintagecraft:anvil_hit", pitch));
			VintageCraft.packetPipeline.sendToServer(new AnvilTechniquePacket(technique, teanvil.getTier()));
		}
    }
}

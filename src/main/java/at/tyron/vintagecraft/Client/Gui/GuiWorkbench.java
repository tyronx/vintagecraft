package at.tyron.vintagecraft.Client.Gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerCarpenterTable;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TECarpenterTable;
import at.tyron.vintagecraft.TileEntity.TENoGUIInventory;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public abstract class GuiWorkbench extends GuiContainer {
	ArrayList<GuiButtonVC> buttons = new ArrayList<GuiButtonVC>();
	boolean mousedown;
	boolean showSelectorGui = false;
	InventoryPlayer playerInventory;
	TENoGUIInventory te;
	
	
	public GuiWorkbench(InventoryPlayer player, TENoGUIInventory te) {
		super(createContainer(player, te));
		
		this.te = te;
		this.playerInventory = player;
		
		initButtons();
	}
	
	
	
	
	void selectorButtons() {
		buttons.clear();
		
		int w = 18;
		int h = 18;
		int i = 0;
		
		buttons.add(new GuiButtonVC(129, 100, w, w, "clear", this));
		
		for (WorkableRecipeBase recipe : getRecipes()) {
			if (!recipe.shouldDisplayInRecipeHelper()) continue;
			
			int dx = (i % 6) * 20 + 30;
			int dy = ((i / 6) * 20) + 20;
			
			buttons.add(new GuiButtonVC(dx, dy, w, w, recipe.getName(), this, recipe));
			i++;
		}
		
	}
	
	

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (showSelectorGui) {
			drawDefaultBackground();
			drawSelectorGui(mouseX, mouseY);
		} else {
			super.drawScreen(mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawGui(mouseX, mouseY);
	}
	
	
	
	private void drawSelectorGui(int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		bindSelectorGUITexture();
		
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
		
		int i = 0, dx = 0, dy = 0;
		
		
		bindWorkbenchGUITexture();
		
		String name = "";
		boolean over;
		
		for (GuiButtonVC button : buttons) {
			bindWorkbenchGUITexture();
			WorkableRecipeBase recipe = null;
			if (button.data instanceof WorkableRecipeBase) {
				recipe = (WorkableRecipeBase)button.data;
			}
			
			over = button.insideButton(mouseX - x, mouseY - y);
			
			if (mousedown && over) {
        		button.draw(x - 1, y - 1, 200, 125 + 18);
        		dy = 1;
        	} else {
        		button.draw(x - 1, y - 1, 200, 125);
        		dy = 0;
        	}
			
			int posX = button.x + x;
			int posY = button.y + y + dy;
			
			if (recipe != null) {
				drawRecipeOutput(recipe, posX, posY);
				if (over) name = getRecipeText(recipe);				
			}
			
			if (button.buttonname.equals("clear")) {
				drawTexturedModalRect(posX, posY, 218, 125, 18, 18);
				if (over) name = "Clear recipe";
			}
			
			i++;
		}

		
		fontRendererObj.drawSplitString(name, x + 30, y + 100, 90, 0x333333	);
	}


	private void drawRecipeOutput(WorkableRecipeBase recipe, int posX, int posY) {
		if (recipe.getIcon() == null) { System.out.println("Recipe icon for "+recipe.getName()+" is null?"); return; }
		if (recipe.getIcon().getItem() == null) { System.out.println("Recipe item icon for "+recipe.getName()+" is null?"); return; }
		
		GL11.glPushMatrix();
			GlStateManager.enableRescaleNormal();
        	RenderHelper.enableGUIStandardItemLighting();
			Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(
				recipe.getIcon(), posX, posY
			);
	        RenderHelper.disableStandardItemLighting();
	        GlStateManager.disableRescaleNormal();

		GL11.glPopMatrix();
		
		
		if (recipe.getUnlocalizedName().equals("fix_ingot") || recipe.getUnlocalizedName().equals("fix_plate")) {
			bindWorkbenchGUITexture();
			drawTexturedModalRect(posX + 8, posY + 10, 215, 35, 8, 8);				
		}
	}


	void drawGui(int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		bindWorkbenchGUITexture();
        
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
        	
    		if (button.data instanceof EnumWorkableTechnique) {
    			this.drawTexturedModalRect(x + button.x, y + button.y + dy - 1, 200 + 13 * ((i-2) % buttonsPerRow()), 67 + 13 * (i > (buttonsPerRow()+1) ? 1 : 0), 13, 13);
    		}
    		
    		if (button.buttonname.equals("recipehelper")) {
    			this.drawTexturedModalRect(x + button.x + 2, y + button.y + 1 + dy, 200, 35, 9, 8);
    		}
        }
	}
	
	void drawTechniques(int x, int y) {     
		EnumWorkableTechnique[] appliedTechniques = getAppliedTechniques();
        if (appliedTechniques == null) return;
        
        int start = Math.max(0, appliedTechniques.length - 9);
        
        // Draw the last 10 techniques
        int j = 0;
        for (int i = start; i < appliedTechniques.length; i++) {
        	EnumWorkableTechnique technique = appliedTechniques[i];
        	drawTexturedModalRect(x + 6 + 15*j, y + 5, 200 + 13 * (technique.ordinal() % buttonsPerRow()), 67 + 13 * (technique.ordinal() >= buttonsPerRow() ? 1 : 0), 13, 13);
        	j++;
        }
	}
	
	
	
	
	void drawTemplates(int x, int y) {
		if (getCurrentTemplate() != -1) {
			int start = 0;
			EnumWorkableTechnique[] techniques = getAppliedTechniques();
	        if (techniques != null) {
	        	start = Math.max(0, techniques.length - 9);
	        }
			
	        WorkableRecipeBase recipe = getRecipes().get(getCurrentTemplate());
	        
			int j = 0; 
			for (int i = start; i < Math.min(10 + start, recipe.getSteps().length); i++) {
				drawTexturedModalRect(
					x + 6 + 15*j, 
					y + 5, 
					200 + 13 * (recipe.getSteps()[i].ordinal() % buttonsPerRow()), 
					93 + 13 * (recipe.getSteps()[i].ordinal() >= buttonsPerRow() ? 1 : 0), 
					13, 13
				);
				j++;
			}
			
			GL11.glPushMatrix();
				GL11.glTranslatef(x+5, y+22, 0);
				GL11.glScalef(0.7f, 0.7f, 0.7f);
				
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
				GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST) ;
				fontRendererObj.drawSplitString(getRecipeText(recipe), 0, 0, 85, 0xffffff);
				GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
				GL11.glDisable(GL11.GL_BLEND);
				
			GL11.glPopMatrix();
			
			bindWorkbenchGUITexture();
		}
	}

	
	
    public abstract EnumWorkableTechnique[] getAppliedTechniques();




	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	
        if (mouseButton == 0) {
        	mousedown = true;
        }
        if (!showSelectorGui) {
        	super.mouseClicked(mouseX, mouseY, mouseButton);
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
    			
    			if (button.data instanceof EnumWorkableTechnique) {
    				TechniqueHit((EnumWorkableTechnique)button.data);
    			}
    			
    			if (button.data instanceof WorkableRecipeBase) {
    				setCurrentTemplate(((WorkableRecipeBase)button.data).getOrdinal());
    				toggleRecipeHelper(false);
    				button.clickSound();
    				return;
    			}
    			
    			if (button.buttonname.equals("clear")) {
    				setCurrentTemplate(-1);
    				toggleRecipeHelper(false);
    				button.clickSound();
    				return;
    			}
    			
    			if (button.buttonname.equals("recipehelper")) {
    				toggleRecipeHelper(true);
    				button.clickSound();
    				return;
    			}
    		}
    	}
    	
    	if (!showSelectorGui) {
    		super.mouseReleased(mouseX, mouseY, state);
    	}
    }
    
    
    void toggleRecipeHelper(boolean recipeHelper) {
    	if (recipeHelper) {
    		selectorButtons();
    		showSelectorGui = true;
    	} else {
    		initButtons();
    		showSelectorGui = false;
    	}
    }
    
    
	String getRecipeText(WorkableRecipeBase recipe) {
		return recipe.getName() + "\n" + recipe.getRecipeText();
	}
	
	// Cannot make this non-static => cannot make it inheritable :/
	static Container createContainer(InventoryPlayer player, TENoGUIInventory te) {
		if (te instanceof TEAnvil) {
			return new ContainerAnvil(player, (TEAnvil)te);
		}
		if (te instanceof TECarpenterTable) {
			return new ContainerCarpenterTable(player, (TECarpenterTable)te);
		}
		return null;
	}

	
	int buttonsPerRow() {
		return 4;
	}
	

	abstract ArrayList<WorkableRecipeBase> getRecipes();

	abstract void initButtons();

	abstract void TechniqueHit(EnumWorkableTechnique technique);
	
	abstract int getCurrentTemplate();
	
	abstract void setCurrentTemplate(int template);
	
	abstract void bindWorkbenchGUITexture();
	
	abstract void bindSelectorGUITexture();
}

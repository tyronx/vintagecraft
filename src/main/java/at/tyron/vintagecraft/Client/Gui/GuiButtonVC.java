package at.tyron.vintagecraft.Client.Gui;

import net.minecraft.client.gui.GuiScreen;

public class GuiButtonVC {
	int x;
	int y;
	int width;
	int height;
	
	String buttonname;
	Object data;
	
	GuiScreen screen;
	
	public GuiButtonVC(int x, int y, int width, int height, String buttonname, GuiScreen screen, Object data) {
		this(x, y, width, height, buttonname, screen);
		this.data = data;
		
	}
	
	public GuiButtonVC(int x, int y, int width, int height, String buttonname, GuiScreen screen) {
		this.x = x;
		this.y = y;
		this.width = width; 
		this.height = height;
		this.buttonname = buttonname;
		this.screen = screen;
	}
	
	public void draw(int refx, int refy, int texturex, int texturey) {
		screen.drawTexturedModalRect(refx + x, refy + y, texturex, texturey, width, height);
	}
	
	public boolean insideButton(int x, int y) {
		return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + height;
	}
	
	public void clickSound() {
		screen.mc.theWorld.playSound(
			screen.mc.thePlayer.posX,
			screen.mc.thePlayer.posY, 
			screen.mc.thePlayer.posZ, 
			"gui.button.press", 
			1f, 
			1f, 
			false
		);
	}
}

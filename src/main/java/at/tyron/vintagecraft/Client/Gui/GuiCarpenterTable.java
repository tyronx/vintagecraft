package at.tyron.vintagecraft.Client.Gui;

import java.util.ArrayList;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.Item.IItemWoodWorkable;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.Network.CarpentryTechniquePacket;
import at.tyron.vintagecraft.Network.SoundEffectToServerPacket;
import at.tyron.vintagecraft.TileEntity.TECarpenterTable;
import at.tyron.vintagecraft.TileEntity.TENoGUIInventory;
import at.tyron.vintagecraft.World.Crafting.EnumWoodWorkingTechnique;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeManager;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiCarpenterTable extends GuiWorkbench {
	private static final ResourceLocation textureCarpenterTable = new ResourceLocation("vintagecraft:textures/gui/container/gui_carpentertable.png");
	private static final ResourceLocation textureSelector = new ResourceLocation("vintagecraft:textures/gui/container/gui_selector.png");

	public GuiCarpenterTable(InventoryPlayer player, TENoGUIInventory te) {
		super(player, te);
	}
	
	public EnumWorkableTechnique[] getAppliedTechniques() {
        ItemStack itemstack = te.getStackInSlot(0);
        if (itemstack == null || !(itemstack.getItem() instanceof IItemWoodWorkable)) return null;
        IItemWoodWorkable woodworkable = (IItemWoodWorkable)itemstack.getItem();
        return woodworkable.getAppliedTechniques(itemstack);
	}


	@Override
	void initButtons() {
		int w = 13;
		int spacing = 14; 
		int basex = 69;
		int basey = 22;
		
        buttons.clear();
		buttons.add(new GuiButtonVC(156, 5, w, w, "recipehelper", this));
		
		buttons.add(new GuiButtonVC(basex, basey, w, w, "split", this, EnumWoodWorkingTechnique.SPLIT));
		buttons.add(new GuiButtonVC(basex + spacing, basey, w, w, "carve", this, EnumWoodWorkingTechnique.CARVE));
		buttons.add(new GuiButtonVC(basex + 2*spacing, basey, w, w, "drill", this, EnumWoodWorkingTechnique.DRILL));
		buttons.add(new GuiButtonVC(basex, basey + spacing, w, w, "plane", this, EnumWoodWorkingTechnique.PLANE));
		buttons.add(new GuiButtonVC(basex + spacing, basey + spacing, w, w, "saw", this, EnumWoodWorkingTechnique.SAW));
		buttons.add(new GuiButtonVC(basex + 2*spacing, basey + spacing, w, w, "join", this, EnumWoodWorkingTechnique.JOIN));
	}

	@Override
	void TechniqueHit(EnumWorkableTechnique technique) {
    	ItemStack hammer = inventorySlots.getSlot(3).getStack();
    	if (
    		hammer == null ||
    		!(hammer.getItem() instanceof ItemToolVC) || 
    		((ItemToolVC)hammer.getItem()).tooltype != EnumTool.CARPENTERSTOOLSET
    	) {
    		return;
    	}
    	
    	EnumWoodWorkingTechnique carpentrytechnique = (EnumWoodWorkingTechnique)technique;
    	
    	
		this.mc.theWorld.playSound(
			mc.thePlayer.posX,
			mc.thePlayer.posY, 
			mc.thePlayer.posZ, 
			carpentrytechnique.getSoundName(), 
			1f, 
			1f, 
			false
		);
		
		
		
		if (!inventorySlots.getSlot(2).getHasStack()) {
			VintageCraft.packetPipeline.sendToServer(new SoundEffectToServerPacket(0, 0, 0, carpentrytechnique.getSoundName(), 1f));
			VintageCraft.packetPipeline.sendToServer(new CarpentryTechniquePacket((EnumWoodWorkingTechnique) technique));
		}
	}

	@Override
	int getCurrentTemplate() {
		return ((TECarpenterTable)te).currentTemplate;
	}

	@Override
	void setCurrentTemplate(int template) {
		((TECarpenterTable)te).currentTemplate = template;
	}

	@Override
	void bindWorkbenchGUITexture() {
		mc.getTextureManager().bindTexture(textureCarpenterTable);
	}

	@Override
	void bindSelectorGUITexture() {
		mc.getTextureManager().bindTexture(textureSelector);
	}

	@Override
	ArrayList<WorkableRecipeBase> getRecipes() {
		return WorkableRecipeManager.carpentry.getRecipes();
	}
	
	@Override
	int buttonsPerRow() {
		return 3;
	}

}

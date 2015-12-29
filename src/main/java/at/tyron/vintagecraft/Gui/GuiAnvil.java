package at.tyron.vintagecraft.Gui;

import java.util.ArrayList;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Item.ItemToolVC;
import at.tyron.vintagecraft.Network.AnvilTechniquePacket;
import at.tyron.vintagecraft.Network.SoundEffectToServerPacket;
import at.tyron.vintagecraft.TileEntity.TEAnvil;
import at.tyron.vintagecraft.TileEntity.TENoGUIInventory;
import at.tyron.vintagecraft.World.Crafting.EnumAnvilTechnique;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeBase;
import at.tyron.vintagecraft.World.Crafting.WorkableRecipeManager;
import at.tyron.vintagecraft.WorldProperties.EnumTool;
import at.tyron.vintagecraft.WorldProperties.EnumWorkableTechnique;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiAnvil extends GuiWorkbench {

	private static final ResourceLocation textureAnvil = new ResourceLocation("vintagecraft:textures/gui/container/gui_anvil.png");
	private static final ResourceLocation textureSelector = new ResourceLocation("vintagecraft:textures/gui/container/gui_selector.png");
	

	public GuiAnvil(InventoryPlayer player, TENoGUIInventory te) {
		super(player, te);
	}
	
	public EnumWorkableTechnique[] getAppliedTechniques() {
        ItemStack itemstack = te.getStackInSlot(0);
        if (itemstack == null || !(itemstack.getItem() instanceof IItemSmithable)) return null;
        IItemSmithable smithable = (IItemSmithable)itemstack.getItem();
        
        return smithable.getAppliedTechniques(itemstack);
	}

	
	
	@Override
	void initButtons() {
		int w = 13;
		int spacing = 14; 
		int basex = 56;
		int basey = 22;
		
        buttons.clear();
		buttons.add(new GuiButtonVC(156, 5, w, w, "recipehelper", this));
		
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
    void TechniqueHit(EnumWorkableTechnique technique) {
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
			VintageCraft.packetPipeline.sendToServer(new AnvilTechniquePacket((EnumAnvilTechnique)technique, ((TEAnvil)te).getTier()));
		}
    }
    
	String getRecipeText(WorkableRecipeBase recipe) {
		return recipe.getCommandSenderName() + "\n" + recipe.getRecipeText();
	}

	
	@Override
	int getCurrentTemplate() {
		return ((TEAnvil)te).currentTemplate;
	}
	
	@Override
	void setCurrentTemplate(int template) {
		((TEAnvil)te).currentTemplate = template;	
	}


	@Override
	void bindWorkbenchGUITexture() {
		mc.getTextureManager().bindTexture(textureAnvil);
	}


	@Override
	void bindSelectorGUITexture() {
		mc.getTextureManager().bindTexture(textureSelector);
	}


	@Override
	ArrayList<WorkableRecipeBase> getRecipes() {
		return WorkableRecipeManager.smithing.getRecipes();
	}



	
}

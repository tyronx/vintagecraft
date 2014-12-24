package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.World.EnumRockType;
import at.tyron.vintagecraft.World.IEnumState;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class VCBlock extends Block {
	public boolean isSoil = false;
	
	protected VCBlock(Material materialIn) {
		super(materialIn);
	}

	
	public VCBlock registerMultiState(String name, Class<? extends ItemBlock> itemclass, String folderprefix, IEnumState []types) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		for (int i = 0; i < types.length; i++) {
			IEnumState enumstate = types[i]; 
			
			ModelBakery.addVariantName(Item.getItemFromBlock(this), "vintagecraft:" + folderprefix + "/" + enumstate.getName());
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), enumstate.getMetaData(), new ModelResourceLocation("vintagecraft:" + folderprefix + "/" + enumstate.getName(), "inventory"));
		}
		
		return this;
	}
	
	
	public VCBlock registerSingleState(String name, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), 0, new ModelResourceLocation("vintagecraft:" + name, "inventory"));
		ModelBakery.addVariantName(Item.getItemFromBlock(this), "vintagecraft:" + name);
		
		return this;
	}
	
	
	
	@Override
	public VCBlock setHardness(float hardness) {
		return (VCBlock) super.setHardness(hardness);
	}

	
	@Override
	public VCBlock setStepSound(SoundType sound) {
		return (VCBlock) super.setStepSound(sound);
	}
	

	@Override
	public VCBlock setBlockUnbreakable() {
		return (VCBlock)super.setBlockUnbreakable();
	}
    
    @Override
    public VCBlock setResistance(float resistance) {
    	return (VCBlock)super.setResistance(resistance);
    }
    
    
	
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
	
    
}

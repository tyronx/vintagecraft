package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.interfaces.IEnumState;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockVC extends Block {
	public boolean isSoil = false;
	
	protected BlockVC(Material materialIn) {
		super(materialIn);
	}

	
	public BlockVC registerMultiState(String name, Class<? extends ItemBlock> itemclass, String folderprefix, IEnumState []types) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		for (int i = 0; i < types.length; i++) {
			IEnumState enumstate = types[i]; 
			
			ModelBakery.addVariantName(Item.getItemFromBlock(this), "vintagecraft:" + folderprefix + "/" + enumstate.getStateName());
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), enumstate.getMetaData(), new ModelResourceLocation("vintagecraft:" + folderprefix + "/" + enumstate.getStateName(), "inventory"));
		}
		
		return this;
	}
	
	
	public BlockVC registerSingleState(String name, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), 0, new ModelResourceLocation("vintagecraft:" + name, "inventory"));
		ModelBakery.addVariantName(Item.getItemFromBlock(this), "vintagecraft:" + name);
		
		return this;
	}
	
	
	
	@Override
	public BlockVC setHardness(float hardness) {
		return (BlockVC) super.setHardness(hardness);
	}

	
	@Override
	public BlockVC setStepSound(SoundType sound) {
		return (BlockVC) super.setStepSound(sound);
	}
	

	@Override
	public BlockVC setBlockUnbreakable() {
		return (BlockVC)super.setBlockUnbreakable();
	}
    
    @Override
    public BlockVC setResistance(float resistance) {
    	return (BlockVC)super.setResistance(resistance);
    }
    
    
	
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
	
    
}

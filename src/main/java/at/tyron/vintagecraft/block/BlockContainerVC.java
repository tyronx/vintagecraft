package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockContainerVC extends BlockContainer implements ISubtypeFromStackPovider {
	BlockClassEntry[] subtypes;

	protected BlockContainerVC(Material materialIn) {
		super(materialIn);
	}
	
	public BlockClassEntry[] getSubTypes() {
		return subtypes;
	}
	
	
	public BlockContainerVC registerSingleState(String name, Class<? extends ItemBlock> itemclass) {
		if (itemclass == null) {
			GameRegistry.registerBlock(this, name);
		} else {
			GameRegistry.registerBlock(this, itemclass, name);
			VintageCraft.instance.proxy.registerItemBlockTexture(this, name);
		}
		
		setUnlocalizedName(name);
		return this;
	}

	
	public BlockContainerVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState []types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}
	
	public BlockContainerVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState []types, String folderprefix) {
		System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IEnumState enumstate = types[i]; 
			
			VintageCraft.instance.proxy.registerItemBlockTexture(this, folderprefix, enumstate.getStateName(), enumstate.getMetaData(this));
		}
		
		return this;
	}


	@Override
	public Block setHardness(float hardness) {
		return (Block) super.setHardness(hardness);
	}

	
	@Override
	public Block setStepSound(SoundType sound) {
		return (Block) super.setStepSound(sound);
	}
	

	@Override
	public Block setBlockUnbreakable() {
		return (Block)super.setBlockUnbreakable();
	}
    
    @Override
    public Block setResistance(float resistance) {
    	return (Block)super.setResistance(resistance);
    }
    
    
	
    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }
	
    
    @Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    	return true;
    }
    
}

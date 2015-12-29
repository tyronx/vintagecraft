package at.tyron.vintagecraft.Block.Organic;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.World.BlocksVC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFenceGateVC extends BlockFenceGate implements IMultiblock {
	public BlockFenceGateVC() {
		super(EnumType.OAK);
	}


	public PropertyBlockClass TREETYPE;
	BlockClassEntry[] subtypes;
	
	public int multistateAvailableTypes() {
		return 1;
	}
	

	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
		setCreativeTab(VintageCraft.craftedBlocksTab);
	}

	@Override
	public IProperty getTypeProperty() {
		return TREETYPE;
	}

	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		this.TREETYPE = property;		
	}

	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.fence;
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum[] types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}

	@Override
	public Block registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum[] types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IStateEnum enumstate = types[i]; 
			
			VintageCraft.instance.proxy.registerItemBlockTexture(this, folderprefix, enumstate.getStateName(), enumstate.getMetaData(this));
		}
		
		VintageCraft.instance.proxy.ignoreProperties(this, new IProperty[] { POWERED });
		
		return this;
	}
	

    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return new BlockState(this, new IProperty[] {FACING, OPEN, POWERED, IN_WALL});
    	}
    	
    	return new BlockState(this, new IProperty[] {FACING, OPEN, POWERED, IN_WALL, getTypeProperty()});
    }
    
    


}

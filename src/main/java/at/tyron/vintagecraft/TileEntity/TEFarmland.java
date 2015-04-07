package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.Block.BlockFarmlandVC;
import at.tyron.vintagecraft.Block.BlockOreVC;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TEFarmland extends TileEntity {
	private IExtendedBlockState state;

	int fertility;
	
    public IExtendedBlockState getState() {
        if(state == null) {
        	state = (IExtendedBlockState)getBlockType().getDefaultState();
        	state = state.withProperty(BlockFarmlandVC.fertilityExact, fertility);
        }
        return state;
    }

    public void setState(IExtendedBlockState state) {
        this.state = state;
    }
    
    
    public int getFertility() {
		return fertility;
	}
    
    public void setFertility(int fertility) {
		this.fertility = fertility;
	}

}

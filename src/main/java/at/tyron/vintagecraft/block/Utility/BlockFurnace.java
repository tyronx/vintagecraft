package at.tyron.vintagecraft.Block.Utility;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.TileEntity.TEHeatSourceWithGUI;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumStrongHeatSource;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFurnace extends BlockStove {
	public PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	public BlockFurnace(boolean burning) {
		super(burning);
	}
	
	
	public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TEHeatSourceWithGUI(EnumStrongHeatSource.STOVE);
    }
    
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {
        return null; //Item.getItemFromBlock(BlocksVC.furnace);
    }
    
    public Block getLitVersion() {
    	return null; //BlocksVC.furnace_lit;
    }

    public Block getExtinguishedVersion() {
    	return null;// BlocksVC.furnace;
    }    

}

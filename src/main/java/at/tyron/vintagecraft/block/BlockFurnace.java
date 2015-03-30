package at.tyron.vintagecraft.block;

import at.tyron.vintagecraft.TileEntity.TileEntityStove;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.EnumFurnace;
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
        return new TileEntityStove(EnumFurnace.FURNACE);
    }
    
    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos) {
        return Item.getItemFromBlock(BlocksVC.furnace);
    }
    
    public Block getLitVersion() {
    	return BlocksVC.furnace_lit;
    }

    public Block getExtinguishedVersion() {
    	return BlocksVC.furnace;
    }    

}

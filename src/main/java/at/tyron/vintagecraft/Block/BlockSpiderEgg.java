package at.tyron.vintagecraft.Block;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Interfaces.ICategorizedBlockOrItem;
import at.tyron.vintagecraft.TileEntity.TileEntityForestSpiderSpawner;
import at.tyron.vintagecraft.WorldProperties.EnumObjectCategory;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSpiderEgg extends BlockMobSpawner implements ICategorizedBlockOrItem {
	public BlockSpiderEgg() {
		super();
		setCreativeTab(VintageCraft.terrainTab);
	}
	
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityForestSpiderSpawner();
    }
    
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("VCForestSpider");
        }
	}

	@Override
	public EnumObjectCategory getCategory() {
		return EnumObjectCategory.Misc;
	}
}

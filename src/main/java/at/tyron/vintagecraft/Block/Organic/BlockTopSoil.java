package at.tyron.vintagecraft.Block.Organic;

import java.util.List;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.TileEntity.TEFarmland;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumFertility;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumTree;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTopSoil extends BlockVC implements IBlockSoil {
	public static final PropertyEnum organicLayer = PropertyEnum.create("organiclayer", EnumOrganicLayer.class);
	public static final PropertyEnum fertility = PropertyEnum.create("fertility", EnumFertility.class, EnumFertility.valuesForTopsoil());


	
	public BlockTopSoil() {
		super(Material.grass);
		this.setDefaultState(this.blockState.getBaseState().withProperty(organicLayer, EnumOrganicLayer.NORMALGRASS).withProperty(fertility, EnumFertility.MEDIUM));
		this.setTickRandomly(true);
		setCreativeTab(VintageCraft.floraTab);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    	for (EnumOrganicLayer organiclayer : EnumOrganicLayer.values()) {
    		for (EnumFertility fertility : EnumFertility.valuesForTopsoil()) {
    			list.add(new ItemStack(itemIn, 1, organiclayer.getMetaData(this) + (fertility.getMetaData(this) << 2)));
    		}
    	}
    }
    
	

	
	
   
    
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	ItemStack itemstack = new ItemStack(Item.getItemFromBlock(this));
    	itemstack.setItemDamage(((EnumFertility)state.getValue(fertility)).getMetaData(this) << 2);
        ret.add(itemstack);
        
    	return ret;
    }

    
    
    


    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {organicLayer, fertility});
    }
    
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return 
        	((EnumOrganicLayer)state.getValue(organicLayer)).getMetaData(this)
        	+ 
        	(((EnumFertility)state.getValue(fertility)).getMetaData(this) << 2);
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	/*System.out.println("convert from meta " + meta);
    	System.out.println("convert from meta fertility " + ((meta >> 2) & 3));*/
    	return 
    		this.blockState.getBaseState()
    			.withProperty(organicLayer, EnumOrganicLayer.fromMeta(meta & 3))
    			.withProperty(fertility, EnumFertility.fromMeta((meta >> 2) & 3))
    	;
    }

    
    
    
    
    
	@Override
	public boolean canSpreadGrass(World world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canGrowGrass(World world, BlockPos pos) {
		return true;
	}
	
	@Override
	public boolean canGrowTallGrass(World world, BlockPos pos) {
		return true;
	}


	@Override
	public EnumFertility getFertility(World world, BlockPos pos) {
		return (EnumFertility)world.getBlockState(pos).getValue(fertility);
	}

	@Override
	public EnumOrganicLayer getOrganicLayer(World world, BlockPos pos) {
		return (EnumOrganicLayer)world.getBlockState(pos).getValue(organicLayer);
	}
	
	@Override
	public void setOrganicLayer(EnumOrganicLayer layer, World world, BlockPos pos) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(organicLayer, layer));		
	}


	@Override
	public boolean canGrowTree(World world, BlockPos pos, EnumTree tree) {
		return true;
	}

	
	@Override
	public void hoeUsed(UseHoeEvent event) {
		EnumFertility fertility = getFertility(event.world, event.pos);
		
		IBlockState newState = BlocksVC.farmland.getDefaultState().withProperty(BlockFarmlandVC.fertility, fertility);
		
		event.world.playSoundEffect((double)((float)event.entityPlayer.posX + 0.5F), (double)((float)event.entityPlayer.posY + 0.5F), (double)((float)event.entityPlayer.posZ + 0.5F), newState.getBlock().stepSound.getStepSound(), (newState.getBlock().stepSound.getVolume() + 1.0F) / 2.0F, newState.getBlock().stepSound.getFrequency() * 0.8F);
		
		event.world.setBlockState(event.pos, newState);
		TEFarmland tileentity = (TEFarmland)event.world.getTileEntity(event.pos);
		if (tileentity != null) {
			tileentity.setFertility(fertility.getAsNumber());			
		} else {
			System.out.println("tileentity was not created?");
		}
		
		event.setResult(Result.ALLOW);
		
		event.entityPlayer.addExhaustion(0.1f);
		
		
		super.hoeUsed(event);
	}
}


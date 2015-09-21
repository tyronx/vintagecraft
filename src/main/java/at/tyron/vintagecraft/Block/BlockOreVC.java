package at.tyron.vintagecraft.Block;

import java.util.List;
import java.util.Locale;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BaseBlockClass;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.BlockClass.OreInRockClassEntry;
import at.tyron.vintagecraft.BlockClass.PropertyBlockClass;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Item.ItemOreVC;
import at.tyron.vintagecraft.Item.ItemStone;
import at.tyron.vintagecraft.World.BlocksVC;
//import at.tyron.vintagecraft.TileEntity.TEOre;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOreType;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOreVC extends BlockVC implements IMultiblock {
	public PropertyBlockClass OREANDROCKTYPE;
	
	public BlockOreVC() {
		super(Material.iron);
		setCreativeTab(VintageCraft.terrainTab);
		this.setDefaultState(this.blockState.getBaseState());
	}

	
	public void init(BlockClassEntry []subtypes, PropertyBlockClass property) {
		this.subtypes = subtypes;
		setTypeProperty(property);
		
		blockState = this.createBlockState();
	
		setDefaultState(subtypes[0].getBlockState(blockState.getBaseState(), getTypeProperty()));
	}
	
	
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		for (BlockClassEntry entry : subtypes) {
			list.add(entry.getItemStack());
		}
		super.getSubBlocks(itemIn, tab, list);
	}
	
	
    
    @Override
    protected BlockState createBlockState() {
    	if (getTypeProperty() == null) {
    		return new BlockState(this, new IProperty[0]);
    	}
    	
        return new BlockState(this, new IProperty[] {getTypeProperty()});
    }
    
    
    
    public IBlockState getStateFromMeta(int meta) {
    	return getBlockClass().getEntryFromMeta(this, meta).getBlockState();
    }


    public int getMetaFromState(IBlockState state) {
    	return getBlockClass().getMetaFromState(state);
    }
 
    
    public static EnumOreType getOreType(IBlockState state) {
    	String[] type = ((OreInRockClassEntry)state.getValue(((IMultiblock)BlocksVC.rawore.getEntryFromState(state).block).getTypeProperty())).getName().split("-");
    	return EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));
    }

    
    public static EnumRockType getRockType(IBlockState state) {
    	String[] type = ((OreInRockClassEntry)state.getValue(((IMultiblock)BlocksVC.rawore.getEntryFromState(state).block).getTypeProperty())).getName().split("-");
    	return EnumRockType.valueOf(type[1].toUpperCase(Locale.ROOT));
    }

    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    	java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
    	
    	World worldIn = (World)world;
    	
     	String[] type = ((OreInRockClassEntry)state.getValue(OREANDROCKTYPE)).getName().split("-");
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase(Locale.ROOT));
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));
     	

     	ItemStack itemstack;
     	if (worldIn.rand.nextInt(2) > 0) {
	     	ret.add(ItemStone.setRockType(new ItemStack(ItemsVC.stone, 1), rocktype));
     	}
        
        switch (oretype) {
        		
        	case OLIVINE:
        		itemstack = new ItemStack(ItemsVC.stone, 1);
    	        ItemStone.setRockType(itemstack, rocktype);
    	        spawnAsEntity(worldIn, pos, itemstack);
    	        break;
    	        
    	    
        	case QUARTZ:
        		if (worldIn.rand.nextInt(10) == 0) {
            		itemstack = new ItemStack(ItemsVC.ore, 1 + (worldIn.rand.nextInt(7) == 0 ? 1 : 0));
    	        	ItemOreVC.setOreType(itemstack, EnumOreType.QUARTZCRYSTAL);
    	        	ret.add(itemstack);
        		}
	        	
        		
        	default: 
	        	itemstack = new ItemStack(ItemsVC.ore, 1 + (worldIn.rand.nextInt(7) == 0 ? 1 : 0));
	        	ItemOreVC.setOreType(itemstack, oretype);
	        	
	        	if (oretype == EnumOreType.BITUMINOUSCOAL || oretype == EnumOreType.SYLVITE_ROCKSALT || oretype == EnumOreType.LIMONITE) {
	        		itemstack.stackSize++;
	        	}
	        	
	        	ret.add(itemstack);
	        	break;
        }
        
        //spawnAsEntity(worldIn, pos, itemstack);
        
    	
    	//super.breakBlock(worldIn, pos, state);
        return ret;
    }




	@Override
	public IProperty getTypeProperty() {
		return OREANDROCKTYPE;
	}


	@Override
	public void setTypeProperty(PropertyBlockClass property) {
		OREANDROCKTYPE = property;
	}


	@Override
	public BaseBlockClass getBlockClass() {
		return BlocksVC.rawore;
	}


	@Override
	public int multistateAvailableTypes() {
		return 16;
	}
	
	
    public int getHarvestLevel(IBlockState state) {
     	String[] type = ((OreInRockClassEntry)state.getValue(OREANDROCKTYPE)).getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));
     	
        return oretype.getHarvestlevel();
    }


    
    public float getBlockHardnessMultiplier(IBlockState state) {
    	if (!BlocksVC.rawore.containsBlock(state.getBlock())) return 1f;
    	
     	String[] type = ((OreInRockClassEntry)state.getValue(OREANDROCKTYPE)).getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));
     	EnumRockType rocktype = EnumRockType.valueOf(type[1].toUpperCase(Locale.ROOT));
     	
        return oretype.hardnessmultiplier * rocktype.getHardnessMultiplier();
    }

    
	public int getHarvestLevelVC(IBlockState state) {
    	if (!BlocksVC.rawore.containsBlock(state.getBlock())) return 1;
    	
     	String[] type = ((OreInRockClassEntry)state.getValue(OREANDROCKTYPE)).getName().split("-");
     	EnumOreType oretype = EnumOreType.valueOf(type[0].toUpperCase(Locale.ROOT));

     	return oretype.harvestlevel;
	}
	
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		player.addExhaustion(0.025F); // Make player hungry five times as much
		
		super.harvestBlock(worldIn, player, pos, state, te);
	}

    public float getExplosionResistance(Entity exploder) {
        return this.blockResistance / 12.0F;
    }

}

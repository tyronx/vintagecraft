package at.tyron.vintagecraft.block;

import java.util.Random;

import com.google.common.util.concurrent.Service.State;

import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.interfaces.IEnumState;
import at.tyron.vintagecraft.interfaces.ISoil;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockVC extends Block implements ISubtypeFromStackPovider {
	public static int multistateAvailableTypes() {
		return 16;
	}
	
	BlockClassEntry[] subtypes;
	
	protected BlockVC(Material materialIn) {
		super(materialIn);
	}
	
	public BlockVC(IEnumState[] states) {
		super (Material.air);
	}
	
	
	public BlockClassEntry[] getSubTypes() {
		return subtypes;
	}
	
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState []types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}
	
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IEnumState []types, String folderprefix) {
		System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IEnumState enumstate = types[i]; 
			
			VintageCraft.instance.proxy.registerItemBlockTexture(this, folderprefix, enumstate.getStateName(), enumstate.getMetaData(this));
		}
		
		return this;
	}

	
	
	public BlockVC registerSingleState(String name, Class<? extends ItemBlock> itemclass) {
		GameRegistry.registerBlock(this, itemclass, name);
		setUnlocalizedName(name);
		
		VintageCraft.instance.proxy.registerItemBlockTexture(this, name);
		
		
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
	
    
    public void hoeUsed(UseHoeEvent event) {
    
    }
    
    
    
    /**** Soil blocks ****/
    
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	if (worldIn.isRemote) return;
    	soilUpdate(worldIn, pos, state, rand);
    }
    
    
    void soilUpdate(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	if (! (this instanceof ISoil)) return;
    	
    	ISoil soil = (ISoil)this;    	
    	
    	// Up or downgrade grass depending on light conditions
    	IProperty property = soil.getOrganicLayerProperty(worldIn, pos);
    	EnumOrganicLayer organiclayer = (EnumOrganicLayer)state.getValue(property);
    	
    	
    	
    	if (organiclayer != EnumOrganicLayer.NoGrass) {
    		BlockPos above = pos.up();
    		int light = Math.max(worldIn.getLight(above), Math.max(Math.max(Math.max(worldIn.getLight(above.north()), worldIn.getLight(above.south())), worldIn.getLight(above.east())), worldIn.getLight(above.west())));
    		
    		EnumOrganicLayer adjustedorganiclayer = organiclayer.adjustToEnviroment(light, VCraftWorld.instance.getRainfall(pos), VCraftWorld.instance.getTemperature(pos));
    		
    		if (adjustedorganiclayer != organiclayer) {
    			worldIn.setBlockState(pos, state.withProperty(property, adjustedorganiclayer));
    			return;
    		}
    		
    		
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    Block block = worldIn.getBlockState(blockpos1.up()).getBlock();
                    IBlockState neighbourblockstate = worldIn.getBlockState(blockpos1);

                    if (neighbourblockstate.getBlock() instanceof ISoil && ((ISoil)neighbourblockstate.getBlock()).canGrowGrass(worldIn, pos)) {
                    	IProperty neighbourproperty = soil.getOrganicLayerProperty(worldIn, pos);
                    	EnumOrganicLayer neighbourorganiclayer = (EnumOrganicLayer)state.getValue(neighbourproperty);
                    	

                        if (worldIn.getLight(blockpos1.up()) >= neighbourorganiclayer.minblocklight 
                            && block.getLightOpacity(worldIn, blockpos1.up()) <= 2
                            && neighbourorganiclayer == EnumOrganicLayer.NoGrass) {
                        	
                        	worldIn.setBlockState(blockpos1, neighbourblockstate.withProperty(neighbourproperty, EnumOrganicLayer.VerySparseGrass));
                        }

                    }
                    
                }
            }
        }
    }
    
    
    
	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
		if (this instanceof ISoil) {
			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
		} else {
			return 16777215;
		}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state) {
		if (this instanceof ISoil) {
			return this.getBlockColor();
		} else {
			return 16777215;
		}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		if (this instanceof ISoil) {
			//return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
			return VCraftWorld.instance.getGrassColorAtPos(pos);
		} else {
			return 16777215;
		}
    }

	
	
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
    	if (this instanceof ISoil) {
    		return EnumWorldBlockLayer.CUTOUT_MIPPED;
    	} else {
    		return EnumWorldBlockLayer.SOLID;
    	}
    }
    
   
    

    @Override
    public String getSubType(ItemStack stack) {
    	return null;
    }

    
}

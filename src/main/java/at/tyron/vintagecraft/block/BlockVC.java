package at.tyron.vintagecraft.Block;

import java.util.Random;

import com.google.common.util.concurrent.Service.State;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.BlockClass.BlockClassEntry;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.Interfaces.ISubtypeFromStackPovider;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldProperties.*;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumOrganicLayer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
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
	protected BlockClassEntry[] subtypes;
	
	protected BlockVC(Material materialIn) {
		super(materialIn);
	}
	
	public BlockVC(IStateEnum[] states) {
		super (Material.air);
	}
	
	
	public BlockClassEntry[] getSubTypes() {
		return subtypes;
	}
	
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types) {
		return registerMultiState(blockclassname, itemclass, types, blockclassname);
	}
	
	public BlockVC registerMultiState(String blockclassname, Class<? extends ItemBlock> itemclass, IStateEnum []types, String folderprefix) {
		if (VintageCraftConfig.debugBlockRegistration) System.out.println("register block " + this);
		GameRegistry.registerBlock(this, itemclass, blockclassname);
		setUnlocalizedName(blockclassname);
		
		for (int i = 0; i < types.length; i++) {
			IStateEnum enumstate = types[i]; 
			
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
    
    // 1 = max speed, 0 = doesn't grow
    public float grassGrowthSpeed() {
    	return 1f;
    }
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	if (worldIn.isRemote) return;
    	soilUpdate(worldIn, pos, state, rand);
    }
    
    
    void soilUpdate(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    	if (! (this instanceof IBlockSoil)) return;
    	
    	
    	
    	IBlockSoil soil = (IBlockSoil)this;    	
    	
    	// Up or downgrade grass depending on light conditions
    	EnumOrganicLayer organiclayer = soil.getOrganicLayer(worldIn, pos);
    	
    	if (organiclayer != EnumOrganicLayer.NOGRASS && rand.nextFloat() <= grassGrowthSpeed()) {
    		BlockPos above = pos.up();

    		if (!worldIn.isAreaLoaded(pos.add(-1, 0, -1), pos.add(1, 1, 1))) return;
    		
    		int light = Math.max(worldIn.getLight(above), Math.max(Math.max(Math.max(worldIn.getLight(above.north()), worldIn.getLight(above.south())), worldIn.getLight(above.east())), worldIn.getLight(above.west())));
    		
    		EnumOrganicLayer adjustedorganiclayer = organiclayer.adjustToEnviroment(light, VCraftWorld.instance.getRainfall(pos), VCraftWorld.instance.getTemperature(pos));
    		
    		if (adjustedorganiclayer != organiclayer) {
    			//System.out.println(organiclayer + " => " + adjustedorganiclayer + " @ " + pos);
    			soil.setOrganicLayer(adjustedorganiclayer, worldIn, pos);
    			return;
    		}
    		
    		// Spread grass
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
                for (int i = 0; i < 4; ++i) {
                    BlockPos posneighbour = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
                    Block block = worldIn.getBlockState(posneighbour.up()).getBlock();
                    IBlockState neighbourblockstate = worldIn.getBlockState(posneighbour);

                    
                    
                    if (neighbourblockstate.getBlock() instanceof IBlockSoil && ((IBlockSoil)neighbourblockstate.getBlock()).canGrowGrass(worldIn, pos)) {
                    	soil = (IBlockSoil)neighbourblockstate.getBlock();
                    	EnumOrganicLayer neighbourorganiclayer = soil.getOrganicLayer(worldIn, posneighbour);
                    	

                    	
                    	if (neighbourorganiclayer == null) System.out.println("neighbourorganiclayer is null! " + neighbourblockstate);

                        if (worldIn.getLight(posneighbour.up()) >= neighbourorganiclayer.minblocklight &&
                            block.getLightOpacity(worldIn, posneighbour.up()) <= 2 &&
                            neighbourorganiclayer == EnumOrganicLayer.NOGRASS) {
                        	
                        	soil.setOrganicLayer(EnumOrganicLayer.VERYSPARSEGRASS, worldIn, posneighbour);
                        }

                    }
                    
                }
            }
        }
    }
    
    
    
	@Override
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
		if (this instanceof IBlockSoil) {
			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
		} else {
			return 16777215;
		}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state) {
		if (this instanceof IBlockSoil) {
			return this.getBlockColor();
		} else {
			return 16777215;
		}
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
		if (this instanceof IBlockSoil) {
			//return BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
			return VCraftWorld.instance.getGrassColorAtPos(pos);
		} else {
			return 16777215;
		}
    }

	
	
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
    	if (this instanceof IBlockSoil) {
    		return EnumWorldBlockLayer.CUTOUT_MIPPED;
    	} else {
    		return EnumWorldBlockLayer.SOLID;
    	}
    }
    
   
    

    @Override
    public String getSubType(ItemStack stack) {
    	return null;
    }

    
	public float getBlockHardnessMultiplier(IBlockState state) {
		return 1f;
	}
	
	public int getHarvetLevel(IBlockState state) {
		return 1;
	}
	
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		player.addExhaustion(0.025F); // Make player hungry twice as much
		
		super.harvestBlock(worldIn, player, pos, state, te);
	}
    
}

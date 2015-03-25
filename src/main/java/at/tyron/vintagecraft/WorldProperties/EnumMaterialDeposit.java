package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.List;

import scala.actors.threadpool.Arrays;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.block.*;
import at.tyron.vintagecraft.interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;
import at.tyron.vintagecraft.interfaces.ISoil;

public enum EnumMaterialDeposit implements IStringSerializable, IGenLayerSupplier {
	// int id, hasOre, adjdepth, color, weight, height, minDepth, maxDepth, relativeDepth, deposittype
	
	NODEPOSIT (-1,      false, DepositSize.NONE, DepositOccurence.noDeposit(18000)),
	
	QUARTZ (19, 		true, DepositSize.HUGE, DepositOccurence.anyRelativeDepth(80, 1, 0, 254)),
	ROCKSALT (20, 		true, DepositSize.HUGE, DepositOccurence.anyRelativeDepth(35, 2, 5, 255, 160)),
	OLIVINE (21, 		true, DepositSize.HUGE, DepositOccurence.anyRelativeDepth(100, 2, 0, 254, 210)),

	CLAY (0,		   false, DepositSize.LARGE, DepositOccurence.inTopSoil(5, 2, 155)),
	PEAT (1,		   false, DepositSize.LARGE, DepositOccurence.inTopSoil(60, 2, 165)),
	
	
	LIGNITE (2,         true, DepositSize.SMALLANDLARGE, DepositOccurence.mixedDepths(50, 1, 10, 50, 0.5f)), 
	BITUMINOUSCOAL (3,  true, DepositSize.LARGE, DepositOccurence.anyBelowSealevel(37, 1, 8, 103)),
	
	NATIVECOPPER (4,    true, DepositSize.SMALLANDLARGE, DepositOccurence.mixedDepths(28, 1, 4, 40, 0.35f)),
	
	LIMONITE (5,		true, DepositSize.SMALLANDLARGE, DepositOccurence.anyBelowSealevel(23, 1, 15, 103)),
	NATIVEGOLD_QUARTZ(6,true, DepositSize.TINY, DepositOccurence.inDeposit(QUARTZ, 1)),

	REDSTONE (7,		true, DepositSize.SMALLANDLARGE, DepositOccurence.anyBelowSealevel(20, 2, 30, 100)),
	
	CASSITERITE (8,		true, DepositSize.SMALLANDLARGE, DepositOccurence.mixedDepths(3, 1, 0, 40, 0.5f)),
	
	IRIDIUM (9,			    true, DepositSize.SMALL, DepositOccurence.anyBelowSealevel(2, 1, 80, 110)),
	PLATINUM (10, 		    true, DepositSize.TINY, DepositOccurence.anyBelowSealevel(2, 1, 90, 110)),
	RHODIUM (11,		    true, DepositSize.SMALL, DepositOccurence.anyBelowSealevel(2, 1, 100, 140)),
	SPHALERITE (12,		    true, DepositSize.SMALL, DepositOccurence.anyBelowSealevel(10, 1, 0, 35)),
	SYLVITE_ROCKSALT (13,   true, DepositSize.SMALL, DepositOccurence.inDeposit(ROCKSALT, 15)), 
	NATIVESILVER_QUARTZ (14,true, DepositSize.NONE, DepositOccurence.inDeposit(QUARTZ, 2)), 
	
	LAPISLAZULI (15,	true, DepositSize.SMALL, DepositOccurence.anyRelativeDepth(50, 1, 0, 35, 220)),
	DIAMOND (16,		true, DepositSize.SINGLE, DepositOccurence.anyBelowSealevel(45, 1, 100, 140)),
	EMERALD (17,		true, DepositSize.SINGLE, DepositOccurence.anyBelowSealevel(60, 1, 100, 140)),
	BISMUTHINITE (18,	true, DepositSize.SMALL, DepositOccurence.anyBelowSealevel(5, 1, 0, 35)),
	
	PERIDOT_OLIVINE (22,true, DepositSize.NONE, DepositOccurence.inDeposit(OLIVINE, 20)),  
	;

	

	public int id;
	public boolean dropsOre;

	public DepositSize size;
	public DepositOccurence occurence;
	
	public EnumMaterialDeposit[] subdeposits;

	
	private EnumMaterialDeposit(int id, boolean dropsOre, DepositSize size, DepositOccurence occurence) {
		this.id = id;
		this.dropsOre = dropsOre;
		
		this.size = size;
		this.occurence = occurence;
		
		if (this.occurence.type == DepositOccurenceType.INDEPOSIT) {
			int idx = 0;
			if (subdeposits == null) {
				subdeposits = new EnumMaterialDeposit[1];
			} else {
				subdeposits = (EnumMaterialDeposit[]) Arrays.copyOf(subdeposits, subdeposits.length + 1);
				idx = subdeposits.length - 1;
			}
			
			subdeposits[idx] = this;
		}
	}
	
	
	

	
	public boolean isParentMaterial(IBlockState state, BlockPos pos) {
		boolean isrock = state.getBlock() instanceof BlockRock && state.getBlock().getMaterial() == Material.rock;
		
		
		
		switch (this) {
			case PEAT:
				if (state.getBlock() instanceof BlockTopSoil || state.getBlock() instanceof BlockSubSoil || state.getBlock() instanceof BlockRegolith) {
					int []climate = VCraftWorld.instance.getClimate(pos);
					if (climate[0] < 18 && climate[2] > 130) return true;
				}
				return false;
			
			case CLAY:
				return state.getBlock() instanceof BlockTopSoil || state.getBlock() instanceof BlockSubSoil || state.getBlock() instanceof BlockRegolith;
			
			case LAPISLAZULI:
				return isrock && BlocksVC.rock.getBlockClassfromState(state).getKey() == EnumRockType.LIMESTONE;
				
			case NATIVEGOLD_QUARTZ:
			case NATIVESILVER_QUARTZ:
				//if (state.getBlock() instanceof BlockOreVC) System.out.println(BlockOreVC.getOreType(state) == EnumOreType.QUARTZ);
				return state.getBlock() instanceof BlockOreVC && BlockOreVC.getOreType(state) == EnumOreType.QUARTZ;
				
			case SYLVITE_ROCKSALT: 
				return state.getBlock() instanceof BlockOreVC && BlockOreVC.getOreType(state) == EnumOreType.ROCKSALT;
				
			case EMERALD:
			case DIAMOND:
				return isrock && BlocksVC.rock.getBlockClassfromState(state).getKey() == EnumRockType.KIMBERLITE;
			
			case OLIVINE:
				//System.out.println(isrock && BlocksVC.rock.getBlockClassfromState(state).getKey() == EnumRockType.BASALT);
				return isrock && BlocksVC.rock.getBlockClassfromState(state).getKey() == EnumRockType.BASALT;
				
			case PERIDOT_OLIVINE:
				return state.getBlock() instanceof BlockOreVC && BlockOreVC.getOreType(state) == EnumOreType.OLIVINE;
				
			default:
				return isrock;
		}
		
		
	}

	/*public Block getBlock() {
		return block;
	}*/
	
	public IBlockState getBlockStateForDepth(int depth, IBlockState parentmaterial) {
		IBlockState state;
		
		switch (this) {
			case PEAT: state = BlocksVC.peat.getDefaultState(); break;
			case CLAY: state = BlocksVC.rawclay.getDefaultState(); break;
			case SYLVITE_ROCKSALT:
			case NATIVEGOLD_QUARTZ:
			case NATIVESILVER_QUARTZ:
			case PERIDOT_OLIVINE:
				state = BlocksVC.rawore.getBlockStateFor(this.getName() + "-" + BlockOreVC.getRockType(parentmaterial).getName());
				break;
			default:
				EnumRockType rocktype = (EnumRockType) BlocksVC.rock.getBlockClassfromState(parentmaterial).getKey();
				state = BlocksVC.rawore.getBlockStateFor(this.getName() + "-" + rocktype.getName());
				break;
		}
		

		if (state == null) System.out.println("block state for " + this + " is null!");
		
		
		if (depth > 1 && state.getBlock() instanceof ISoil) {
			return state.withProperty(((ISoil)state.getBlock()).getOrganicLayerProperty(null, null), EnumOrganicLayer.NoGrass);
		}
		
		
		
		return state;
	}
	
	public void init(Block block) {
	//	this.block = block;
	}
	
	
	
	public static EnumMaterialDeposit byId(int id) {
		EnumMaterialDeposit[] deposits = values();
		for (int i = 0; i < deposits.length; i++) {
			if (deposits[i].id == id)
				return deposits[i];
		}
		return null;
		
	}


	public static EnumMaterialDeposit byColor(int color) {
		EnumMaterialDeposit[] deposits = values();
		
		for (int i = 0; i < deposits.length; i++) {
			if (deposits[i].getColor() == color)
				return deposits[i];
		}
		
		return null;
	}
	
	
	@Override
	public int getColor() {
		return (id + 1);
	}


	@Override
	public int getWeight() {
		return occurence.weight;
	}
	
	
	@Override
	public int getDepthMax() {
		return occurence.maxdepth;
	}
	
	@Override
	public int getDepthMin() {
		return occurence.mindepth;
	}


	@Override
	public String getName() {
		return name().toLowerCase();
	}
	
	
	
    public static String[] getNames() {
    	String[] names = new String[values().length];
    	
    	for (int i = 0; i < values().length; i++) {
    		names[i] = values()[i].name().toLowerCase();
    	}
    	return names;
    }

	@Override
	public int getSize() {
		if (size == DepositSize.HUGE) return 4;
		return 1;
	}	

        
	
}

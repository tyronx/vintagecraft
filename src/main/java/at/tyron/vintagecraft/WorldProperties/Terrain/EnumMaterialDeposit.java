package at.tyron.vintagecraft.WorldProperties.Terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;
import at.tyron.vintagecraft.Block.*;
import at.tyron.vintagecraft.Block.Organic.BlockPeat;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Interfaces.IGenLayerSupplier;
import at.tyron.vintagecraft.Interfaces.IBlockSoil;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.VCraftWorld;
import at.tyron.vintagecraft.WorldGen.Helper.DepositOccurence;
import at.tyron.vintagecraft.WorldProperties.EnumDepositOccurenceType;
import at.tyron.vintagecraft.WorldProperties.EnumDepositSize;

public enum EnumMaterialDeposit implements IStringSerializable, IGenLayerSupplier {
	// int id, hasOre, adjdepth, color, weight, height, minDepth, maxDepth, relativeDepth, deposittype
	
	NODEPOSIT 			(-1, false, EnumDepositSize.NONE, DepositOccurence.noDeposit(18000)),

	PEAT 				( 1, false, EnumDepositSize.HUGE, DepositOccurence.followSurface(40, 2, 1, 145)),
	LIGNITE 			( 2, true, EnumDepositSize.SMALLANDLARGE, DepositOccurence.mixedDepths(50, 1, 10, 50, 0.5f)), 
	BITUMINOUSCOAL      ( 3, true, EnumDepositSize.LARGE, DepositOccurence.anyBelowSealevel(37, 1, 8, 103)),

	CLAY 				( 0, false, EnumDepositSize.LARGE, DepositOccurence.followSurface(11, 2, 1, 155)),
	FIRECLAY 			(22, false, EnumDepositSize.LARGE, DepositOccurence.followSurface(6, 2, 2, 160)),

	QUARTZ 				(19, true, EnumDepositSize.HUGE, DepositOccurence.anyRelativeDepth(80, 1, 0, 254)),
	ROCKSALT 			(20, true, EnumDepositSize.HUGE, DepositOccurence.anyRelativeDepth(45, 2, 5, 255, 170)),
	OLIVINE 			(21, true, EnumDepositSize.HUGE, DepositOccurence.anyRelativeDepth(100, 2, 0, 254, 210)),
	
	NATIVECOPPER 		( 4, true, EnumDepositSize.SMALLANDLARGE, DepositOccurence.mixedDepths(28, 1, 4, 60, 0.35f)),
	SPHALERITE 			(12, true, EnumDepositSize.SMALL, DepositOccurence.anyBelowSealevel(10, 1, 0, 35)),
	CASSITERITE 		( 8, true, EnumDepositSize.SMALLANDLARGE, DepositOccurence.anyRelativeDepth(8, 1, 0, 40)),
	BISMUTHINITE 		(18, true, EnumDepositSize.SMALL, DepositOccurence.anyRelativeDepth(12, 1, 0, 50)),

	LIMONITE 			( 5, true, EnumDepositSize.SMALLANDLARGE, DepositOccurence.anyBelowSealevel(23, 1, 15, 103)),

	IRIDIUM 			( 9, true, EnumDepositSize.SMALL, DepositOccurence.anyBelowSealevel(2, 1, 80, 110)),
	PLATINUM 			(10, true, EnumDepositSize.TINY, DepositOccurence.anyBelowSealevel(2, 1, 90, 110)),
	ILMENITE 			(11, true, EnumDepositSize.SMALL, DepositOccurence.anyBelowSealevel(2, 1, 100, 140)),
	
	LAPISLAZULI 		(15, true, EnumDepositSize.HUGE, DepositOccurence.anyRelativeDepth(5, 1, 0, 35, 220)),
	DIAMOND 			(16, true, EnumDepositSize.TINY, DepositOccurence.anyBelowSealevel(15, 1, 100, 140)),
	EMERALD 			(17, true, EnumDepositSize.SINGLE, DepositOccurence.anyBelowSealevel(60, 1, 100, 140)),
		
	NATIVEGOLD_QUARTZ   ( 6, true, EnumDepositSize.TINY, DepositOccurence.inDeposit(QUARTZ, 1)),
	SYLVITE_ROCKSALT    (13, true, EnumDepositSize.SMALL, DepositOccurence.inDeposit(ROCKSALT, 35)), 
	NATIVESILVER_QUARTZ (14, true, EnumDepositSize.SMALL, DepositOccurence.inDeposit(QUARTZ, 2)), 
	PERIDOT_OLIVINE     (23, true, EnumDepositSize.SMALL, DepositOccurence.inDeposit(OLIVINE, 20)),  

	
	GALENA 				( 7, true, EnumDepositSize.SMALLANDLARGE, DepositOccurence.anyRelativeDepth(15, 1, 30, 100)),
	SULFUR 				(24, true, EnumDepositSize.LARGE, DepositOccurence.anyBelowSealevel(10, 1, 20, 108)),
	SALTPETER			(25, true, EnumDepositSize.HUGE, DepositOccurence.anyBelowSealevel(50, 15, 10, 200))
	
	;

	

	public int id;
	public boolean dropsOre;

	public EnumDepositSize size;
	public DepositOccurence occurence;
	
	public EnumMaterialDeposit[] subdeposits;

	
	private EnumMaterialDeposit(int id, boolean dropsOre, EnumDepositSize size, DepositOccurence occurence) {
		this.id = id;
		this.dropsOre = dropsOre;
		
		this.size = size;
		this.occurence = occurence;
		
		if (this.occurence.type == EnumDepositOccurenceType.INDEPOSIT) {
			
			
			int idx = 0;
			if (this.occurence.indeposit.subdeposits == null) {
				this.occurence.indeposit.subdeposits = new EnumMaterialDeposit[1];
			} else {
				this.occurence.indeposit.subdeposits = (EnumMaterialDeposit[]) Arrays.copyOf(this.occurence.indeposit.subdeposits, this.occurence.indeposit.subdeposits.length + 1);
				idx = this.occurence.indeposit.subdeposits.length - 1;
			}
			
			this.occurence.indeposit.subdeposits[idx] = this;
		}
	}
	
	
	

	
	public boolean isParentMaterial(IBlockState state, BlockPos pos) {
		boolean isrock = state.getBlock() instanceof BlockRock && state.getBlock().getMaterial() == Material.rock;
		
		EnumRockType rocktype = null;
		if (isrock) {
			rocktype = (EnumRockType) BlocksVC.rock.getEntryFromState(state).getKey();
		}
		
		switch (this) {
			case PEAT:
				if (state.getBlock() instanceof BlockTopSoil || state.getBlock() instanceof BlockSubSoil || state.getBlock() instanceof BlockRegolith) {
					int []climate = VCraftWorld.instance.getClimate(pos);
					if (climate[0] < 18 && climate[2] > 130) return true;
				}
				return false;
			
			case CLAY:
			case FIRECLAY:
				return state.getBlock() instanceof BlockTopSoil || state.getBlock() instanceof BlockSubSoil || state.getBlock() instanceof BlockRegolith;
			
			case NATIVEGOLD_QUARTZ:
			case NATIVESILVER_QUARTZ:
				return state.getBlock() instanceof BlockOreVC && BlockOreVC.getOreType(state) == EnumOreType.QUARTZ;
				
			case SYLVITE_ROCKSALT: 
				return state.getBlock() instanceof BlockOreVC && BlockOreVC.getOreType(state) == EnumOreType.ROCKSALT;
								
			case PERIDOT_OLIVINE:
				return state.getBlock() instanceof BlockOreVC && BlockOreVC.getOreType(state) == EnumOreType.OLIVINE;

	
			case SALTPETER:
				return state.getBlock() == Blocks.air;
				
			default:
				return EnumOreType.byId(this.id).isParentMaterial(rocktype);
		}
		
		
	}

	
	public IBlockState getBlockStateForPos(IBlockState parentmaterial, World world, BlockPos pos) {
		IBlockState state;
		
		switch (this) {
			case PEAT: 
				state = BlocksVC.peat.getDefaultState(); 
				
				if (!(parentmaterial.getBlock() instanceof BlockTopSoil)) {
					state = state.withProperty(BlockPeat.organicLayer, EnumOrganicLayer.NOGRASS);
				}
			break;
			
			case CLAY: 
				state = BlocksVC.rawclay.getDefaultState();

				if (!(parentmaterial.getBlock() instanceof BlockTopSoil)) {
					state = state.withProperty(BlockPeat.organicLayer, EnumOrganicLayer.NOGRASS);
				}
				break;
			
			case FIRECLAY: 
				state = BlocksVC.rawfireclay.getDefaultState(); 
				break;
			
			case SALTPETER:
				// Will be null when no solid sides found 
				state = BlocksVC.saltpeter.getFullyCoatingBlockAtPos(world, pos);
				if(state == null) state = Blocks.air.getDefaultState();
				break;
			
				
			case SYLVITE_ROCKSALT:
			case NATIVEGOLD_QUARTZ:
			case NATIVESILVER_QUARTZ:
			case PERIDOT_OLIVINE:
				state = BlocksVC.rawore.getBlockStateFor(this.getName() + "-" + BlockOreVC.getRockType(parentmaterial).getName());
				break;
			default:
				EnumRockType rocktype = (EnumRockType) BlocksVC.rock.getEntryFromState(parentmaterial).getKey();
				state = BlocksVC.rawore.getBlockStateFor(this.getName() + "-" + rocktype.getName());
				break;
		}
		

		if (state == null) System.out.println("block state for " + this + " is null!");
		
		
		/*if (depth > 0 && state.getBlock() instanceof IBlockSoil) {
			return state.withProperty(((IBlockSoil)state.getBlock()).getOrganicLayerProperty(null, null), EnumOrganicLayer.NoGrass);
		}*/
		
		
		
		return state;
	}
	
	public void init(Block block) {
	//	this.block = block;
	}
	
	
	
	public static EnumMaterialDeposit[] valuesSorted() {
		EnumMaterialDeposit []values = values();
		EnumMaterialDeposit []sorted = new EnumMaterialDeposit[values.length];
		
		int id = -1;
		int remaining = values().length;
		int i = 0;
		
		while(remaining > 0) {
			for (EnumMaterialDeposit value : values) {
				if (value.id == id) {
					sorted[i++] = value;
					remaining--;
					break;
				}
			}
			
			id++;
		}
		
		return sorted;
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
		return name().toLowerCase(Locale.ROOT);
	}
	
	
	
    public static String[] getNames() {
    	String[] names = new String[values().length];
    	
    	for (int i = 0; i < values().length; i++) {
    		names[i] = values()[i].name().toLowerCase(Locale.ROOT);
    	}
    	return names;
    }

	@Override
	public int getSize() {
		if (size == EnumDepositSize.GIGANTIC) return 16;
		if (size == EnumDepositSize.HUGE) return 9;
		return 1;
	}	

        
	
}

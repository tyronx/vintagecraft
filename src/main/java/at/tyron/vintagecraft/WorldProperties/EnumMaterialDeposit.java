package at.tyron.vintagecraft.WorldProperties;

import java.util.ArrayList;
import java.util.List;

import sun.security.krb5.Realm;
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
import at.tyron.vintagecraft.World.BiomeVC;
import at.tyron.vintagecraft.block.BlockOreVC;
import at.tyron.vintagecraft.block.BlockRegolith;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockSubSoil;
import at.tyron.vintagecraft.block.BlockTopSoil;
import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.interfaces.IGenLayerSupplier;
import at.tyron.vintagecraft.interfaces.ISoil;

public enum EnumMaterialDeposit implements IStringSerializable, IGenLayerSupplier {
	// int id, hasOre, adjdepth, color, weight, height, minDepth, maxDepth, relativeDepth, deposittype
	
	NODEPOSIT (-1,                  false, false, 0,  14000,   0,  0, 0, DepositType.NONE),
	
	CLAY (0,			false, true,  30,  5,   2,  0,    1, true, 155, DepositType.LARGE),
	PEAT (1,			false, true,  40,  60,  2,  0,    1, true, 155, DepositType.LARGE),
	
	
	LIGNITE (2,         true, true,   60,  33,  1,  10,  50, true, 254, DepositType.SMALLANDLARGE),
	BITUMINOUSCOAL (3,  true, true,   80,  25,  1,  8, 103, DepositType.LARGE),
	
	NATIVECOPPER (4,    true, false,  90,  18,  1,  4,   40, true, 254, DepositType.SMALLANDLARGE),
	
	LIMONITE (5,		true, false, 100,  23,  1,  15, 103, DepositType.SMALLANDLARGE),
	NATIVEGOLD_QUARTZ (6,		true, false, 120,  5,  1,  0, 200, DepositType.NONE), // generated during quartz gen

	REDSTONE (7,		true, false, 140,  20,  2,  30, 130, DepositType.SMALLANDLARGE),
	
	CASSITERITE (8,		true, false, 160,   3,  1, 0, 35, true, 220, DepositType.SMALLANDLARGE),
	
	IRIDIUM (9,			true, false, 170,   2,  1, 80, 110, DepositType.SMALL),
	PLATINUM (10, 		true, false, 175,   1,  1, 90, 110, DepositType.SMALL),
	RHODIUM (11,		true, false, 180,   2,  1, 100, 140, DepositType.SMALL),
	SPHALERITE (12,		true, false, 185,  10,  1, 0, 35, DepositType.SMALL),
	SYLVITE_ROCKSALT (13,		true, false, 190,  15,  1, 0, 35, DepositType.NONE),  // generated during salt gen
	NATIVESILVER_QUARTZ (14,	true, false, 195, 10,  1, 0, 200, DepositType.NONE),  // generated during quartz gen
	LAPISLAZULI (15,	true, false, 200,  50,  1, 0, 35, true, 220, DepositType.SMALL),
	DIAMOND (16,		true, false, 205,  45,  1, 100, 140, DepositType.TINY),
	EMERALD (17,		true, false, 210,  60,  1, 100, 140, DepositType.TINY),
	BISMUTHINITE (18,	true, false, 215,   5,  1, 0, 35, true, 220, DepositType.SMALL),
	
	QUARTZ (19, 		true, true, 220, 70, 1, 0, 254, true, 255, DepositType.HUGE),
	ROCKSALT (20, 		true, true, 225, 30, 2, 0, 254, true, 210, DepositType.HUGE),
	OLIVINE (21, 		true, true, 227,100, 2, 0, 254, true, 210, DepositType.HUGE),
	PERIDOT_OLIVINE (22,true, false, 226, 20,  1, 0, 200, DepositType.NONE),  // generated during quartz gen
	;

	

	public int id;
	//Block block;
	public boolean hasOre;
	public boolean adjusttodepth;
	public DepositType deposittype;
	int color;
	public int height;
	public int minDepth;
	public int maxDepth;
	public EnumMetal smelted;
	public int ore2IngotRatio;
	//public final BiomeVC[] biomes;
	public int weight;
	public boolean relativeDepth;
	public int maxheightOnRelDepth; // 0..255    only relevant on relativeDepth = true 
	
	private EnumMaterialDeposit(int id, boolean hasOre, boolean adjusttodepth, int color, int weight, int averageHeight, int minDepth, int maxDepth, DepositType deposittype) {
		this(id, hasOre, adjusttodepth, color, weight, averageHeight, minDepth, maxDepth, false, 255, deposittype);
	}
	
	private EnumMaterialDeposit(int id, boolean hasOre, boolean adjusttodepth, int color, int weight, int averageHeight, int minDepth, int maxDepth, boolean relativeDepth, int maxheightOnRelDepth, DepositType deposittype) {
		this.id = id;
		
		this.weight = weight;
		this.deposittype = deposittype;
		//this.block = block;
		this.hasOre = hasOre;
		this.adjusttodepth = adjusttodepth;
		this.height = averageHeight;
		this.minDepth = minDepth;
		this.maxDepth = maxDepth;
		//this.biomes = biomes;
		this.color = color;
		this.relativeDepth = relativeDepth;
		this.maxheightOnRelDepth = maxheightOnRelDepth;
	}
	
	
	
	public static EnumMaterialDeposit depositForColor(int color) {
		EnumMaterialDeposit[] deposits = values();
		for (int i = 0; i < deposits.length; i++) {
			if (deposits[i].color == color)
				return deposits[i];
		}
		return null;
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


	
	@Override
	public int getColor() {
		return color;
	}


	@Override
	public int getWeight() {
		return weight;
	}
	
	
	@Override
	public int getDepthMax() {
		return maxDepth;
	}
	
	@Override
	public int getDepthMin() {
		return minDepth;
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
		if (deposittype == DepositType.HUGE) return 4;
		return 1;
	}	

        
	
}

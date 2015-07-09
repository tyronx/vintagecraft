package at.tyron.vintagecraft.BlockClass;

import java.util.Locale;

import at.tyron.vintagecraft.Interfaces.EnumStateImplementation;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class CoatingClass extends BaseBlockClass {
	String getBlockClassName() { return name; }
	Class<? extends Block> getBlockClass() { return blockclass; }
	Class<? extends ItemBlock> getItemClass() { return itemclass; }
	float getHardness() { return hardness; }
	SoundType getStepSound() { return stepsound; }
	String getHarvestTool() { return harvesttool; }
	int getHarvestLevel() { return harvestlevel; }
	String getTypeName() { return "facings"; }
	
	public CoatingClass(String name, Class<? extends Block> blockclass, Class<? extends ItemBlock> itemclass, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		this.name = name;
		this.blockclass = blockclass;
		this.itemclass = itemclass;
		this.hardness = hardness;
		this.stepsound = stepsound;
		this.harvesttool = harvesLevelTool;
		this.harvestlevel = harvestLevel;
	}
	
	
	public CoatingClass init() {
		
		String name;
		
		// Generates every possible combination of covering 1-6 sides 
		for (int i = 1; i < 64; i++) {
			name =
				((i & 1) > 0 ? "d" : "") +
				((i & 2) > 0 ? "u" : "") +
				((i & 4) > 0 ? "n" : "") +
				((i & 8) > 0 ? "s" : "") +
				((i & 16) > 0 ? "w" : "") +
				((i & 32) > 0 ? "e" : "")
			;
			
			EnumStateImplementation key = new EnumStateImplementation(i, 0, name);
			values.put(key, new BlockClassEntry(key));
		}
		
		
		initBlocks(
			getBlockClassName(), 
			getBlockClass(), 
			getItemClass(), 
			getHardness(), 
			getStepSound(), 
			getHarvestTool(), 
			getHarvestLevel()
		);
		return this;
	}
	
	char[] facingsletters = new char[]{'d', 'u', 'n', 's', 'w', 'e'};
	public EnumFacing []getFacings(String value) {
		EnumFacing[] facings = new EnumFacing[value.length()];
		
		for (int i = 1; i < value.length(); i++) {
			for (int letterindex = 0; letterindex < 6; letterindex++) {
				
				if (value.charAt(i) == facingsletters[letterindex]) {
					facings[i] = EnumFacing.values()[letterindex];
					break;
				}				
			}
		}
		
		
		return facings;
	}
	
	
	
	
	public String getSolidFacesAtPos(World world, BlockPos pos) {
		String facings = "";
		
		for (EnumFacing facing : EnumFacing.values()) {
			if (world.isSideSolid(pos.offset(facing), facing.getOpposite())) {
				facings += facing.getName().substring(0, 1).toLowerCase(Locale.ROOT);
			}
		}
		
		return facings;
	}

	public IBlockState getFullyCoatingBlockAtPos(World world, BlockPos pos) {
		return getBlockStateFor(getSolidFacesAtPos(world, pos));
	}
	
	
	
}

package at.tyron.vintagecraft.WorldGen.Village;

import java.util.Random;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.WorldProperties.Terrain.EnumRockType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class DynVillageGen {
	NumFloat []params;
	Random rand = new Random();
	
	
	World world;
	
	public DynVillageGen(NumFloat []params) {
		this.params = params;
	}
	
	public float[] genAngles(float quantity) {
		int actualQuantity = (int)quantity + (rand.nextFloat() < quantity - (int)quantity ? 1 : 0);
		float minAngleDistance = NatFloat.PI / actualQuantity;
		
		// By this much degress the angles can vary
		float angleVariance = (NatFloat.PI * 2) / actualQuantity - minAngleDistance;
		float bonusVariance = -angleVariance; // if one road is quite close to the previous, then we have some extra space for the next one
		
		float[] angles = new float[actualQuantity];
		
		angles[0] = rand.nextFloat() * NumFloat.PI * 2;
		actualQuantity--;
		int i = 1;
		
		while (actualQuantity-- > 0) {
			float variance = rand.nextFloat()*(angleVariance + Math.max(0, bonusVariance));
			bonusVariance = angleVariance - variance;
			
			angles[i] = angles[i - 1] + minAngleDistance + variance;
			i++;
		}
		
		
		
		return angles;
	}
	
	public void generate(World world, BlockPos pos, float size) {
		this.world = world;
		
		float quantity = params[Param.centerBranchingQuantity.ordinal()].nextFloat();
		float[] angles = genAngles(quantity);

		
		for (float angle : angles) {
			System.out.println("Road at " + angle);
			buildRoad(
				0,
				size * params[Param.centerBranchingWidth.ordinal()].nextFloat(),
				pos,
				0f,
				0f,
				angle
			);
		}
	}
	
	
	void buildRoad(int recursion, float width, BlockPos posOffet, float xOffset, float zOffset, float angleOffset) {
		//System.out.println("gen road, width " + width);
		
		params[Param.branchingAngle.ordinal()].init(0);
		params[Param.branchingQuantity.ordinal()].init(0);
		params[Param.branchingSpacing.ordinal()].init(0);
		params[Param.branchingStart.ordinal()].init(0);
		params[Param.branchingWidth.ordinal()].init(0);
		params[Param.branchingWidthLoss.ordinal()].init(0);
		params[Param.branchAngleOffset.ordinal()].init(0);
		
		
		float widthloss = params[Param.branchWidthLoss.ordinal()].nextFloat();
		float branchstart = params[Param.branchingStart.ordinal()].nextFloat();
		float branchspacing = params[Param.branchingSpacing.ordinal()].nextFloat();
		float maxrecursion = params[Param.branchingMaxRecursion.ordinal()].nextFloat();
		
		if (width / widthloss > 500) {
			System.out.println("To many iterations! " + (width/widthloss));
			return;
		}
		
		int iteration = 0;
		float sequencesPerIteration = 1000 / (width / widthloss);
		
		float angle;
		float reldistance = 0, lastreldistance = 0;
		float totaldistance = width / widthloss;
		
		while (width > 0 && iteration++ < 200) {
			width -= widthloss;

			angle = angleOffset + params[Param.branchAngleOffset.ordinal()].nextFloat();
			//System.out.println(angle);
			
			xOffset += MathHelper.cos(angle);
			zOffset += MathHelper.sin(angle);			
			params[Param.branchAngleOffset.ordinal()].advance(sequencesPerIteration);
			
			
			placeRoad(width, world, posOffet.add(xOffset, 0, zOffset), angle);
			
			reldistance = MathHelper.sqrt_float(xOffset*xOffset+ zOffset*zOffset) / totaldistance;
			
			//System.out.println(reldistance + " / " + lastreldistance + " / " + branchstart + " / " + branchspacing);
			
			if (reldistance < branchstart || width < 0 || recursion+1 > maxrecursion) continue;
			if (reldistance > lastreldistance + branchspacing * (1f - reldistance)) {
				branchspacing = params[Param.branchingSpacing.ordinal()].nextFloat();
				lastreldistance = reldistance;
				
				float quantity = params[Param.branchingQuantity.ordinal()].nextFloat();
				float[] angles = new float[(int)quantity+1];
				int i = 0;
				//System.out.println(angle + params[Param.branchingAngle.ordinal()].nextFloat());
				
				while (quantity-- >= 1 || (quantity > 0 && rand.nextFloat() < quantity)) {
					angles[i] = angle + params[Param.branchingAngle.ordinal()].nextFloat();
					
					while (i > 0 && Math.abs(angles[i - 1] - angles[i]) > 0.4f) {
						angles[i] = angle + params[Param.branchingAngle.ordinal()].nextFloat();
					}
					
					buildRoad(
						recursion + 1,
						width * params[Param.branchingWidth.ordinal()].nextFloat(),
						posOffet,
						xOffset,
						zOffset,
						angles[i]
					);
					
					width = width  * params[Param.branchingWidthLoss.ordinal()].nextFloat();
					i++;
				}
			}

		}
	}
	
	
	IBlockState roadBlock(float width) {
		float rnd = 0f;
		if (width > 1) rnd = 0.05f / width;
		else rnd = 1f - (1f - width) / 2; 
			
		if (rand.nextFloat() < width) return BlocksVC.gravel.getBlockStateFor(EnumRockType.ANDESITE);
		return null;
	}
	
	void placeRoad(float width, World world, BlockPos pos, float angle) {
		float roadsize = 1.5f;
		
		float placeWidth = width * roadsize;
		float placeWidthSq = placeWidth * placeWidth; 
		//float dx, dz, factor;
		
		if (placeWidth < 0) return;
		
		for (float dx = -placeWidth / 2; dx < placeWidth; dx++) {
			for (float dz = -placeWidth / 2; dz < placeWidth; dz++) {
				if (dx*dx + dz*dz < placeWidthSq) {
					IBlockState blockstate = roadBlock(width);
					if (blockstate != null) {
						world.setBlockState(world.getHorizon(pos.add(dx, 0, dz)).down(), blockstate);
					}					
				}
			}
		}
		
		/*while (placeWidth > 0) {
			factor = (placeWidth - width * roadsize / 2);
			
			dx = factor * MathHelper.cos(angle + NumFloat.PIHALF);
			dz = factor * MathHelper.sin(angle + NumFloat.PIHALF);
			
			IBlockState blockstate = roadBlock(width);
			if (blockstate != null) {
				world.setBlockState(world.getHorizon(pos.add(dx, 0, dz)).down(), blockstate);
			}
			
			placeWidth--;
		}*/

	}
	
	
	
	
	
	public static enum Param {
		centerBranchingQuantity (Component.CENTER),
		centerBranchingWidth (Component.CENTER),
		
		branchAngleOffset (Component.ROAD),
		branchWidthLoss (Component.ROAD),
		
		branchingAngle (Component.ROAD),
		branchingStart (Component.ROAD),
		branchingSpacing (Component.ROAD),
		branchingQuantity (Component.ROAD),
		branchingWidth (Component.ROAD),
		branchingWidthLoss (Component.ROAD),
		
		branchingMaxRecursion (Component.ROAD),

		
		
		
		
		;
		
		
		Component ofsection;
		
		private Param(Component ofsection) {
			this.ofsection = ofsection; 
		}
		
		
		public static enum Component {
			CENTER,
			ROAD,
			HOUSE
		}
	}

}
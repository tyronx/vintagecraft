package at.tyron.vintagecraft.Interfaces.Block;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockEatableGrass {

	public boolean canBeEatenBy(EntityLiving entity, World world, BlockPos pos);
	
	public void setEatenBy(EntityLiving entity, World world, BlockPos pos);
	
}

package at.tyron.vintagecraft.Interfaces.Block;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IBlockIgniteable {

	public boolean ignite(World world, BlockPos pos, ItemStack firestarter);
}

package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import com.google.common.base.Objects;

import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.block.BlockRock;
import at.tyron.vintagecraft.block.BlockSubSoil;
import at.tyron.vintagecraft.block.BlockTopSoil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;

public class MapGenCavesVC extends MapGenBase {
	// All copied from Vanilla minecraft with minor adjustments
	// I wish someone could figure out what this actually does :/
	
	//this.func_180703_a(this.rand.nextLong(), chunkX, chunkZ, primer, xCoord, yCoord, zCoord);
    protected void func_180703_a(long randNum, int chunkX, int chunkZ, ChunkPrimer primer, double xcoord, double ycoord, double zcoord) {
        this.func_180702_a(randNum, chunkX, chunkZ, primer, xcoord, ycoord, zcoord, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void func_180702_a(long randNum, int chunkX, int chunkZ, ChunkPrimer primer, double xcoord, double ycoord, double zcoord, float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, double p_180702_17_)
    {
        double d4 = (double)(chunkX * 16 + 8);
        double d5 = (double)(chunkZ * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;
        Random random = new Random(randNum);

        if (p_180702_16_ <= 0)
        {
            int j1 = this.range * 16 - 16;
            p_180702_16_ = j1 - random.nextInt(j1 / 4);
        }

        boolean flag2 = false;

        if (p_180702_15_ == -1)
        {
            p_180702_15_ = p_180702_16_ / 2;
            flag2 = true;
        }

        int k1 = random.nextInt(p_180702_16_ / 2) + p_180702_16_ / 4;

        for (boolean flag = random.nextInt(6) == 0; p_180702_15_ < p_180702_16_; ++p_180702_15_)
        {
            double d6 = 1.5D + (double)(MathHelper.sin((float)p_180702_15_ * (float)Math.PI / (float)p_180702_16_) * p_180702_12_ * 1.0F);
            double d7 = d6 * p_180702_17_;
            float f5 = MathHelper.cos(p_180702_14_);
            float f6 = MathHelper.sin(p_180702_14_);
            xcoord += (double)(MathHelper.cos(p_180702_13_) * f5);
            ycoord += (double)f6;
            zcoord += (double)(MathHelper.sin(p_180702_13_) * f5);

            if (flag)
            {
                p_180702_14_ *= 0.92F;
            }
            else
            {
                p_180702_14_ *= 0.7F;
            }

            p_180702_14_ += f4 * 0.1F;
            p_180702_13_ += f3 * 0.1F;
            f4 *= 0.9F;
            f3 *= 0.75F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag2 && p_180702_15_ == k1 && p_180702_12_ > 1.0F && p_180702_16_ > 0)
            {
                this.func_180702_a(random.nextLong(), chunkX, chunkZ, primer, xcoord, ycoord, zcoord, random.nextFloat() * 0.5F + 0.5F, p_180702_13_ - ((float)Math.PI / 2F), p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
                this.func_180702_a(random.nextLong(), chunkX, chunkZ, primer, xcoord, ycoord, zcoord, random.nextFloat() * 0.5F + 0.5F, p_180702_13_ + ((float)Math.PI / 2F), p_180702_14_ / 3.0F, p_180702_15_, p_180702_16_, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0)
            {
                double d8 = xcoord - d4;
                double d9 = zcoord - d5;
                double d10 = (double)(p_180702_16_ - p_180702_15_);
                double d11 = (double)(p_180702_12_ + 2.0F + 16.0F);

                if (d8 * d8 + d9 * d9 - d10 * d10 > d11 * d11)
                {
                    return;
                }

                if (xcoord >= d4 - 16.0D - d6 * 2.0D && zcoord >= d5 - 16.0D - d6 * 2.0D && xcoord <= d4 + 16.0D + d6 * 2.0D && zcoord <= d5 + 16.0D + d6 * 2.0D)
                {
                    int k3 = MathHelper.floor_double(xcoord - d6) - chunkX * 16 - 1;
                    int l1 = MathHelper.floor_double(xcoord + d6) - chunkX * 16 + 1;
                    int l3 = MathHelper.floor_double(ycoord - d7) - 1;
                    int i2 = MathHelper.floor_double(ycoord + d7) + 1;
                    int i4 = MathHelper.floor_double(zcoord - d6) - chunkZ * 16 - 1;
                    int j2 = MathHelper.floor_double(zcoord + d6) - chunkZ * 16 + 1;

                    if (k3 < 0)
                    {
                        k3 = 0;
                    }

                    if (l1 > 16)
                    {
                        l1 = 16;
                    }

                    if (l3 < 1)
                    {
                        l3 = 1;
                    }

                    if (i2 > 248)
                    {
                        i2 = 248;
                    }

                    if (i4 < 0)
                    {
                        i4 = 0;
                    }

                    if (j2 > 16)
                    {
                        j2 = 16;
                    }

                    boolean flag3 = false;
                    int k2;

                    for (k2 = k3; !flag3 && k2 < l1; ++k2)
                    {
                        for (int l2 = i4; !flag3 && l2 < j2; ++l2)
                        {
                            for (int i3 = i2 + 1; !flag3 && i3 >= l3 - 1; --i3)
                            {
                                if (i3 >= 0 && i3 < 256)
                                {
                                    IBlockState iblockstate = primer.getBlockState(k2, i3, l2);

                                    if (isOceanBlock(primer, k2, i3, l2, chunkX, chunkZ))
                                    {
                                        flag3 = true;
                                    }

                                    if (i3 != l3 - 1 && k2 != k3 && k2 != l1 - 1 && l2 != i4 && l2 != j2 - 1)
                                    {
                                        i3 = l3;
                                    }
                                }
                            }
                        }
                    }

                    if (!flag3)
                    {
                        for (k2 = k3; k2 < l1; ++k2)
                        {
                            double d14 = ((double)(k2 + chunkX * 16) + 0.5D - xcoord) / d6;

                            for (int j4 = i4; j4 < j2; ++j4)
                            {
                                double d12 = ((double)(j4 + chunkZ * 16) + 0.5D - zcoord) / d6;
                                boolean flag1 = false;

                                if (d14 * d14 + d12 * d12 < 1.0D)
                                {
                                    for (int j3 = i2; j3 > l3; --j3)
                                    {
                                        double d13 = ((double)(j3 - 1) + 0.5D - ycoord) / d7;

                                        if (d13 > -0.7D && d14 * d14 + d13 * d13 + d12 * d12 < 1.0D)
                                        {
                                            IBlockState iblockstate1 = primer.getBlockState(k2, j3, j4);
                                            IBlockState iblockstate2 = (IBlockState)Objects.firstNonNull(primer.getBlockState(k2, j3 + 1, j4), Blocks.air.getDefaultState());

                                            if (isTopBlock(primer, k2, j3, j4, chunkX, chunkZ))
                                            {
                                                flag1 = true;
                                            }
                                            digBlock(primer, k2, j3, j4, chunkX, chunkZ, flag1, iblockstate1, iblockstate2);
                                        }
                                    }
                                }
                            }
                        }

                        if (flag2)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }
    

    protected boolean caveableBlock(IBlockState blockstate, IBlockState aboveblockstate) {
    	if (aboveblockstate.getBlock().getMaterial() == Material.water) return false;
    	
    	return 
    		blockstate.getBlock() instanceof BlockRock 
    		|| blockstate.getBlock() instanceof BlockOre 
    		|| blockstate.getBlock() instanceof BlockSubSoil 
    		|| blockstate.getBlock() instanceof BlockTopSoil;
    	
    }
	
    
    // Generate function
    protected void func_180701_a(World worldIn, int dx, int dz, int chunkX, int chunkZ, ChunkPrimer primer)
    {
        int i1 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);

        if (this.rand.nextInt(7) != 0)
        {
            i1 = 0;
        }

        for (int j1 = 0; j1 < i1; ++j1)
        {
            double xCoord = (double)(dx * 16 + this.rand.nextInt(16));
            double yCoord = (double)this.rand.nextInt(this.rand.nextInt(220) + 8);
            double zCoord = (double)(dz * 16 + this.rand.nextInt(16));
            int k1 = 1;

            if (this.rand.nextInt(4) == 0)
            {
                this.func_180703_a(this.rand.nextLong(), chunkX, chunkZ, primer, xCoord, yCoord, zCoord);
                k1 += this.rand.nextInt(4);
            }

            for (int l1 = 0; l1 < k1; ++l1)
            {
                float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float f2 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(10) == 0)
                {
                    f2 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
                }

                this.func_180702_a(this.rand.nextLong(), chunkX, chunkZ, primer, xCoord, yCoord, zCoord, f2, f, f1, 0, 0, 1.0D);
            }
        }
    }

    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ)
    {
        net.minecraft.block.Block block = data.getBlockState(x, y, z).getBlock();
        return block== Blocks.flowing_water || block == Blocks.water;
    }

    //Exception biomes to make sure we generate like vanilla
    private boolean isExceptionBiome(net.minecraft.world.biome.BiomeGenBase biome) {
        if (biome == net.minecraft.world.biome.BiomeGenBase.beach) return true;
        if (biome == net.minecraft.world.biome.BiomeGenBase.desert) return true;
        return false;
    }

    //Determine if the block at the specified location is the top block for the biome, we take into account
    //Vanilla bugs to make sure that we generate the map the same way vanilla does.
    private boolean isTopBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ)
    {
        net.minecraft.world.biome.BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
        IBlockState state = data.getBlockState(x, y, z);
        return (isExceptionBiome(biome) ? state.getBlock() == Blocks.grass : state.getBlock() == biome.topBlock);
    }

    /**
     * Digs out the current block, default implementation removes stone, filler, and top block
     * Sets the block to lava if y is less then 10, and air other wise.
     * If setting to air, it also checks to see if we've broken the surface and if so
     * tries to make the floor the biome's top block
     *
     * @param data Block data array
     * @param index Pre-calculated index into block data
     * @param x local X position
     * @param y local Y position
     * @param z local Z position
     * @param chunkX Chunk X position
     * @param chunkZ Chunk Y position
     * @param foundTop True if we've encountered the biome's top block. Ideally if we've broken the surface.
     */
    protected void digBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ, boolean foundTop, IBlockState state, IBlockState up) {
       // net.minecraft.world.biome.BiomeGenBase biome = worldObj.getBiomeGenForCoords(new BlockPos(x + chunkX * 16, 0, z + chunkZ * 16));
       /* IBlockState top = biome.topBlock;
        IBlockState filler = biome.fillerBlock;*/

        if (this.caveableBlock(state, up)) {
            if (y < 40) {
                data.setBlockState(x, y, z, Blocks.lava.getDefaultState());
            } else {
                data.setBlockState(x, y, z, Blocks.air.getDefaultState());

               /* if (foundTop && data.getBlockState(x, y - 1, z).getBlock() == filler.getBlock()) {
                    data.setBlockState(x, y - 1, z, top.getBlock().getDefaultState());
                }*/
            }
        }
    }


    
}

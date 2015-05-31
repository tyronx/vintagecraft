package at.tyron.vintagecraft.WorldGen;

import java.util.Random;

import com.google.common.base.Objects;

import at.tyron.vintagecraft.Block.BlockRock;
import at.tyron.vintagecraft.Block.Organic.BlockSubSoil;
import at.tyron.vintagecraft.Block.Organic.BlockTopSoil;
import at.tyron.vintagecraft.World.BlocksVC;
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
	
    protected void digCave(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double xcoord, double ycoord, double zcoord) {
        this.digTunnel(seed, chunkX, chunkZ, primer, xcoord, ycoord, zcoord, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    protected void digTunnel(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double xcoord, double ycoord, double zcoord, float horizontalsize, float coordangle, float speedangle, int counter, int maxiterations, double verticalsize) {
        double xchunkmiddle = (double)(chunkX * 16 + 8);
        double zchunkmiddle = (double)(chunkZ * 16 + 8);
        float f3 = 0.0F;
        float f4 = 0.0F;
        Random random = new Random(seed);

        if (maxiterations <= 0) {
            int j1 = range * 16 - 16;
            maxiterations = j1 - random.nextInt(j1 / 4);
        }

        boolean flag2 = false;

        if (counter == -1) {
            counter = maxiterations / 2;
            flag2 = true;
        }

        int k1 = random.nextInt(maxiterations / 2) + maxiterations / 4;

        for (boolean rnd = random.nextInt(6) == 0; counter < maxiterations; ++counter) {
            double horizontalradius = 1.5D + (double)(MathHelper.sin((float)counter * (float)Math.PI / (float)maxiterations) * horizontalsize * 1.0F);
            double verticalradius = horizontalradius * verticalsize;
            float advancex = MathHelper.cos(speedangle);
            float advancey = MathHelper.sin(speedangle);
            xcoord += (double)(MathHelper.cos(coordangle) * advancex);
            ycoord += (double)advancey;
            zcoord += (double)(MathHelper.sin(coordangle) * advancex);

            if (rnd) {
                speedangle *= 0.92F;
            } else {
                speedangle *= 0.7F;
            }

            speedangle += f4 * 0.1F;
            coordangle += f3 * 0.1F;
            f4 *= 0.9F;
            f3 *= 0.75F;
            f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!flag2 && counter == k1 && horizontalsize > 1.0F && maxiterations > 0) {
                this.digTunnel(random.nextLong(), chunkX, chunkZ, primer, xcoord, ycoord, zcoord, random.nextFloat() * 0.5F + 0.5F, coordangle - ((float)Math.PI / 2F), speedangle / 3.0F, counter, maxiterations, 1.0D);
                this.digTunnel(random.nextLong(), chunkX, chunkZ, primer, xcoord, ycoord, zcoord, random.nextFloat() * 0.5F + 0.5F, coordangle + ((float)Math.PI / 2F), speedangle / 3.0F, counter, maxiterations, 1.0D);
                return;
            }

            if (flag2 || random.nextInt(4) != 0) {
                double xdisttomiddle = xcoord - xchunkmiddle;
                double zdisttomiddle = zcoord - zchunkmiddle;
                double iterationsleft = (double)(maxiterations - counter);
                double d11 = (double)(horizontalsize + 2.0F + 16.0F);

                if (xdisttomiddle * xdisttomiddle + zdisttomiddle * zdisttomiddle - iterationsleft * iterationsleft > d11 * d11) {
                    return;
                }

                if (xcoord >= xchunkmiddle - 16.0D - horizontalradius * 2.0D && zcoord >= zchunkmiddle - 16.0D - horizontalradius * 2.0D && xcoord <= xchunkmiddle + 16.0D + horizontalradius * 2.0D && zcoord <= zchunkmiddle + 16.0D + horizontalradius * 2.0D) {
                    int minx = MathHelper.floor_double(xcoord - horizontalradius) - chunkX * 16 - 1;
                    int maxx = MathHelper.floor_double(xcoord + horizontalradius) - chunkX * 16 + 1;
                    int miny = MathHelper.floor_double(ycoord - verticalradius) - 1;
                    int maxy = MathHelper.floor_double(ycoord + verticalradius) + 1;
                    int minz = MathHelper.floor_double(zcoord - horizontalradius) - chunkZ * 16 - 1;
                    int maxz = MathHelper.floor_double(zcoord + horizontalradius) - chunkZ * 16 + 1;
                    
                    minx = Math.max(0, minx);
                    maxx = Math.min(16, maxx);
                    
                    miny = Math.max(1, miny);
                    maxy = Math.min(248, maxy);

                    minz = Math.max(0, minz);
                    maxz = Math.min(16, maxz);


                    boolean stopcave = false;
                    int dx;

                    for (dx = minx; !stopcave && dx < maxx; ++dx) {
                        for (int dz = minz; !stopcave && dz < maxz; ++dz) {
                            for (int dy = maxy + 1; !stopcave && dy >= miny - 1; --dy) {
                                if (dy >= 0 && dy < 256) {
                                    IBlockState iblockstate = primer.getBlockState(dx, dy, dz);

                                    if (isOceanBlock(primer, dx, dy, dz, chunkX, chunkZ)) {
                                        stopcave = true;
                                    }

                                    if (dy != miny - 1 && dx != minx && dx != maxx - 1 && dz != minz && dz != maxz - 1) {
                                        dy = miny;
                                    }
                                }
                            }
                        }
                    }

                    if (!stopcave) {
                        for (dx = minx; dx < maxx; ++dx) {
                            double xdist = ((double)(dx + chunkX * 16) + 0.5D - xcoord) / horizontalradius;

                            for (int dz = minz; dz < maxz; ++dz) {
                                double zdist = ((double)(dz + chunkZ * 16) + 0.5D - zcoord) / horizontalradius;
                                boolean foundtop = false;

                                if (xdist * xdist + zdist * zdist < 1.0D) {
                                    for (int dy = maxy; dy > miny; --dy) {
                                        double ydist = ((double)(dy - 1) + 0.5D - ycoord) / verticalradius;

                                        if (ydist > -0.7D && xdist * xdist + ydist * ydist + zdist * zdist < 1.0D) {
                                            IBlockState iblockstate1 = primer.getBlockState(dx, dy, dz);
                                            IBlockState iblockstate2 = (IBlockState)Objects.firstNonNull(primer.getBlockState(dx, dy + 1, dz), Blocks.air.getDefaultState());

                                            if (isTopBlock(primer, dx, dy, dz, chunkX, chunkZ)) {
                                                foundtop = true;
                                            }
                                            digBlock(primer, dx, dy, dz, chunkX, chunkZ, foundtop, iblockstate1, iblockstate2);
                                        }
                                    }
                                }
                            }
                        }

                        if (flag2) {
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
	
    
    // Generate function, called by MapGenBase
    protected void func_180701_a(World worldIn, int dx, int dz, int chunkX, int chunkZ, ChunkPrimer primer) {
        int quantityCaves = rand.nextInt(rand.nextInt(rand.nextInt(15) + 1) + 1);

        if (rand.nextInt(7) != 0) {
            quantityCaves = 0;
        }

        for (int i = 0; i < quantityCaves; ++i) {
            double xCoord = (double)(dx * 16 + rand.nextInt(16));
            double yCoord = (double)rand.nextInt(rand.nextInt(220) + 8);
            double zCoord = (double)(dz * 16 + rand.nextInt(16));
            int quantitySubCaves = 1;

            if (rand.nextInt(4) == 0) {
                digCave(rand.nextLong(), chunkX, chunkZ, primer, xCoord, yCoord, zCoord);                
                quantitySubCaves += rand.nextInt(4);
            }

            for (int l1 = 0; l1 < quantitySubCaves; ++l1) {
                float f = rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = (rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float horizontalSize = rand.nextFloat() * 2.0F + rand.nextFloat();

                if (rand.nextInt(10) == 0) {
                    horizontalSize *= rand.nextFloat() * rand.nextFloat() * 3.0F + 1.0F;
                }

                digTunnel(rand.nextLong(), chunkX, chunkZ, primer, xCoord, yCoord, zCoord, horizontalSize, f, f1, 0, 0, 1.0D);
            }
        }
    }

    protected boolean isOceanBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
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
    private boolean isTopBlock(ChunkPrimer data, int x, int y, int z, int chunkX, int chunkZ) {
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
        if (this.caveableBlock(state, up)) {
            if (y < 12) {
                data.setBlockState(x, y, z, Blocks.lava.getDefaultState());
            } else {
                data.setBlockState(x, y, z, Blocks.air.getDefaultState());
            }
        }
    }


    
}

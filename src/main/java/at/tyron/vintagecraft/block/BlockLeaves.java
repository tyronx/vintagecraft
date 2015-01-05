package at.tyron.vintagecraft.block;

import java.util.Random;

import javax.naming.spi.StateFactory;

import at.tyron.vintagecraft.VCraftWorld;
import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.WorldProperties.EnumTree;
import at.tyron.vintagecraft.item.ItemLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLeaves extends BlockVC {
	public boolean fancyGfx; 
	
	public static final PropertyBool CHECK_DECAY = PropertyBool.create("checkdecay");
	public static final PropertyEnum TREETYPE = PropertyEnum.create("treetype", EnumTree.class);
	
	
	int[] surroundings;
	
    
	
	public BlockLeaves() {
		super(Material.leaves);
		this.setTickRandomly(true);
		this.setLightOpacity(1);
        this.setStepSound(soundTypeGrass);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        
        this.setDefaultState(this.blockState.getBaseState().withProperty(TREETYPE, EnumTree.MOUNTAINDOGWOOD));
	}

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return !VintageCraft.proxy.isFancyGraphics() && worldIn.getBlockState(pos).getBlock() == this ? false : super.shouldSideBeRendered(worldIn, pos, side);
    }
    
    
    
    
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    
    
    
    
    @SideOnly(Side.CLIENT)
    public int getBlockColor() {
        return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state) {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass) {
       // return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
    	return VCraftWorld.getGrassColorAtPos(pos);
    }
    
    
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        return ItemLeaves.setTreeType(
        	new ItemStack(getItem(world,pos)), 
        	(EnumTree)world.getBlockState(pos).getValue(TREETYPE)
        );
    }
    

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        byte b0 = 1;
        int i = b0 + 1;
        int j = pos.getX();
        int k = pos.getY();
        int l = pos.getZ();

        if (worldIn.isAreaLoaded(new BlockPos(j - i, k - i, l - i), new BlockPos(j + i, k + i, l + i))) {
            for (int i1 = -b0; i1 <= b0; ++i1) {
                for (int j1 = -b0; j1 <= b0; ++j1) {
                    for (int k1 = -b0; k1 <= b0; ++k1) {
                        BlockPos blockpos1 = pos.add(i1, j1, k1);
                        IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);

                        if (iblockstate1.getBlock().isLeaves(worldIn, blockpos1)) {
                            iblockstate1.getBlock().beginLeavesDecay(worldIn, blockpos1);
                        }
                    }
                }
            }
        }
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!worldIn.isRemote && ((Boolean)state.getValue(CHECK_DECAY)).booleanValue()) {
            byte b0 = 4;
            int i = b0 + 1;
            int j = pos.getX();
            int k = pos.getY();
            int l = pos.getZ();
            byte b1 = 32;
            int i1 = b1 * b1;
            int j1 = b1 / 2;

            if (this.surroundings == null) {
                this.surroundings = new int[b1 * b1 * b1];
            }

            int k1;

            if (worldIn.isAreaLoaded(new BlockPos(j - i, k - i, l - i), new BlockPos(j + i, k + i, l + i))) {
                int l1;
                int i2;

                for (k1 = -b0; k1 <= b0; ++k1) {
                    for (l1 = -b0; l1 <= b0; ++l1) {
                        for (i2 = -b0; i2 <= b0; ++i2) {
                            BlockPos tmp = new BlockPos(j + k1, k + l1, l + i2);
                            Block block = worldIn.getBlockState(tmp).getBlock();

                            if (!block.canSustainLeaves(worldIn, tmp)) {
                                if (block.isLeaves(worldIn, tmp)) {
                                    this.surroundings[(k1 + j1) * i1 + (l1 + j1) * b1 + i2 + j1] = -2;
                                }
                                else
                                {
                                    this.surroundings[(k1 + j1) * i1 + (l1 + j1) * b1 + i2 + j1] = -1;
                                }
                            }
                            else
                            {
                                this.surroundings[(k1 + j1) * i1 + (l1 + j1) * b1 + i2 + j1] = 0;
                            }
                        }
                    }
                }

                for (k1 = 1; k1 <= 4; ++k1)
                {
                    for (l1 = -b0; l1 <= b0; ++l1)
                    {
                        for (i2 = -b0; i2 <= b0; ++i2)
                        {
                            for (int j2 = -b0; j2 <= b0; ++j2)
                            {
                                if (this.surroundings[(l1 + j1) * i1 + (i2 + j1) * b1 + j2 + j1] == k1 - 1)
                                {
                                    if (this.surroundings[(l1 + j1 - 1) * i1 + (i2 + j1) * b1 + j2 + j1] == -2)
                                    {
                                        this.surroundings[(l1 + j1 - 1) * i1 + (i2 + j1) * b1 + j2 + j1] = k1;
                                    }

                                    if (this.surroundings[(l1 + j1 + 1) * i1 + (i2 + j1) * b1 + j2 + j1] == -2)
                                    {
                                        this.surroundings[(l1 + j1 + 1) * i1 + (i2 + j1) * b1 + j2 + j1] = k1;
                                    }

                                    if (this.surroundings[(l1 + j1) * i1 + (i2 + j1 - 1) * b1 + j2 + j1] == -2)
                                    {
                                        this.surroundings[(l1 + j1) * i1 + (i2 + j1 - 1) * b1 + j2 + j1] = k1;
                                    }

                                    if (this.surroundings[(l1 + j1) * i1 + (i2 + j1 + 1) * b1 + j2 + j1] == -2)
                                    {
                                        this.surroundings[(l1 + j1) * i1 + (i2 + j1 + 1) * b1 + j2 + j1] = k1;
                                    }

                                    if (this.surroundings[(l1 + j1) * i1 + (i2 + j1) * b1 + (j2 + j1 - 1)] == -2)
                                    {
                                        this.surroundings[(l1 + j1) * i1 + (i2 + j1) * b1 + (j2 + j1 - 1)] = k1;
                                    }

                                    if (this.surroundings[(l1 + j1) * i1 + (i2 + j1) * b1 + j2 + j1 + 1] == -2)
                                    {
                                        this.surroundings[(l1 + j1) * i1 + (i2 + j1) * b1 + j2 + j1 + 1] = k1;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            k1 = this.surroundings[j1 * i1 + j1 * b1 + j1];

            if (k1 >= 0)
            {
                worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, Boolean.valueOf(false)), 4);
            }
            else
            {
                this.destroy(worldIn, pos);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (worldIn.canLightningStrike(pos.up()) && !World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && rand.nextInt(15) == 1)
        {
            double d0 = (double)((float)pos.getX() + rand.nextFloat());
            double d1 = (double)pos.getY() - 0.05D;
            double d2 = (double)((float)pos.getZ() + rand.nextFloat());
            worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    private void destroy(World worldIn, BlockPos pos)
    {
        this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
        worldIn.setBlockToAir(pos);
    }

    public int quantityDropped(Random random)
    {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.sapling);
    }

    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {}

    protected int getSaplingDropChance(IBlockState state)
    {
        return 20;
    }

    public boolean isOpaqueCube() {
        return !VintageCraft.proxy.isFancyGraphics();
    }

   
    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() {
        return VintageCraft.proxy.isFancyGraphics() ? EnumWorldBlockLayer.CUTOUT_MIPPED : EnumWorldBlockLayer.SOLID;
    }

    public boolean isVisuallyOpaque() {
        return false;
    }

    public boolean isLeaves(IBlockAccess world, BlockPos pos){ return true; }

    
    @Override
    public void beginLeavesDecay(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (!(Boolean)state.getValue(CHECK_DECAY))
        {
            world.setBlockState(pos, state.withProperty(CHECK_DECAY, true), 4);
        }
    }

    @Override
    public java.util.List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        java.util.List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        return ret;
    }
    
    
    
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {TREETYPE, CHECK_DECAY});
    }

    
    @Override
    public int getMetaFromState(IBlockState state) {
    	return
    		(Boolean)state.getValue(CHECK_DECAY) ? 1 : 0
    		+ ((EnumTree)state.getValue(TREETYPE)).meta << 1
    	;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
    	return super.getStateFromMeta(meta)
    			.withProperty(CHECK_DECAY, (meta & 1) > 0 ? true : false)
    			.withProperty(TREETYPE, EnumTree.byMeta(meta >> 1))
    	;
    }
    

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        return null;
    }
}

package at.tyron.vintagecraft.Entity;

import at.tyron.vintagecraft.World.ItemsVC;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityMinecartVC extends EntityMinecart {
	public int[][][] matrix = new int[][][] {{{0, 0, -1}, {0, 0, 1}}, {{ -1, 0, 0}, {1, 0, 0}}, {{ -1, -1, 0}, {1, 0, 0}}, {{ -1, 0, 0}, {1, -1, 0}}, {{0, 0, -1}, {0, -1, 1}}, {{0, -1, -1}, {0, 0, 1}}, {{0, 0, 1}, {1, 0, 0}}, {{0, 0, 1}, { -1, 0, 0}}, {{0, 0, -1}, { -1, 0, 0}}, {{0, 0, -1}, {1, 0, 0}}};
	
	public float railYOffset = 0.125f;  
	
	
	public EntityMinecartVC(World worldIn, double p_i1713_2_, double p_i1713_4_, double p_i1713_6_) {
		super(worldIn, p_i1713_2_, p_i1713_4_, p_i1713_6_);
	}
	
	public EntityMinecartVC(World worldIn) {
		super(worldIn);
	}


	@Override
	public EnumMinecartType getMinecartType() {
		return EnumMinecartType.RIDEABLE;
	}
	
	
    public void killMinecart(DamageSource p_94095_1_) {
        this.setDead();
        
        ItemStack itemstack;
        
        if (getMinecartType() == EnumMinecartType.FURNACE) {
        	itemstack = new ItemStack(ItemsVC.coalpoweredMinecart, 1);
        } else {
        	itemstack = new ItemStack(ItemsVC.emptyMinecart, 1);
        }
        

        this.entityDropItem(itemstack, 0.0F);
    }


	// Copied from original minecart code so that I can position the minecart a bit higher above the rails
    public Vec3 func_70489_a(double p_70489_1_, double p_70489_3_, double p_70489_5_) {
        int i = MathHelper.floor_double(p_70489_1_);
        int j = MathHelper.floor_double(p_70489_3_);
        int k = MathHelper.floor_double(p_70489_5_);

        if (BlockRailBase.isRailBlock(this.worldObj, new BlockPos(i, j - 1, k)))
        {
            --j;
        }

        IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));

        if (BlockRailBase.isRailBlock(iblockstate))
        {
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(((BlockRailBase)iblockstate.getBlock()).getShapeProperty());
            int[][] aint = matrix[enumraildirection.getMetadata()];
            double d3 = 0.0D;
            double d4 = (double)i + 0.5D + (double)aint[0][0] * 0.5D;
            double d5 = (double)j + railYOffset + (double)aint[0][1] * 0.5D;
            double d6 = (double)k + 0.5D + (double)aint[0][2] * 0.5D;
            double d7 = (double)i + 0.5D + (double)aint[1][0] * 0.5D;
            double d8 = (double)j + railYOffset + (double)aint[1][1] * 0.5D;
            double d9 = (double)k + 0.5D + (double)aint[1][2] * 0.5D;
            double d10 = d7 - d4;
            double d11 = (d8 - d5) * 2.0D;
            double d12 = d9 - d6;

            if (d10 == 0.0D)
            {
                p_70489_1_ = (double)i + 0.5D;
                d3 = p_70489_5_ - (double)k;
            }
            else if (d12 == 0.0D)
            {
                p_70489_5_ = (double)k + 0.5D;
                d3 = p_70489_1_ - (double)i;
            }
            else
            {
                double d13 = p_70489_1_ - d4;
                double d14 = p_70489_5_ - d6;
                d3 = (d13 * d10 + d14 * d12) * 2.0D;
            }

            p_70489_1_ = d4 + d10 * d3;
            p_70489_3_ = d5 + d11 * d3;
            p_70489_5_ = d6 + d12 * d3;

            if (d11 < 0.0D)
            {
                ++p_70489_3_;
            }

            if (d11 > 0.0D)
            {
                p_70489_3_ += 0.5D;
            }

            return new Vec3(p_70489_1_, p_70489_3_, p_70489_5_);
        }
        else
        {
            return null;
        }
    }
	
}

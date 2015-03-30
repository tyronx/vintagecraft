package at.tyron.vintagecraft.item;

import java.util.List;

import at.tyron.vintagecraft.ModInfo;
import at.tyron.vintagecraft.TileEntity.TEIngotPile;
import at.tyron.vintagecraft.World.BlocksVC;
import at.tyron.vintagecraft.World.ItemsVC;
import at.tyron.vintagecraft.WorldProperties.EnumMetal;
import at.tyron.vintagecraft.WorldProperties.EnumRockType;
import at.tyron.vintagecraft.block.BlockIngotPile;
import at.tyron.vintagecraft.interfaces.ISubtypeFromStackPovider;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIngot extends ItemVC implements ISubtypeFromStackPovider {
	public static final int maxstacksize = 64;
	public static final int maxpilesize = 64;
	
	public ItemIngot() {
        this.setHasSubtypes(true);
        setCreativeTab(CreativeTabs.tabMaterials);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
    	ItemStack stack;
    	for (EnumMetal metal : EnumMetal.values()) {
    		stack = new ItemStack(ItemsVC.ingot);
    		setMetal(stack, metal);
    		subItems.add(stack);
    	}
    }
    
    
	@Override
	public int getMetadata(ItemStack stack) {
		return stack.getTagCompound().getInteger("metal");
	}


	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if (getMetal(stack) == null) {
			return super.getUnlocalizedName() + ".unknown";
		}
		return super.getUnlocalizedName() + "." + getMetal(stack).getName();
	}
	
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Melting Point: " + getMetal(itemstack).meltingpoint + " deg.");
		tooltip.add("Hardness: " + getMetal(itemstack).hardness);
	}
	
	
	
	
	public static EnumMetal getMetal(ItemStack itemstack) {
		if (itemstack.getTagCompound() != null) {
			return EnumMetal.byId(itemstack.getTagCompound().getInteger("metal"));
		}
		return null;
	}
	
	
	public static ItemStack setMetal(ItemStack itemstack, EnumMetal metal) {
		NBTTagCompound nbt = itemstack.getTagCompound(); 
		if (nbt == null) {
			itemstack.setTagCompound(nbt = new NBTTagCompound());
		}	
		
		nbt.setInteger("metal", metal.id);
		itemstack.setTagCompound(nbt);
		return itemstack;
	}
	
	public static ItemStack getItemStack(EnumMetal metal, int quantity) {
//		System.out.println(new ItemStack(ItemsVC.ingot, quantity).getItem());
		return setMetal(new ItemStack(ItemsVC.ingot, quantity), metal);
	}

	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
	/*	if (worldIn.isRemote) {
			System.out.println("refresh resources");
			Minecraft.getMinecraft().refreshResources();
		}*/
		
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}


	@Override
	public String getSubType(ItemStack stack) {
		return getMetal(stack).getName();
	}
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	
	
	private boolean createPile(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, int l) {

		boolean fullStack = true;

		TEIngotPile te = null;

		if (world.getTileEntity(x, y, z) instanceof TEIngotPile && world.getBlock(x,y,z) == TFCBlocks.IngotPile)
		{
			te = (TEIngotPile)world.getTileEntity(x, y, z);
			if (te.contentsMatch(0,itemstack) && te.getStackInSlot(0).stackSize < te.getInventoryStackLimit())
			{
				fullStack = false;
				te.injectContents(0, 1);
			}
		}
		else{fullStack = true;}

		if(fullStack && isPlaceable(itemstack))
		{
			if(side == 0 && world.isAirBlock(x, y-1, z) && isValid(world, x, y-1, z))
			{
				world.setBlock( x, y-1, z, TFCBlocks.IngotPile, l, 0x2);
				if(world.isRemote) {
					world.markBlockForUpdate(x, y-1, z);
				}
				te = (TEIngotPile)world.getTileEntity(x, y-1, z);
			}
			else if(side == 1 && world.isAirBlock(x, y+1, z) && isValid(world, x, y+1, z))
			{
				world.setBlock( x, y+1, z, TFCBlocks.IngotPile, l, 0x2);
				if(world.isRemote) {
					world.markBlockForUpdate(x, y+1, z);
				}
				te = (TEIngotPile)world.getTileEntity(x, y+1, z);
			}
			else if(side == 2 && world.isAirBlock(x, y, z-1) && isValid(world, x, y, z-1))
			{
				world.setBlock( x, y, z-1, TFCBlocks.IngotPile, l, 0x2);
				if(world.isRemote) {
					world.markBlockForUpdate(x, y, z-1);
				}
				te = (TEIngotPile)world.getTileEntity(x, y, z-1);
			}
			else if(side == 3 && world.isAirBlock(x, y, z+1) && isValid(world, x, y, z+1))
			{
				world.setBlock( x, y, z+1, TFCBlocks.IngotPile, l, 0x2);
				if(world.isRemote) {
					world.markBlockForUpdate(x, y, z+1);
				}
				te = (TEIngotPile)world.getTileEntity(x, y, z+1);
			}
			else if(side == 4 && world.isAirBlock(x-1, y, z) && isValid(world, x-1, y, z))
			{
				world.setBlock( x-1, y, z, TFCBlocks.IngotPile, l, 0x2);
				if(world.isRemote) {
					world.markBlockForUpdate(x-1, y, z);
				}
				te = (TEIngotPile)world.getTileEntity(x-1, y, z);
			}
			else if(side == 5 && world.isAirBlock(x+1, y, z) && isValid(world, x+1, y, z))
			{
				world.setBlock( x+1, y, z, TFCBlocks.IngotPile, l, 0x2);
				if(world.isRemote) {
					world.markBlockForUpdate(x+1, y, z);
				}
				te = (TEIngotPile)world.getTileEntity(x+1, y, z);
			}
			else
			{
				return false;
			}

			if(te != null)
			{
				te.storage[0] = new ItemStack(this,1,0);
				te.setType(MetalRegistry.instance.getMetalFromItem(this).Name);

				if(entityplayer.capabilities.isCreativeMode)
				{
					te.storage[0] = new ItemStack(this,32,0);
				}
			}
		}		
		return true;
	}
	*/
	
	
	
	
	public boolean isPlaceable(ItemStack is) {
		return true;
	}

	


	
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!entityplayer.isSneaking() || !isPlaceable(itemstack)) return false;
		
		boolean ingotPileAtPos = world.getBlockState(pos).getBlock() instanceof BlockIngotPile;
		
		//int dir = MathHelper.floor_double(entityplayer.rotationYaw * 4F / 360F + 0.5D) & 3;
		
		if (!ingotPileAtPos) {
			return BlockIngotPile.tryCreatePile(itemstack, world, pos.offset(side));
		} else {
			BlocksVC.ingotPile.onBlockActivated(world, pos, world.getBlockState(pos), entityplayer, side, hitX, hitY, hitZ);
		}
		
		return false;
	}
/*		} else {
			TEIngotPile teingotpile = (TEIngotPile)world.getTileEntity(pos);
			
			if (teingotpile == null) return false;
			
			teingotpile.getBlockType().onBlockActivated(world, pos, world.getBlockState(pos), entityplayer, side, hitX, hitY, hitZ);
			
			
		}
		
		
			if(!world.isRemote && !ingotPileAtPos) {

				if(createPile(itemstack, entityplayer, world, pos, side, dir)) {

					itemstack.stackSize = itemstack.stackSize-1;
					world.addBlockEvent(x,y,z,TFCBlocks.IngotPile,0,0);
					return true;

				}
			}
			else if(ingotPileAtPos)
			{
				TEIngotPile te = (TEIngotPile)world.getTileEntity(x, y, z);
				//TileEntityIngotPile te2 = (TileEntityIngotPile)Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
				if(te != null)
				{
					te.getBlockType().onBlockActivated(world, x, y, z, entityplayer, side, hitX, hitY, hitZ);
					if(te.storage[0] != null && te.contentsMatch(0,itemstack) && te.storage[0].stackSize < 64) 
					{
						te.injectContents(0,1);
						te.validate();
					} 
					else if(te.storage[0] != null && !te.contentsMatch(0,itemstack) && te.storage[0].stackSize < 64) 
					{
						return false;
					}
					else if(te.storage[0] == null) 
					{
						te.addContents(0, new ItemStack(this,1));
					} 
					else
					{
						if(CreatePile(itemstack, entityplayer, world, x, y, z, side, dir))
						{
							itemstack.stackSize = itemstack.stackSize-1;
							if (world.getTileEntity(x,y,z) != null)
							{
								//((TileEntityIngotPile)world.getTileEntity(x,y,z)).setType(MetalRegistry.instance.getMetalFromItem(this).Name);
							}
							world.addBlockEvent(x,y,z,TFCBlocks.IngotPile,0,0);
							te.getBlockType().onBlockActivated(world, x, y, z, entityplayer, side, hitX, hitY, hitZ);
						}
						return true;

					}
					itemstack.stackSize = itemstack.stackSize-1;
					if (world.getTileEntity(x,y,z) != null)
					{
						//((TileEntityIngotPile)world.getTileEntity(x,y,z)).setType(MetalRegistry.instance.getMetalFromItem(this).Name);
					}
					world.addBlockEvent(x,y,z,TFCBlocks.IngotPile,0,0);
					return true;
				}

			}
			else
			{
				int m = itemstack.getItemDamage();
				if(side == 1)
				{
					if (m>=16){
						world.setBlock(x, y+1, z, TFCBlocks.IngotPile, m, 0x2);
						itemstack.stackSize = itemstack.stackSize-1;
					}
					else{
						world.setBlock(x, y+1, z, TFCBlocks.IngotPile, m, 0x2);
						itemstack.stackSize = itemstack.stackSize-1;
					}
				}
				else if(side == 0 && world.isAirBlock(x, y-1, z))
				{
					if(m >=16){
						world.setBlock(x, y-1, z, TFCBlocks.IngotPile, m, 0x2);
						itemstack.stackSize = itemstack.stackSize-1;
					}
					else{
						world.setBlock(x, y-1, z, TFCBlocks.IngotPile, m, 0x2);
						itemstack.stackSize = itemstack.stackSize-1;
					}
				}
				else if(side == 2 && world.isAirBlock(x, y, z-1))
				{
					setSide(world, itemstack, m, dir, x, y, z, 0, 0, -1);
				}
				else if(side == 3 && world.isAirBlock(x, y, z+1))
				{
					setSide(world, itemstack, m, dir, x, y, z, 0, 0, 1);
				}
				else if(side == 4 && world.isAirBlock(x-1, y, z))
				{
					setSide(world, itemstack, m, dir, x, y, z, -1, 0, 0);
				}
				else if(side == 5 && world.isAirBlock(x+1, y, z))
				{
					setSide(world, itemstack, m, dir, x, y, z, 1, 0, 0);
				}
				if (world.getTileEntity(x,y,z) != null && world.getTileEntity(x,y,z) instanceof TEIngotPile)
				{
					//((TileEntityIngotPile)world.getTileEntity(x,y,z)).setType(this.getItem() - 16028 - 256);
				}
				world.addBlockEvent(x,y,z,TFCBlocks.IngotPile,0,0);
				return true;
			}

		}
			
		return false;
	}*/

	/*public boolean isValid(World world, int i, int j, int k)
	{
		if(world.isSideSolid(i, j-1, k, ForgeDirection.UP) || world.getBlock(i, j-1, k)==TFCBlocks.IngotPile)
		{
			TileEntity te = world.getTileEntity(i, j-1, k);

			if (te instanceof TEIngotPile)
			{
				TEIngotPile ip = (TEIngotPile)te;

				if(ip != null)
				{
					if(ip.storage[0] == null || ip.storage[0].stackSize < 64) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}

	public void setSide(World world, ItemStack itemstack, int m, int dir, int x, int y, int z, int i, int j, int k)
	{
		if(m < 8)
		{
			if(dir == 0 || dir == 2) {
				world.setBlock(x+i, y+j, z+k, TFCBlocks.IngotPile, m, 0x2);
			} else {
				world.setBlock(x+i, y+j, z+k, TFCBlocks.IngotPile, m | 8, 0x2);
			}
			itemstack.stackSize = itemstack.stackSize-1;
		}
		else if(m >= 16)
		{
			if(dir == 0 || dir == 2) {
				world.setBlock(x+i, y+j, z+k, TFCBlocks.IngotPile, m-8, 0x2);
			} else {
				world.setBlock(x+i, y+j, z+k, TFCBlocks.IngotPile, m-8 | 8, 0x2);
			}
			itemstack.stackSize = itemstack.stackSize-1;
		}

	}*/

}



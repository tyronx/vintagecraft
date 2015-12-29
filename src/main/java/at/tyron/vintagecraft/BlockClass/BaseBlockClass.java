package at.tyron.vintagecraft.BlockClass;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;

import at.tyron.vintagecraft.VintageCraftConfig;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;
import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;


/* This class acts as an abstraction layer to blocks and their meta data 
 *
 * This allows for near Zero-Configuration subtyping of blocks as it takes out the mess of registering and comparing subblocks
 * 
 */

public abstract class BaseBlockClass {
	boolean debug = VintageCraftConfig.debugBlockRegistration;
	
	LinkedHashMap<IStateEnum, BlockClassEntry> values = new LinkedHashMap<IStateEnum, BlockClassEntry>();
	

	String name;
	Class<? extends Block> blockclass;
	Class<? extends ItemBlock> itemclass;
	float hardness;
	SoundType stepsound;
	String harvesttool;
	int harvestlevel;
	
	abstract String getBlockClassName();
	abstract Class<? extends Block> getBlockClass();
	abstract Class<? extends ItemBlock> getItemClass();
	abstract float getHardness();
	abstract SoundType getStepSound();
	abstract String getHarvestTool();
	abstract int getHarvestLevel();
	
	abstract String getTypeName();

	
	
	public Block[] initFromEnum(Class<? extends Enum> enumclass) {
		for (Enum item : enumclass.getEnumConstants()) {
			values.put((IStateEnum) item, new BlockClassEntry((IStateEnum)item));
		}
		
		return initBlocks(getBlockClassName(), getBlockClass(), getItemClass(), getHardness(), getStepSound(), getHarvestTool(), getHarvestLevel());
	}
	
	
	Object invokeMethod(Class theclass, Object instance, String methodname, Object[]args) {
		try {
			boolean found = false;

			while (!found) {
				for (Method method : theclass.getDeclaredMethods()) {
					if (method.getName().equals(methodname)) {
						found = true;
						return method.invoke(instance, args);
					}
				}
				if (!found) {
					theclass = theclass.getSuperclass();
					if (theclass == null) break;
				}
			}
			
			if (!found) throw new Exception();
			
			
			
		} catch (Exception e) {
			System.out.println(methodname + "() in " + theclass.getName() + " not found");
			e.printStackTrace();
		}
		return null;
	}
	
	protected Block[] initBlocks(String name, Class<? extends Block> blockclass, Class<? extends ItemBlock> itemclass, float hardness, SoundType stepsound, String harvesLevelTool, int harvestLevel) {
		if(debug) System.out.println("init " + (values().length) + " of type " + name + " (block = " + blockclass + ")");
		
		// I hate Java not being able to allow method overriding of static methods :/
		int typesperblock = 1;
		try {
			typesperblock = (Integer)invokeMethod(blockclass, blockclass.newInstance(), "multistateAvailableTypes", new Object[0]);
		} catch (Exception e) { System.out.println("Unable to get multistateAvailableTypes ("+e.getMessage()+") for "+name+"! Will use 1 (= waste of blockids)"); } 
		
		BlockClassEntry[][] chunked = split(values(), typesperblock);
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (BlockClassEntry[] blockclassentrychunk : chunked) {
			if(debug) System.out.println("register chunk piece of size " + blockclassentrychunk.length);
			Block block;
			try {
				block = blockclass.newInstance();
				blocks.add(block);
				
				int meta = 0;
				for (BlockClassEntry blockclassentry : blockclassentrychunk) {
					if(debug) System.out.println("init blockclassentry " + (name + ((blocks.size() > 1) ? blocks.size() : "")) + " with meta " + meta + "     (key = " + blockclassentry.key.getStateName() + ")");
					blockclassentry.init(block, meta++);
				}
				
				invokeMethod(blockclass, block, "init", new Object[]{blockclassentrychunk, createProperty(getTypeName(), blockclassentrychunk)});
				
				((IMultiblock)block).registerMultiState(name + ((blocks.size() > 1) ? blocks.size() : "") , itemclass, blockclassentrychunk, name);
				
				block.setHardness(hardness).setStepSound(stepsound);
				
				if (harvesLevelTool != null) {
					block.setHarvestLevel(harvesLevelTool, harvestLevel);
				}
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return blocks.toArray(new Block[0]);
	}
	
	
	
	static <T> T[][] split(T[] elements, int chunksize) {
		int chunks = (int) Math.ceil((1f * elements.length) / chunksize);
		
		ArrayList<T> result = new ArrayList<T>(); 
		
		for (int i = 0; i < chunks; i++) {
			result.add((T) Arrays.copyOfRange(elements, i * chunksize, Math.min(elements.length, i*chunksize + chunksize)));
		}
		
		return (T[][]) result.toArray((T[])Array.newInstance(elements.getClass(), 0));
		
	}
	
	
	public BlockClassEntry getEntryFromState(IBlockState state) {
		return getEntryFromMeta(state.getBlock(), state.getBlock().getMetaFromState(state));
	}
	
	public BlockClassEntry getEntryFromMeta(Block block, int meta) {
		for (BlockClassEntry enumitem: values()) {
			if (enumitem.metadata == meta && enumitem.block == block) return enumitem;
		}
		
		for (BlockClassEntry enumitem: values()) {
			System.out.println(enumitem.metadata+" == "+meta+" && "+enumitem.block.getUnlocalizedName()+" == "+block.getUnlocalizedName());
		}
		
		throw new RuntimeException("BlockClassEntry not found for block " + block + " / meta " + meta);
	}
	
	public BlockClassEntry getEntryFromKey(IStateEnum key) {
		return values.get(key);
	}
	
	public BlockClassEntry getEntryFromItemStack(ItemStack itemstack) {
		if (itemstack.getItem() instanceof ItemBlock) {
			return getEntryFromMeta(((ItemBlock)itemstack.getItem()).block, itemstack.getItemDamage());
		}
		return null;
	}
	

	public int getMetaFromState(IBlockState state) {
		for (BlockClassEntry enumitem: values()) {
			if (enumitem.block == state.getBlock() && enumitem == state.getValue(((IMultiblock)enumitem.block).getTypeProperty())) return enumitem.metadata;
		}
		
		for (BlockClassEntry enumitem: values()) {
			System.out.println((enumitem.block == state.getBlock()) + " && " + enumitem + " == " + state.getValue(((IMultiblock)enumitem.block).getTypeProperty()));
		}
		
		throw new RuntimeException("Meta not found for state " + state + "\r\n num values: " + values().length);
	}
	
	

	
	public PropertyBlockClass createProperty(String name) {
		return new PropertyBlockClass(name, BlockClassEntry.class, values.values().toArray(new BlockClassEntry[0]));
	}
	
	public PropertyBlockClass createProperty(String name, BlockClassEntry[] values) {
		return new PropertyBlockClass(name, BlockClassEntry.class, values);
	}
	


	public int getLength() {
		return values.size();
	}
	
	public BlockClassEntry[] values() {
		return values.values().toArray(new BlockClassEntry[0]);
	}
	
	public Collection<BlockClassEntry> asList() {
		return values.values();
	}
	
	
	public IBlockState getBlockStateFor(IStateEnum enumitem) {
		return values.get(enumitem).getBlockState();
	}
	
	public IBlockState getBlockStateFor(String statename) {
		for (IStateEnum state : values.keySet()) {
			if (state.getStateName().equals(statename)) return values.get(state).getBlockState();
		}
		return null;
	}
	
	public boolean containsBlock(Block block) {
		for (BlockClassEntry entry : values()) {
			if (entry.block == block) return true;
		}
		return false;
	}

	public ItemStack getItemStackFor(String statename) {
		for (IStateEnum state : values.keySet()) {
			if (state.getStateName().equals(statename)) return values.get(state).getItemStack();
		}
		return null;
	}

	public ItemStack getItemStackFor(IStateEnum enumitem) {
		if (values.get(enumitem) == null) { 
			return null;
		}
		return values.get(enumitem).getItemStack();
	}
	public ItemStack getItemStackFor(IStateEnum enumitem, int quantity) {
		return values.get(enumitem).getItemStack(quantity);
	}

	
	public ItemStack getItemStackFor(IBlockState state) {
		for (BlockClassEntry enumitem: values()) {
			if (enumitem.block == state.getBlock() && enumitem == state.getValue(((IMultiblock)enumitem.block).getTypeProperty())) return enumitem.getItemStack();
		}
		
		for (BlockClassEntry enumitem: values()) {
			System.out.println((enumitem.block == state.getBlock()) + " && " + enumitem + " == " + state.getValue(((IMultiblock)enumitem.block).getTypeProperty()));
		}
		
		throw new RuntimeException("Meta not found for state " + state + "\r\n num values: " + values().length);
	}
	
	public String getCommandSenderName() {
		return name;
	}
		
   

	
}

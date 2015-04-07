package at.tyron.vintagecraft.BlockClass;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import javax.management.RuntimeErrorException;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import at.tyron.vintagecraft.Block.BlockVC;
import at.tyron.vintagecraft.Interfaces.IMultiblock;
import at.tyron.vintagecraft.Interfaces.IStateEnum;

public abstract class BlockClass {
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
		System.out.println("init " + (values().length) + " of type " + name + " (block = " + blockclass + ")");
		
		// I hate Java not being able to allow method overriding of static methods :/
		int typesperblock = 1;
		try {
			typesperblock = (Integer)invokeMethod(blockclass, blockclass.newInstance(), "multistateAvailableTypes", new Object[0]);
		} catch (Exception e) { System.out.println("Unable to get multistateAvailableTypes ("+e.getMessage()+") for "+name+"! Will use 1 (= waste of blockids)"); } 
		
		BlockClassEntry[][] chunked = split(values(), typesperblock);
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for (BlockClassEntry[] blockclassentrychunk : chunked) {
			System.out.println("register chunk piece of size " + blockclassentrychunk.length);
			Block block;
			try {
				block = blockclass.newInstance();
				blocks.add(block);
				
				int meta = 0;
				for (BlockClassEntry blockclassentry : blockclassentrychunk) {
					System.out.println("init blockclassentry " + (name + ((blocks.size() > 1) ? blocks.size() : "")) + " with meta " + meta + "     (key = " + blockclassentry.key.getStateName() + ")");
					blockclassentry.init(block, meta++);
				}
				
				//blockclass.getDeclaredMethod("init", new Class[]{BlockClassEntry[].class, PropertyInteger.class}).invoke(block, new Object[]{blockstates, createProperty(name, blockstates.length)});
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
	
	
	/*public void genBlockStateJson(String name, IProperty[] properties) {
		IProperty property = properties[0];
				
		for (Object value : property.getAllowedValues()) {
			IStringSerializable val = (IStringSerializable)value;
			
		}
		
		for (IProperty property : properties) {
			
			
		}
	}*/
	
	
	static <T> T[][] split(T[] elements, int chunksize) {
		int chunks = (int) Math.ceil((1f * elements.length) / chunksize);
		
		/*for (T element : elements) {
			System.out.println(((IEnumState)element).getStateName());
		}*/
		
		ArrayList<T> result = new ArrayList<T>(); 
		
		for (int i = 0; i < chunks; i++) {
			result.add((T) Arrays.copyOfRange(elements, i * chunksize, Math.min(elements.length, i*chunksize + chunksize)));
		}
		
		return (T[][]) result.toArray((T[])Array.newInstance(elements.getClass(), 0));
		
	}
	
	
	public BlockClassEntry getBlockClassfromState(IBlockState state) {
		return getBlockClassfromMeta(state.getBlock(), state.getBlock().getMetaFromState(state));
	}
	
	public BlockClassEntry getBlockClassfromMeta(Block block, int meta) {
		for (BlockClassEntry enumitem: values()) {
			if (enumitem.metadata == meta && enumitem.block == block) return enumitem;
		}
		
		for (BlockClassEntry enumitem: values()) {
			System.out.println(enumitem.metadata+" == "+meta+" && "+enumitem.block.getUnlocalizedName()+" == "+block.getUnlocalizedName());
		}
		
		throw new RuntimeException("BlockClassEntry not found for block " + block + " / meta " + meta);
	}
	
	
	public BlockClassEntry getFromKey(IStateEnum key) {
		return values.get(key);
	}

	
	
	/*public int getMetaFromBlockClass(BlockClassEntry blockclassentry) {
		for (BlockClassEntry enumitem: values()) {
			if (enumitem.getId() == blockclassentry.getId()) return enumitem.metadata;
		}

		throw new RuntimeException("Meta not found for blockclass " + blockclassentry);
	}*/
	
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

	public ItemStack getItemStackFor(IStateEnum enumitem) {
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
	
	public String getName() {
		return name;
	}
		
   

	
}

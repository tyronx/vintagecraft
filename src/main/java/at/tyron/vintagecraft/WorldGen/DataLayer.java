package at.tyron.vintagecraft.WorldGen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class DataLayer {
	public static DataLayer[] layers = new DataLayer[256];

	public static DataLayer Granite = new DataLayer(0, Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 0, 0, "Granite");
	public static DataLayer Diorite = new DataLayer(1, Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), 1, 1, "Diorite");
	public static DataLayer Andesite = new DataLayer(15, Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), 2, 13, "Andesite");
	public static DataLayer SandStone = new DataLayer(15, Blocks.sandstone.getDefaultState(), 2, 13, "Sandstone");
	public static DataLayer Slate = new DataLayer(18, Blocks.stone.getDefaultState(), 1, 16, "Slate");
	

	public static DataLayer NoTree = new DataLayer(29, -1);
	public static DataLayer Ash = new DataLayer(30, 7);
	public static DataLayer Aspen = new DataLayer(31, 1);
	public static DataLayer Birch = new DataLayer(32, 2);
	public static DataLayer Chestnut = new DataLayer(33, 3);
	public static DataLayer DouglasFir = new DataLayer(34, 4);
	public static DataLayer Hickory = new DataLayer(35, 5);
	public static DataLayer Koa = new DataLayer(45, 0);
	public static DataLayer Maple = new DataLayer(36, 6);
	public static DataLayer Oak = new DataLayer(37, 0);
	public static DataLayer Pine = new DataLayer(38, 8);
	public static DataLayer Redwood = new DataLayer(39, 9);
	public static DataLayer Spruce = new DataLayer(40, 10);
	public static DataLayer Sycamore = new DataLayer(41, 11);
	public static DataLayer SavannahAcacia = new DataLayer(46, 0);
	public static DataLayer WhiteCedar = new DataLayer(42, 12);
	public static DataLayer WhiteElm = new DataLayer(43, 13);
	public static DataLayer Willow = new DataLayer(44, 14);

	public static DataLayer EVT_0_125 = new DataLayer(80,"0.125", 0.125f);
	public static DataLayer EVT_0_25 = new DataLayer(81,"0.25", 0.25f);
	public static DataLayer EVT_0_5 = new DataLayer(82,"0.5", 0.5f);
	public static DataLayer EVT_1 = new DataLayer(83,"1", 1f);
	public static DataLayer EVT_2 = new DataLayer(84,"2", 2f);
	public static DataLayer EVT_4 = new DataLayer(85,"4", 4f);
	public static DataLayer EVT_8 = new DataLayer(86,"8", 8f);
	public static DataLayer EVT_16 = new DataLayer(87,"16", 16f);

	public static DataLayer Rain_62_5 = new DataLayer(90,"62.5", 62.5f);
	public static DataLayer Rain_125 = new DataLayer(91,"125", 125f);
	public static DataLayer Rain_250 = new DataLayer(92,"250", 250f);
	public static DataLayer Rain_500 = new DataLayer(93,"500", 500f);
	public static DataLayer Rain_1000 = new DataLayer(94,"1000", 1000f);
	public static DataLayer Rain_2000 = new DataLayer(95,"2000", 2000f);
	public static DataLayer Rain_4000 = new DataLayer(96,"4000", 4000f);
	public static DataLayer Rain_8000 = new DataLayer(97,"8000", 8000f);

	public static DataLayer SeismicStable = new DataLayer(110, 0);
	public static DataLayer SeismicUnStable = new DataLayer(111, 1);

	public static DataLayer DrainageNone = new DataLayer(120, "None", 0);
	public static DataLayer DrainageVeryPoor = new DataLayer(121, "Very Poor", 1);
	public static DataLayer DrainagePoor = new DataLayer(122, "Poor", 2);
	public static DataLayer DrainageNormal = new DataLayer(123, "Normal", 3);
	public static DataLayer DrainageGood = new DataLayer(124, "Good", 4);
	public static DataLayer DrainageVeryGood = new DataLayer(125, "Very Good", 5);

	public static DataLayer PH_AcidHigh = new DataLayer(130, "High Acidity", 0);
	public static DataLayer PH_AcidLow = new DataLayer(131, "Low acidity", 1);
	public static DataLayer PH_Neutral = new DataLayer(132, "Neutral", 2);
	public static DataLayer PH_AlkalineLow = new DataLayer(133, "Low Alkalinity", 3);
	public static DataLayer PH_AlkalineHigh = new DataLayer(134, "High Alkalinity", 4);

	public int ID;
	public IBlockState block = Blocks.air.getDefaultState();
	public int data1;
	public int data2;//used as metadata in rocks
	public float floatdata1;
	public String name = "";

	public DataLayer(int index, int i)
	{
		ID = index;
		data1 = i;
		data2 = 0;
		floatdata1 = 0;
		layers[index] = this;
	}

	public DataLayer(int index, IBlockState b, int meta)
	{
		ID = index;
		block = b;
		data1 = 0;
		data2 = meta;
		floatdata1 = 0;
		layers[index] = this;
	}

	public DataLayer(int index, IBlockState b, int meta, int altMeta, String n)
	{
		ID = index;
		block = b;
		data1 = altMeta;
		data2 = meta;
		floatdata1 = 0;
		name = n;
		layers[index] = this;
	}

	public DataLayer(int index, String n, IBlockState b)
	{
		ID = index;
		block = b;
		data1 = 0;
		data2 = 0;
		floatdata1 = 0;
		name = n;
		layers[index] = this;
	}

	public DataLayer(int index, String n, int i)
	{
		ID = index;
		floatdata1 = 0;
		data1 = i;
		data2 = 0;
		name = n;
		layers[index] = this;
	}

	public DataLayer(int index, String n, float f)
	{
		ID = index;
		floatdata1 = f;
		data1 = 0;
		data2 = 0;
		name = n;
		layers[index] = this;
	}
}

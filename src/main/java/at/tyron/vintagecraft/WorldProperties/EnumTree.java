package at.tyron.vintagecraft.WorldProperties;

import at.tyron.vintagecraft.block.BlockVC;
import at.tyron.vintagecraft.interfaces.IEnumState;

public enum EnumTree implements IEnumState {
//	ASH						(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
	BIRCH					(100, 3, 12, 95, 170, 10, 100, 0f, 0.5f),		// http://en.wikipedia.org/wiki/Birch
//	LARCH					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://upload.wikimedia.org/wikipedia/commons/2/28/Larix_sibirica_-_Siperianlehtikuusi,_Sibirisk_l%C3%A4rk,_Siberian_larch_IMG_9213_C.JPG
	OAK						(100, 0,  0,  0,    0, 0,   0, 0f, 0f),  		// http://cdn1.arkive.org/media/4A/4A8E82BD-D487-4C74-805B-7C29A318029F/Presentation.Large/Sessile-oak-tree-in-winter.jpg
	MAPLE					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://m3.i.pbase.com/u37/10kzoomfz/large/24239783.tree.jpg
	MOUNTAINDOGWOOD			(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
//	WILLOW					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// https://c1.staticflickr.com/9/8467/8371823596_2c2e65240e.jpg
	
//	APPLE					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://www.gardenista.com/files/styles/733_0s/public/img/sub/uimg/01-2013/700_pruned-apple-tree-jpeg.jpg
	
	SCOTSPINE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://en.wikipedia.org/wiki/Scots_pine
	//FOXTAILPINE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://en.wikipedia.org/wiki/Pinus_balfouriana
//	PINYONPINE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://www.history.com/images/media/slideshow/new-mexico/new-mexico-state-tree-pinyon-pine.jpg  | http://images.fineartamerica.com/images-medium-large/pinyon-pine-tree-james-marvin-phelps.jpg
	
	SPRUCE					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
	
//	AFRICANBLACKWOOD		(100, 0,  0,  0,    0, 0,   0, 0f, 0f),  		// http://www.prota4u.org/plantphotos/Dalbergia%20melanoxylon%202.jpg
//	BAOBAB					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
	ACACIA					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://static.panoramio.com/photos/large/37061566.jpg
	KAPOK					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://www.rainforest-alliance.org/sites/default/files/uploads/4/kapok-tree-inset2.jpg
//	SEQUOIA					(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// 
	
	// Jungle Trees: http://www.rainfor.org/upload/publication-store/2013/ter%20Steege/ter%20Steege%20et%20al%202013%20Hyperdominance%20in%20the%20Amazonian%20Tree%20Flora%20Science.pdf
	
//	DIPTEROCARP				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			//   http://atbioversity.net/edukitmys/images/Shorea.JPG (http://en.wikipedia.org/wiki/Dipterocarpaceae)
	
	COCONUTPALM				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
//	DATEPALM				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),
	
//	COCOATREE				(100, 0,  0,  0,    0, 0,   0, 0f, 0f),			// http://images.fineartamerica.com/images-medium-large/cacao-tree-granger.jpg
	
	
	;

	
	int weight;
	
	int mintemp;
	int maxtemp ;
	
	int minrain;
	int maxrain;
	
	int minfertility;
	int maxfertility;
	
	// Height of 0f == sealevel
	// Height of 1f == 255
	float minheight;
	float maxheight;
	
	
	
	EnumTree(int weight, int mintemp, int maxtemp, int minrain, int maxrain, int minfertility, int maxfertility, float minheight, float maxheight) {
		this.weight = weight;
		this.mintemp = mintemp;
		this.maxtemp = maxtemp;
		this.minrain = minrain;
		this.maxrain = maxrain;
		this.minfertility = minfertility;
		this.maxfertility = maxfertility;
	}
	
	
	@Override
	public int getMetaData(BlockVC block) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStateName() {
		return name().toLowerCase();
	}

	@Override
	public void init(BlockVC block, int meta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getId() {
		return ordinal();
	}
}

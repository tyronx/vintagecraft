package at.tyron.vintagecraft.WorldProperties;

public enum EnumItemSize {
	TINY (10),
	SMALL (30),
	MEDIUM (50),
	LARGE (70),
	HUGE (100)
	
	;
	
	int size;
	
	private EnumItemSize(int size) {
		this.size = size;
	}
	
	public int getExactSize() {
		return size;
	}
}

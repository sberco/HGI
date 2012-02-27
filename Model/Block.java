package Model;

public class Block implements Comparable<Block> {
	
	public Block( int ID, int firstWindow, int lastWindow, double threshold ) {
		this.ID = ID;
		this.firstWindow = firstWindow;
		this.lastWindow = lastWindow;
		this.threshold = threshold;
	}
	
	public int compareTo(Block o) {
		return firstWindow-o.firstWindow;
	}
	
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	public int getID() {
		return ID;
	}
	
	public int getFirstWindow() {
		return firstWindow;
	}
	
	public int getLastWindow() {
		return lastWindow;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	private int ID;
	private int firstWindow;
	private int lastWindow;
	private double threshold;
}

package Model;

public class Block implements Comparable<Block> {
	
	public Block( int ID, int startWindow, int endWindow, double threshold ) {
		this.ID = ID;
		this.startWindow = startWindow;
		this.endWindow = endWindow;
		this.threshold = threshold;
	}
	
	public int compareTo(Block o) {
		return startWindow-o.startWindow;
	}
	
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	public int getID() {
		return ID;
	}
	
	public int getStartWindow() {
		return startWindow;
	}
	
	public int getEndWindow() {
		return endWindow;
	}
	
	public double getThreshold() {
		return threshold;
	}
	
	private int ID;
	private int startWindow;
	private int endWindow;
	private double threshold;
}

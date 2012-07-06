package Model;

public class Block implements Comparable<Block> {
	
	public Block( int ID, double startCM, double endCM, int winIdx[], double thresholds[] ) {
		this.ID = ID;
		this.startCM = startCM;
		this.endCM = endCM;
		this.winIdx = winIdx;
		this.thresholds = thresholds;
	}
	
	public int compareTo(Block o) {
		if ( startCM-o.startCM>0 ) return 1;
		if ( startCM-o.startCM<0 ) return -1;
		return 0;
	}
	
	public boolean equals(Object arg0) {
		return super.equals(arg0);
	}

	public int getID() {
		return ID;
	}
	
	public double getStartCM() {
		return startCM;
	}
	
	public double getEndCM() {
		return endCM;
	}
	
	public int[] getWinIdx() {
		return winIdx;
	}
	
	public double[] getThresholds() {
		return thresholds;
	}
		
	private int ID;
	private double startCM;
	private double endCM;
	private int winIdx[];
	private double thresholds[];
}

package Experiments.IBDMap.WTCCC;

public class Interval {
	
	private int indIdx1, indIdx2, winStart, winEnd;
	private double score;
	
	public Interval( int indIdx1, int indIdx2, int winStart, int winEnd, double score ) {
		this.indIdx1 = indIdx1;
		this.indIdx2 = indIdx2;
		this.winStart = winStart;
		this.winEnd = winEnd;
		this.score = score;
		
		if ( indIdx1 > indIdx2 ) {
			this.indIdx2 = indIdx1;
			this.indIdx1 = indIdx2;
		}
		
	}
	
	public int getInd1() {
		return indIdx1;
	}
	
	public int getInd2() {
		return indIdx2;
	}	
	
	public double getScore() {
		return score;
	}
	
	public boolean overlap( int position ) {
		if ( position>=winStart && position<=winEnd ) return true;
		return false;
	}
	
}

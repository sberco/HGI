package Model;

public class WindowIndex {
	
	public WindowIndex( int numConfs ) {
		confLists = new int[numConfs][];
	}
	
	public void setConfList( int conf, int[] indIdxArr ) {
		confLists[conf] = indIdxArr;
	}
	
	public int[] getIndList( int conf ) {
		return confLists[conf];
	}
	
	public int getNumConfs() {
		return confLists.length;
	}
	
	private int[][] confLists;
	
}

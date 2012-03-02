package Model;

public class WindowIndex {
	
	public WindowIndex( int numConfs ) {
		confLists = new int[numConfs][];
		confStrArr = new String[ numConfs ];
	}
	
	public void setConfList( int conf, String confStr, int[] indIdxArr ) {
		confLists[conf] = indIdxArr;
		confStrArr[conf] = confStr;
	}
	
	public int[] getIndList( int conf ) {
		return confLists[conf];
	}
	
	public String getConfigurationString( int conf ) {
		return confStrArr[ conf ];
	}
	
	public int getNumConfs() {
		return confLists.length;
	}
	
	public int getNumAllocatedConfs() {
		int count=0;
		for ( int i=0;i<confLists.length;i++ ) {
			if ( confLists[i]!=null ) count++;
		}
		return count;
	}
	
	private int[][] confLists;
	private String[] confStrArr;
	
}

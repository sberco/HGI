package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import Utils.Arrays;

public class Index {
	
	public Index( int windowSize ) {
		this.numConfs = (int)Math.pow(3, windowSize);
		this.windowSize = windowSize;
		windowIndexMap = new HashMap<Integer, WindowIndex>(); 
	}
	
	public int getNumConfs() {
		return numConfs;
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public WindowIndex getWindowIndex( int windowIdx ) {
		if ( !windowIndexMap.containsKey( windowIdx ) ) {
			windowIndexMap.put( windowIdx, new WindowIndex( numConfs ) );
		}
		return windowIndexMap.get( windowIdx );
	}
	
	public static Index load( String fileName, int windowSize ) throws IOException {
		Index index = new Index( windowSize );
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		while ( (line=br.readLine())!=null ) {
			StringTokenizer st = new StringTokenizer( line );
			int windowIdx = Integer.parseInt( st.nextToken() );
			int conf = Integer.parseInt( st.nextToken() );
			String confStr = st.nextToken();
			Vector<Integer> indIdxVec = new Vector<Integer>();
			String indexListStr = st.nextToken();
			String indices[] = indexListStr.split(",");
			for ( String indIdxStr : indices ) {
				int indIdx = Integer.decode( indIdxStr );
				indIdxVec.add( indIdx );
			}
			int indIdxArr[] = Arrays.toPrimitiveInteger( indIdxVec );
			WindowIndex wi = index.getWindowIndex( windowIdx );
			wi.setConfList( conf, confStr, indIdxArr );
		}
		br.close();
		return index;
	}
	
	private HashMap<Integer, WindowIndex> windowIndexMap;
	private int numConfs, windowSize;
	
}

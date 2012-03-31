package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Scores {
	
	public Scores( int windowSize ) {
		this.windowSize = windowSize;
		numConfs = (int)Math.pow(3, windowSize );
		scoreMap = new HashMap<Integer, double[]>();
		badSet = new HashSet<Integer>();
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public boolean isBad( int index ) {
		return badSet.contains(index);
	}
	
	public void setBadIndex( int index ) {
		badSet.add( index );
	}
	
	
	public void setScore( int windowIdx, double scoresArr[] ) {
		scoreMap.put( windowIdx, scoresArr );
	}
	
	public double[] getScore( int windowIdx ) {
		return scoreMap.get( windowIdx );
	}
	
	public double getScore( int windowIdx, int conf1, int conf2 ) {
		double [] scoreArr = scoreMap.get( windowIdx );
		return scoreArr[ conf2*numConfs+conf1 ];
	}
	
	public static Scores load( String fileName, int windowSize ) throws IOException {
		Scores score = new Scores( windowSize );
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		int numConfs = (int)Math.pow( 3, windowSize );
		int count = 0;
		while ((line=br.readLine())!=null ) {
			count++;
//			System.out.println("C:"+count);
			StringTokenizer st = new StringTokenizer( line );
			int windowIdx = Integer.decode( st.nextToken() );
			double fullScoreMatrix[] = new double[ numConfs* numConfs ];			
			for ( int i=0;i<numConfs;i++ ) {
				for (int j=i;j<numConfs;j++ ) {
//				for (int j=0;j<=i;j++ ) {
					String valStr = st.nextToken();
					if ( isBad( valStr ) ) score.setBadIndex( windowIdx );
					double v = Double.parseDouble( normalize(valStr) );
					fullScoreMatrix[i*numConfs+j] = v;
					fullScoreMatrix[j*numConfs+i] = v;
				}
			}
			
			//
			//	Inflate score to full matrix
			score.setScore( windowIdx, fullScoreMatrix );
		}
		br.close();
		return score;
	}
	
	private static String normalize( String str ) {
		if (str.indexOf("inf")!=-1 || str.indexOf("nan")!=-1) return "0";
		return str;
	}
	
	private static boolean isBad( String str ) {
		if (str.indexOf("inf")!=-1 || str.indexOf("nan")!=-1) return true;
		return false;
	}
	
	
	private int windowSize, numConfs;
	private HashMap<Integer, double[]> scoreMap;
	private HashSet<Integer> badSet;
	
}

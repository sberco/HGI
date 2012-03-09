package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import Utils.Arrays;

public class Scores {
	
	public Scores( int windowSize ) {
		this.windowSize = windowSize;
		numConfs = (int)Math.pow(2, windowSize );
		scoreMap = new HashMap<Integer, double[]>();
	}
	
	public int getWindowSize() {
		return windowSize;
	}
	
	public void setScore( int windowIdx, double scoresArr[] ) {
		scoreMap.put( windowIdx, scoresArr );
	}
	
	public double[] getScore( int windowIdx ) {
		return scoreMap.get( windowIdx );
	}
	
	public double getScore( int windowIdx, int conf1, int conf2 ) {
		return scoreMap.get( windowIdx )[ conf2*numConfs+conf1 ];
	}
	
	public static Scores load( String fileName, int windowSize ) throws IOException {
		Scores score = new Scores( windowSize );
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		int numConfs = (int)Math.pow( 3, windowSize );
		int count = 0;
		while ((line=br.readLine())!=null ) {
			count++;
			System.out.println("C:"+count);
			StringTokenizer st = new StringTokenizer( line );
			int windowIdx = Integer.decode( st.nextToken() );
			double fullScoreMatrix[] = new double[ numConfs* numConfs ];			
			for ( int i=0;i<numConfs;i++ ) {
				for (int j=i;j<numConfs;j++ ) {
					double v = Double.parseDouble( normalize(st.nextToken()) );
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
	
	private int windowSize, numConfs;
	private HashMap<Integer, double[]> scoreMap;
	
}

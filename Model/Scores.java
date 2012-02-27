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
		if ( conf1<conf2 ) {
			int tmp = conf1;
			conf1 = conf2;
			conf2 = tmp;
		}
		return scoreMap.get( windowIdx )[ conf2*numConfs+conf1 ];
	}
	
	public static Scores load( String fileName, int windowSize ) throws IOException {
		Scores score = new Scores( windowSize );
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		while ((line=br.readLine())!=null ) {
			StringTokenizer st = new StringTokenizer( line );
			int windowIdx = Integer.decode( st.nextToken() );
			Vector<Double> scores = new Vector<Double>();
			for ( int i=0;i<windowSize;i++ ) {
				for (int j=i;j<windowSize;j++ ) {
					scores.add( Double.parseDouble( st.nextToken()) );
				}
			}
			double scoresArr[] = Arrays.toPrimitive(scores);
			score.setScore( windowIdx, scoresArr );
		}
		br.close();
		return score;
	}
	
	private int windowSize, numConfs;
	private HashMap<Integer, double[]> scoreMap;
	
}

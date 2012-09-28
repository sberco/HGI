package Experiments.IBDMap.WTCCC;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.Vector;


public class Intervals {
	
	private Vector<Interval> intervals;
	private IndividualList indList;
	private int maxPos = -1;
	
	private Intervals( IndividualList indList ) {
		intervals = new Vector<Interval>();
		this.indList = indList;
	}
	
	public static Intervals load( String fileName, IndividualList indList ) throws IOException  {
		BufferedReader br = new BufferedReader( new FileReader( fileName ) );
		String line;
		
		//
		//	Initialize intervals
		Intervals intervals = new Intervals( indList );
		
		//
		//	Skip header
		br.readLine();
		
		//
		//	Parse lines
		while ( (line=br.readLine())!=null ) {
			StringTokenizer st = new StringTokenizer( line ) ;

			//
			//	Get individuals
			String id1 = st.nextToken();
			String id2 = st.nextToken();
			int indIdx1 = indList.getIndividualIndex( id1 );
			int indIdx2 = indList.getIndividualIndex( id2 );
			
			//
			//	Skip fields
			st.nextToken();
			st.nextToken();
			
			//
			//	Get region
			int winIdx1 = Integer.decode( st.nextToken() );
			int winIdx2 = Integer.decode( st.nextToken() );
			intervals.updateMax( winIdx2 );
			
			//
			//	Skip fields
			st.nextToken();
			st.nextToken();
			
			double score = Double.parseDouble( st.nextToken() );
			
			intervals.add( new Interval( indIdx1, indIdx2, winIdx1, winIdx2, score ) );
			
		}
		br.close();
		
		return intervals;
	}
	
	
	public void add( Interval interval ) {
		intervals.add( interval );
	}
	
	 
	public int countUnique( int position ) {
		
		HashSet<Integer> pairs = new HashSet<Integer>();
		
		for ( Interval interval : intervals ) {
			if ( interval.overlap( position ) ) {
				int pairIdx = interval.getInd1() * indList.size() + interval.getInd2();
				pairs.add( pairIdx );
			}
		}
		
		return pairs.size();
	}
	
	public double sumScore( int position ) {
		
		HashMap<Integer,Double> pairs = new HashMap<Integer,Double>();
		
		//
		//	Enumerate intervals
		for ( Interval interval : intervals ) {
			if ( interval.overlap( position ) ) {
				int pairIdx = interval.getInd1() * indList.size() + interval.getInd2();
				if ( !pairs.containsKey( pairIdx ) ) {
					pairs.put( pairIdx, 0.0);
				}
				pairs.put( pairIdx, Math.max( pairs.get( pairIdx), interval.getScore()) );
			}
		}
		
		//
		//	Compute score
		double globalScore = 0;
		for ( int i : pairs.keySet() ) {
			double s = pairs.get(i);
			globalScore += s;
		}
		
		return globalScore;
	}
	
	public double thresholdCount( int position, double threshold ) {
		
		HashMap<Integer,Double> pairs = new HashMap<Integer,Double>();
		
		//
		//	Enumerate intervals
		for ( Interval interval : intervals ) {
			if ( interval.overlap( position ) ) {
				int pairIdx = interval.getInd1() * indList.size() + interval.getInd2();
				if ( !pairs.containsKey( pairIdx ) ) {
					pairs.put( pairIdx, 0.0);
				}
				pairs.put( pairIdx, Math.max( pairs.get( pairIdx), interval.getScore()) );
			}
		}
		
		//
		//	Compute score
		double counts = 0;
		for ( int i : pairs.keySet() ) {
			double s = pairs.get(i);
			if ( s>threshold) counts++;
		}
		
		return counts;
	}
	
	
	public void updateMax( int newMaxPos ) {
		if ( newMaxPos> maxPos ) {
			maxPos = newMaxPos;
		}
	}
	
	public int getMaxPos() {
		return maxPos;
	}
	
	
}

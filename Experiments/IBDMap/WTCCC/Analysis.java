package Experiments.IBDMap.WTCCC;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import Utils.Log;

public class Analysis {
	
	private static String phenotypeNames[] = {	
		"58C","CAD","CD","HT","NBS","RA","T1D","T2D"
	};
	
	private static double sampleSize[] = {
			 1480.0,
			  1929.0,
			  1752.0,
			  1952.0,
			  1458.0,
			  1860.0,
			  1963.0,
			  1924.0
	};
	
//	private static String phenotypeNames[] = {	
//		"58C","RA"
//	};
//	
//	private static double sampleSize[] = {
//			 1480.0,
//			  1860.0
//	};
	
	
	private static IndividualList indLists[] = new IndividualList[phenotypeNames.length];
	private static Intervals intervals[] = new Intervals[phenotypeNames.length];
	
	private static String baseDir = "/Data/Genetics/Parente/Data/out/";
	
	
	public static void main(String[] args) {
		Log.log("Analyzing the WTCCC data.");
		Log.enter();
			
		//
		//	Loading individuals
		try {
			Log.log("Loading individual lists");
			Log.enter();			
			for ( int i=0;i<phenotypeNames.length;i++ ) {
				indLists[i] = IndividualList.load( baseDir+phenotypeNames[i]+".tfam");
			}			
			Log.exit();			
		} catch ( IOException exp ) {
			exp.printStackTrace();
		}
		
		//
		//	Loading intervals
		try {
			
//			for ( int chr=1;chr<23;chr++ ) {
			for ( int chr=22;chr>0;chr-- ) {
				Log.log("Analyzing chromosome ["+chr+"]");
				Log.enter();
				
				int maxPos = -1;
				Log.log("Loading intervals");
				Log.enter();				
				for ( int i=0;i<phenotypeNames.length;i++ ) {					
					Log.log("Analyzing genotypes from individuals with phenotype ["+phenotypeNames[i]+"]");
					Log.enter();
					intervals[i] = Intervals.load( baseDir+phenotypeNames[i]+"-chr"+chr+".call", indLists[i]);
					Log.exit();	
					
					//
					//	Update max pos
					maxPos = Math.max( intervals[i].getMaxPos(), maxPos );
					
				}			
				Log.exit();
				
				//
				//	Output positions
				Log.log("Writing results");
				Log.enter();
				BufferedWriter bw = new BufferedWriter( new FileWriter( baseDir + "out."+chr ) );
				
				//
				//	Output header
				bw.write("POS");
				for ( int i=0;i<phenotypeNames.length;i++ ) {
					bw.write( "\t"+phenotypeNames[i] );
				}
				bw.write("\n");
				
				//
				//	Compute the number of pairs per origin
				double numPairs[] = new double[ phenotypeNames.length ];
				for ( int i=0;i<phenotypeNames.length;i++ ) {
					numPairs[i] = (sampleSize[i] * (sampleSize[i]+1)) /2.0;
				}
				
				for ( int pos=0;pos<=maxPos;pos++ ) {
					if ( (pos % 100) ==0 ) {
						Log.log("Progress ["+pos+"/"+maxPos+"]");
					}
					
//				for ( int pos=100;pos<=100;pos++ ) {
					bw.write(""+pos);
					for ( int i=0;i<phenotypeNames.length;i++ ) {
//						bw.write( "\t"+(intervals[i].countUnique(pos) / numPairs[i]) );
//						bw.write( "\t"+(intervals[i].sumScore(pos)  / numPairs[i] ) );
						bw.write( "\t"+(intervals[i].thresholdCount(pos, 20)  / numPairs[i] ) );
					}
					bw.write("\n");
				}
				bw.close();
				Log.exit();
				
				Log.exit();				
			}
			
		} catch ( IOException exp ) {
			exp.printStackTrace();
		}		
		
		Log.exit();
		Log.log("Done.");
	}
	
}

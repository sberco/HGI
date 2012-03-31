package Analysis;

import java.util.HashSet;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

import Model.Block;
import Model.Blocks;
import Model.Index;
import Model.Labels;
import Model.Scores;
import Model.WindowIndex;
import Utils.Arrays;

public class LinearAnalysis {

	public int [] getRelated( Labels labels, int[] queryConfs, Index index, Blocks blocks, Scores scoresMatrix ) {

		HashSet<Integer> relatedIndividuals = new HashSet<Integer>();

		double scores[] = new double[ labels.size() ];
		for ( int i=0;i<labels.size();i++ ) scores[i] = 0;
		int lastStart = 100000000;

		//
		//	Enumerate over blocks
		for ( Block block : blocks ) {

			//
			//	Zero scores
			for ( int i=0;i<scores.length;i++ ) scores[i] = 0;
			
			//
			//	Enumerate over windows within blocks
			boolean shouldSkipBlock = false;
			for ( int winIdx=block.getFirstWindow();winIdx<=block.getLastWindow();winIdx++ ) {
				
				//
				//	Skip bad-score windows
				if ( scoresMatrix.isBad(winIdx) ) {
					shouldSkipBlock = true;
					break;
				}
				
				WindowIndex wi = index.getWindowIndex(winIdx);
				
				//
				//	Enumerate over the configurations
				for ( int c=0;c<index.getNumConfs();c++ ) {
					
					double localScores = scoresMatrix.getScore( winIdx, queryConfs[winIdx], c );
					int indIdxArr[] = wi.getIndList(c);
					if ( indIdxArr==null ) continue;
					
					for ( int indIdx : indIdxArr ) {
						scores[indIdx] += localScores;
					}
				}
			}
			if ( shouldSkipBlock ) continue;


//						//
//						//	Remove left tail
//						for ( int winIdx=lastStart;winIdx<block.getFirstWindow();winIdx++ ) {
//							WindowIndex wi = index.getWindowIndex(winIdx);
//							for ( int c=0;c<index.getNumConfs();c++ ) {
//								double localScores = scoresMatrix.getScore( winIdx, queryConfs[winIdx], c );
//								for ( int indIdx : wi.getIndList(c) ) {
//									scores[indIdx] -= localScores;
//								}
//							}
//						}
//						
//						//
//						//	Add right tail
//						for ( int winIdx=block.getFirstWindow();winIdx<=block.getLastWindow();winIdx++ ) {
//							WindowIndex wi = index.getWindowIndex(winIdx);
//							for ( int c=0;c<index.getNumConfs();c++ ) {
//								double localScores = scoresMatrix.getScore( winIdx, queryConfs[winIdx], c );
//								int indIdxArr[] = wi.getIndList(c);
//								if ( indIdxArr==null ) continue;
//								for ( int indIdx : indIdxArr ) {
//									scores[indIdx] += localScores;
//								}
//							}				
//						}

			//
			//	Determine if any of the scores are above the threshold
			double maxScore = scores[0];
			for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
				if ( scores[indIdx]>maxScore ) maxScore = scores[indIdx];
				if ( scores[indIdx]>block.getThreshold() ) {
//				if ( scores[indIdx]>Math.max(20.3,block.getThreshold()) ) {
					relatedIndividuals.add( indIdx );
					
					//
					//	TODO: Shows individual specific scores --
					//	TODO: Turn this back on to see (very) relevant information
					System.out.println("\t"+""+indIdx+"\t"+block.getFirstWindow()+"\t"+block.getLastWindow()+"\t"+scores[indIdx]+"\t"+block.getThreshold());

				}
			}
//			if ( block.getFirstWindow()> 200 ) System.exit(-1);
//			System.out.println("M:"+ maxScore + "\t"+ block.getThreshold());
			
//			Vector<String> scoresStr = new Vector<String>();
//			for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
//				scoresStr.add( scores[indIdx]+"" );
//			}
//			System.out.println( block.getFirstWindow()+"-"+block.getLastWindow()+"\t"+	block.getStartCM()+"\t"+(block.getStartCM()+block.getDistCM())+"\t"+StringUtils.join( scoresStr ,"\t") );
			
		}

		Vector<Integer> relatedIndividualsVec = new Vector<Integer>();
		for ( int i : relatedIndividuals ) {
			relatedIndividualsVec.add( i );
		}

		return Arrays.toPrimitiveInteger( relatedIndividualsVec );
	}

}

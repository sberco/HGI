package Analysis;

import java.util.HashSet;
import java.util.Vector;

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

		for ( Block block : blocks ) {

			for ( int i=0;i<scores.length;i++ ) scores[i] = 0;
			for ( int winIdx=block.getFirstWindow();winIdx<=block.getLastWindow();winIdx++ ) {
				WindowIndex wi = index.getWindowIndex(winIdx);
				for ( int c=0;c<index.getNumConfs();c++ ) {
					double localScores = scoresMatrix.getScore( winIdx, queryConfs[winIdx], c );
					int indIdxArr[] = wi.getIndList(c);
					if ( indIdxArr==null ) continue;
					
					for ( int indIdx : indIdxArr ) {
						scores[indIdx] += localScores;
					}
				}
			}


			//			//
			//			//	Remove left tail
			//			for ( int winIdx=lastStart;winIdx<block.getFirstWindow();winIdx++ ) {
			//				WindowIndex wi = index.getWindowIndex(winIdx);
			//				for ( int c=0;c<index.getNumConfs();c++ ) {
			//					double localScores = scoresMatrix.getScore( winIdx, queryConfs[winIdx], c );
			//					for ( int indIdx : wi.getIndList(c) ) {
			//						scores[indIdx] -= localScores;
			//					}
			//				}
			//			}
			//			
			//			//
			//			//	Add right tail
			//			for ( int winIdx=block.getFirstWindow();winIdx<=block.getLastWindow();winIdx++ ) {
			//				WindowIndex wi = index.getWindowIndex(winIdx);
			//				for ( int c=0;c<index.getNumConfs();c++ ) {
			//					double localScores = scoresMatrix.getScore( winIdx, queryConfs[winIdx], c );
			//					int indIdxArr[] = wi.getIndList(c);
			//					if ( indIdxArr==null ) continue;
			//					for ( int indIdx : indIdxArr ) {
			//						scores[indIdx] += localScores;
			//					}
			//				}				
			//			}

			//
			//	Determine if any of the scores are above the threshold
			double maxScore = scores[0];
			for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
				if ( scores[indIdx]>maxScore ) maxScore = scores[indIdx];
				if ( scores[indIdx]>block.getThreshold() ) {
					relatedIndividuals.add( indIdx );
					System.out.println("\t"+""+indIdx+"\t"+block.getFirstWindow()+"\t"+block.getLastWindow());

				}
			}
			if ( block.getFirstWindow()> 200 ) System.exit(-1);
			System.out.println("M:"+ maxScore + "\t"+ block.getThreshold());
		}

		Vector<Integer> relatedIndividualsVec = new Vector<Integer>();
		for ( int i : relatedIndividuals ) {
			relatedIndividualsVec.add( i );
		}

		return Arrays.toPrimitiveInteger( relatedIndividualsVec );
	}

}

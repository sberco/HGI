package Analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import Model.Block;
import Model.Blocks;
import Model.Index;
import Model.Labels;
import Model.Relations;
import Model.Scores;
import Model.WinModel;
import Model.WinModels;
import Model.WindowIndex;
import Model.Windows;
import Utils.Arrays;

/**
 * 
 * This class computes LRT1 on the fly, based on single SNP scores (by addition)
 *
 */
public class LinearAnalysisComputeLRT1 {

	public int [] getRelated( Labels labels, int[] queryConfs, Index w1index, Blocks blocks, Windows windows, WinModels winModels, Scores w1ScoresMatrix, Relations relations, String queryID, Vector<Vector<Integer> > rel_blocks ) {

		HashSet<Integer> relatedIndividuals = new HashSet<Integer>();
		HashMap<Integer, Vector<Integer> > relatedBlocks = new HashMap<Integer, Vector<Integer> >();

		HashMap<Integer, Block> bestBlocks = new HashMap<Integer, Block>();
		HashMap<Integer, Double> bestScoreDiffs = new HashMap<Integer, Double>();

		double scores[] = new double[ labels.size() ];
		double localScoresArr[] = new double[ labels.size() ];
		for ( int i=0;i<labels.size();i++ ) scores[i] = 0;
		int lastStart = 100000000;

		////////////////////////////////////////////////////////////////////////////////
		//
		//	Checking whether some of the windows and or blocks are bad
		//
		////////////////////////////////////////////////////////////////////////////////

		//
		//	Enumerate over blocks
		for ( Block block : blocks ) {

			//
			//	Enumerate over windows within the block
			for (int winIdx : block.getWinIdx() ) {
				if ( w1ScoresMatrix.isBad(winIdx) ) {
					System.err.println("Block is bad..." + block.getID());
					break;
				}
			}
		}

		////////////////////////////////////////////////////////////////////////////////
		//
		//	Computing scores
		//
		////////////////////////////////////////////////////////////////////////////////

		//
		//	Enumerate over blocks
		int b = - 1;
		for ( Block block : blocks ) {
			b++;
			//
			//	Reset block scores
			for ( int i=0;i<scores.length;i++ ) scores[i] = 0;

			//
			//	Enumerate over windows within the block
			boolean shouldSkipBlock = false;
			for (int winIdx : block.getWinIdx() ) {

				//
				//	Skip bad-score windows
				if ( w1ScoresMatrix.isBad(winIdx) ) {
					shouldSkipBlock = true;
					System.err.println("Block is bad..." + block.getID());
					break;
				}

				//
				//	Detemrine local win model
				WinModel winModel = winModels.getModel( winIdx );

				//
				//	Reset local window scores
				for ( int i=0;i<localScoresArr.length;i++ ) localScoresArr[i] = 0;

				//
				//	Enumerate over all SNPs in the window to compute LRT1 score
				for ( int snpIdx : windows.get( winIdx ).getSNPs() ) {

					//
					//	Compute LRT1 scores
					WindowIndex wi = w1index.getWindowIndex(snpIdx);
					for ( int c=0;c<w1index.getNumConfs();c++ ) {

						double localScores = w1ScoresMatrix.getScore( snpIdx, queryConfs[snpIdx], c );
						int indIdxArr[] = wi.getIndList(c);
						if ( indIdxArr==null ) continue;

						for ( int indIdx : indIdxArr ) {
							localScoresArr[indIdx] += localScores;
						}
					}
				}

				//
				//	Compute LRT2 scores given the LRT1 scores
				for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
					scores[indIdx] += winModel.logLikelihoodRatio( localScoresArr[indIdx] );
				}

			}	// END - Enumerate over windows

			if ( shouldSkipBlock ) continue;

			////////////////////////////////////////////////////////////////////////////////
			//
			//	Check block's results
			//
			////////////////////////////////////////////////////////////////////////////////

			//
			//	Determine if any of the scores are above the threshold
			double maxScore = scores[0];
			String i1 = "p1.i1";
			String i2 = "p1.i2";
			for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {

				//
				//	Keep track of highest score
				if ( scores[indIdx]>maxScore ) { 
					maxScore = scores[indIdx];
				}

				double scoreDiff = scores[indIdx] - block.getThresholds()[0];
				if (!bestScoreDiffs.containsKey(indIdx) || scoreDiff > bestScoreDiffs.get(indIdx)) {
					bestBlocks.put(indIdx, block);
					bestScoreDiffs.put(indIdx, scoreDiff);
				}

				//
				//	Check if individual passes minimal threshold for IBD
				if ( scores[indIdx]>block.getThresholds()[0] ) {

					//
					//	Add individual to results set
					relatedIndividuals.add( indIdx );

					//
					// Append this block to the list of blocks that this relative has IBD
					if ( !relatedBlocks.containsKey(indIdx) ) {
						relatedBlocks.put(indIdx, new Vector<Integer>());
					}
					relatedBlocks.get(indIdx).add( block.getID() );

					//
					//	TODO: Shows individual specific scores --
					//	TODO: Turn this back on to see (very) relevant information
					double cMStart = block.getStartCM();
					double cMEnd = block.getEndCM();
					System.err.println("IBD Block: " + b + " ind: "+ labels.getString(indIdx) +"\twin.startIdx: "+block.getWinIdx()[0]+"\twin.lastIdx: " +block.getWinIdx()[block.getWinIdx().length-1] + "\tsnp.startCM: " + cMStart + "\tsnp.stopIdx: " + cMEnd + "\tscore: "+(Math.round(scores[indIdx] * 100.0) / 100.0)+"\t> thresh: "+block.getThresholds()[0]);
				} // END - Statistic passed threshold
			} // END - Enumerate over individuals
		}	// END - Enumerate over blocks
		
		
		////////////////////////////////////////////////////////////////////////////////
		//
		//	Compute final results
		//
		////////////////////////////////////////////////////////////////////////////////

		for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
			String indName = labels.getString(indIdx);
			if (relations.isRelated(queryID, indName))
			{
				System.err.print(queryID + " is actually to be related to: " + indName);
				Block block = bestBlocks.get(indIdx);
				System.err.println(" best score diff = " + bestScoreDiffs.get(indIdx) + " @ block " + block.getID() + ", win.start=" + block.getWinIdx()[0] );

			}
		}

		// Prepare return values
		Vector<Integer> relatedIndividualsVec = new Vector<Integer>();
		rel_blocks.clear();
		for ( int i : relatedIndividuals ) {
			relatedIndividualsVec.add( i );
			rel_blocks.add(relatedBlocks.get(i));
		}

		return Arrays.toPrimitiveInteger( relatedIndividualsVec );

	}

}

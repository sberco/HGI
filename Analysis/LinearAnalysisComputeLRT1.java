package Analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;
import java.util.Properties;

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
import Model.Result;
import Utils.Arrays;
import java.util.Map;

/**
 * 
 * This class computes LRT1 on the fly, based on single SNP scores (by addition)
 *
 */
public class LinearAnalysisComputeLRT1 {

	public int [] getRelated( Labels labels, int[] queryConfs, Index snpIndex, Blocks blocks, Windows windows, WinModels winModels, Scores snpScoresMatrix, Relations relations, String queryID, Vector<Vector<Result> > rel_blocks, Properties opts ) throws java.io.IOException {

    boolean do_lrt2 = true;
    boolean do_tail_fix = true;
    if (opts.getProperty("doLrt2") != null && opts.getProperty("doLrt2").equals("false"))
      do_lrt2 = false;
    if (opts.getProperty("doTailFix") != null && opts.getProperty("doTailFix").equals("false"))
      do_tail_fix = false;

		HashSet<Integer> relatedIndividuals = new HashSet<Integer>();
		HashMap<Integer, Vector<Result> > relatedBlocks = new HashMap<Integer, Vector<Result> >();

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
		for ( Map.Entry<Integer, Block> blockEntry : blocks ) {

      Block block = blockEntry.getValue();
			//
			//	Enumerate over windows within the block
			for (int winIdx : block.getWinIdx() ) {
				if ( snpScoresMatrix.isBad(winIdx) ) {
					System.err.println("Block is bad..." + block.getID());
          System.exit(1);
				}
			}
		}

		////////////////////////////////////////////////////////////////////////////////
		//
		//	Computing scores
		//
		////////////////////////////////////////////////////////////////////////////////

    // Check if the query name is in the database names so we can skip it
    int dbIdxToSkip = -1;
    for (int dbIdx = 0; dbIdx < labels.size(); ++dbIdx)
    {
      String dbIndName = labels.getString(dbIdx);
      if (dbIndName.equals(queryID))
        dbIdxToSkip = dbIdx;
    }


		//
		//	Enumerate over blocks
		int b = - 1;
		for ( Map.Entry<Integer, Block> blockEntry : blocks ) {
      Block block = blockEntry.getValue();
      double thresh;
      {
        double[] tmp_thresholds = block.getThresholds().clone();
        java.util.Arrays.sort(tmp_thresholds);
        thresh = tmp_thresholds[0];
      }

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
				if ( snpScoresMatrix.isBad(winIdx) ) {
					System.err.println("Block is bad..." + block.getID());
					System.exit(1);
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
					//	Compute LRT1 score for window
					WindowIndex si = snpIndex.getWindowIndex(snpIdx);
					for ( int c=0;c<snpIndex.getNumConfs();c++ ) {

						double localScores = snpScoresMatrix.getScore( snpIdx, queryConfs[snpIdx], c );
						int indIdxArr[] = si.getIndList(c);
						if ( indIdxArr==null ) continue;

						for ( int indIdx : indIdxArr ) {
							localScoresArr[indIdx] += localScores;
						}
					}
				}

        if (do_lrt2)
        {
          //
          //	Compute LRT2 scores given the LRT1 scores
          for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
            scores[indIdx] += winModel.logLikelihoodRatio( localScoresArr[indIdx], do_tail_fix );
          }
        }
        else
        {
          for ( int indIdx=0;indIdx<scores.length;indIdx++ )
            scores[indIdx] += localScoresArr[indIdx];
        }
			}	// END - Enumerate over windows

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

        // Skip any db person with the same name as the query person
        if (indIdx == dbIdxToSkip)
          continue;

				//
				//	Keep track of highest score
				if ( scores[indIdx]>maxScore ) { 
					maxScore = scores[indIdx];
				}

				double scoreDiff = scores[indIdx] - thresh;
				if (!bestScoreDiffs.containsKey(indIdx) || scoreDiff > bestScoreDiffs.get(indIdx)) {
					bestBlocks.put(indIdx, block);
					bestScoreDiffs.put(indIdx, scoreDiff);
				}

				//
				//	Check if individual passes minimal threshold for IBD
				if ( scores[indIdx] > block.getThresholds()[0] ) {

					//
					//	Add individual to results set
					relatedIndividuals.add( indIdx );

					//
					// Append this block to the list of blocks that this relative has IBD
					if ( !relatedBlocks.containsKey(indIdx) ) {
						relatedBlocks.put(indIdx, new Vector<Result>());
					}
					relatedBlocks.get(indIdx).add(new Result(block, scores[indIdx]));

					//
					//	TODO: Shows individual specific scores --
					//	TODO: Turn this back on to see (very) relevant information
					// double cMStart = block.getStartCM();
					// double cMEnd = block.getEndCM();
					System.err.println("IBD Block: " + block.getID() + " ind: "+ labels.getString(indIdx) +"\twin.startIdx: "+block.getWinIdx()[0]+"\twin.lastIdx: " +block.getWinIdx()[block.getWinIdx().length-1] + "\tscore: "+(Math.round(scores[indIdx] * 100.0) / 100.0)+"\t> thresh: "+block.getThresholds()[0]);
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
		for ( int ind : relatedIndividuals ) {
			relatedIndividualsVec.add(ind);
			rel_blocks.add(relatedBlocks.get(ind));
		}

		return Arrays.toPrimitiveInteger( relatedIndividualsVec );
	}
}

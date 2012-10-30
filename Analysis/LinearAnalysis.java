package Analysis;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

//import org.apache.commons.lang3.StringUtils;

import Model.Block;
import Model.Blocks;
import Model.Index;
import Model.Labels;
import Model.Scores;
import Model.Relations;
import Model.WindowIndex;
import Utils.Arrays;
import java.util.HashMap;

public class LinearAnalysis {

  public int[] getRelated(Labels labels,
                          int[] queryConfs,
                          Index index,
                          Blocks blocks,
                          Scores scoresMatrix,
                          Relations relations,
                          String queryID,
                          Vector<Vector<Integer> > rel_blocks) {

    HashSet<Integer> relatedIndividuals = new HashSet<Integer>();
    HashMap<Integer, Vector<Integer> > relatedBlocks = new HashMap<Integer, Vector<Integer> >();

    HashMap<Integer, Block> bestBlocks = new HashMap<Integer, Block>();
    HashMap<Integer, Double> bestScoreDiffs = new HashMap<Integer, Double>();

    double scores[] = new double[labels.size()];
    for (int i = 0; i < labels.size(); i++) scores[i] = 0;
    int lastStart = 100000000;

    // Just to show which blocks are bad
    for (Map.Entry<Integer, Block> blockEntry : blocks) {
      final Block block = blockEntry.getValue();
      for (int winIdx = block.getFirstWindow(); winIdx <= block.getLastWindow(); ++winIdx)
      {
        if (scoresMatrix.isBad(winIdx))
        {
          System.err.println("Block is bad..." + block.getID());
          break;
        }
      }
    }

    //	Enumerate over blocks
    int b = -1;
    for (Map.Entry<Integer, Block> blockEntry : blocks) {
      final Block block = blockEntry.getValue();
      ++b;
      //	Zero scores
      for (int i = 0; i < scores.length; i++)
        scores[i] = 0;

      //	Enumerate over windows within blocks
      boolean shouldSkipBlock = false;
      for (int winIdx = block.getFirstWindow(); winIdx <= block.getLastWindow(); winIdx++) {

        //	Skip bad-score windows
        if (scoresMatrix.isBad(winIdx)) {
          shouldSkipBlock = true;
          System.err.println("Block is bad..." + block.getID());
          break;
        }

        WindowIndex wi = index.getWindowIndex(winIdx);

        //	Enumerate over the configurations
        for (int c = 0; c < index.getNumConfs(); c++) {
          double localScores = scoresMatrix.getScore(winIdx, queryConfs[winIdx], c);
          int indIdxArr[] = wi.getIndList(c);
          if (indIdxArr == null)
            continue;
          for (int indIdx : indIdxArr) {
            scores[indIdx] += localScores;
          }
        }
      }
      if (shouldSkipBlock)
        continue;


/*
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
*/

      //	Determine if any of the scores are above the threshold
      double maxScore = scores[0];
      String i1 = "p1.i1";
      String i2 = "p1.i2";
      for (int indIdx = 0; indIdx < scores.length; indIdx++) {

        if (scores[indIdx] > maxScore)
          maxScore = scores[indIdx];

        /* Debug for one specific test case 
        if (block.getID() == 465)
        {
          if (labels.getString(indIdx).equals(i1) && queryID.equals(i2)) {
            System.err.println("Score, thresh at known ibd block " + scores[indIdx] + ", " + block.getThreshold());
          }
        }
        */

        double scoreDiff = scores[indIdx] - block.getFirstThreshold();
        if (!bestScoreDiffs.containsKey(indIdx) || scoreDiff > bestScoreDiffs.get(indIdx))
        {
          bestBlocks.put(indIdx, block);
          bestScoreDiffs.put(indIdx, scoreDiff);
        }

        if (scores[indIdx] > block.getFirstThreshold()) {
          relatedIndividuals.add(indIdx);

          // Append this block to the list of blocks that this relative has IBD
          if (!relatedBlocks.containsKey(indIdx))
            relatedBlocks.put(indIdx, new Vector<Integer>());

          relatedBlocks.get(indIdx).add(b);
					
          //	TODO: Shows individual specific scores --
          //	TODO: Turn this back on to see (very) relevant information
          int snp_start = index.getWindowSize() * block.getFirstWindow();
          int snp_stop = index.getWindowSize() * block.getLastWindow();
          System.err.println("IBD Block: " + b +
                             " ind: " + labels.getString(indIdx) +
                             "\twin.startIdx: " + block.getFirstWindow() + 
                             "\twin.lastIdx: " + block.getLastWindow() +
                             "\tsnp.startIdx: " + snp_start +
                             "\tsnp.stopIdx: " + snp_stop +
                             "\tscore: " + (Math.round(scores[indIdx] * 100.0) / 100.0) +
                             "\t> thresh: " + block.getFirstThreshold());
        }
      }
/*
//			if ( block.getFirstWindow()> 200 ) System.exit(-1);
//			System.out.println("M:"+ maxScore + "\t"+ block.getThreshold());
			
//			Vector<String> scoresStr = new Vector<String>();
//			for ( int indIdx=0;indIdx<scores.length;indIdx++ ) {
//				scoresStr.add( scores[indIdx]+"" );
//			}
//			System.out.println( block.getFirstWindow()+"-"+block.getLastWindow()+"\t"+	block.getStartCM()+"\t"+(block.getStartCM()+block.getDistCM())+"\t"+StringUtils.join( scoresStr ,"\t") );
*/
    }

    for (int indIdx = 0; indIdx < scores.length; indIdx++) {
      String indName = labels.getString(indIdx);
      if (relations.isRelated(queryID, indName))
      {
        System.err.print(queryID + " is actually to be related to: " + indName);
        Block block = bestBlocks.get(indIdx);
        System.err.println(" best score diff = " + bestScoreDiffs.get(indIdx) + " @ block " + block.getID() + ", win.start=" + block.getFirstWindow() );
      }
    }
    
    // Prepare return values
    Vector<Integer> relatedIndividualsVec = new Vector<Integer>();
    rel_blocks.clear();
    for (int i : relatedIndividuals) {
      relatedIndividualsVec.add(i);
      rel_blocks.add(relatedBlocks.get(i));
    }

    return Arrays.toPrimitiveInteger(relatedIndividualsVec);
  }
  
}


// Local Variables:
// c-basic-offset: 2
// c-file-style: "bsd"
// End:

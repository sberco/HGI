package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import Model.Block;
import Model.Blocks;
import Model.Index;
import Model.Labels;
import Model.Scores;
import Model.WindowIndex;

public class IndexFromModel {

	public static InvIndex createIndexFromModel(Labels labels, Index index, Blocks blocks, Scores scoresMatrix, boolean sortByWeights) {
		HashMap<String, ArrayList<PostingObj>> postingLists = new LinkedHashMap<String, ArrayList<PostingObj>>();
		LinkedHashSet<Integer> allWindows = new LinkedHashSet<Integer>();
		for (Map.Entry<Integer, Block> blockEntry : blocks) {
            Block block = blockEntry.getValue();
			for (int winIdx=block.getFirstWindow(); winIdx <= block.getLastWindow(); winIdx++) {
				allWindows.add(winIdx);
			}
		}
		for(int winIdx: allWindows) {
			WindowIndex wi = index.getWindowIndex(winIdx);
			for (int cPosting = 0; cPosting < index.getNumConfs(); cPosting++) {
				String postingListId = winIdx + ":" + cPosting;
				ArrayList<PostingObj> postingList = new ArrayList<PostingObj>();
				for (int c=0; c<index.getNumConfs(); c++) {
					float weight = (float) scoresMatrix.getScore(winIdx, cPosting, c);
					for (int indIdx: wi.getIndList(c)) {
						PostingObj posting = new PostingObj(indIdx, weight);
						postingList.add(posting);
					}
				}
				ArrayList<PostingObj> prevValue = postingLists.put(postingListId, postingList);
				assert prevValue == null;
			}
		}
		return new InvIndex(postingLists, sortByWeights);
	}

}

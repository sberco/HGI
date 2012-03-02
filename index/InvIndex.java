package index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class InvIndex {
	private final static PostingByIdComparator postingByIdComparator_ = new PostingByIdComparator();
	private final static PostingByWeightComparator postingByWeightComparator_ = new PostingByWeightComparator();
	private final HashMap<String, Integer> stringPostingListIdToIdx_;
	private final int[][] ids_;
	private final float[][] weights_;
	private final int idSize_;
	private final boolean sortByWeights_;

	public InvIndex(HashMap<String, ArrayList<PostingObj>> postingLists, boolean sortByWeights) {
		sortByWeights_ = sortByWeights;
		int numOfLists = postingLists.size();
		stringPostingListIdToIdx_ = new LinkedHashMap<String, Integer>(numOfLists);
		ids_ = new int[numOfLists][];
		weights_ = new float[numOfLists][];
		int maxId = 0;
		int listIdx = 0;
		for(String stringPostingListId: postingLists.keySet()) {
			Integer prevVal = stringPostingListIdToIdx_.put(stringPostingListId, listIdx);
			assert prevVal == null;
			ArrayList<PostingObj> list = postingLists.get(stringPostingListId);
			list = new ArrayList<PostingObj>(list);
			if (sortByWeights)
				Collections.sort(list, postingByWeightComparator_);
			else
				Collections.sort(list, postingByIdComparator_);
			int listLength = list.size();
			int[] ids = ids_[listIdx] = new int[listLength];
			float[] weights = weights_[listIdx] = new float[listLength];
			for(int i=0; i<list.size(); i++) {
				PostingObj posting = list.get(i);
				ids[i] = posting.getId();
				int id = posting.getId();
				assert id >= 0;
				float weight = posting.getWeight();
				maxId = Math.max(maxId, id);
				weights[i] = weight;
			}
			listIdx++;
		}
		idSize_ = maxId + 1;
	}

	public int searchNRATop1(String[] query, float threshold) {
		assert sortByWeights_;
		int[] queryIdx = new int[query.length];
		for(int i=0; i<query.length; i++) 
			queryIdx[i] = stringPostingListIdToIdx_.get(query[i]);
		float scoreUBGlobal = 0;
		float[] scoreUBDelta = new float[idSize_];
		float[] scoreUB = new float[idSize_];
		float[] scoreLB = new float[idSize_];
		float maxScoreUB = Float.MAX_VALUE;
		float maxScoreLB = -(Float.MAX_VALUE/2);
		int maxScoreLBId = -1;
		for(int pos = 0; ; pos++) {
			boolean allReachedEnd = true;
			for(int i=0; i<queryIdx.length; i++) {
				int listIdx = queryIdx[i];
				if (pos >= ids_[listIdx].length)
					continue;
				allReachedEnd = false;
				int id = ids_[listIdx][pos];
				float weight = weights_[listIdx][pos];
				float prevWeight = pos > 0 ? weights_[listIdx][pos-1] : 0;
				scoreUBGlobal += weight - prevWeight;
				if (pos > 0) {
					int prevId = ids_[listIdx][pos-1];
					scoreUBDelta[prevId] += prevWeight;
					scoreUB[prevId] = scoreUBGlobal + scoreUBDelta[prevId] - weight;
				}
				float newScoreLB = scoreLB[id] = scoreLB[id] + weight;
				if (newScoreLB > maxScoreLB) {
					maxScoreLB = newScoreLB;
					maxScoreLBId = id;
				}
			}
			if (allReachedEnd)
				break;
			boolean recomputeMaxScoreUB = pos > 0 && pos % (idSize_ / queryIdx.length) == 0;
			if (recomputeMaxScoreUB) {
				float newMaxScoreUB = scoreUBGlobal;
				for(float ub: scoreUB) {
					if (newMaxScoreUB < ub)
						newMaxScoreUB = ub;
				}
				maxScoreUB = newMaxScoreUB;
			}
			if (maxScoreUB < maxScoreLB)
				break;
			if (maxScoreLB >= threshold)  // early termination - returns id above threshold but not necessarily with the highest scored
				break;
		}
		return maxScoreLBId;
	}

}

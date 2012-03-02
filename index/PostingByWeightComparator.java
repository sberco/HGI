package index;

import java.util.Comparator;

public class PostingByWeightComparator implements Comparator<PostingObj> {

	@Override
	public int compare(PostingObj o1, PostingObj o2) {
		return Float.compare(o1.getWeight(), o2.getWeight());
	}

}

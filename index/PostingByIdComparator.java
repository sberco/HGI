package index;

import java.util.Comparator;

public class PostingByIdComparator implements Comparator<PostingObj> {

	@Override
	public int compare(PostingObj o1, PostingObj o2) {
		return Integer.compare(o1.getId(), o2.getId());
	}

}

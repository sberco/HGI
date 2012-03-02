package index;

public class PostingObj {
	private final int id_;
	private final float weight_;

	public PostingObj(int id, float weight) {
		super();
		id_ = id;
		weight_ = weight;
	}

	public int getId() {
		return id_;
	}

	public float getWeight() {
		return weight_;
	}

}

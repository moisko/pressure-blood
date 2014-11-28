package pb.model;

public enum Hand {

	LEFT_HAND("LEFT_HAND"), RIGHT_HAND("RIGHT_HAND");

	private String hand;

	private Hand(String hand) {
		this.hand = hand;
	}

	public String getValue() {
		return hand;
	}
}

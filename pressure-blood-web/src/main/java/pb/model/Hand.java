package pb.model;

public enum Hand {

	LEFT_HAND("leftHand"), RIGHT_HAND("rightHand");

	private String hand;

	private Hand(String hand) {
		this.hand = hand;
	}

	public String getValue() {
		return hand;
	}
}

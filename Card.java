// Card class represents a playing card
class Card {
    private final Suit suit;
    private final Rank rank;

    // Constructor to create a card with a specified suit and rank
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Method to get the rank of the card
    public Rank getRank() {
        return rank;
    }

    // Method to get the suit of the card
    public Suit getSuit() {
        return suit;
    }

    // Method to check if the card is a hidden card
    public boolean isHiddenCard() {
        return this.equals(BlackjackGUI.hiddenCard);
    }

    // Method to get the image name of the card
    public String getCardImageName() {
        if (isHiddenCard()) {
            return "HIDDEN_of_CARD.png";
        } else {
            return rank + "_of_" + suit + ".png";
        }
    }
}
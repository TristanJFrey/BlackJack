import java.util.ArrayList;
import java.util.Collections;
// Deck class represents a deck of playing cards
class Deck {
    private final ArrayList<Card> cards;

    // Constructor to create and initialize a deck of cards
    public Deck() {
        cards = new ArrayList<>();
        // Loop through each suit and rank to create cards and add them to the deck
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    // Method to draw a card from the deck
    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }
}
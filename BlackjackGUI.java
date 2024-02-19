import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class BlackjackGUI extends JFrame {
    private Deck deck; // The deck of cards
    private ArrayList<Card> playerHand;
    private ArrayList<Card> dealerHand;

    // GUI components
    private JLabel playerLabel;
    private JLabel dealerLabel;
    private JButton hitButton;
    private JButton standButton;
    private JPanel playerCardPanel;
    private JPanel dealerCardPanel;

    public static Card hiddenCard; // Placeholder for dealer's hidden card

    // Constructor
    public BlackjackGUI() {
        deck = new Deck(); // Initialize the deck
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        // Initialize GUI components
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");

        hitButton.addActionListener(e -> {
            Card drawnCard = deck.drawCard();
            if (drawnCard != null) {
                playerHand.add(drawnCard);
                updateLabels();
                int playerValue = calculateHandValue(playerHand);
                if (playerValue > 21) {
                    JOptionPane.showMessageDialog(this, "Player busts! Dealer wins.");
                    resetGame();
                }
            }
        });

        standButton.addActionListener(e -> dealerTurn());

        // Panel for labels
        playerLabel = new JLabel("Player Hand: ");
        dealerLabel = new JLabel("Dealer Hand: ");
        playerCardPanel = new JPanel(new FlowLayout());
        dealerCardPanel = new JPanel(new FlowLayout());
        JPanel labelPanel = new JPanel(new GridLayout(2, 1));
        labelPanel.add(playerLabel);
        labelPanel.add(dealerLabel);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        // Set layout of the JFrame
        setLayout(new BorderLayout());
        add(labelPanel, BorderLayout.NORTH);
        add(playerCardPanel, BorderLayout.WEST);
        add(dealerCardPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Blackjack");
        setSize(800, 400);
        setVisible(true);

        initializeGame(); // Initialize the game with the appropriate cards
        updateLabels(); // Initial update to display cards
    }

    // Method to create a resized ImageIcon for a card
    private ImageIcon createResizedCardImageIcon(Card card, int width, int height) {
        ImageIcon originalIcon = createCardImageIcon(card);
        Image image = originalIcon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    // Method to create ImageIcon for a card
    private ImageIcon createCardImageIcon(Card card) {
        String imagePath = "cards/" + card.getCardImageName();
        return new ImageIcon(imagePath);
    }

    // Method to update the card panels with the latest cards
    private void updateCardPanels(JPanel panel, ArrayList<Card> hand) {
        panel.removeAll();
        for (Card card : hand) {
            ImageIcon icon;
            icon = createResizedCardImageIcon(card, 100, 150);
            JLabel label = new JLabel(icon);
            panel.add(label);
        }
        panel.revalidate();
        panel.repaint();
    }

    // Method to update the labels displaying hand values
    private void updateLabels() {
        updateCardPanels(playerCardPanel, playerHand);
        updateCardPanels(dealerCardPanel, dealerHand);

        int playerTotal = calculateHandValue(playerHand);
        int dealerTotal = calculateHandValue(dealerHand);

        if (dealerHand.size() > 1 && !dealerHand.get(0).isHiddenCard()) {
            // If the dealer's first card is not hidden, show the total
            dealerLabel.setText("Dealer Hand: " + dealerTotal);
        } else {
            // Otherwise, keep the dealer's total hidden
            dealerLabel.setText("Dealer Hand: Hidden");
        }
        playerLabel.setText("Player Hand: " + playerTotal);
    }

    // Method to calculate the total value of a hand
    private int calculateHandValue(ArrayList<Card> hand) {
        ArrayList<Card> cardList = (ArrayList<Card>) hand; // Explicit cast to ArrayList<Card>

        int value = 0;
        boolean hasAce = false;
        int aceCount = 0;

        for (Card card : cardList) {
            Rank rank = card.getRank();
            switch (rank) {
                case ACE:
                    hasAce = true;
                    aceCount++;
                    value += 11;
                    break;
                case TWO:
                    value += 2;
                    break;
                case THREE:
                    value += 3;
                    break;
                case FOUR:
                    value += 4;
                    break;
                case FIVE:
                    value += 5;
                    break;
                case SIX:
                    value += 6;
                    break;
                case SEVEN:
                    value += 7;
                    break;
                case EIGHT:
                    value += 8;
                    break;
                case NINE:
                    value += 9;
                    break;
                default:
                    value += 10; // For TEN, Jack, Queen, King
                    break;
            }
        }

        while (hasAce && value > 21 && aceCount > 0) {
            value -= 10;
            aceCount--;
        }
        return value;
    }

    // Method for dealer's turn
    private void dealerTurn() {
        // Reveal the dealer's hidden card and update the GUI
        dealerHand.remove(hiddenCard);
        dealerHand.add(deck.drawCard());
        updateLabels();

        while (calculateHandValue(dealerHand) < 17) {
            Card drawnCard = deck.drawCard();
            if (drawnCard != null) {
                dealerHand.add(drawnCard);
                updateLabels();
            }
        }
        determineWinner();
    }

    // Method to determine the winner of the game
    private void determineWinner() {
        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        if (playerValue > 21) {
            JOptionPane.showMessageDialog(this, "Player busts! Dealer wins.");
        } else if (dealerValue > 21) {
            JOptionPane.showMessageDialog(this, "Dealer busts! Player wins.");
        } else if (playerValue == dealerValue) {
            JOptionPane.showMessageDialog(this, "It's a tie!");
        } else if (playerValue > dealerValue) {
            JOptionPane.showMessageDialog(this, "Player wins!");
        } else {
            JOptionPane.showMessageDialog(this, "Dealer wins!");
        }

        resetGame();
    }

    // Method to initialize the game with the appropriate cards
    private void initializeGame() {
        playerHand.clear();
        dealerHand.clear();
        hiddenCard = deck.drawCard(); // First card drawn for dealer (to be hidden)
        dealerHand.add(hiddenCard);
        dealerHand.add(deck.drawCard()); // Second card drawn for dealer
        playerHand.add(deck.drawCard()); // First card drawn for player
        playerHand.add(deck.drawCard()); // Second card drawn for player
    }

    // Method to reset the game for the next round
    private void resetGame() {
        playerHand.clear();
        dealerHand.clear();
        deck = new Deck();
        initializeGame();
        updateLabels();
    }

    // Main method to start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlackjackGUI::new);
    }
}
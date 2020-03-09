
package flashcards;

public class Card {
    private String term;
    private String definition;
    private CardDeck cardDeck;

    public Card(String term, String definition, CardDeck cardDeck) {
        this.term = term;
        this.definition = definition;
        this.cardDeck = cardDeck;
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    public AskStat getCardStat() {
        return cardDeck.getCardStat(term);
    }

}

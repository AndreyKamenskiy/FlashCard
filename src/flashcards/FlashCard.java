
package flashcards;

public class FlashCard {

    boolean play;
    CardDeck cardDeck = new CardDeck();
    private String exportFile = null;

    void start() {
        play = true;
        loop();
    }

    void loop() {

        while (play) {
            Log.printf("\nInput the action (%s):\n",Actions.getAllNames());
            Actions.getAction(Log.nextLine()).action(this);
        }

        if (exportFile != null) {
            cardDeck.exportToFile(exportFile);
        }
    };

    public void exit() {
        play = false;
    }

    public CardDeck getCardDeck() {
        return cardDeck;
    }

    public void setExportFile(String exportFile){
        this.exportFile = exportFile;
    }

    public void importCardDeck(String fileName) {
        if (fileName != null) {
            cardDeck.importCardDeck(fileName);
        }
    }
}

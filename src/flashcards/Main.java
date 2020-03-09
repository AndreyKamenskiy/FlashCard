
package flashcards;

public class Main {

    public static void main(String[] args) {
        var fc = new FlashCard();
        CommandLine cl = new CommandLine(args);
        cl.parse();
        if (cl.containsKey("-export")) {
            fc.setExportFile(cl.getValue("-export", null));
        }

        if (cl.containsKey("-import")) {
            fc.importCardDeck(cl.getValue("-import", null));
        }

        fc.start();
    }
}

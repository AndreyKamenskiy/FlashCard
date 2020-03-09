package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;

public interface MakeAction {
    void action(FlashCard fc);
}

class AddCard implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        CardDeck cd = fc.getCardDeck();

        Log.println("The card:");

        String term = Log.nextLine();
        if (cd.containsTerm(term)){
            Log.printf("The card \"%s\" already exists.\n", term);
            return;
        }

        Log.println("The definition of the card:");
        String definition = Log.nextLine();
        if (cd.containsDefinition(definition)){
            Log.printf("The definition \"%s\" already exists.\n", definition);
            return;
        }

        if (cd.addCard(term, definition)) {
            Log.printf("The pair (\"%s\":\"%s\") has been added.\n",term,definition);
        }
        else {
            Log.printf("Error adding card \"%s\":\"%s\".\n",term,definition);
        }
    }
}

class RemoveCard implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        CardDeck cd = fc.getCardDeck();

        Log.println("The card:");

        String term = Log.nextLine();
        if (!cd.containsTerm(term)) {
            Log.printf("Can't remove \"%s\": there is no such card.\n", term);
            return;
        }

        if (cd.removeCard(term)) {
            Log.print("The card has been removed.\n");
        }
        else {
            Log.printf("Error removing card \"%s\".\n",term);
        }
    }
}

class ImportCardSet implements MakeAction {
    @Override
    public void action(FlashCard fc) {

        CardDeck cd = fc.getCardDeck();

        Log.println("File name:");

        String fileName = Log.nextLine();

        cd.importCardDeck(fileName);
    }
}

class ExportCardSet implements MakeAction {
    @Override
    public void action(FlashCard fc) {

        CardDeck cd = fc.getCardDeck();

        Log.println("File name:");

        String fileName = Log.nextLine();

        cd.exportToFile(fileName);
    }
}

class AskCards implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        CardDeck cd = fc.getCardDeck();

        Log.println("How many times to ask?");

        int questions = Integer.parseInt(Log.nextLine());
        Card[] cardsArray = cd.getCardsArray();
        for (int i = 0; i < questions; i++) {

            int num = i % cardsArray.length;
            String term = cardsArray[num].getTerm();
            String definition = cardsArray[num].getDefinition();
            if (term == null || definition == null) {
                Log.printf("Error card (\"%s\":\"%s\").\n", term, definition);
                return;
            }

            Log.printf("Print the definition of \"%s\":\n", term);

            String answer = Log.nextLine();

            if (cd.isValidAnswer(term, answer)) {
                Log.print("Correct answer.\n");
            } else if (cd.containsDefinition(answer)) {
                Log.printf("Wrong answer. The correct one is \"%s\" "
                        + ", you've just written the definition of \"%s\".\n", definition, cd.getTerm(answer));
            } else {
                Log.printf("Wrong answer. The correct one is \"%s\".\n", definition);
            }
        }
    }
}

class Exit implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        Log.print("Bye bye!\n");
        fc.exit();
    }
}

class UnknownCommand implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        Log.println("Unknown command. Try better");
    }
}

class ShowLog implements MakeAction {
    @Override
    public void action(FlashCard fc) {

        CardDeck cd = fc.getCardDeck();

        Log.println("File name:");

        String fileName = Log.nextLine();

        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(Log.getLog());
            Log.print("The log has been saved.\n");
        } catch (IOException e) {
            Log.printf("An exception occurs %s", e.getMessage());
        }
    }
}

class HardestCard implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        CardDeck cd = fc.getCardDeck();
        int maxInvalidAnswer = cd.getMaxInvalidAnswerCount();

        if (maxInvalidAnswer == 0) {
            Log.println("There are no cards with errors.");
        } else {
            StringBuilder message = new StringBuilder("The hardest card");
            Set<String> hardestCard = cd.getHardestCard();
            if (hardestCard.size() == 1) {
                message.append(" is ");
            } else {
                message.append("s are ");
            }


            for (String term : hardestCard) {
                message.append("\"");
                message.append(term);
                message.append("\", ");
            }

            message.delete(message.length() - 2, message.length()); // delete last ", "

            message.append(". You have ");
            message.append(maxInvalidAnswer);
            message.append(" errors answering them.\n");

            Log.print(message.toString());
        }
    }
}

class ResetStats implements MakeAction {
    @Override
    public void action(FlashCard fc) {
        fc.getCardDeck().resetStats();
        Log.println("Card statistics has been reset.");
    }
}

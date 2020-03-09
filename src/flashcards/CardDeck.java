
package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

// there are two cases: 1. store statistic in the CardDeck class.
//  + more effective. High speed.
//  - not able to get statistic for each card from the Card class.
// 2. Store stats in the Card class. Not optimise. No variant for quickly getting whole statistic.
//  But this variant more clear.
// Another variant is to save link to CardDeck in the Card class and get statistics by the link from CardDeck.

public class CardDeck {

    CardDeck() {
        cardDeck = new LinkedHashMap<>(100, 0.75f);
        statHashMap = new HashMap<>(100, 0.75f);
    }

    private LinkedHashMap<String, String> cardDeck;

    private HashMap<String, AskStat> statHashMap;

    public boolean addCard(String term, String definition) {
        if (!cardDeck.containsKey(term) && !cardDeck.containsValue(definition)) {
            cardDeck.put(term, definition);
            return true;
        }
        return false;
    }

    public boolean UpdateCard(String term, String definition) {
        //add or update card.
        if (!cardDeck.containsKey(term) && !cardDeck.containsValue(definition)) {
            cardDeck.put(term, definition);
            return true;
        } else if (cardDeck.containsKey(term) && !cardDeck.containsValue(definition)) {
            // such term exist and definition is not
            cardDeck.replace(term, definition);

            statHashMap.remove(term); // clear card statistic.

            return true;
        }
        return false;
    }

    public boolean containsTerm(String term) {
        return cardDeck.containsKey(term);
    }

    public boolean containsDefinition(String definition) {
        return cardDeck.containsValue(definition);
    }

    public boolean removeCard(String term) {
        if (cardDeck.containsKey(term)) {
            cardDeck.remove(term);
            if (statHashMap.containsKey(term)) {
                statHashMap.remove(term); // clear card statistic
            }
            return true;
        }
        return false;
    }

    public Card[] getCardsArray() {
        Card[] cardsArray = new Card[cardDeck.size()];
        int i = 0;
        for (Entry<String, String> card : cardDeck.entrySet()) {
            cardsArray[i] = new Card(card.getKey(), card.getValue(), this);
            i++;
        }
        return cardsArray;
    }

    /*public Card[] getRandomCards(int quantity) {
        Card[] cardsArray = new Card[quantity];

        ArrayList<String> cardsList = new ArrayList<>(cardDeck.keySet());

        Random rnd = new Random();

        for (int i = 0; i < quantity; i++) {
            int number = rnd.nextInt(cardsList.size() + 1);
            String term = cardsList.get(number);
            String definition = cardDeck.get(term);
            cardsArray[i] = new Card(term,definition);

            cardsList.remove(number);
            if (cardsList.isEmpty()) {
                cardsList.addAll(cardDeck.keySet());
            }
        }
        return cardsArray;
    }*/

    public String getTerm(String definition) {
        for (Entry<String, String> card : cardDeck.entrySet()) {
            if (card.getValue().equals(definition)) {
                return card.getKey();
            }
        }
        return null;
    }

    public boolean isValidAnswer(String term, String definition) {
        if (!cardDeck.containsKey(term) || definition == null) {
            return false; // not valid term or definition. Don't save in statHashMap
        }

        boolean validAnswer = definition.equals(cardDeck.get(term));

        //save in stat
        if (statHashMap.containsKey(term)) {
            statHashMap.get(term).incAskedTimes(validAnswer); // not sure that it is rigth method to change object

            // may be it is need to do following
          /*AskStat oldStat = statHashMap.get(term);
          AskStat newStat = new AskStat(oldStat.getAskedTimes(), oldStat.getValidAnswers());
          newStat.incAskedTimes(validAnswer);
          statHashMap.replace(term, newStat); */

        } else {
            AskStat newStat = new AskStat(1, (validAnswer) ? 1 : 0);
            statHashMap.put(term, newStat);
        }

        return validAnswer;
    }

    public void resetStats() {
        statHashMap.clear();
    }

    public int getMaxInvalidAnswerCount() {

        int max = 0;
        for (AskStat val : statHashMap.values()) {
            int invalidAnswers = val.getInvalidAnswers();
            if (invalidAnswers > max) {
                max = invalidAnswers;
            }
        }
        return max;
    }

    public Set<String> getHardestCard() {
        int max = 0;
        Set<String> hardestCard = new HashSet<>();

        for (Entry<String, AskStat> entry : statHashMap.entrySet()) {
            int invalidAnswers = entry.getValue().getInvalidAnswers();
            if (invalidAnswers == max) {
              hardestCard.add(entry.getKey());
            } else if (invalidAnswers > max) {
                max = invalidAnswers;
                hardestCard.clear();
                hardestCard.add(entry.getKey());
            }
        }

        return hardestCard;
    }

    public AskStat getCardStat(String term) {
        if (statHashMap.containsKey(term)) {
            return statHashMap.get(term);
        }
        return new AskStat(0, 0);
    }

    public void updateCardStat(String term, int askedTimes, int validAnswers) {
        if (statHashMap.containsKey(term)) {
            AskStat stat = statHashMap.get(term);
            stat.setAskedTimes(stat.getAskedTimes() + askedTimes);
            stat.setValidAnswers(stat.getValidAnswers() + validAnswers);
        } else {
            statHashMap.put(term, new AskStat(askedTimes, validAnswers));
        }
    }

    public boolean isEmpty () {
        return cardDeck.isEmpty();
    }

    public void exportToFile(String exportFile) {
        File file = new File(exportFile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            Card[] cardsArray = this.getCardsArray();
            for (Card card : cardsArray) {
                printWriter.println(card.getTerm());
                printWriter.println(card.getDefinition());
                //save Stat
                AskStat stat = card.getCardStat();
                printWriter.println(stat.getAskedTimes());
                printWriter.println(stat.getValidAnswers());
            }
            Log.printf("%d cards have been saved\n", cardsArray.length);
        } catch (IOException e) {
            Log.printf("An exception occurs %s", e.getMessage());
        }
    }

    public void importCardDeck(String fileName) {

        File file = new File(fileName);
        try (Scanner fileScanner = new Scanner(file)) {

            int loadedCards = 0;
            while (fileScanner.hasNext()) {
                String term = fileScanner.nextLine();
                String definition;
                if (fileScanner.hasNext()) {
                    definition = fileScanner.nextLine();
                } else {
                    Log.println("Wrong file format");
                    break;
                }

                int askedTimes;
                if (fileScanner.hasNext()) {
                    askedTimes = Integer.parseInt(fileScanner.nextLine());
                } else {
                    Log.println("Wrong file format");
                    break;
                }

                int validAnswers;
                if (fileScanner.hasNext()) {
                    validAnswers = Integer.parseInt(fileScanner.nextLine());
                } else {
                    Log.println("Wrong file format");
                    break;
                }

                loadedCards++;

                boolean added;
                if (containsTerm(term)) {
                    added = UpdateCard(term,definition);
                } else {
                    added = addCard(term, definition); // may be change to updatedCard ++;
                }

                if (!added) {
                    Log.printf("Error adding card %s:%s", term, definition);
                }

                if (askedTimes > 0) {
                    updateCardStat(term, askedTimes, validAnswers);
                }
            }

            Log.printf("%d cards have been loaded.",loadedCards);
        } catch (FileNotFoundException e) {
            Log.println("File not found.\n ");
        }

    }
}

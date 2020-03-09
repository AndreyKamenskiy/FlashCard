package flashcards;

import java.util.Objects;

public class AskStat {
    private int askedTimes;
    private int validAnswers;

    public AskStat(int askedTimes, int validAnswers) {
        this.askedTimes = askedTimes;
        this.validAnswers = validAnswers;
    }

    public AskStat() {
        askedTimes = 0;
        validAnswers = 0;
    }

    public int getAskedTimes() {
        return askedTimes;
    }

    public int getValidAnswers() {
        return validAnswers;
    }

    public void setAskedTimes(int askedTimes) {
        this.askedTimes = askedTimes;
    }

    public void setValidAnswers(int validAnswers) {
        this.validAnswers = validAnswers;
    }

    public int getInvalidAnswers() {
        return askedTimes - validAnswers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(askedTimes, validAnswers);
        // or return askedTimes*37 + validAnswers;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null
                && (obj instanceof AskStat)
                && askedTimes == ((AskStat) obj).getAskedTimes()
                && validAnswers == ((AskStat) obj).getValidAnswers();
    }

    public void incAskedTimes(boolean valid){
        askedTimes++;
        if (valid) {
            validAnswers++;
        }
    }

}

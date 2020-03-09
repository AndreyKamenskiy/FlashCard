
package flashcards;

public enum Actions {
    ADD("add", new AddCard()),
    REMOVE("remove", new RemoveCard()),
    IMPORT("import", new ImportCardSet()),
    EXPORT("export", new ExportCardSet()),
    ASK("ask", new AskCards()),
    EXIT("exit", new Exit()),
    LOG("log", new ShowLog()),
    HARDEST_CARD("hardest card", new HardestCard()),
    RESET_CARD("reset stats", new ResetStats()),
    UNKNOWN_COMMAND("", new UnknownCommand());

    private String name;
    private MakeAction action;

    Actions(String name, MakeAction action) {
        this.name = name;
        this.action = action;
    }

    public static MakeAction getAction(String name) {
        for (Actions act : Actions.values()) {
            if (act.gatName().equals(name)) {
                return act.getAction();
            }
        }
        return UNKNOWN_COMMAND.getAction(); // if nothing was found;
    }

    private MakeAction getAction() {
        return action;
    }

    private String gatName() {
        return name;
    }

    public static String getAllNames(){
        StringBuilder res = new StringBuilder("");
        for (Actions act : Actions.values()) {
            if (act != UNKNOWN_COMMAND) {
                res.append(act.gatName());
                res.append(", ");
            }
        }
        res.delete(res.length() - 2, res.length());
        return res.toString();
    }
}

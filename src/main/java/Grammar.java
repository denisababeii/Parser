import java.util.ArrayList;
import java.util.List;

public class Grammar {
    private boolean isCFG;
    private List<String> terminals;
    private List<String> nonTerminals;
    private List<Production> productions;

    public Grammar(String file) {
        terminals = new ArrayList<>();
        nonTerminals = new ArrayList<>();
        productions = new ArrayList<>();
        isCFG = true;
        read(file);
    }

    public void read(String file) {

    }

    public void printNonTerminals() {

    }

    public void printTerminals() {

    }

    public void printProductions() {

    }

    public void printProductionsForNonTerminal(String nonTerminal) {

    }

    public void checkCFG() {

    }
}

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
        this.productions.forEach(production -> System.out.println(production.toString()));
    }

    public void printProductionsForNonTerminal(String nonTerminal) {
        List<Production> productionsForNonTerminal = new ArrayList<>();
        for (Production production : productions) {
            for (List<String> rule : production.getRules())
                if (rule.indexOf(nonTerminal) != -1)
                    productionsForNonTerminal.add(production);
        }
        productionsForNonTerminal.forEach(p-> System.out.println(p.toString()));
    }

    public void checkCFG() {
        System.out.println(this.isCFG);
    }
}

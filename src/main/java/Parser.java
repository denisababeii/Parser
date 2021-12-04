import java.util.*;

public class Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public Map<String, Set<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getFollow() {
        return follow;
    }

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.first = new HashMap<>();
        this.follow = new HashMap<>();
        generateFirst();
        generateFollow();
    }

    public void generateFirst() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            first.put(nonTerminal, this.firstOf(nonTerminal));
        }
    }

    public Set<String> firstOf(String nonTerminal) {
        Set<String> terminalsForCurrentNonTerminal = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();

        for (Production production : grammar.getProductionsForNonTerminalOnLeftSide(nonTerminal)) {
            for (List<String> rule : production.getRules()) {
                String firstSymbol = rule.get(0);
                if (firstSymbol.equals("@"))
                    terminalsForCurrentNonTerminal.add("@");
                else if (terminals.contains(firstSymbol))
                    terminalsForCurrentNonTerminal.add(firstSymbol);
                else
                    terminalsForCurrentNonTerminal.addAll(firstOf(firstSymbol));
            }
        }
        return terminalsForCurrentNonTerminal;
    }

    public void generateFollow() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            if(follow.get(nonTerminal)==null)
                follow.put(nonTerminal, this.followOf(nonTerminal));
        }
    }

    public Set<String> followOf(String nonTerminal) {
        Set<String> terminalsForCurrentNonTerminal = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();

        if(nonTerminal.equals(grammar.getStartingSymbol()))
            terminalsForCurrentNonTerminal.add("$");

        var productionsForNonTerminal = grammar.getProductionsForNonTerminal(nonTerminal);
        for(Production production : productionsForNonTerminal) {
            String symbol = production.getSymbols().get(0);
            var rules = production.getRules();
            for(List<String> rule : rules) {
                if(rule.contains(nonTerminal)) {
                    if (rule.indexOf(nonTerminal) != rule.size() - 1) {
                        var afterSymbol = rule.get(rule.indexOf(nonTerminal) + 1);
                        if (terminals.contains(afterSymbol))
                            terminalsForCurrentNonTerminal.add(afterSymbol);
                        else
                            terminalsForCurrentNonTerminal.addAll(first.get(afterSymbol));
                        terminalsForCurrentNonTerminal.remove("@");
                    } else {
                        if (follow.get(symbol) == null)
                            follow.put(symbol, followOf(symbol));
                        terminalsForCurrentNonTerminal.addAll(follow.get(symbol));
                    }
                }
            }
        }
        return terminalsForCurrentNonTerminal;
    }
}

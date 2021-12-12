import java.util.*;

public class Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public ParseTable getParseTable() {
        return parseTable;
    }

    private ParseTable parseTable = new ParseTable();
    // Pair contains values of a Production; First String is starting Symbol and the list is for rules
    // Integer is the number of the production
    private Map<Pair<String, List<String>>, Integer> numberedProductions = new HashMap<>();

    private void countProductions() {
        int index = 1;
        for (Production production: grammar.getProductions())
            for (List<String> rule: production.getRules())
                numberedProductions.put(new Pair<>(production.getSymbols().get(0), rule), index++);
    }

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
        createParseTable();
    }

    public void generateFirst() {
        for (String nonTerminal : grammar.getNonTerminals()) {
            first.put(nonTerminal, this.firstOf(nonTerminal));
        }
    }

    public Set<String> firstOf(String nonTerminal) {
        if (first.containsKey(nonTerminal))
            return first.get(nonTerminal);
        Set<String> terminalsForCurrentNonTerminal = new HashSet<>();
        Set<String> terminals = grammar.getTerminals();

        for (Production production : grammar.getProductionsForNonTerminalOnLeftSide(nonTerminal))
            for (List<String> rule : production.getRules()) {
                String firstSymbol = rule.get(0);
                if (firstSymbol.equals("@"))
                    terminalsForCurrentNonTerminal.add("@");
                else if (terminals.contains(firstSymbol))
                    terminalsForCurrentNonTerminal.add(firstSymbol);
                else{
                    if (first.get(firstSymbol) == null)
                        first.put(firstSymbol, firstOf(firstSymbol));
                    terminalsForCurrentNonTerminal.addAll(first.get(firstSymbol));
                    terminalsForCurrentNonTerminal.remove("@");
                    if(first.get(firstSymbol).contains("@")){
                        if(rule.size() > 1)
                            terminalsForCurrentNonTerminal.add(rule.get(1));
                        else terminalsForCurrentNonTerminal.add("@");
                    }
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
                        if (follow.get(symbol) == null && !symbol.equals(nonTerminal))
                            follow.put(symbol, followOf(symbol));
                        if(!symbol.equals(nonTerminal))
                            terminalsForCurrentNonTerminal.addAll(follow.get(symbol));
                    }
                }
            }
        }
        return terminalsForCurrentNonTerminal;
    }

    private void createParseTable() {
        // initialize productions' indexes
        countProductions();

        // get terminals for table's top row (head of the table)
        List<String> columnSymbols = new LinkedList<>(grammar.getTerminals());
        columnSymbols.add("$");

        // for the $-$ pair at the bottom of table we insert "acc"
        parseTable.put(new Pair<>("$", "$"), new Pair<>(Collections.singletonList("acc"), -1));
        // for every terminal-terminal matching pair we insert "pop"
        for (String terminal: grammar.getTerminals())
            parseTable.put(new Pair<>(terminal, terminal), new Pair<>(Collections.singletonList("pop"), -1));


        numberedProductions.forEach((key, value) -> {
            String rowSymbol = key.getKey();
            List<String> rule = key.getValue();
            Pair<List<String>, Integer> parseTableValue = new Pair<>(rule, value);

            for (String columnSymbol : columnSymbols) {
                Pair<String, String> parseTableKey = new Pair<>(rowSymbol, columnSymbol);

                // if our column-terminal is exactly first of rule
                if (rule.get(0).equals(columnSymbol) && !columnSymbol.equals("@"))
                    parseTable.put(parseTableKey, parseTableValue);

                // if the first symbol is a non-terminal and it's first contains our column-terminal
                else if (grammar.getNonTerminals().contains(rule.get(0)) && first.get(rule.get(0)).contains(columnSymbol)) {
                    if (!parseTable.containsKey(parseTableKey)) {
                        parseTable.put(parseTableKey, parseTableValue);
                    }
                }
                else {
                    // if the first symbol is ε then everything in FOLLOW(rowSymbol) will be in parse table
                    if (rule.get(0).equals("@")) {
                        for (String b : follow.get(rowSymbol))
                            parseTable.put(new Pair<>(rowSymbol, b), parseTableValue);

                        // if ε is in FIRST(rule)
                    } else {
                        Set<String> firsts = new HashSet<>();
                        for (String symbol : rule)
                            if (grammar.getNonTerminals().contains(symbol))
                                firsts.addAll(first.get(symbol));
                        if (firsts.contains("@")) {
                            for (String b : first.get(rowSymbol)) {
                                if (b.equals("@"))
                                    b = "$";
                                parseTableKey = new Pair<>(rowSymbol, b);
                                if (!parseTable.containsKey(parseTableKey)) {
                                    parseTable.put(parseTableKey, parseTableValue);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

}

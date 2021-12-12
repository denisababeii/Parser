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
        createTable();
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
        follow.put(grammar.getStartingSymbol(), Collections.singleton("$"));
        for (String nonTerminal : grammar.getNonTerminals()){
            if(!nonTerminal.equals(grammar.getStartingSymbol()))
                follow.put(nonTerminal, new HashSet<>());
        }
        Map<String, Set<String>> currentFollow = follow();
        var run = true;
        while(run) {
            currentFollow = follow();
            var keys = currentFollow.keySet();
            run = false;
            for (var key: keys) {
                if(!(currentFollow.get(key).equals(follow.get(key))))
                    run=true;
            }
            follow.clear();
            follow.putAll(currentFollow);
        }
    }

    public Map<String, Set<String>> follow() {
        Map<String, Set<String>> currentFollow = new HashMap<>();
        for (String nonTerminal : grammar.getNonTerminals()){
            if(follow.get(nonTerminal).size()!=0){
                currentFollow.put(nonTerminal,follow.get(nonTerminal));
            }
            else {
                currentFollow.put(nonTerminal,new HashSet<>());
            }

            var productionsForNonTerminal = grammar.getProductionsForNonTerminal(nonTerminal);
            for (var production:productionsForNonTerminal) {
                var rules = production.getRules();
                for(var rule:rules){
                    if(rule.contains(nonTerminal)) {
                        var index = rule.indexOf(nonTerminal);
                        if(index!=rule.size()-1) {
                            var afterSequence = new ArrayList<String>();
                            for(int i=index+1;i<rule.size();i++)
                                afterSequence.add(rule.get(i));
                            var firstOfRule = firstOfRule(afterSequence);
                            for (var symbol:firstOfRule) {
                                if (!symbol.equals("@")) {
                                    var current = currentFollow.get(nonTerminal);
                                    current.add(symbol);
                                    currentFollow.put(nonTerminal, current);
                                }
                                else {
                                    var symbolsOfLeftTerminal = follow.get(production.getSymbols().get(0));
                                    var current = currentFollow.get(nonTerminal);
                                    for (var symbolLeftTerminal: symbolsOfLeftTerminal) {
                                        if(!current.contains(symbolLeftTerminal))
                                            current.add(symbolLeftTerminal);
                                    }
                                    currentFollow.put(nonTerminal,current);
                                }
                            }
                        }
                        else {
                            var symbolsOfLeftTerminal = follow.get(production.getSymbols().get(0));
                            var current = currentFollow.get(nonTerminal);
                            for (var symbol: symbolsOfLeftTerminal) {
                                if(!current.contains(symbol))
                                    current.add(symbol);
                            }
                            currentFollow.put(nonTerminal,current);
                        }
                    }
                }
            }
        }
        return currentFollow;
    }

    private Set<String> firstOfRule(List<String> rule) {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();

        if(grammar.getTerminals().contains(rule.get(0)))
        {
            set1.add(rule.get(0));
            return set1;
        }
        else {
            if(first.get(rule.get(0))!=null)
                set1.addAll(first.get(rule.get(0)));
        }

        if(rule.size() == 1)
            return set1;

        var concatenationResult = new HashSet<String>();
        var i = 1;
        while(i<rule.size()){
            if (grammar.getTerminals().contains(rule.get(i))) {
                set2.add(rule.get(i));
            }
            else {
                if(first.get(rule.get(i))!=null)
                    set2.addAll(first.get(rule.get(i)));
            }
            if(set2.size() == 0)
                break;
            for(var symbol1: set1) {
                for(var symbol2: set2) {
                    if(symbol1.equals("@"))
                        concatenationResult.add(symbol2);
                    else
                        concatenationResult.add(symbol1);
                }
            }
            set1.clear();
            set1.addAll(set2);
            i++;
        }
        return concatenationResult;
    }

    private void createTable() {
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

            var firstOfRule = firstOfRule(rule);
            if(firstOfRule.size() != 0) {
                for (var item : firstOfRule) {
                    if (!item.equals("@")) {
                        var parseTableKey = new Pair<String, String>(rowSymbol, item);
                        var parseTableValue = new Pair<List<String>, Integer>(rule, value);
                        if(!parseTable.containsKey(parseTableKey))
                            parseTable.put(parseTableKey, parseTableValue);
                    }
                }
            }

            if((rule.size()==1 && rule.get(0).equals("@")) || firstOfRule.contains("@")) {
                var parseTableValue = new Pair<List<String>, Integer>(rule,value);
                var followOfNonTerminal = follow.get(rowSymbol);
                for(var symbol: followOfNonTerminal){
                    var parseTableKey = new Pair<String,String>(rowSymbol,symbol);
                    if(!parseTable.containsKey(parseTableKey))
                        parseTable.put(parseTableKey,parseTableValue);
                }
            }
        });
    }
}

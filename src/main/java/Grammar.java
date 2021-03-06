import java.io.*;
import java.util.*;

public class Grammar {

    private String outputFile;
    private boolean isCFG;
    private Set<String> terminals;
    private List<String> nonTerminals;
    private List<Production> productions;
    private String startingSymbol;

    public List<Production> getProductions() {
        return productions;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public Grammar(String file, String outputFile) {
        terminals = new HashSet<>();
        nonTerminals = new ArrayList<>();
        productions = new ArrayList<>();
        this.outputFile = outputFile;
        isCFG = true;
        read(file);
    }

    public void read(String file) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            int lineCount = 1;
            while (line != null) {
                if (lineCount == 1) { // if I am on the first line, where the terminals are enumerated
                    var terminals = line.split(" ");
                    this.terminals.addAll(Arrays.asList(terminals));
                } else if (lineCount == 2) { // if I am on the second line, where the nonTerminals are enumerated
                    var nonTerminals = line.split(" ");
                    this.nonTerminals.addAll(Arrays.asList(nonTerminals));
                } else if (lineCount == 3) { // if I am on the third line, where the starting symbol is
                    startingSymbol = line;
                } else {
                    var sides = line.split("->");
                    var leftHandSide = sides[0];
                    var rightHandSide = sides[1];

                    List<List<String>> rules = new ArrayList<>();
                    List<String> symbols = new ArrayList<>();
                    for (String rule: rightHandSide.split("\\|"))
                        rules.add(Arrays.asList(rule.split(" ")));
                    for (String symbol: leftHandSide.split(" "))
                        symbols.add(symbol);
                    if(symbols.size()>1) // if there isn't a single symbol on the left-hand side
                        isCFG = false;
                    productions.add(new Production(symbols, rules));
                }
                line=reader.readLine();
                lineCount++;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String printNonTerminals() {
        var result = new StringBuilder();
        for (var nonTerminal:
             nonTerminals) {
            result.append(nonTerminal).append(", ");
        }
        result.delete(result.length()-2, result.length());
        System.out.println(result.toString());
        return result.toString();
    }

    public String printTerminals() {
        var result = new StringBuilder();
        for (var terminal:
                terminals) {
            result.append(terminal+", ");
        }
        result.delete(result.length()-2, result.length());
        System.out.println(result.toString());
        return result.toString();
    }

    public void printProductions() {
        this.productions.forEach(production -> System.out.println(production.toString()));
    }

    public Set<Production> getProductionsForNonTerminal(String nonTerminal) {
        Set<Production> productionsForNonTerminal = new HashSet<>();
        for (Production production : productions) {
            for (List<String> rule : production.getRules())
                if (rule.contains(nonTerminal))
                    productionsForNonTerminal.add(production);
        }
        return productionsForNonTerminal;
    }

    public Set<Production> printProductionsForNonTerminal(String nonTerminal) {
        var productionsForNonTerminal = getProductionsForNonTerminal(nonTerminal);
        productionsForNonTerminal.forEach(p-> System.out.println(p.toString()));
        return productionsForNonTerminal;
    }

    public List<Production> getProductionsForNonTerminalOnLeftSide(String nonTerminal) {
        List<Production> productionsForNonTerminal = new LinkedList<>();
        for (Production production : productions) {
            if (production.getSymbols().get(0).equals(nonTerminal)) {
                productionsForNonTerminal.add(production);
            }
        }
        return productionsForNonTerminal;
    }

    public boolean checkCFG() {
        System.out.println(this.isCFG);
        return this.isCFG;
    }
}

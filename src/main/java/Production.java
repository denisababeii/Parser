import java.util.List;

public class Production {
    private List<String> symbols;
    private List<List<String>> rules;

    Production(List<String> symbols, List<List<String>> rules) {
        this.symbols = symbols;
        this.rules = rules;
    }

    List<List<String>> getRules() {
        return rules;
    }

    List<String> getSymbols() {
        return symbols;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(var symbol: symbols) {
            sb.append(symbol+" ");
        }
        sb.delete(sb.length()-1,sb.length());
        sb.append("->");

        for (List<String> rule: rules) {
            for (String element: rule)
                sb.append(element).append(" ");
        }
        sb.replace(sb.length() - 3, sb.length() - 1, "");
        return sb.toString();
    }
}

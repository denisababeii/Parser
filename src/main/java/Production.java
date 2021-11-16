import java.util.List;

public class Production {
    private String startingSymbol;
    private List<List<String>> rules;

    Production(String startingSymbol, List<List<String>> rules) {
        this.startingSymbol = startingSymbol;
        this.rules = rules;
    }

    List<List<String>> getRules() {
        return rules;
    }

    String getStartingSymbol() {
        return startingSymbol;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(startingSymbol + "->");
        for (List<String> rule: rules) {
            for (String element: rule)
                sb.append(element).append(" ");
            sb.append(startingSymbol);
        }
        sb.replace(sb.length() - 3, sb.length() - 1, "");
        return sb.toString();
    }
}

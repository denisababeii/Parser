import java.util.Scanner;

public class Program {
    public static void printMenu(Grammar grammar) {
        var run = true;
        while (run) {
            System.out.println("\n1. Get nonTerminals\n" +
                    "2. Get terminals\n" +
                    "3. Get productions\n" +
                    "4. Get productions for a certain nonTerminal\n" +
                    "5. Is it a CFG?\n" +
                    "6. Exit\n");

            Scanner scanner = new Scanner(System.in);
            var choice = scanner.next();
            switch (choice) {
                case "1":
                    grammar.printNonTerminals();
                    break;
                case "2":
                    grammar.printTerminals();
                    break;
                case "3":
                    grammar.printProductions();
                    break;
                case "4":
                    var nonTerminal = scanner.next();
                    grammar.printProductionsForNonTerminal(nonTerminal);
                    break;
                case "5":
                    grammar.checkCFG();
                    break;
                case "6":
                    run = false;
                    break;
                default:
                    System.out.println("Please pick an available choice!");
            }
        }
    }

    public static void main(String[] args) {
        var grammar = new Grammar("src/main/resources/SimpleGrammar.txt");
        //var grammar = new Grammar("src/main/resources/OurGrammar.txt");
        try {
            printMenu(grammar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

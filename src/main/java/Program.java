import java.util.Scanner;

public class Program {
    public static void printMenu(Grammar grammar) {
        Parser parser = new Parser(grammar);
        var run = true;
        while (run) {
            System.out.println("\n1. Get nonTerminals\n" +
                    "2. Get terminals\n" +
                    "3. Get productions\n" +
                    "4. Get productions for a certain nonTerminal\n" +
                    "5. Is it a CFG?\n" +
                    "6. Get First\n" +
                    "7. Get Follow\n" +
                    "8. Get First Of\n" +
                    "9. Get Follow Of\n" +
                    "10. Parse Table\n" +
                    "11. Exit\n");

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
                    System.out.println(parser.getFirst());
                    break;
                case "7":
                    System.out.println(parser.getFollow());
                    break;
                case "8":
                    var nonTerminal2 = scanner.next();
                    System.out.println(parser.followOf(nonTerminal2));
                    break;
                case "9":
                    var nonTerminal3 = scanner.next();
                    System.out.println(parser.firstOf(nonTerminal3));
                    break;
                case "10":
                    System.out.println(parser.getParseTable().toString());
                    break;
                case "11":
                    run = false;
                    break;
                default:
                    System.out.println("Please pick an available choice!");
            }
        }
    }

    public static void main(String[] args) {
        //var grammar = new Grammar("D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\SecondSimpleGrammar.txt");
        //var grammar = new Grammar("D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\SimpleGrammar.txt");
        //var grammar = new Grammar("D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\OurGrammar.txt");
        var grammar = new Grammar("src/main/resources/SecondSimpleGrammar.txt");
        try {
            printMenu(grammar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

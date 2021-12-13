import java.util.Scanner;

public class Program {
    public static void printMenu(Grammar grammar) {
        Parser parser = new Parser(grammar);
        var run = true;
        while (run) {
            System.out.println("\n1. Get nonTerminals\n" +
                    "2. Get terminals\n" +
                    "3. Get productions\n" +
                    "4. Get productions for a certain NonTerminal\n" +
                    "5. Is it a CFG?\n" +
                    "6. Get First\n" +
                    "7. Get Follow\n" +
                    "8. Get First Of NonTerminal\n" +
                    "9. Get Follow Of NonTerminal\n" +
                    "10. Get Parse Table\n" +
                    "11. Parse sequence and print Parsing Tree\n" +
                    "12. Exit\n");

            Scanner scanner = new Scanner(System.in);
            var choice = scanner.nextLine();
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
                    System.out.println(parser.getFollow().get(nonTerminal2));
                    break;
                case "9":
                    var nonTerminal3 = scanner.next();
                    System.out.println(parser.firstOf(nonTerminal3));
                    break;
                case "10":
                    System.out.println(parser.getParseTable().toString());
                    break;
                case "11":
                    var sequence = scanner.nextLine();
                    var parserOutput = new ParserOutput(sequence,parser);
                    System.out.println(parserOutput.getResult());
                    System.out.println(parserOutput.getTree());
                    break;
                case "12":
                    run = false;
                    break;
                default:
                    System.out.println("Please pick an available choice!");
            }
        }
    }

    public static Grammar pickGrammar() {
        //var grammar = new Grammar("D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\SecondSimpleGrammar.txt");
        //var grammar = new Grammar("src/main/resources/SecondSimpleGrammar.txt");
        System.out.println("""
                Pick grammar:\s
                1. Simple grammar
                2. Our grammar
                """);
        Scanner scanner = new Scanner(System.in);
        Grammar grammar = null;
        var choice = scanner.nextLine();
        switch (choice) {
            case "1":
                //return new Grammar("D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\SimpleGrammar.txt","D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\SimpleGrammar.out");
                return new Grammar("src/main/resources/SimpleGrammar.txt", "src/main/resources/SimpleGrammar.out");
            case "2":
                //return new Grammar("D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\OurGrammar.txt","D:\\University\\YEAR_3_SEM_1\\FCLD\\Parser\\Parser\\src\\main\\resources\\OurGrammar.out");
                return new Grammar("src/main/resources/OurGrammar.txt", "src/main/resources/OurGrammar.out");
            default:
                System.out.println("Error");
                return null;
        }
    }

    public static void main(String[] args) {
        try {
            var grammar = pickGrammar();
            printMenu(grammar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

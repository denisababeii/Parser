public class Test {
    public static void main(String[] args) {
        //var grammar = new Grammar("src/main/resources/SimpleGrammar.txt");
        var grammar = new Grammar("src/main/resources/OurGrammar.txt");
        var parser = new Parser(grammar);
        var first = parser.getFirst();
        var follow = parser.getFollow();

        System.out.println(follow);
    }
}

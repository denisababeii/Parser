import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParserOutput {
    private String sequence;
    private Parser parser;
    private List<Node> tree;
    private  String result;

    public String getResult() {
        return result;
    }

    public ParserOutput(String sequence, Parser parser) {
        this.sequence = sequence;
        this.parser = parser;
        tree = new ArrayList<Node>();
        result = parse();
    }

    private void printToFile(String content) {
        try {
            FileWriter fileWriter = new FileWriter(parser.getGrammar().getOutputFile());
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(content);
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTree() {
        var sb = new StringBuilder();
        if(tree.size()==0)
            return "Empty tree";
        for (var node: tree) {
            sb.append(node.toString()).append("\n");
        }
        printToFile(sb.toString());
        return sb.toString();
    }

    private void makeTree(List<Integer> outputStack){
        tree.add(new Node(1,parser.getGrammar().getStartingSymbol(),0,0));
        makeTreeRecursive(outputStack,1);
    }

    private void makeTreeRecursive(List<Integer> outputStack, int parentIndex){
        var index = tree.size()+1;
        var productionIndex = outputStack.remove(0);
        var productionSymbols = parser.getProductionByIndex(productionIndex);
        var left = 0;
        var indexes = new ArrayList<Integer>();
        for (var symbol: productionSymbols.getValue()) {
            tree.add(new Node(index,symbol,parentIndex,left));
            indexes.add(index);
            left=index;
            index++;
        }
        for(int i=0; i<productionSymbols.getValue().size(); i++){
            if(parser.getGrammar().getNonTerminals().contains(productionSymbols.getValue().get(i)))
                makeTreeRecursive(outputStack,indexes.get(i));
        }
        if(outputStack.size() == 0)
            return;
    }

    private String parse(){
        var inputStack = new ArrayList<String>(Arrays.asList(sequence.split(" ")));
        var workingStack = new ArrayList<String>();
        var outputStack = new ArrayList<Integer>();
        var sb = new StringBuilder();

        inputStack.add("$");
        workingStack.add(parser.getGrammar().getStartingSymbol());
        workingStack.add("$");

        while(inputStack.size() > 0){
            var inputElem = inputStack.get(0);
            var workingElem = workingStack.get(0);
            if (workingElem.equals("@"))
                workingStack.remove(0);
            else{
                var ll1Entry = parser.getParseTable().get(new Pair<String,String>(workingElem,inputElem));
                if(ll1Entry==null) {
                    sb.append("Error at: ").append(inputElem).append(", ").append(workingElem);
                    return sb.toString();
                }
                else if (ll1Entry.getKey().contains("pop")) {
                    workingStack.remove(0);
                    inputStack.remove(0);
                }
                else if (ll1Entry.getKey().contains("acc")) {
                    sb.append("Sequence accepted");
                    makeTree(outputStack);
                    return sb.toString();
                }
                else {
                    workingStack.remove(0);
                    var rhs = ll1Entry.getKey();
                    var workingStackContent = new ArrayList<>(workingStack);
                    workingStack.clear();
                    workingStack.addAll(rhs);
                    workingStack.addAll(workingStackContent);
                    outputStack.add(ll1Entry.getValue());
                }
            }
        }
        return sb.toString();
    }
}

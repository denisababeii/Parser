import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class TestParser {
    Grammar grammar;
    Parser parser;
    @Before
    public void setUp() throws Exception {
        grammar = new Grammar("src/main/resources/SimpleGrammar.txt");
        parser = new Parser(grammar);
    }

    public List<Production> getProductions() {
        List<Production> testProductions = new ArrayList<Production>();
        List<String> symbols1 = new ArrayList<String>();
        List<String> symbols2 = new ArrayList<String>();
        List<String> symbols3 = new ArrayList<String>();
        symbols1.add("S");
        symbols2.add("B");
        symbols3.add("C");
        List<List<String>> rules1 = new ArrayList<List<String>>();
        List<List<String>> rules2 = new ArrayList<List<String>>();
        List<List<String>> rules3 = new ArrayList<List<String>>();
        List<String> rule1 = new ArrayList<String>();
        List<String> rule2 = new ArrayList<String>();
        List<String> rule3 = new ArrayList<String>();
        List<String> rule4 = new ArrayList<String>();
        List<String> rule5 = new ArrayList<String>();
        rule1.add("B"); rule1.add("b");
        rule2.add("C"); rule2.add("d");
        rule3.add("a"); rule3.add("C");
        rule4.add("@");
        rule5.add("c");
        rules1.add(rule1); rules1.add(rule2);
        rules2.add(rule3); rules2.add(rule4);
        rules3.add(rule5); rules3.add(rule4);
        Production prod1 = new Production(symbols1, rules1);
        Production prod2 = new Production(symbols2, rules2);
        Production prod3 = new Production(symbols3, rules3);
        testProductions.add(prod1); testProductions.add(prod2); testProductions.add(prod3);
        return testProductions;
    }

    @Test
    public void testCheckCFG(){
        Assert.assertTrue(grammar.checkCFG());
    }

    @Test
    public void testPrintNonTerminals(){
        Assert.assertEquals("S, B, C" , grammar.printNonTerminals());
    }

    @Test
    public void testGetNonTerminals(){
        List<String> nonTerminals = new ArrayList<String>();
        nonTerminals.add("S");
        nonTerminals.add("B");
        nonTerminals.add("C");
        Assert.assertEquals(nonTerminals, grammar.getNonTerminals());
    }

    @Test
    public void testGetStartingSymbol() {
        Assert.assertEquals("S", grammar.getStartingSymbol());
    }

    @Test
    public void testPrintTerminals() {
        var terminals = new StringBuilder();
        terminals.append("@, a, b, c, d");
        Assert.assertEquals(terminals.toString(), grammar.printTerminals());
    }

    @Test
    public void testPrintProductions() {
        List<Production> productions = grammar.getProductions();
        List<Production> testProductions = getProductions();
        Assert.assertEquals(testProductions.toString(), productions.toString());
    }

    @Test
    public void testGetRules(){
        List<Production> testProductions = getProductions();
        List<Production> productions = grammar.getProductions();
        Assert.assertEquals(testProductions.get(0).getRules(), productions.get(0).getRules());
    }

    @Test
    public void testGetSymbols() {
        List<Production> testProductions = getProductions();
        List<Production> productions = grammar.getProductions();
        Assert.assertEquals(testProductions.get(0).getSymbols(), productions.get(0).getSymbols());
    }

    @Test
    public void testGetProductionsForACertainNonTerminal(){
        List<String> symbols = new ArrayList<>();
        symbols.add("B");
        List<List<String>> rules1 = new ArrayList<>();
        List<String> rule1 = new ArrayList<>();
        List<String> rule2 = new ArrayList<>();
        rule1.add("a"); rule1.add("C");
        rule2.add("@");
        rules1.add(rule1); rules1.add(rule2);
        Production prod1 = new Production(symbols, rules1);
        Set<Production> prodSet = new HashSet<>();
        prodSet.add(prod1);
        Assert.assertEquals(prodSet.toString(), grammar.getProductionsForNonTerminal("a").toString());
    }

    @Test
    public void testGetFirst(){
        Map<String, Set<String>> first = new HashMap<>();
        Set<String> first1 = new HashSet<>();
        Set<String> first2 = new HashSet<>();
        Set<String> first3 = new HashSet<>();
        first1.add("@"); first1.add("a");
        first2.add("@"); first2.add("a"); first2.add("c");
        first3.add("@"); first3.add("c");
        first.put("B", first1);
        first.put("S", first2);
        first.put("C", first3);
        Assert.assertEquals(first, parser.getFirst());
    }

    @Test
    public void testGetFollow(){
        Map<String, Set<String>> first = new HashMap<>();
        Set<String> first1 = new HashSet<>();
        Set<String> first2 = new HashSet<>();
        Set<String> first3 = new HashSet<>();
        first1.add("b");
        first2.add("$");
        first3.add("b"); first3.add("d");
        first.put("B", first1);
        first.put("S", first2);
        first.put("C", first3);
        Assert.assertEquals(first, parser.getFollow());
    }

    @Test
    public void testFollowOf(){
        Set<String> terminalsForCurrentNonTerminal = new HashSet<>();
        terminalsForCurrentNonTerminal.add("c");
        Assert.assertEquals(terminalsForCurrentNonTerminal, parser.getFollow().get("a"));
    }

    @Test
    public void testFirstOf(){
        Set<String> terminalsForCurrentNonTerminal = new HashSet<>();
        terminalsForCurrentNonTerminal.add("@");
        terminalsForCurrentNonTerminal.add("a");
        Assert.assertEquals(terminalsForCurrentNonTerminal, parser.firstOf("B"));
    }
}

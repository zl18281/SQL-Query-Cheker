package com.fan.parser;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class ParserDemo {

    private String code;

    public ParserDemo(String code) {
        System.out.println("hahaha");
        this.code = code;
    }

    public String parseSql() {
        CharStream input = CharStreams.fromString(this.code);
        MySqlLexer lexer = new MySqlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MySqlParser parser = new MySqlParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new VerboseListener());
        ParseTree tree = parser.root();
        return tree.toStringTree(parser);

    }

    public static void main (String[] args) {
        ParserDemo pd = new ParserDemo("SELECT FROM hero WHERE name='fan';");
        System.out.println(pd.parseSql());
    }
}

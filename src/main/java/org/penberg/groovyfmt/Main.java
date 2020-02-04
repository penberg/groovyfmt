package org.penberg.groovyfmt;

import antlr.collections.AST;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.groovy.antlr.treewalker.SourcePrinter;
import org.codehaus.groovy.antlr.treewalker.Visitor;

public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("Formatting " + args[0] + " ...");
    SourceBuffer sourceBuffer = new SourceBuffer();
    FileReader reader = new FileReader(args[0]);
    UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(reader, sourceBuffer);
    GroovyLexer lexer = new GroovyLexer(unicodeReader);
    unicodeReader.setLexer(lexer);
    GroovyRecognizer parser = GroovyRecognizer.make(lexer);
    String[] tokenNames = parser.getTokenNames();
    parser.setSourceBuffer(sourceBuffer);
    parser.compilationUnit();
    AST ast = parser.getAST();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Visitor visitor = new SourcePrinter(System.out, tokenNames, true);
    AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
    traverser.process(ast);
  }
}

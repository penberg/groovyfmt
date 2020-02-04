package org.penberg.groovyfmt;

import antlr.collections.AST;
import java.io.FileReader;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;

public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("Formatting " + args[0] + " ...");
    SourceBuffer sourceBuffer = new SourceBuffer();
    FileReader reader = new FileReader(args[0]);
    UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(reader, sourceBuffer);
    GroovyLexer lexer = new GroovyLexer(unicodeReader);
    unicodeReader.setLexer(lexer);
    GroovyRecognizer parser = GroovyRecognizer.make(lexer);
    parser.setSourceBuffer(sourceBuffer);
    parser.compilationUnit();
    AST ast = parser.getAST();
  }
}

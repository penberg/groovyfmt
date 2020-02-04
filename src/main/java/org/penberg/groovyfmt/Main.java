package org.penberg.groovyfmt;

import antlr.collections.AST;
import io.airlift.airline.Arguments;
import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.Option;
import io.airlift.airline.SingleCommand;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;
import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.groovy.antlr.treewalker.SourcePrinter;

@Command(name = "grooyfmt", description = "A tool to format Groovy/Jenkinsfile source code")
public class Main {
  @Inject
  public HelpOption helpOption;

  @Arguments(description = "Files to format")
  public List<String> files = new LinkedList();

  @Option(name = {"-i", "--indent"}, description = "Indentation width in spaces or 'tab' for tabs")
  public String indent;

  public static void main(String[] args) throws Exception {
    Main app = SingleCommand.singleCommand(Main.class).parse(args);
    if (app.helpOption.showHelpIfRequested()) {
      return;
    }
    app.run();
  }

  public void run() throws Exception {
    for (String file : files) {
      format(file);
    }
  }

  private void format(String file) throws Exception {
    System.out.println("Formatting " + file + " ...");
    GroovyRecognizer parser = parse(file);
    print(parser);
  }

  private GroovyRecognizer parse(String file) throws Exception {
    SourceBuffer sourceBuffer = new SourceBuffer();
    FileReader reader = new FileReader(file);
    UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(reader, sourceBuffer);
    GroovyLexer lexer = new GroovyLexer(unicodeReader);
    unicodeReader.setLexer(lexer);
    GroovyRecognizer parser = GroovyRecognizer.make(lexer);
    parser.setSourceBuffer(sourceBuffer);
    parser.compilationUnit();
    return parser;
  }

  private void print(GroovyRecognizer parser) {
    String[] tokenNames = parser.getTokenNames();
    AST ast = parser.getAST();
    SourcePrinter printer = new SourcePrinter(System.out, tokenNames, true);
    if (indent != null) {
      if (indent.equals("tab")) {
        printer.setIndent("\t");
      } else {
        int count = Integer.parseInt(indent);
        // This is not as pretty as String.repeat(), but it's what's available everywhere pre-Java 11.
        String indent = new String(new char[count]).replace("\0", " ");
        printer.setIndent(indent);
      }
    }
    AntlrASTProcessor traverser = new SourceCodeTraversal(printer);
    traverser.process(ast);
  }
}

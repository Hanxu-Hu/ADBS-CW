package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.Atom;
import ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * In-memory database system
 *
 */






public class Minibase {

    public static void main(String[] args) {
        PrintStream original = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));

        if (args.length != 3) {
            System.err.println("Usage: Minibase database_dir input_file output_file");
            return;
        }

        String databaseDir = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
        evaluateCQ(databaseDir, inputFile, outputFile);

    }


    public static void evaluateCQ(String databaseDir, String inputFile, String outputFile) {
        try {
            Interpreter interpreter = new Interpreter(databaseDir, inputFile, outputFile);
            System.out.println("a flag");
            interpreter.evaluateQuery();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.operator.*;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    PrintStream printStream;
    Operator root;

    public Interpreter(String databaseDir, String inputFile, String outputFile) throws IOException {
        // read query
        Query query = QueryParser.parse(Paths.get(inputFile));
        // read database
        Catalog.INSTANCE.initialize(databaseDir);
        // plan query
        planQuery(query);
        // prepare PrintStream
        printStream = new PrintStream(new FileOutputStream(outputFile, true));
    }

    public void evaluateQuery() {
        root.dump(printStream);
    }

    private void planQuery(Query query) {
        RelationalAtom head = query.getHead();
        List<Atom> body = query.getBody();
        List<ComparisonAtom> comp = new ArrayList<>();
        RelationalAtom relationalAtom = null;
        for (Atom x: body){
            if (ComparisonAtom.class.isInstance(x)){

                comp.add((ComparisonAtom) x);

            }else{
                relationalAtom = (RelationalAtom) x;
            };
        }
        planSingleRelationQuery(head, relationalAtom, comp);
    }

    private void planSingleRelationQuery(RelationalAtom head, RelationalAtom rAtom,
                                          List<ComparisonAtom> bodyComparisonAtoms) {

        Operator scanOp = null;
        try {
            scanOp = new ScanOperator(rAtom.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Term> terms = rAtom.getTerms();
         List<Term> headTerms = head.getTerms();
         String s1 = "";
         String s2 = "";

         for (Term x: terms){
             s1+= x.toString()+",";
         }
         for (Term x: headTerms){
             s2+= x.toString()+",";
         }

         boolean requireProjection = !s1.equals(s2);
         boolean requireSelectionExplicit = bodyComparisonAtoms.size() >= 1;
         System.out.println(requireSelectionExplicit);
         boolean requireSelectionImplicit = rAtom.getTerms().stream().anyMatch(term -> term instanceof Constant);
         boolean requireSelection = requireSelectionExplicit || requireSelectionImplicit;
         if (!requireProjection && !requireSelection) {
            root = scanOp;

         } else if (requireProjection && !requireSelection) {
            root = new ProjectionOperator( rAtom, head);
            ((ProjectionOperator) root).SetChild(scanOp);
         } else if (!requireProjection && requireSelection) {
            root = new SelectOperator(rAtom, bodyComparisonAtoms);
            ((SelectOperator) root).SetChild(scanOp);
         } else if (requireProjection && requireSelection) {
            Operator selectOp = new SelectOperator(rAtom, bodyComparisonAtoms);
            root = new ProjectionOperator(rAtom, head);
            ((ProjectionOperator) root).SetChild(selectOp);
             ((SelectOperator) selectOp).SetChild(scanOp);
         }
     }


}

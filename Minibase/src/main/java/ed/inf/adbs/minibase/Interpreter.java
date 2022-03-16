package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.operator.*;
import ed.inf.adbs.minibase.parser.QueryParser;
import jdk.jfr.Relational;

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
        List<Atom> body = query.getBody();
        System.out.println(body.get(0).toString());
        RelationalAtom atom = changeTermName((RelationalAtom) body.get(0));
        System.out.println(atom.toString());
        printStream = new PrintStream(new FileOutputStream(outputFile, true));
    }

    public void evaluateQuery() {
        root.dump(printStream);
    }


    public RelationalAtom changeTermName(RelationalAtom atom){
        List<Term> terms = atom.getTerms();
        String rName = atom.getName();
        for (Term x: terms){
            String s = rName+"_"+x.toString();
            x = new Variable(s);
        }
        return atom;
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


    private void planMultipleRelationQuery(RelationalAtom head, List<RelationalAtom> rAtoms, List<ComparisonAtom> bodyComparisonAtoms) throws IOException {
        RelationalAtom leftAtom = rAtoms.get(0);
        RelationalAtom rightAtom = rAtoms.get(1);
        Operator leftScanOp = new ScanOperator(leftAtom);
        Operator rightScanOp = null;

        for (int i = 0; i <rAtoms.size()-1 ; i++) {

            //ScanOperator leftScanOp = null;
            rightAtom = rAtoms.get(i+1);
            try {
                rightScanOp = new ScanOperator(rightAtom);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //SelectOperator leftSelectOperator = new SelectOperator(leftScanOp,leftAtom, bodyComparisonAtoms);
            SelectOperator rightSelectOperator = new SelectOperator(rightScanOp,rightAtom, bodyComparisonAtoms);

            assert leftScanOp != null;

            JoinOperator joinOp = new JoinOperator(leftScanOp, rightSelectOperator, leftAtom, rightAtom, bodyComparisonAtoms);
            //RelationalAtom joinRe = joinOp.joinRelation();
            leftScanOp = joinOp;

        }

    }


    private void planSingleRelationQuery(RelationalAtom head, RelationalAtom rAtom,
                                          List<ComparisonAtom> bodyComparisonAtoms) {

        Operator scanOp = null;
        try {
            scanOp = new ScanOperator(rAtom);
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
            root = new ProjectionOperator(scanOp, rAtom, head);
            //((ProjectionOperator) root).SetChild(scanOp);
         } else if (!requireProjection && requireSelection) {
            root = new SelectOperator(scanOp, rAtom, bodyComparisonAtoms);
            //((SelectOperator) root).SetChild(scanOp);
         } else if (requireProjection && requireSelection) {
            Operator selectOp = new SelectOperator(scanOp,rAtom, bodyComparisonAtoms);
            root = new ProjectionOperator(selectOp,rAtom, head);
            //((ProjectionOperator) root).SetChild(selectOp);
             // ((SelectOperator) selectOp).SetChild(scanOp);
         }
     }


}

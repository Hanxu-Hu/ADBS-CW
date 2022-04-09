package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.*;
import ed.inf.adbs.minibase.operator.*;
import ed.inf.adbs.minibase.parser.QueryParser;
//import jdk.jfr.Relational;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    PrintStream printStream;
    Operator root;
    /**
     * this method is to build the whole pipeline of query evaluation, which includes parse the query, initialize catalog,
     * and build the query plan, then print the outputs
     * @param databaseDir  the directory of the database files
     * @param inputFile    the direcotry of input file
     * @param outputFile   the directory of outputfile
     * @throws IOException
     */
    public Interpreter(String databaseDir, String inputFile, String outputFile) throws IOException {
        // read query
        Query query = QueryParser.parse(Paths.get(inputFile));
        // read database
        Catalog.INSTANCE.initialize(databaseDir);
        // plan query


        planQuery(query);
        // prepare PrintStream
        List<Atom> body = query.getBody();
        //System.out.println("plan");
        RelationalAtom atom = changeTermName((RelationalAtom) body.get(0));
        //System.out.println(atom.toString());
        printStream = new PrintStream(new FileOutputStream(outputFile, false));
    }
    /**
     * this method is to dump all the outputs of the root operator built by planQuery method.
     *
     */
    public void evaluateQuery() {
        root.dump(printStream);
    }
    /**
     *
     * @param atom a RelationalAtom
     * @return return a RelationalAtom
     * this method is to change the name of a relational atom when join two relational atoms.
     */
    public RelationalAtom changeTermName(RelationalAtom atom){
        List<Term> terms = atom.getTerms();
        String rName = atom.getName();
        for (Term x: terms){
            String s = rName+"_"+x.toString();
            x = new Variable(s);
        }
        return atom;
    }

    /**
     * this method split a query into head and body, and divide the body into comparison atoms and relational atoms.
     * then let the head, comparison atoms and relational atoms as input arguments of "planMultipleRelationQuer" method
     * to do the query plan.
     * @param query a Query,
     * @throws IOException
     */
    private void planQuery(Query query) throws IOException {
        RelationalAtom head = query.getHead();
        List<Atom> body = query.getBody();
        List<ComparisonAtom> comp = new ArrayList<>();
        List<RelationalAtom> relationalAtoms = new ArrayList<>();
        for (Atom x: body){
            if (ComparisonAtom.class.isInstance(x)){

                comp.add((ComparisonAtom) x);

            } else {
                relationalAtoms.add((RelationalAtom) x);
            };
        }
        //if (relationalAtoms.size()==1) {
        //    planSingleRelationQuery(head, relationalAtoms.get(0), comp);
        //} else {
        planMultipleRelationQuery(head,relationalAtoms,comp);
        //}
    }

    /**
     * This method build the whole plan of the query. Firstly, it will get the first atom of the input relational atoms
     * as the left atom, then using this left atom to initialize a ScanOperator class, called leftsCanOp, then using this
     * operator as the input to initialize a SelectOperator.
     * if there is only one relational atom, the SelectOperator will be used to initialize the projectionOperator directly,
     * if there are more than on relational atoms, a for-loop will be used to construct a tree of operators. Specifically,
     * for every step of the loop, the join operator and join relational atom of the last step will become new left operator
     * and left relational atom, then they will be join with new right atom and right operator at this step, for the left-deep join rule.
     * then the join operator and relational atom of final step will be used as the input arguments of a projection operator with head.
     * Then, it will be determined that whether there is a aggregator or not, and what type of the aggregator.
     * Finally, if there is a aggregator in the head, an SumOperator or AvgOperator will be added according to the aggregator's type.
     * @param head the head of the query,
     * @param rAtoms the body of the query, a list of RelationalAtom.
     * @param bodyComparisonAtoms the body of the query, a list of ComparisonAtom.
     * @throws IOException
     */
    private void planMultipleRelationQuery(RelationalAtom head, List<RelationalAtom> rAtoms, List<ComparisonAtom> bodyComparisonAtoms) throws IOException {
        RelationalAtom leftAtom = rAtoms.get(0).clone();

        ScanOperator leftScanOp = new ScanOperator(leftAtom);

        SelectOperator leftOp = new SelectOperator(leftScanOp,leftAtom, bodyComparisonAtoms);
        //System.out.println(bodyComparisonAtoms);
        ScanOperator rightScanOp = null;
        JoinOperator preJoinOp = null;
        JoinOperator joinOp = null;
        if (rAtoms.size()>1){
            RelationalAtom rightAtom = rAtoms.get(1).clone();

            for (int i = 0; i <rAtoms.size()-1 ; i++) {
                rightAtom = rAtoms.get(i+1);
                try {
                    rightScanOp = new ScanOperator(rightAtom);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                leftAtom = leftAtom.clone();
                rightAtom = rightAtom.clone();
                SelectOperator rightSelectOperator = new SelectOperator(rightScanOp,rightAtom, bodyComparisonAtoms);
                if (i==0){
                    joinOp = new JoinOperator(leftOp, rightSelectOperator, leftAtom, rightAtom, bodyComparisonAtoms);
                }
                else{
                    joinOp = new JoinOperator(preJoinOp, rightSelectOperator, leftAtom, rightAtom, bodyComparisonAtoms);
                    joinOp = new JoinOperator(preJoinOp, rightSelectOperator, leftAtom, rightAtom, bodyComparisonAtoms);

                }
                leftAtom = joinOp.joinRelation();
                preJoinOp = joinOp;
            }
        }
        List<Term> headTerms = head.getTerms();
        boolean flag = false;
        int i = 0;
        String opName = "";
        for (Term term : headTerms){

            if (term instanceof Aggregator){
                flag = true;
                opName = ((Aggregator) term).getOp();
                Variable var = ((Aggregator) term).getVar();
                List<Term> newHeadTerms = new ArrayList<Term>(headTerms);
                newHeadTerms.set(i,var);
                String hName = head.getName();
                RelationalAtom newHead = new RelationalAtom(hName,newHeadTerms);
                ProjectionOperator projOp;
                if (rAtoms.size()>1){
                     projOp = new ProjectionOperator(joinOp,leftAtom,newHead, flag);

                } else {
                     projOp = new ProjectionOperator(leftOp,leftAtom,newHead, flag);
                }

                if (opName.equals("AVG")){
                    root = new AvgOperator(projOp,head);
                } else{
                    root = new SumOperator(projOp,head);
                }

                break;
            }
            i+=1;
        }
        if (flag==false){
            if (rAtoms.size()>1){
                root = new ProjectionOperator(joinOp,leftAtom,head, flag);
            } else {
                root = new ProjectionOperator(leftOp,leftAtom,head, flag);
            }
        }

    }


}

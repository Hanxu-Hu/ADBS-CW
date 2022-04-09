package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;

import java.util.List;

public class SelectOperator extends Operator {
    private Operator child;
    private List<ComparisonAtom> comp;
    private RelationalAtom atom;


    /**
     * this method is constructing an instance of select operator
     * for every terms in the relational atom, determine whether the term is variable or not,
     * if it is not a variable, it indicate that this term is an implicit equivalence,
     * so a comparison atom should be created.
     * then, the comparison atoms and relational atom will be saved as private variable for further use.
     * @param child the child operator of the select operator
     * @param rAtom the relational atom
     * @param compAtoms a list of comparison atoms
     */
    public SelectOperator(Operator child, RelationalAtom rAtom, List<ComparisonAtom> compAtoms){
        this.child = child;
        List<Term> terms= rAtom.getTerms();                     //implicit equivalence predicate
        for (Term x:terms){
            boolean isVar = Variable.class.isInstance(x);
            if(isVar==false){
                ComparisonOperator op = ComparisonOperator.EQ;
                Term term1 = x;
                Term term2 =x;

                Variable var  = new Variable(x.toString());

                ComparisonAtom comp = new ComparisonAtom(var, term2, op);
                compAtoms.add(comp);
            }
        }
        this.comp =compAtoms;
        this.atom = rAtom;

    }

    @Override
    /**
     * this getNextTuple() method first call the getNextTuple() of child operator,
     * then if the return of the child getNextTuple() method is not null; it will use "check" method to know whether this
     * tuple satisfied the condition of the comparison atoms.
     */
    public Tuple getNextTuple() {
        Tuple tuple = child.getNextTuple();
        SelectCheck check = new SelectCheck();
        while (tuple!=null){
            if (check.check(this.comp, this.atom, tuple)){
                return tuple;
            }
            else{
                tuple = child.getNextTuple();
            }
        }
        return null;
    }

    @Override
    /**
     * the reset() method call the reset() method of the child operator.
     */
    public void reset() {
        this.child.reset();
    }
}

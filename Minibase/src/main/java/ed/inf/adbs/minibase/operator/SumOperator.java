package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.Aggregator;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;

import java.util.List;

public class SumOperator extends Operator {
    private AggregateOperator agg;

    /**
     * this method construct a sum operator, initialize an aggregator operator as a child operator.
     * @param childOp child operator
     * @param head head of query.
     */
    public SumOperator(Operator childOp, RelationalAtom head){
        agg = new AggregateOperator(childOp, head, "SUM");
    }

    /**
     * extended of getNextTuple() method
     */
    @Override
    public Tuple getNextTuple() {
        Tuple tuple = agg.getNextTuple();
        return tuple;
    }

    /**
     * extended of reset() method.
     */
    @Override
    public void reset() {
        agg.reset();

    }
}

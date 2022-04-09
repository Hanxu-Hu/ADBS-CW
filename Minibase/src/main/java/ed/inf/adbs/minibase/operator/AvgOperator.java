package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.Aggregator;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;

import java.util.List;

public class AvgOperator extends Operator {
    private AggregateOperator agg;
    /**
     * this method construct a avg operator, initialize an aggregator operator as a child operator.
     * @param childOp child operator
     * @param head head of query.
     */
    public AvgOperator(Operator childOp, RelationalAtom head){
        agg = new AggregateOperator(childOp, head, "AVG");
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


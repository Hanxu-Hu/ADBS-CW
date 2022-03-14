package ed.inf.adbs.minibase.operator;

import java.io.PrintStream;

public abstract class Operator {

    public abstract Tuple getNextTuple();
    public abstract void reset();


    public void dump(PrintStream printStream) {
        Tuple tuple = null;
        tuple = getNextTuple();


        while(tuple!= null) {
            printStream.println(tuple);
            tuple = getNextTuple();
        }

    }

}



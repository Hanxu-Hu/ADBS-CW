package ed.inf.adbs.minibase.operator;

import java.io.PrintStream;

public abstract class Operator {


    /**
     * this method is to get the next tuple of the operator
     * @return
     */
    public abstract Tuple getNextTuple();

    /**
     * reset and return the first tuple when next calling getNextTuple()
     */
    public abstract void reset();


    /**
     *this method is to print all the tuple of the operator and write it to the output file using Printstream.
     * @param printStream input an instance of Printstream class
     */
    public void dump(PrintStream printStream) {
        Tuple tuple = null;
        tuple = getNextTuple();


        while(tuple!= null) {
            printStream.println(tuple);
            tuple = getNextTuple();
        }

    }

}



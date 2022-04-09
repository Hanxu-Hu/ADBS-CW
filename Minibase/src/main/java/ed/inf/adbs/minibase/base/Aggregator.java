package ed.inf.adbs.minibase.base;


public class Aggregator extends Term {
    private String name;
    private Variable variable;
    private String op= "";

    public Aggregator(String name, String op) {
        this.name = op + '_' + name;
        variable = new Variable(name);
        this.op = op;
    }


    public Variable getVar() {
        return variable;
    }

    public String getOp() {
        return this.op;
    }


    @Override
    public String toString() {
        return name;
    }
}

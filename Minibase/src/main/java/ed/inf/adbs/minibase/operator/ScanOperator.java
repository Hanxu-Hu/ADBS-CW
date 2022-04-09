package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.RelationalAtom;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScanOperator extends Operator {
    private String reName;
    private List<String> tuples;
    //private int step=0;
    private BufferedReader br;

    /**
     * this method initialize a ScanOperator class, using the input relational atom and the catalog to get the path of
     * files related to the atom. the using a BufferReader instance for further reading the content of the files to get
     * tuples.
     * @param atom a RelationalAtom instance
     * @throws IOException
     */
    public ScanOperator(RelationalAtom atom) throws IOException {

        this.reName = atom.getName();
        //System.out.println(reName);
        // Open the file
        String path = Catalog.INSTANCE.getRelations(reName);
        List<String> tuples = new ArrayList<String>();
        FileInputStream fstream = new FileInputStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        this.br = br;
        br.mark(1000);
        //readTxt(path,tuples);
        this.tuples =tuples;
    }
    @Override
    /**
     * this method use the BufferReader instance initialized when constructing the class,
     * reading a line from the bufferreader instance to get the next tuple.
     * if all lines have read, return null.
     */
    public Tuple getNextTuple() {

        String strLine = null;
        Tuple tuple = new Tuple();
        try {
            //System.out.println(br.readLine());
            if ((strLine = br.readLine()) != null)   {
                String setName =this.reName;
                tuple.setTuple(strLine,setName);
                return tuple;
            }
            else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    /**
     * this method using reset() method of BufferReader class to reset, because the BufferReader instance use mark() method
     * when constructing the class, it will get back to that status, then return the first tuple when calling getNextTuple() next time.
     */
    public void reset() {
        try {
            this.br.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

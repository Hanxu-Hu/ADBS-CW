package ed.inf.adbs.minibase.operator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScanOperator extends Operator {
    private String reName;
    private List<String> tuples;
    //private int step=0;
    private BufferedReader br;

    public ScanOperator(String reName) throws IOException {
        this.reName = reName;
        System.out.println(reName);
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
    public Tuple getNextTuple() {

        String strLine = null;
        Tuple tuple = new Tuple();


        try {
            //System.out.println(br.readLine());
            if ((strLine = br.readLine()) != null)   {
                // Print the content on the console



                //if (this.step<=(this.tuples.size()-1)){
                //    String sTuple=this.tuples.get(this.step);
                //   String setName = new String();
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

        //TODO;  readline


    }

    public void readTxt(String filePath, List<String> context) {

        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {

                    context.add(lineTxt);
                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }

    }

    @Override
    public void reset() {
        try {
            this.br.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this.step =0;
        //String res = new String();
        //res =this.tuples.get(this.step);
        //Tuple tuple = new Tuple();
        //tuple.setTuple(res,this.reName);


    }
}

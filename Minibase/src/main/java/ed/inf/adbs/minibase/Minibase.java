package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.Atom;
import ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


/**
 * In-memory database system
 *
 */






public class Minibase {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println("Usage: Minibase database_dir input_file output_file");
            return;
        }

        String databaseDir = args[0];
        String inputFile = args[1];
        String outputFile = args[2];

        try {
            Interpreter interpreter = new Interpreter(databaseDir, inputFile, outputFile);
            interpreter.evaluateQuery();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        ArrayList<String> SchemaNameList =  new ArrayList<String>();
//        String path = databaseDir;
//        String ends ="txt";
//        getAllFileName(path, SchemaNameList, ends);
//        List<String> context = new ArrayList<String>();
//        for (String name:SchemaNameList){
//            readTxt(name,context);
//        }
//        List<String> reName = new ArrayList<>();
//        for (String line:context) {
//             String[] x =line.split(" ");
//             reName.add(x[0]);
//        }
//       ;
//
//
//
//
//
//        ArrayList<String> Relation_NameList =  new ArrayList<String>();
//        ends ="csv";
//        getAllFileName(path, Relation_NameList, ends);
//        ArrayList<String> R_NameList =  new ArrayList<String>();
//
//
//        for (String name: Relation_NameList){
//            String[] name_list = name.split("/");
//            int len = name_list.length-1;
//            String re_name = name_list[len];
//            String[] name_list_ =re_name.split("\\.");
//            String re_name_ =name_list_[0];
//            R_NameList.add(re_name_);
//
//
//
//        }
//        System.out.println(R_NameList);
//        Catalog catalog = Catalog.INSTANCE;
//        HashMap data = new HashMap();
//        int i = 0;
//        for (String re: R_NameList){
//
//            data.put(re, Relation_NameList.get(i));
//            i+=1;
//        }
//        HashMap schema = new HashMap();
//        i=0;
//        for (String c:context){
//
//            schema.put(reName.get(i), c);
//            i+=1;
//        }
//
//
//        catalog.setSchema(schema);
//
//        String res = catalog.getSchema(reName.get(1));
//
//
//
//        //for (String sc: SchemaNameList ){
//        //    schema.put(SchemaNameLis)
//
//        //}
//
//        catalog.setRelations(data);
//        String path_ =catalog.getRelations(R_NameList.get(0));
//        String rename = "S";
//        ScanOperator scan = new ScanOperator(rename,catalog);
//        Tuple tuple = scan.getNextTuple();
//
//        String sTuple = tuple.toString();
//        System.out.println(sTuple);
//        Tuple tuple_1 = scan.getNextTuple();
//        String sTuple_1 =tuple_1.toString();
//        System.out.println(sTuple_1);
//
//        scan.reset();
//        scan.dump(System.out);
//
//        PrintStream filePrintStream = null;
//        try {
//            filePrintStream = new PrintStream(
//                    new FileOutputStream("test.txt", true));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        scan.reset();
//        scan.dump(filePrintStream);
//
//        SelectCheck select = new SelectCheck();
//        System.out.println(atom);
//        System.out.println(compAtom);
//        String name =tuple_1.getRename();
//        System.out.println(name);
//
//        boolean result = select.Check(compAtom, atom, tuple_1);
//        System.out.println(result);




//        parsingExample(inputFile);
    }


    public static void readTxt(String filePath, List<String> context) {

        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    System.out.println(lineTxt);
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


    public static void getAllFileName(String path,ArrayList<String> fileNameList, String ends) {
        //ArrayList<String> files = new ArrayList<String>();
        boolean flag = false;
        File file = new File(path);
        File[] tempList = file.listFiles();


        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String temp = tempList[i].toString();
                if (temp.endsWith(ends)){
                    fileNameList.add(temp);


                }

            }
            if (tempList[i].isDirectory()) {

                getAllFileName(tempList[i].getAbsolutePath(),fileNameList, ends);
            }
        }
        //return;
    }


    public static void evaluateCQ(String databaseDir, String inputFile, String outputFile) {
        // TODO: add your implementation
    }

    /**
     * Example method for getting started with the parser.
     * Reads CQ from a file and prints it to screen, then extracts Head and Body
     * from the query and prints them to screen.
     */

    public static void parsingExample(String filename) {
        try {
            Query query = QueryParser.parse(Paths.get(filename));
//            Query query = QueryParser.parse("Q(x, y) :- R(x, z), S(y, z, w), z < w");
//            Query query = QueryParser.parse("Q(x, w) :- R(x, 'z'), S(4, z, w), 4 < 'test string' ");

            System.out.println("Entire query: " + query);
            RelationalAtom head = query.getHead();
            System.out.println("Head: " + head);
            List<Atom> body = query.getBody();
            System.out.println("Body: " + body);
        }
        catch (Exception e)
        {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }

}

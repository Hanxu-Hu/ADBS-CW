package ed.inf.adbs.minibase.operator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ed.inf.adbs.minibase.Minibase.readTxt;


public enum Catalog{
    INSTANCE;
    private HashMap<String, String> relations;
    private HashMap<String, String> schema;

    public void initialize(String databaseDir) {

        ArrayList<String> schemaFileList =  new ArrayList<String>();
        ArrayList<String> relationFileList =  new ArrayList<String>();
        getAllFileName(databaseDir, schemaFileList, "txt");
        getAllFileName(databaseDir, relationFileList, "csv");
        schema = schemaFileToMap(schemaFileList);
        relations = relationFileToMap(relationFileList);

    }


    public HashMap<String,String> schemaFileToMap(List<String> schemaFileList){

        List<String> schemaContent = new ArrayList<String>();
        for (String name:schemaFileList){
            readTxt(name,schemaContent);
        }
        HashMap<String, String> schemaMap = new HashMap<String, String>();
        for (String line:schemaContent) {
            String[] schemaContentArray =line.split(" ");
            schemaMap.put(schemaContentArray[0], line);
        }
        return schemaMap;
    }

    public HashMap<String, String> relationFileToMap(List<String> relationFileList){
        HashMap<String, String> relationMap = new HashMap<String, String>();
        for (String path: relationFileList){
            String[] nameList = path.split("/");
            int len = nameList.length-1;
            String reName = nameList[len];
            String[] nameListFinal =reName.split("\\.");
            String relationName =nameListFinal[0];
            relationMap.put(relationName, path);
        }
        return relationMap;
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


    public String getRelations(String relation_name){

        Object re_path =this.relations.get(relation_name);
        String copy_path =new String();

        copy_path =re_path.toString();



        return copy_path;
    }


    public String getSchema(String re_name){
        Object schema_ = this.schema.get(re_name);
        String schema = (String) schema_;
        String res = new String();
        res = schema;
        return res;
    }



}

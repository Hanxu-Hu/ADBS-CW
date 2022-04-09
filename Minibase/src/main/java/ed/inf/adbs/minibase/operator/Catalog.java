package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.RelationalAtom;

import java.io.*;
import java.util.*;


public enum Catalog{
    INSTANCE;
    private HashMap<String, String> relations;
    private HashMap<String, String> schema;

    /**
     * this method initialize the catalog  by read the schema and relation files.
     * @param databaseDir the directory of database
     * @throws IOException
     */
    public void initialize(String databaseDir) throws IOException {
        ArrayList<String> relationFileList =  new ArrayList<String>();
        relationFileList = getAllFileName(databaseDir);
        schema = schemaFileToMap(databaseDir);
        System.out.println(schema);
        relations = relationFileToMap(relationFileList);
    }

    /**
     * this method add the schema about the join relation
     * @param atom1 left relational atom
     * @param atom2 right relational atom
     * @param map the mappings about the duplicated position.
     */
    public void addSchema(RelationalAtom atom1, RelationalAtom atom2,HashMap<Integer,Integer> map){
        String name1 = atom1.getName();
        String name2 = atom2.getName();
        String joinName = name1+"_"+name2;
        String schemaContent1 = schema.get(name1);
        String schemaContent2 = schema.get(name2);
        String[] schemaContentArray1 = schemaContent1.split(" ");
        String[] schemaContentArray2 = schemaContent2.split(" ");
        //List<String> schemaContentList1 =  Arrays.asList(schemaContentArray1);
        List<String> schemaContentList1 = new LinkedList<String>(Arrays.asList(schemaContentArray1));
        //System.out.println(schemaContentList1.get(0));
        schemaContentList1.remove(0);
        List<String> schemaContentList2 = new LinkedList<String>(Arrays.asList(schemaContentArray2));
        schemaContentList2.remove(0);
        int i = 0;
        if (map.keySet() != null) {
            for (Integer key : map.keySet()) {
                schemaContentList1.remove(key-i);
                i+=1;
            }
        }
        List<String> mergeSchemaContent = new ArrayList<>();
        mergeSchemaContent.addAll(schemaContentList1);
        mergeSchemaContent.addAll(schemaContentList2);
        String mergeSchemaString = joinName+" ";
        for (int j = 0; j < mergeSchemaContent.size()-1; j++) {
            mergeSchemaString+=mergeSchemaContent.get(j)+" ";
        }
        mergeSchemaString+=mergeSchemaContent.get(mergeSchemaContent.size()-1);
        schema.put(joinName,mergeSchemaString);

    }


    /**
     * this method convert the schema to the mappings between relation name and schema content and return the mapping.
     * @param path the schema file's path
     * @return a hashmap
     * @throws IOException
     */
    public HashMap<String,String> schemaFileToMap(String path) throws IOException {
        path += "/schema.txt";
        BufferedReader schema = new BufferedReader(new FileReader(path));

        List<String> schemaContent = new ArrayList<String>();
        String str = "";
        while ((str=schema.readLine())!=null) {
            schemaContent.add(str);
        }
        HashMap<String, String> schemaMap = new HashMap<String, String>();
        for (String line:schemaContent) {
            String[] schemaContentArray =line.split(" ");
            schemaMap.put(schemaContentArray[0], line);
        }
        return schemaMap;
    }

    /**
     * this method convert the relation content files' paths to the mapping between the relation names and the paths.
     * @param relationFileList a list contain the paths of relation files.
     * @return a hashmap
     */
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

    /**
     * this method get the list of all relation's file paths.
     * @param path the directory of the database.
     * @return the list of all relation's file paths.
     * @throws IOException
     */
    public static ArrayList<String> getAllFileName(String path) throws IOException {
        ArrayList<String> files = new ArrayList<String>();
        boolean flag = false;
        path += "/files/";
        File file = new File(path);
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                files.add(fileList[i].toString());
            }
        }
        return files;
    }

    /**
     * this method get a relation's corresponding file path.
     * @param relation_name the name of relation.
     * @return
     */
    public String getRelations(String relation_name){

        Object re_path =this.relations.get(relation_name);
        String copy_path =new String();
        copy_path =re_path.toString();
        return copy_path;
    }

    /**
     * this method get schema of a relation name
     * @param re_name the name of a relation
     * @return
     */
    public String getSchema(String re_name){
        Object schema_ = this.schema.get(re_name);
        String schema = (String) schema_;
        String res = new String();
        res = schema;
        return res;
    }



}

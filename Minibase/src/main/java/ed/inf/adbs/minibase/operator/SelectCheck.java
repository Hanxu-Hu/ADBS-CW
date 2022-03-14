package ed.inf.adbs.minibase.operator;

import ed.inf.adbs.minibase.base.*;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;



public class SelectCheck {


    public SelectCheck(){



    }
    public boolean Check(ComparisonAtom condition, RelationalAtom atom,Tuple tuple ){
        String rename = tuple.getRename();
        System.out.println(rename);

        String schema =Catalog.INSTANCE.getSchema(rename);
        System.out.println(schema);
        HashMap<String,String>  map= new HashMap<String,String> ();   // map from terms to tuple

        String sTuple = tuple.toString();
        String[] tuples =sTuple.split(", ");
        List<Term> terms = atom.getTerms();
        ComparisonOperator op = condition.getOp();
        Term term1 =condition.getTerm1();
        String s1  = term1.toString();

        Term term2 =condition.getTerm2();
        String s2 = term2.toString();

        HashMap<String,String>  schemaMap = new HashMap<String,String> ();    //map from terms to scheme
        String[] schemas = schema.split(" ");
        int i =0;
        for (Term x: terms){
            i+=1;
            String s = x.toString();
            schemaMap.put(s,schemas[i]);

        }
        i=0;
        for (Term x: terms){

            String s = x.toString();

            map.put(s,tuples[i]);
            i+=1;

        }
        String value1 = new String();
        String value2 = new String();
        String sTerm1  = new String();
        String sTerm2  = new String();
        String schema1 = new String();
        String schema2 = new String();
        if((Variable.class.isInstance(term1))&&(Variable.class.isInstance(term2))) {
            sTerm1 = term1.toString();
            sTerm2 = term2.toString();
            schema1 = schemaMap.get(sTerm1);
            schema2 = schemaMap.get(sTerm2);


            value1 = map.get(sTerm1);
            value2 = map.get(sTerm2);

            if ((schema1.equals("int")) && (schema2.equals("int"))) {
                boolean res = CompareInt(value1, value2, op);
                return res;
            }

            if ((schema1.equals("string")) && (schema2.equals("string"))) {
                boolean res = CompareString(value1, value2, op);
                return res;
            }
            return false;
        }
        if ((Variable.class.isInstance(term1)==false)&&(Variable.class.isInstance(term2))){
            value1 = term1.toString();

            schema2 = schemaMap.get(sTerm2);
            value2 = map.get(sTerm2);
            if (schema2.equals("int")) {
                boolean res = CompareInt(value1, value2, op);
                return res;

            }
            if (schema2.equals("string")){
                boolean res = CompareString(value1, value2, op);
                return res;
            }

            return false;
        }

        if ((Variable.class.isInstance(term1))&&(Variable.class.isInstance(term2)==false)){
            value2 = term2.toString();
            sTerm1 = term1.toString();
            schema1 = schemaMap.get(sTerm1);
            value1 = map.get(sTerm1);
            if (schema1.equals("int")) {
                boolean res = CompareInt(value1, value2, op);
                return res;

            }
            if (schema1.equals("string")){
                boolean res = CompareString(value1, value2, op);
                return res;
            }

            return false;
        }
        if ((Variable.class.isInstance(term1)==false)&&(Variable.class.isInstance(term2))){
            value1 = term1.toString();
            sTerm2 = term2.toString();
            schema2 = schemaMap.get(sTerm2);
            value2 = map.get(sTerm2);
            if (schema2.equals("int")) {
                boolean res = CompareInt(value1, value2, op);
                return res;

            }
            if (schema2.equals("string")){
                boolean res = CompareString(value1, value2, op);
                return res;
            }

            return false;
        }

        if ((Variable.class.isInstance(term1)==false)&&(Variable.class.isInstance(term2)==false)){
            value1= term1.toString();
            value2= term2.toString();
            if (MatchInt(value1)){
                boolean res = CompareInt(value1, value2, op);
                return res;
            }
            else{
                boolean res = CompareString(value1, value2, op);
                return res;
            }

        }
        return false;

    }






    public boolean MatchInt(String content){


            String pattern = "\\d+";

            boolean isMatch = Pattern.matches(pattern, content);
            return isMatch;
    }


    public boolean CompareString(String v1, String v2, ComparisonOperator op){
        String sOp= op.toString();
        if (sOp.equals(">")){
            if (v1.compareTo(v2)>0){
                return true;
            }
            else{
                return false;
            }

        }

        if (sOp.equals(">=")){
            if (v1.compareTo(v2)>=0){
                return true;
            }
            else{
                return false;
            }

        }
        if (sOp.equals("=")){
            if (v1.compareTo(v2)==0){
                return true;
            }
            else{
                return false;
            }

        }
        if (sOp.equals("<")){
            if (v1.compareTo(v2)<0){
                return true;
            }
            else{
                return false;
            }

        }
        if (sOp.equals("<=")){
            if (v1.compareTo(v2)<=0){
                return true;
            }
            else{
                return false;
            }

        }
        if (sOp.equals("!=")){
            if (v1.compareTo(v2)!=0){
                return true;
            }
            else{
                return false;
            }

        }



        return false;
    }
    public boolean CompareInt(String v1, String v2, ComparisonOperator op){
        String sOp= op.toString();

        if (sOp.equals("<")){
            System.out.println(v1);
            System.out.println(v1);
            int a1 = Integer.parseInt(v1);
            int a2 = Integer.parseInt(v2);

            if (a1<a2){
                return true;
            }
            else{
                return false;
            }
        }
        if (sOp.equals("<=")){

            int a1 = Integer.parseInt(v1);
            int a2 = Integer.parseInt(v2);
            if (a1<=a2){
                return true;
            }
            else{
                return false;
            }
        }
        if (sOp.equals(">")){

            int a1 = Integer.parseInt(v1);
            int a2 = Integer.parseInt(v2);
            if (a1>a2){
                return true;
            }
            else{
                return false;
            }
        }

        if (sOp.equals(">=")){

            int a1 = Integer.parseInt(v1);
            int a2 = Integer.parseInt(v2);
            if (a1>=a2){
                return true;
            }
            else{
                return false;
            }
        }
        if (sOp.equals("!=")){

            int a1 = Integer.parseInt(v1);
            int a2 = Integer.parseInt(v2);
            if (a1!=a2){
                return true;
            }
            else{
                return false;
            }
        }
        if (sOp.equals("=")){

            int a1 = Integer.parseInt(v1);
            int a2 = Integer.parseInt(v2);
            if (a1==a2){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
}

package ed.inf.adbs.minibase;

import ed.inf.adbs.minibase.base.Atom;
import ed.inf.adbs.minibase.base.Query;
import ed.inf.adbs.minibase.base.RelationalAtom;
import ed.inf.adbs.minibase.base.Term;
import ed.inf.adbs.minibase.base.Variable;
import ed.inf.adbs.minibase.parser.QueryParser;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

import java.util.regex.*;

import org.antlr.v4.runtime.misc.ObjectEqualityComparator;
/**
 *
 * Minimization of conjunctive queries
 *
 */
public class CQMinimizer {
    static RelationalAtom head;
    static List<HashMap> hist= new ArrayList<HashMap>();
    public static void main(String[] args) throws IOException {

        String inputFile = args[0];
        String outputFile = args[1];
        minimizeCQ(inputFile, outputFile);
    }


    /**
     * CQ minimization procedure
     *
     * @param inputFile string, the directory of input file
     * @param outputFile string, the directory of output file
     * @throws IOException
     *
     *
     * Assume the body of the query from inputFile has no comparison atoms
     * but could potentially have constants in its relational atoms.
     *
     * the logic of this method is using a for-loop to traverse all possibility of removing a particular atom
     * for every atom in the query, the algorithm will try to remove them using "backtrack" method, which return a boolean to decide whether this atom could be reomove or not.
     * this method will finally get the final minimized query and use a printstream class to print it and save it to a txt file.
     */
    public static void minimizeCQ(String inputFile, String outputFile) throws IOException {


        System.err.println("Usage: CQMinimizer input_file output_file");
        //parsingExample();
        Query query = QueryParser.parse(Paths.get(inputFile));
        Query query1 = QueryParser.parse(Paths.get(inputFile));
        List<Atom> body = query.getBody();
        List<Atom> body1 = query1.getBody();
        head = query.getHead();
        //System.out.println(head);
        String str = body.toString();
        HashMap<String,Object> queryMap = new HashMap<String,Object>();
        queryMap.put("homos", null);
        //System.out.println(body);
        queryMap.put("atoms", body);
        int size = body1.size();
        Atom atom3;
        //System.out.println(body);
        int i =0;

        List<Atom> body1New = new ArrayList<Atom>(body1);
        List<Atom> body2New = new ArrayList<Atom>(body1);
        for (;i<body1.size(); i+=1){
            atom3 = body1.get(i);
            body1New.remove(atom3);
            boolean flag = backtrack(queryMap, body1New);
            if (flag==true){
                body2New = new ArrayList<Atom>(body1New);
                queryMap.put("atoms", body2New);
            }
            else{
                body1New.add(atom3);
            }
            HashMap<String, String> newHomos = new HashMap<>();
            queryMap.put("homos", newHomos);
            //System.out.println(body1New);
        };
        StringBuilder result = new StringBuilder();
        Query resQuery = new Query(query.getHead(),body1New);
        //System.out.println(body1New);

        PrintStream printStream = new PrintStream(new FileOutputStream(outputFile, false));
        printStream.println(resQuery.toString());
        //System.out.println(body);
        return;
    }


    /**
     *
     * @param homos a list of Term
     * @param y a string
     * @return
     *
     * this method is to determine whether the name of a term in a list of terms is equal to a string y.
     */
    public static boolean contain(List<Term> homos, String y){
        for (Term x :homos ){
            if (x.toString().equals(y)){
                return true;
            }
        }
        return false;
    }


    /**
     *
     * @param homo a hashmap which represent the homomorphism
     * @param query a hashmap which includes a list of homomorphism and a list of atoms to represent current status
     *              the type of query's key is string, which is "homos" and "atoms"
     *              and the value of the key "homos" is a hashmap which represent existing homomorphism.
     *              the value of the key "atoms" is a list of atoms, which represent current atoms
     *              a possible example could be :
     *              "homos": [x:2, y:y] , "atoms": R(2,y), T(y,z)
     *
     * @return boolean
     * this method could judge whether a homomorphism could applied to current relations based on existing homos.
     * if the homomorphism is suitable, it could be added to the existing homos
     */
    public static boolean judge(HashMap<String,String> homo, HashMap query ){
        HashMap<String, String> homos = (HashMap<String, String>) query.get("homos");
        HashMap<String, String> homosNew = (HashMap<String, String>) homos;  //Homos_ = homosNew
        //System.out.println("homosnew");
        //System.out.println(homosNew);
        //System.out.println("h");
        //System.out.println(homo);

        for (String x :homo.keySet()){
            if ((homosNew!=null)&&(homosNew.containsKey(x))){
                String y = homosNew.get(x);
                String xNew = homo.get(x);
                if (!(xNew.equals(y))){
                    return false;
                }
            }
        }
        if (homosNew==null){
            //System.out.println("homos_");
            //System.out.println("null");
            homosNew = new HashMap<String, String>();
            for (String key: homo.keySet()){
                homosNew.put(key, homo.get(key));
            }
        }
        else{
            for (String key: homo.keySet()){
                homosNew.put(key, homo.get(key));
            }
        }
        query.put("homos", homosNew);
        return true;
    }



    /**
     *
     * @param query a HashMap, which the content and structure of the query is the same with aforementioned "query" argument in
     *              "judge" method.a possible example could be : "homos": [x:2, y:y] , "atoms": R(2,y), T(y,z)
     * @param query1 a list of Atom, which represent the target atoms of the minimization.
     * @return a boolean value, indicate whether current atoms of query could be minimized to a target query.
     *
     *
     * this method could judge whether current atoms of query could be minimized to a target query.
     *
     * the for loop in this method is to traverse every atom in the target query, to seek where the current orignial atom
     * could be mapped to. If finding the mapping successfully, then remove the current first atom in the "query1" list,
     * and use the backtrack method recursively to finding whether there is an atom in target query which the current
     * first atom could map to.
     *
     * Therefore, when the current original list of atoms is removed to empty, it can indicate the target query is reasonable.
     *
     */
    public static boolean backtrack(HashMap query, List<Atom> query1){
        for (Atom atom : query1){
            //System.out.println("current query");
            //System.out.println(query);
            //System.out.println("purpose query");
            //System.out.println(query1);
            Atom atomNew = new Atom();  //atom
            atomNew =atom; //(u,z)
            //System.out.println("atom");
            //System.out.println(atomNew);
            String s ="atoms";
            String sHomos= "homos";
            //HashMap<Term, Term> homos = (HashMap<Term,Term>) query.get(sHomos);
            Object atoms = query.get(s);

            List<Atom> atomsNew = (List<Atom>) atoms;
            if (atomsNew.size()==0){
                return true;
            }
            HashMap<String,String> homo = makeHomo(atomsNew.get(0), atomNew);
            if (homo==null){
                //System.out.println("homo is null");
                continue;
            }
            boolean flag =judge(homo, query);
            if ((homo!=null)&&(flag==false)){
                continue;
            }
            if ((homo!=null)&&(flag==true)){

                hist.add(homo);
                Atom cur = atomsNew.get(0);
                atomsNew.remove(0);
                query.put(s, atomsNew);
                //System.out.println("purpose quer1");
                //System.out.println(query1);
                boolean back =backtrack(query, query1);
                if (back){
                    return true;
                }
                else{

                    atomsNew.add(0, cur);
                    int ind = hist.size()-1;
                    HashMap<String,String> curHomo = hist.get(ind);
                    hist.remove(hist.size()-1);
                    //System.out.println("query1");
                    //System.out.println(query);

                    HashMap<String, String> homosNew = (HashMap<String,String>) query.get(sHomos);
                    if (curHomo!=null) {
                        for (String key: curHomo.keySet()){
                            homosNew.remove(key);
                        }
                    }

                    query.put(sHomos, homosNew);
                    //System.out.println("query2");
                    //System.out.println(query);
                }
            }
        }
        return false;
    }




    /**
     *
     * @param atom1 atom1
     * @param atom2 atom2
     * @return return a boolean value
     *
     * this method can determine whether two atoms is equal or not.
     */
    public static boolean isEqual(Atom atom1, Atom atom2){
        RelationalAtom atom1New = (RelationalAtom) atom1;
        RelationalAtom atom2New = (RelationalAtom) atom2;
        List<Term> terms1= atom1New.getTerms();
        List<Term> terms2= atom2New.getTerms();
        String name1 = atom1New.getName();
        String name2 = atom2New.getName();
        int i =0;
        if ((terms1.size()==terms2.size())&&(name1.equals(name2))){
            //System.out.println(terms1);
            //System.out.println(terms2);
            for (;i<terms1.size();i+=1){
                String x=terms1.get(i).toString();
                String y=terms2.get(i).toString();
                if ((x.equals(y)==false)){
                    return false;
                }
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @param atom1 atom1
     * @param atom2 atom2
     * @return a HashMap which contain the homomorphism from atom1 to atom2, if there is no homomorphism, return null.
     *
     * this method produce the possible homomorphism from atom1 to atom2, which use isEqual method to determine whether
     * these two atoms is the same or not, then use a series of "if" to determine whether these two atoms could produce
     * homomorphism or not and find the suitable homomorphisms.
     */
    public static HashMap makeHomo(Atom atom1, Atom atom2){
        RelationalAtom atom1New = (RelationalAtom) atom1;
        RelationalAtom atom2New = (RelationalAtom) atom2;
        List<Term> terms1= atom1New.getTerms();
        List<Term> terms2= atom2New.getTerms();
        List<Term> hTerms = head.getTerms();
        //System.out.println("atom1 and atom2");
        //System.out.println(atom1);
        //System.out.println(atom2);
        boolean haveVariable = false;
        //List<String> result = new ArrayList<>();
        HashMap<String, String> result = new HashMap<String, String>();
        if (isEqual(atom1, atom2)){
            for (int i=0;i<terms1.size();i+=1){
                result.put(terms1.get(i).toString(),terms2.get(i).toString());
            }
            return result;
        }
        int i=0;
        String name1 =atom1New.getName();
        String name2 =atom2New.getName();
        if ((terms1.size() == terms2.size())&&((name1.equals(name2)))){
            boolean flag =false;
            for (Term x : terms1){
                Term y = terms2.get(i);
                i+=1;
                haveVariable = Variable.class.isInstance(x);
                if ((haveVariable==true)&&(!contain(hTerms, x.toString()))){
                    result.put(x.toString(), y.toString());
                    flag=true;
                };
                if ((contain(hTerms, x.toString()))&&((x.toString().equals(y.toString()))==false)){
                    return null;
                }
                boolean b = (x.toString().equals(y.toString())) == false;
                if ((!haveVariable)&& b){
                    return null;
                }
            }
            if (flag==true){
                return result;
            };
        }
        return null;
    }
}


package ed.inf.adbs.minibase.operator;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class Tuple {
    private String content;
    private String rename;
    public Tuple(){
    }

    /**
     * this method set the tuple's name and content.
     * @param input content of the input tuple
     * @param rename name of the tuple's relation
     */
    public void setTuple(String input, String rename){
        this.content = input;
        this.rename = rename;
    }

    /**
     * this method convert a tuple to the form of string
     * @return return the tuple as a string
     */
    public String toString(){
        String content = this.content;
        //System.out.println(content);
        return content;
    }

    /**
     * this method convert a tuple to the form of array.
     * @return return the array of the tuple
     */
    public String[] getArray(){
        return this.content.split(", ");
    }

    /**
     * this tuple convert a tuple to the form of list
     * @return return tuple as the form of list
     */
    public List<String> getList() {
        String[] contentArray = this.content.split(", ");
        List<String> s = Arrays.asList(contentArray);
        //System.out.println(s);
        //s.remove(0);
        return s;
    }

    /**
     * this method get the name of relation of this tuple
     * @return return the name of relation
     */
    public String getRename(){
        return this.rename;
    }



}

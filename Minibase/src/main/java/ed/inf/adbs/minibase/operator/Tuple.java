package ed.inf.adbs.minibase.operator;

import java.util.List;

public class Tuple {
    private String content;
    private String rename;
    public Tuple(){


    }
    public void setTuple(String input, String rename){
        this.content = input;
        this.rename = rename;
    }
    public String toString(){
        String content = this.content;
        System.out.println(content);
        return content;
    }
    public String[] getArray(){
        return this.content.split(", ");
    }

    public String getRename(){
        return this.rename;
    }



}

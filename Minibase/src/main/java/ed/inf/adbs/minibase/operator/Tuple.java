package ed.inf.adbs.minibase.operator;

public class Tuple {
    private String content;
    private String rename;
    public Tuple(){


    }
    public void setTuple(String input, String rename){
        String content = new String();
        content =input;
        this.content = content;
        this.rename = rename;
    }
    public String toString(){
        String content = new String();
        content =this.content;
        System.out.println(content);
        return content;


    }
    public String getRename(){
        String rename = new String();
        rename =this.rename;
        return rename;
    }



}

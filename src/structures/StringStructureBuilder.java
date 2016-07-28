package structures;

import java.util.ArrayList;

/**
 * Created by Marius on 25/07/2016.
 */
public class StringStructureBuilder {

    private ArrayList<String> fields;
    private ArrayList<String> values;
    private int maxLen = 0;

    public StringStructureBuilder(){
        fields = new ArrayList();
        values = new ArrayList();
    }

    public void append(String field, String value){

        if(field.length()>maxLen)
            maxLen = field.length();

        fields.add(field);
        values.add(value);

    }

    public void append(String field, int value){

        if(field.length()>maxLen)
            maxLen = field.length();

        fields.add(field);
        values.add(Integer.toString(value));
    }

    public void append(String field, boolean value){

        if(field.length()>maxLen)
            maxLen = field.length();

        fields.add(field);
        values.add(Boolean.toString(value));

    }

    public void append(String field, int value, String format){

        if(field.length()>maxLen)
            maxLen = field.length();

        fields.add(field);
        values.add(String.format(format, value));
    }

    public void append(String field, float value){

        if(field.length()>maxLen)
            maxLen = field.length();

        fields.add(field);
        values.add(Float.toString(value));
    }

    public void append(String s){
        if(fields.size()==values.size()){
            if(s.length()>maxLen)
                maxLen = s.length();

            fields.add(s);
        }
        else
            values.add(s);
    }

    public void append(Object o){
        if(fields.size()!=values.size())
            throw new RuntimeException("Field value must be a string. First append the field name.");

        values.add(o.toString());
    }

    public void append(int i){
        if(fields.size()!=values.size())
            throw new RuntimeException("Field value can't be an integer. First append the field name.");

        values.add(Integer.toString(i));

    }

    public void append(int i, String format){
        if(fields.size()!=values.size())
            throw new RuntimeException("Field value can't be an integer. First append the field name.");

        values.add(String.format(format, i));

    }

    public void append(float f){
        if(fields.size()!=values.size())
            throw new RuntimeException("Field value can't be an integer. First append the field name.");

        values.add(Float.toString(f));
    }

    @Override
    public String toString(){
        if(fields.size()!=values.size())
            throw new RuntimeException(("Missing value for field: '"+fields.get(fields.size()-1)+"'."));
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<fields.size(); i++){
            sb.append(padRight(fields.get(i)+":", maxLen+1));
            sb.append(values.get(i));
            sb.append("\n");
        }

        return sb.toString();

    }

    private String padRight(String s, int len){
        return String.format("%1$-" + len + "s", s);
    }


}

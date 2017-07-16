package Handler.ToDo;

import Handler.IHandler;
import Handler.Website;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Sufian Vaio on 15.05.2017.
 */
public class ToDoAdd implements IHandler {
    private final ArrayList<String> toDoList;
    private String[] arraySplited;
    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n\n";


    public ToDoAdd(String[] arraySpited, ArrayList<String> list) {
        this.toDoList = list;
        this.arraySplited = arraySpited;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
       switch(arraySplited[0]){
           case "POST":
               String data = arraySplited[arraySplited.length-1].substring(arraySplited[arraySplited.length-1].indexOf("data=")+5).replace("+"," ");
               toDoList.add(data);
               out.write(HEADER_HTTP.getBytes());
               out.write("Saved".getBytes());
               break;
           case "GET":
               new Website(arraySplited).handle(out);
               break;

        }
    }
}

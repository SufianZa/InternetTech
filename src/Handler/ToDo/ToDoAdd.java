package Handler.ToDo;

import Handler.IHandler;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Sufian Vaio on 15.05.2017.
 */
public class ToDoAdd implements IHandler {
    private final ArrayList<String> toDoList;
    private String[] link;
    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n\n";


    public ToDoAdd(String[] link, ArrayList<String> list) {
        this.toDoList = list;
        this.link = link;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
       switch(link[0]){
           case "POST":
               String data = link[link.length-1].substring(link[link.length-1].indexOf("data=")+5).replace("+"," ");
               toDoList.add(data);
               out.write(HEADER_HTTP.getBytes());
               out.write("Saved".getBytes());
               break;

        }
    }
}

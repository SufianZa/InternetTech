package Handler.ToDo;

import Handler.IHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Sufian Vaio on 19.05.2017.
 */
public class ToDoGet implements IHandler {
    private final ArrayList<String> toDoList;
    private final String[] array;
    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n\n";

    public ToDoGet(String[] array, ArrayList<String> toDoList) {
        this.toDoList = toDoList;
        this.array = array;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        switch (array[0]) {
            case "GET":
                out.write(HEADER_HTTP.getBytes());
                out.write("<body>\n".getBytes());
                out.write(("<h2>Your ToDo List</h2>" + "\n").getBytes());
                out.write("<ul>\n".getBytes());
                for (String string : toDoList) {
                    out.write(("<li>" + string + "</li>" + "\n").getBytes());
                }
                out.write("</ul>\n".getBytes());
                out.write("</body>\n".getBytes());

                break;
        }
    }
}

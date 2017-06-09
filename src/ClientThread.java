import Handler.BadRequestHandler;
import Handler.IHandler;
import Handler.Website;
import Handler.ToDo.ToDoAdd;
import Handler.ToDo.ToDoGet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Sufian Vaio on 28.04.2017.
 */
public class ClientThread extends Thread {
    private final Socket client;
    private static ArrayList<String> toDoList = new ArrayList<>(); //static is important

    public ClientThread(Socket client) {
        this.client = client;
    }

    public void run() {
        try (OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {

            getHandler(in).handle(out);

            in.close();
            out.close();
            client.close();

            System.out.println("Connection timed out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public IHandler getHandler(InputStream in) throws IOException {
        byte[] b = new byte[1024];
        in.read(b);
        String s = new String(b, 0, b.length);
        String[] array = s.split(" ");
        switch (array[1]) {
            case "/public/todoadd.html":
                return new ToDoAdd(array,toDoList);
            case "/public/todoget.html":
                return new ToDoGet(array,toDoList);
            case "/public/canvas.html":
            case "/public/index.html":
            case "/public/text.txt":
            case "/public/start.html":
                return new Website(array);
            default:
                return new BadRequestHandler();
        }
    }
}

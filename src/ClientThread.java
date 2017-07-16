import Handler.*;
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
            try {
            getHandler(in).handle(out);
            }catch (ArrayIndexOutOfBoundsException e){
                System.err.println("Empty request from Browser");
            }
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
        String[] arraySplited = s.split(" ");
               if(arraySplited[1].contains("getcanvas?id=")){
            return new GetSavedCanvas(arraySplited);
        }
            switch (arraySplited[1]) {
                case "/public/todoadd.html":
                    return new ToDoAdd(arraySplited, toDoList);
                case "/public/todoget.html":
                    return new ToDoGet(arraySplited, toDoList);
                case "/public/login":
                    return new Login(arraySplited);
                case "/public/canvaslist":
                    return new CanvasList(arraySplited);
                case "/public/canvas_save":
                    return new CanvasSave(arraySplited);
                case "/public/getcanvas":
                    return new GetAllCanvas(arraySplited);
                case "/public/canvas.html":
                case "/public/index.html":
                case "/public/text.txt":
                case "/public/start.html":
                    return new Website(arraySplited);
                default:
                    return new BadRequestHandler();
            }
    }
}

import Handler.BadRequestHandler;
import Handler.GETHandler;
import Handler.IHandler;
import Handler.POSTHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Sufian Vaio on 28.04.2017.
 */
public class ClientThread extends Thread {
    private final Socket client;

    public ClientThread(Socket client) {
        this.client = client;

    }

    public void run() {
        try (OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {

            getHandler(in).handle(out);

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
        String s = new String(b, 0, 1024);
        String[] array = s.split(" ");

        switch (array[0]) {
            case "GET":
                return new GETHandler(array[1]);
            case "POST":
                return new POSTHandler(array[1]);
            default:
                return new BadRequestHandler();
        }
    }
}

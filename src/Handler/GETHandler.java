package Handler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sufian Vaio on 15.05.2017.
 */
public class GETHandler implements IHandler {
    private String link;
    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n\n";

    public GETHandler(String link) {
        this.link = link;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            new FileInputStream("src" + link).read(buffer);
            out.write((HEADER_HTTP).getBytes());
            out.write(buffer);
        }catch (FileNotFoundException e){
            out.write(("HTTP/1.1 400 Bad Request\n\n").getBytes());

        }
    }
}

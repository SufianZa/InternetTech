package Handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sufian Vaio on 15.05.2017.
 */
public class BadRequestHandler implements IHandler {
    private final String HEADER_HTTP = "HTTP/1.1 404 OK\n";
    public BadRequestHandler() {

    }

    @Override
    public void handle(OutputStream out) throws IOException {
        FileInputStream fis = new FileInputStream("src/public/bad.html");
        int length = fis.available();
        byte[] b =new byte[length];
        fis.read(b);
        out.write(HEADER_HTTP.getBytes());
        out.write("\n\n".getBytes());
        out.write(b);
    }
}

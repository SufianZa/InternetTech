package Handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sufian Vaio on 19.05.2017.
 */
public class Website implements IHandler {
    private  String[] array;
    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n\n";


    public Website(String[] array) {
        this.array = array;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        switch (array[0]){
            case "GET":
                byte[] b = new byte[1024];
                new FileInputStream("src"+array[1]).read(b);
                out.write(HEADER_HTTP.getBytes());
                out.write(b);
        }
    }
}

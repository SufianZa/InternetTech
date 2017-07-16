package Handler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sufian Vaio on 16.07.2017.
 */
public class CanvasList implements IHandler {
    String[] arraySplited;
    String html = "";

    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n";


    public CanvasList(String[] arraySplited) {
        this.arraySplited = arraySplited;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        switch (arraySplited[0]) {
            case "GET":
                out.write(HEADER_HTTP.getBytes());
                out.write("\n\n".getBytes());
                out.write(("<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<body>").getBytes());
                for(int i = 0 ; i < GenerateID.idList.size();i++) {
                    out.write(("<p><a href=\"http://localhost:8080/public/getcanvas?id="+GenerateID.idList.get(i)+"\">Canvas id:"+GenerateID.idList.get(i)+"</a></p>").getBytes());
                }
                out.write(("</body>\n" +
                        "</html>").getBytes());
        }
    }
}

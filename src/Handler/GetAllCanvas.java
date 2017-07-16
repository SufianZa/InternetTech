package Handler;

import Handler.JSONParser.JSONSerialize;

import java.io.*;

/**
 * Created by Sufian Vaio on 15.07.2017.
 */
public class GetAllCanvas implements IHandler {
    String[] arraySplited;
    String html ="";
    private final String HEADER_HTTP = "HTTP/1.1 200 OK\n";
    private final String CONTENT_LENGTH = "Content-Length: ";
    private final String CONTENT_TYPE = "Content-Type: text/html\n";
    private final String CONNECTION = "Connection: close\n";
    public GetAllCanvas(String[] arraySplited) {
        this.arraySplited = arraySplited;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        switch (arraySplited[0]) {
            case "GET":
                for (JSONSerialize obj : CanvasSave.coordinates) {
                    html +=
                                    "  context.beginPath();\n" +
                                    "        context.moveTo(" + obj.getKey("startx") + ", " + obj.getKey("starty") + ");\n" +
                                    "        context.lineTo(" + obj.getKey("lastx") + ", " + obj.getKey("lasty") + ");\n" +
                                    "        context.stroke();\n" +
                                    "        context.closePath();\n";
                }

                FileInputStream fis = new FileInputStream("src/public/can.txt");
                FileInputStream fis2 = new FileInputStream("src/public/vas.txt");
                byte[] b = new byte[fis.available()];
                byte[] b2 = new byte[fis2.available()];
                fis.read(b);
                fis2.read(b2);
                String can = new String(b);
                String vas = new String(b2);
                File canvas = new File("src/public/canvas2.html");
                String all = can + html + vas;
                FileOutputStream fos = new FileOutputStream(canvas);
                fos.write(all.getBytes());

                out.write(HEADER_HTTP.getBytes());
                out.write((CONTENT_TYPE).getBytes());
                out.write((CONTENT_LENGTH + all.getBytes().length + "\n").getBytes());
                out.write((CONNECTION).getBytes());
                out.write("\n\n".getBytes());
                out.write(all.getBytes());

        }

        }
}

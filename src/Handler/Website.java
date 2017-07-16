package Handler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sufian Vaio on 19.05.2017.
 */
public class Website implements IHandler {
    private  String[] arraySplited;
    private final String HEADER_HTTP = "HTTP/1.1 200 OK";
    private final String CONTENT_LENGTH = "Content-Length: ";
    private final String CONTENT_TYPE = "Content-Type: text/html";
    private final String CONNECTION = "Connection: close";
    GenerateID id = new GenerateID();

    public Website(String[] array) {
        this.arraySplited = array;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        switch (arraySplited[0]){
            case "GET":
                if(arraySplited[1].contains("canvas.html")){
                    FileInputStream fis = new FileInputStream("src"+ arraySplited[1]);
                    int length = fis.available();
                    byte[] b =new byte[length];
                    fis.read(b);
                    String str = new String(b);
                    String str2 = str.substring(str.indexOf("<script>")+8);
                    String all = str.substring(0,str.indexOf("<script>")+8)+"var id = "+id.generate() +";\n"+str2;
                    out.write(HEADER_HTTP.getBytes());
                    out.write((CONTENT_TYPE).getBytes());
                    out.write((CONTENT_LENGTH+all.length()).getBytes());
                    out.write((CONNECTION).getBytes());
                    out.write("\n\n".getBytes());
                    out.write(all.getBytes());
                    return;
                }
                FileInputStream fis = new FileInputStream("src"+ arraySplited[1]);
                int length = fis.available();
                byte[] b =new byte[length];
                fis.read(b);
                out.write(HEADER_HTTP.getBytes());
                out.write((CONTENT_TYPE).getBytes());
                out.write((CONTENT_LENGTH+length).getBytes());
                out.write((CONNECTION).getBytes());
                out.write("\n\n".getBytes());
                out.write(b);

        }
    }
}

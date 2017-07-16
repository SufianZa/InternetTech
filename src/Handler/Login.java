package Handler;

import Handler.JSONParser.JSONSerialize;

import java.io.IOException;
import java.io.OutputStream;
/**
 * Created by Sufian Vaio on 23.06.2017.
 */
public class Login implements IHandler {
    String [] arraySplited;
    JSONSerialize jsoNserialize = new JSONSerialize();

    public Login(String[] arraySplited) {
        this.arraySplited = arraySplited;
    }

    @Override
    public void handle(OutputStream out) throws IOException {
        switch (arraySplited[0]){
            case "POST":
                String loginData = arraySplited[arraySplited.length-1].substring(arraySplited[arraySplited.length-1].indexOf("{"),arraySplited[arraySplited.length-1].indexOf("}")+1);
                jsoNserialize.parseString(loginData);
                Authenticate verify = new Authenticate();
                out.write(verify.validate((String)jsoNserialize.getKey("user"),(String)jsoNserialize.getKey("pass")).getBytes());
        }
    }
}

package Handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sufian Vaio on 01.07.2017.
 */
public class Authenticate {
    File f = new File("src/public/loginData.txt");
    FileReader in = new FileReader(f);
    BufferedReader br = new BufferedReader(in);
    static Map<String, Object> allLoginData = new HashMap<>();
    Authenticate() throws IOException {
        String [] all ;
        String s="";
        while ((s = br.readLine())!= null){
            all = s.split("-");
            allLoginData.put(all[0],all[1]);
        }
    }


    public static String validate(String username, String password) {
        for (Map.Entry<String, Object> entry : allLoginData.entrySet()) {
            if(username.equals(entry.getKey()) && password.equals(entry.getValue())){
                return "{\"success\":true}";
            }
        }
    return "{\"success\":false}";
    }
}

package Handler;

import Handler.JSONParser.JSONSerialize;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Sufian Vaio on 15.07.2017.
 */
public class CanvasSave implements IHandler {

    String[] arraySplited;

   static ArrayList<JSONSerialize> coordinates = new ArrayList<>();

     int id ;
    public CanvasSave(String[] arraySplited) {
        this.arraySplited = arraySplited;

    }


    @Override
    public void handle(OutputStream out) throws IOException {
        switch (arraySplited[0]) {
            case "POST":
                String canvasData = arraySplited[arraySplited.length-1].substring(arraySplited[arraySplited.length-1].indexOf("{"),arraySplited[arraySplited.length-1].indexOf("}")+1);
                JSONSerialize jsoNserialize = new JSONSerialize();
                jsoNserialize.parseString(canvasData);
                coordinates.add(jsoNserialize);
                System.out.println(jsoNserialize.getString());
        }
    }
}

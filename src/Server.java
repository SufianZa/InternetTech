import Handler.JSONParser.JSONSerialize;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sufian Vaio on 28.04.2017.
 */
public class Server {
    public static void main(String[] args) throws IOException {

        JSONSerialize jsoNserialize = new JSONSerialize();
        jsoNserialize.customAddArray("helloArray", new Object[]{2,"s",45, true, 2.5});
        String x = "{\n" +
                "  \"bookoftheyear\": \"sufian\",  " +
                "\"author\": \"T.Knowsitall\",\n" +
                "  \"price\": 0.50,\n" +
                "  \"availability\": 177,\n" +
                "  \"thing\": [4,\"2\", true, null, 5.2, 42],\n" +
                "  \"newThing\": [42],\n" +
                "  \"123123123\": \"123123\",\n"+
                "  \"1231232123\": 123123,\n"+
                "  \"bookoftheyear\": false," +
                "\"days\" : [ \"Monday\", \"Wednesday\", \"Thursday\"],"+
                "\"author\": \"T.Knowsitall\",\n"+"}";
        String x2 = "{\n" +
                "  \"bookoftheyear\": \"sufian\"," +
               "}";
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Waiting...");
            while (true) {
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                    System.out.println("Client Connected");
                    new ClientThread(clientSocket).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
         e.printStackTrace();
        }
    }
}

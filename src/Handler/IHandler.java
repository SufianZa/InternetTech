package Handler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Sufian Vaio on 15.05.2017.
 */
public interface IHandler {
    public void handle(OutputStream out) throws IOException;
}

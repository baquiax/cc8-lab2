import java.net.ServerSocket;
import edu.galileo.baquiax.http.HttpRequest;

public final class ChamanServer {
    private static final int PORT = 8080;
    public static void main(String args[]) {        
        try {
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {
                try {
                    Thread t = new Thread (new HttpRequest(ss.accept()));
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }        
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
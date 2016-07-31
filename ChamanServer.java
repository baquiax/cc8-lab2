import java.net.ServerSocket;
import edu.galileo.baquiax.http.HttpRequest;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.InputStream;
import java.io.FileInputStream;

public final class ChamanServer {
    private static final int PORT = 8080;
    public static void main(String args[]) {        
        try {
            Properties prop = new Properties();
            InputStream is = new FileInputStream("config.properties");
            prop.load(is);
            String maxThreadsString = prop.getProperty("MaxThreads");
            int maxThreads = 1;
            if (maxThreadsString != null) {
                maxThreads = Math.max(1,Integer.parseInt(maxThreadsString));    
            }
            ExecutorService threadPoolExecutor = new ThreadPoolExecutor (
                5,
                maxThreads,
                5000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
            );
            
            ServerSocket ss = new ServerSocket(PORT);
            while (true) {
                try {
                    //Thread t = new Thread (new HttpRequest(ss.accept()));
                    //t.start();
                    threadPoolExecutor.execute(new HttpRequest(ss.accept()));
                } catch (Exception e) {
                    e.printStackTrace();
                }        
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
}
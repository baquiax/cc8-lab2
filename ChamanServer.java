import java.net.ServerSocket;
import edu.galileo.baquiax.http.HttpRequest;
import edu.galileo.baquiax.utils.Utils;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.io.InputStream;
import java.io.FileInputStream;

public final class ChamanServer {
    public static final int PORT = 8080;    

    public static void main(String args[]) {        
        try {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {                
                        Utils.writeReport();
                        System.out.println("Shouting down ...");        
                    } catch (Exception ex) {
                        // TODO Auto-generated catch block
                        ex.printStackTrace();
                    }
                }
            });

            Properties prop = new Properties();
            InputStream is = new FileInputStream("config.properties");
            prop.load(is);
            String maxThreadsString = prop.getProperty("MaxThreads");
            int maxThreads = 1;
            if (maxThreadsString != null) {
                maxThreads = Math.max(1,Integer.parseInt(maxThreadsString));    
            }
            System.out.println("MaxThreads: " + maxThreads);
            Utils.MAX_THREADS = maxThreads;
            ExecutorService threadPoolExecutor = new ThreadPoolExecutor (
                maxThreads,
                maxThreads,
                15000,
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main {
    volatile static ArrayList<VisionTarget> targets = new ArrayList<>();
    public static void main(String[] args) {
        String host = "0.0.0.0";
        int port = 8080;
        try {
            Thread server = new Thread(new UDPServer(host, port));
            server.start();

            System.out.format("Running on %s:%s...\n", host, port);

            while (true) {
                if (targets.size() > 0) {
                    for (VisionTarget target: Main.targets) {
                        System.out.printf("%s, ", target);
                    }
                    System.out.println();
                } else {
                    System.out.println("No targets found");
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}

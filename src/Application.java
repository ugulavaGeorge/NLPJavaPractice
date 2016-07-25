import dataManager.DataManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by George on 06.07.2016.
 */
public class Application {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Future<String> preprocessedText = executor.submit(new DataManager());
        executor.shutdown();
    }
}

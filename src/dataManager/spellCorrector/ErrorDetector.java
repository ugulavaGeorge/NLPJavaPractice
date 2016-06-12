package dataManager.spellCorrector;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by George on 12.06.2016.
 */
public class ErrorDetector implements Callable<ArrayList<String>> {

    private String sourceString;

    public ErrorDetector(String sourceString) {
        this.sourceString = sourceString;
    }

    @Override
    public ArrayList<String> call() throws Exception {
        //TODO implement search for mistakes
        return null;
    }
}

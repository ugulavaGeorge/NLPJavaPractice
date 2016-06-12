package dataManager.spellCorrector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by George on 12.06.2016.
 */
public class SuggestionsGenerator implements Callable<HashMap<String, ArrayList<String>>> {

    private ArrayList<String> mistakes;
    private HashMap<String, ArrayList<String>> suggestionsForCorrection;

    public SuggestionsGenerator(ArrayList<String> mistakes) {
        this.mistakes = mistakes;
        suggestionsForCorrection = new HashMap<>();
    }

    @Override
    public HashMap<String, ArrayList<String>> call() throws Exception {
        //TODO implement suggestions search
        return null;
    }
}

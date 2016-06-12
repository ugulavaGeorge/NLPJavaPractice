package dataManager.spellCorrector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by George on 12.06.2016.
 */
public class ErrorsCorrector implements Callable<ArrayList<String>> {

    private HashMap<String, ArrayList<String>> suggestionsForCorrection;
    private ArrayList<String> corrections;

    public ErrorsCorrector(HashMap<String, ArrayList<String>> suggestionsForCorrection) {
        this.suggestionsForCorrection = suggestionsForCorrection;
        this.corrections = new ArrayList<>();
    }

    @Override
    public ArrayList<String> call() throws Exception {
        //TODO implement corrections search
        return null;
    }

}

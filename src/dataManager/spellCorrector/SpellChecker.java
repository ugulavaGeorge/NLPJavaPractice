package dataManager.spellCorrector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * Created by George on 12.06.2016.
 */
public class SpellChecker {

    private ErrorDetector errorDetector;
    private SuggestionsGenerator suggestionsGenerator;
    private ErrorsCorrector errorsCorrector;

    private Future<ArrayList<String>> mistakes;
    private Future<HashMap<String, ArrayList<String>>> suggestionsForCorrection;
    private Future<ArrayList<String>> corrections;

    private String sourceString;

    public SpellChecker(String sourceText) {
        this.sourceString = sourceText;
    }

    //TODO no suggestions Handler

    public String checkAndCorrectSpelling(){
        //TODO implement
        return null;
    }

}

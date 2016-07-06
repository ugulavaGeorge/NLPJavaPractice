package dataManager;

import dataManager.spellCorrector.SpellChecker;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by George on 14.06.2016.
 */
public class DataManager implements Callable<String>{


    @Override
    public String call() throws Exception {
        String result = null;
        File file = new File("Text.txt");
        Tokenizer tokenizer = new Tokenizer(file);
        ArrayList<String> tokens = tokenizer.tokenize();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        Future<String> correctedText = null;
        Future<ArrayList<String>> textWithoutAcronyms = executor.submit(new AcronymsRemover(tokens));
        while (!textWithoutAcronyms.isDone())
             correctedText = executor.submit(new SpellChecker(tokens));
        executor.shutdown();
        result = correctedText.get();
        return result;
    }

}

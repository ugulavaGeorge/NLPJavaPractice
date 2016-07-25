package dataManager.spellCorrector;

import dataManager.support.Pair;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by George on 12.06.2016.
 * Error detector search in parallel fashion all word from source message to see:
 * if word is not in dictionary that it should be corrected.
 * Method call returns indexes of words which probably misspelled.
 */
public class ErrorDetector implements Callable<ArrayList<Integer>> {

    private ArrayList<String> sourceText;
    private TreeSet<String> dictionary;
    private int namberOfCores;

    public ErrorDetector(ArrayList<String> sourceText, TreeSet<String> dictionary, int coreNumber) {
        this.sourceText = sourceText;
        this.dictionary = dictionary;
        this.namberOfCores = coreNumber;
    }

    @Override
    public ArrayList<Integer> call() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(namberOfCores);
        ArrayList<Future<Pair<String, Integer>>> futures = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < sourceText.size(); i++) {
            Pair<String, Integer> pair = new Pair<>(sourceText.get(i), Integer.valueOf(i));
            Future<Pair<String, Integer>> future = executor.submit(new Task(pair));
            futures.add(future);
        }
        executor.shutdown();
        for (Future<Pair<String, Integer>> future : futures) {
            if (future.get().isMarked())
                result.add(future.get().getCollateralValue());
        }
        return result;
    }

    class Task implements Callable<Pair<String, Integer>> {
        private Pair<String, Integer> searchWord;

        public Task(Pair<String, Integer> searchWord) {
            this.searchWord = searchWord;
        }

        @Override
        public Pair<String, Integer> call() throws Exception {
            if (!dictionary.contains(searchWord.getMainValue().toLowerCase())
                    && !(searchWord.getMainValue().length() == 1 &&
                    !Character.isAlphabetic(searchWord.getMainValue().codePointAt(0))
                    && !Character.isDigit(searchWord.getMainValue().codePointAt(0))))
                searchWord.setMarked();
            return searchWord;
        }
    }
}

package dataManager.spellCorrector;

import dataManager.support.DamerauLevenshteinAlgorithm;
import dataManager.support.Pair;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by George on 12.06.2016.
 */

public class SuggestionsGenerator implements Callable<HashMap<String, ArrayList<String>>> {

    private ArrayList<String> mistakes;
    private HashMap<String, ArrayList<String>> suggestionsForCorrection;
    private TreeSet<String> dictionary;
    private int numberOfCores;

    public SuggestionsGenerator(TreeSet<String> dictionary, ArrayList<String> mistakes, int numberOfCores) {
        this.dictionary = dictionary;
        this.mistakes = mistakes;
        this.numberOfCores = numberOfCores;
        suggestionsForCorrection = new HashMap<>();
    }

    @Override
    public HashMap<String, ArrayList<String>> call() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(numberOfCores);
        ArrayList<Future<Pair<String, ArrayList<String>>>> suggestions = new ArrayList<>();
        for (int i = 0; i < mistakes.size(); i++) {
            Future<Pair<String, ArrayList<String>>> future =
                    executor.submit(new Task(new Pair<String, ArrayList<String>>(mistakes.get(i), new ArrayList<String>())));
            suggestions.add(future);
        }
        executor.shutdown();
        //TODO debug output : remove later.
        for (Future<Pair<String, ArrayList<String>>> suggestion : suggestions) {
            System.out.println(suggestion.get().getMainValue() + ' ' + suggestion.get().getCollateralValue().size());
        }

        for (Future<Pair<String, ArrayList<String>>> suggestion : suggestions) {
            suggestionsForCorrection.put(suggestion.get().getMainValue(), suggestion.get().getCollateralValue());
        }

        return suggestionsForCorrection;
    }

    private ArrayList<String> getTwoGramsInString(String str) {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length() - 1; i++) {
            stringBuilder.append(str.substring(i, i + 2));
            result.add(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        return result;
    }

    //TODO there is a possibility to increase concurrency e.g determine edit Distance in different threads.
    class Task implements Callable<Pair<String, ArrayList<String>>> {

        private Pair<String, ArrayList<String>> mistake;

        public Task(Pair<String, ArrayList<String>> mistake) {
            this.mistake = mistake;
        }

        @Override
        public Pair<String, ArrayList<String>> call() throws Exception {
            ArrayList<String> twoGrams = getTwoGramsInString(mistake.getMainValue());
            HashMap<String, Integer> correctionsDraft = new HashMap<>();
            DamerauLevenshteinAlgorithm distanceEstimator = new DamerauLevenshteinAlgorithm(1,1,1,1);
            for (String word : dictionary){
                int editDistance = distanceEstimator.execute(mistake.getMainValue(),word);
                correctionsDraft.put(word,editDistance);
            }
            Set<Entry<String, Integer>> entries = correctionsDraft.entrySet();
            int minDistance = 100000;
            for (Entry<String, Integer> entry : entries){
                if (entry.getValue() < minDistance)
                    minDistance = entry.getValue();
            }
            int minimumDistance = minDistance;
            entries.removeIf(e -> e.getValue()>minimumDistance);
            for (Entry<String, Integer> entry : entries){
                mistake.getCollateralValue().add(entry.getKey());
            }
            return mistake;
        }
    }
}



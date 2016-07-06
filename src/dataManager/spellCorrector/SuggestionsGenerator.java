package dataManager.spellCorrector;

import dataManager.support.Pair;

import java.time.Duration;
import java.time.Instant;
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
        //TODO bullshit detected
        //for (Future<Pair<String, ArrayList<String>>> suggestion : suggestions) {
        //    System.out.println(suggestion.get().getCollateralValue().size());
        //}

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

    class Task implements Callable<Pair<String, ArrayList<String>>> {

        private Pair<String, ArrayList<String>> mistake;

        public Task(Pair<String, ArrayList<String>> mistake) {
            this.mistake = mistake;
        }

        @Override
        public Pair<String, ArrayList<String>> call() throws Exception {
            ArrayList<String> twoGrams = getTwoGramsInString(mistake.getMainValue());
            HashMap<String, Integer> correctionsDraft = new HashMap<>();
            for (String gram : twoGrams) {
                for (String word : dictionary) {
                    if (word.contains(gram)) {
                        if (correctionsDraft.containsKey(word)) {
                            Integer value = correctionsDraft.get(word);
                            correctionsDraft.replace(word, ++value);
                        } else
                            correctionsDraft.put(word, 1);
                    }
                }
            }

            Set<Entry<String, Integer>> entries = correctionsDraft.entrySet();
            //TODO Think how to improve that.
            //ArrayList<Pair<String, Integer>> topCandidates = new ArrayList<>();
            //entries.forEach(e -> {
            //    int maxValueInList = -1;
            //    if (e.getValue() >= maxValueInList) {
            //        maxValueInList = e.getValue();
            //        topCandidates.add(new Pair<>(e.getKey(), e.getValue()));
            //        if (topCandidates.size() > 300) {
            //            for (Pair<String, Integer> candidate : topCandidates) {
            //                if (candidate.getCollateralValue() < maxValueInList)
            //                    topCandidates.remove(candidate);
            //            }
            //        }
            //    }
            //});
            //for (Pair<String, Integer> candidate : topCandidates) {
            //    mistake.getCollateralValue().add(candidate.getMainValue());
            //}
            int maxCount = 0;
            for (Entry<String, Integer> entry : entries){
                if(entry.getValue() > maxCount)
                    maxCount = entry.getValue();
            }
            int maxCount2 = maxCount;
            entries.removeIf(e -> e.getValue() < maxCount2);
            for (Entry<String, Integer> entry: entries){
                mistake.getCollateralValue().add(entry.getKey());
            }
            return mistake;
        }
    }
}



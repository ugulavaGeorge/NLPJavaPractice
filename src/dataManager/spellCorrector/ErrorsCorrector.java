package dataManager.spellCorrector;

import dataManager.support.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;

/**
 * Created by George on 12.06.2016.
 */
public class ErrorsCorrector implements Callable<ArrayList<String>> {

    private HashMap<String, ArrayList<String>> suggestionsForCorrection;
    private ArrayList<String> correctedText;
    private ArrayList<Integer> mistakes;
    private ArrayList<String> sourceText;
    private final int N_GRAM_VALUE = 4;//actually this is n-gram-1 value:)
    private int numberOfCores;



    public ErrorsCorrector(HashMap<String, ArrayList<String>> suggestionsForCorrection,
                           ArrayList<String> sourceText, ArrayList<Integer> mistakes,
                           int numberOfCores) {
        this.suggestionsForCorrection = suggestionsForCorrection;
        this.sourceText = sourceText;
        this.mistakes = mistakes;
        this.numberOfCores = numberOfCores;
    }

    @Override
    public ArrayList<String> call() throws Exception {
        correctedText = new ArrayList<>();
        for (String word : sourceText){
            correctedText.add(word);
        }
        ArrayList<Pair<String, String>> queriesMeta;
        for (Integer mistake : mistakes) {
            ExecutorService executor = Executors.newFixedThreadPool(numberOfCores);
            queriesMeta = getArgumentsForQueryForMistake(mistake);
            ArrayList<Future<Pair<String, Integer>>> suggestionStats = new ArrayList<>();
            for (Pair<String, String> metaData : queriesMeta){
                Future <Pair<String, Integer>> future = executor.submit(new Task(metaData));
                suggestionStats.add(future);
            }
            executor.shutdown();
            int maxValue = -1;
            String mistakeReplacement = null;
            for (Future<Pair<String, Integer>> stat : suggestionStats){
                if(stat.get().getCollateralValue() > maxValue){
                    maxValue = stat.get().getCollateralValue();
                    mistakeReplacement = stat.get().getMainValue();
                }
            }
            correctedText.set(mistake,mistakeReplacement);
        }
        return correctedText;
    }

    private ArrayList<Pair<String, String>> getArgumentsForQueryForMistake(int mistakeIndex) {
        ArrayList<Pair <String, String>> queries = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        String queryWrapPart = "select noccurrences from ngrams where value = '";
        ArrayList<String> suggestions = suggestionsForCorrection.get(sourceText.get(mistakeIndex));
        if (mistakeIndex >= 5) { //highest priority word at the end
            for (int i = 0; i < mistakeIndex; i++) {
                if (mistakeIndex - i <= N_GRAM_VALUE)
                    stringBuilder.append(sourceText.get(i)+ ' ');
            }
            for (String suggestion : suggestions){
                queries.add(new Pair<>(suggestion,queryWrapPart + stringBuilder + suggestion + '\''));
            }
        } else {
            int offset = 0;
            for (int i = 0; i < N_GRAM_VALUE + 1; i++) {
                if(i == mistakeIndex)
                    offset = stringBuilder.length();
                stringBuilder.append(sourceText.get(i));
            }
            for (String suggestion : suggestions){
                StringBuilder sb = stringBuilder;
                sb.insert(offset,suggestion);
                queries.add(new Pair<>(suggestion, queryWrapPart + sb.toString() + '\''));
            }
        }
        return queries;
    }

    class Task implements Callable<Pair<String, Integer>> {

        private String query;
        private String suggestion;
        private Statement statement = null;
        private Connection connection = null;

        public Task(Pair<String, String> suggestionWithQuery){
            this.suggestion = suggestionWithQuery.getMainValue();
            this.query = suggestionWithQuery.getCollateralValue();
            try {
                connection = DriverManager.getConnection("jdbc:mysql://10.241.1.4:3306/wordnet", "george", "11235g");
                if (connection != null)
                    this.statement = connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Pair<String, Integer> call() throws Exception {
            Integer numberOfOccurrences = null;
            try {
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next())
                    numberOfOccurrences = resultSet.getInt("noccurrences");
                resultSet.close();
                statement.close();
                connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
            //  System.out.println(numberOfOccurrences);
            return new Pair<String, Integer>(suggestion, numberOfOccurrences);
        }
    }

}



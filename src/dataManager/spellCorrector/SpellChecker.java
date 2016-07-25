package dataManager.spellCorrector;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.*;

/**
 * Created by George on 12.06.2016.
 */
public class SpellChecker implements Callable<ArrayList<String>>, Serializable {

    private TreeSet<String> dictionary;
    private final int numberOfCores = Runtime.getRuntime().availableProcessors();

    /**
     * ArrayList mistakes contains indexes of mistakes in source text.
     * suggestionsForCorrection contains suggestions for correction for detected mistakes.
     * corrections contains decisions made for correction found mistakes.
     */
    private Future<ArrayList<Integer>> mistakes;
    private Future<HashMap<String, ArrayList<String>>> suggestionsForCorrection;
    private Future<ArrayList<String>> correctedText;

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    private ArrayList<String> sourceText;

    private void establishConnectionToDBAndGetDictionary() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Unable to load Driver");
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://10.241.1.4:3306/wordnet", "george", "11235g");
            if (connection != null)
                statement = connection.createStatement();
            if (statement != null)
                resultSet = statement.executeQuery("SELECT value FROM word_list");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (resultSet != null) {
            try {
                while (resultSet.next())
                    dictionary.add(resultSet.getString(1));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SpellChecker(ArrayList<String> sourceText) {
        this.sourceText = sourceText;
        dictionary = new TreeSet<>();
        establishConnectionToDBAndGetDictionary();
    }

    //TODO no suggestions Handler


    private void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String wrapText(ArrayList<String> text){
        StringBuilder stringBuilder = new StringBuilder();
        for(String word : text){
            stringBuilder.append(word);
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    @Override
    public ArrayList<String> call() {
        ArrayList<String> result = new ArrayList<>();
        try {
            ExecutorService executor = Executors.newFixedThreadPool(numberOfCores);
            mistakes = executor.submit(new ErrorDetector(sourceText, dictionary, numberOfCores));
            while (!mistakes.isDone()) {
                ArrayList<String> mistakesBox = new ArrayList<>();
                for (Integer mistake : mistakes.get()) {
                    mistakesBox.add(sourceText.get(mistake));
                }
                suggestionsForCorrection = executor.submit(new SuggestionsGenerator(dictionary, mistakesBox, numberOfCores));
            }
            while (!suggestionsForCorrection.isDone()){
                correctedText = executor.submit(new ErrorsCorrector(suggestionsForCorrection.get(),
                        sourceText, mistakes.get(),numberOfCores));
            }
            executor.shutdown();
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
            closeConnection();
            //result = wrapText(correctedText.get());
            result = correctedText.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
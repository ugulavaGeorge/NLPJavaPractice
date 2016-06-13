package dataManager.spellCorrector;

import java.sql.*;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Created by George on 12.06.2016.
 */
public class ErrorDetector implements Callable<ArrayList<String>> {

    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;


    private static final File firstPart = new File("/data/yahooNgrams/dataset1/ngrams.1");

    private String sourceString;

    public ErrorDetector(String sourceString) {
        this.sourceString = sourceString;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("Unable to load Driver");
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wordnet", "root", "11235g");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Unable to connect to data base");
        }

        if (connection != null) {
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                System.out.println("Unable to create statement");
            }
        }
    }

    @Override
    public ArrayList<String> call() throws Exception {
        //TODO implement search for mistakes
        if (statement != null)
            resultSet = statement.executeQuery("SELECT * FROM words");
        //TODO implement search for mistakes
        resultSet.close();
        statement.close();
        connection.close();
        return null;
    }
}

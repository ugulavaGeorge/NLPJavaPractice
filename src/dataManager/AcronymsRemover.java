package dataManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

/**
 * Created by George on 05.07.2016.
 */
public class AcronymsRemover implements Callable<ArrayList<String>>{

    private ArrayList<String> tokenizedText;
    private HashMap<String, String> acronyms;
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public AcronymsRemover(ArrayList<String> tokenizedText) {
        this.tokenizedText = tokenizedText;
        acronyms = new HashMap<>();
    }

    private void establishConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://10.241.1.4:3306/wordnet", "george", "11235g");
            if (connection != null){
                statement = connection.createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void obtainAcronymsList(){
        try {
            if (statement != null) {
                resultSet = statement.executeQuery("Select * from acronims");
            }
            if (resultSet != null){
                while (resultSet.next()){
                    acronyms.put(resultSet.getString(1),resultSet.getString(2));
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    @Override
    public ArrayList<String> call() throws Exception {
        establishConnection();
        obtainAcronymsList();
        for (int i=0; i < tokenizedText.size(); i++){
            if(acronyms.containsKey(tokenizedText.get(i))){
                String [] split = acronyms.get(tokenizedText.get(i)).split("\\W+");
                tokenizedText.set(i, split[0]);
                for (int k = 1; k <split.length; k++){
                    tokenizedText.add(i+k,split[k]);
                }
            }
        }
        return tokenizedText;
    }
}

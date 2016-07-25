package draft;

import dataManager.support.DamerauLevenshteinAlgorithm;
import knowledgeExtractor.DependenciesExtractor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by George on 06.07.2016.
 */
public class Draft {

    private static String wrapText(ArrayList<String> text){

        StringBuilder stringBuilder = new StringBuilder();
        for(String word : text){
            stringBuilder.append(word);
            stringBuilder.append(' ');
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        //ArrayList<String> list = new ArrayList<>(Arrays.asList("Fuck", "Shit", "bullshit", "arg" , "."));
        //String all = wrapText(list);
        //System.out.println(all);
        //int editDistance = new DamerauLevenshteinAlgorithm(1,1,1,1).execute("abxc","aabcx");
        //System.out.println(editDistance);
        DependenciesExtractor extractor = new DependenciesExtractor("It is dangerous to go alone, take this blade my precious one, because i will renounce the fun.");
        extractor.call();
    }
}

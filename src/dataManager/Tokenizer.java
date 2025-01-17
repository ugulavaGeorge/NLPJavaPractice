package dataManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.CoreLabelTokenFactory;

/**
 * Created by George on 12.06.2016.
 * Text Tokenizer with Standford PTBTokenizer.
 * Accuracy of splitting the text into tokens is about 95.69%.
 */
public class Tokenizer {

    File sourceTextFile;

    /**
     * Constructor which defines file to be tokenized.
     * @param sourceTextFile file to be tokenized.
     */
    public Tokenizer(File sourceTextFile) {
        this.sourceTextFile = sourceTextFile;
    }

    /**
     * the tokenize function.
     * @returns ArrayList of tokens which produced by Standford Tokenizer.
     */
    public ArrayList<String> tokenize(){
        ArrayList<String> tokens = new ArrayList<>();
        try {
            PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<>(
                    new FileReader(sourceTextFile), new CoreLabelTokenFactory(),"");
            while (tokenizer.hasNext()){
                CoreLabel token = tokenizer.next();
                tokens.add(token.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        removeFirstClitics(tokens);
        return tokens;
    }

    //first level contraction removing
    //TODO think how to improve this piece of shit.
    private void removeFirstClitics(ArrayList<String> tokens){
        for(int i=0; i<tokens.size(); i++){
            if (tokens.get(i).equals("'ll")){
                tokens.remove(i);
                tokens.add(i,"will");
            }
            else if(tokens.get(i).equals("'m")){
                tokens.remove(i);
                tokens.add(i, "am");
            }
            else if(tokens.get(i).equals("'re")){
                tokens.remove(i);
                tokens.add(i, "are");
            }
            else if (tokens.get(i).equals("'ve")){
                tokens.remove(i);
                tokens.add(i,"have");
            }
        }
    }
}

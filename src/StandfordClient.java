/**
 * Created by George on 02.06.2016.
 */

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.process.*;

import java.io.FileReader;
import java.io.IOException;

public class StandfordClient {
    public static void main(String[] args) {
        try{
            PTBTokenizer<CoreLabel> tokenizer = new PTBTokenizer<>(
                    new FileReader("TestFile"), new CoreLabelTokenFactory(),"");
            while (tokenizer.hasNext()){
                CoreLabel label = tokenizer.next();
                System.out.println(label);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}

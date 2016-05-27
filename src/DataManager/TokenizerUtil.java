package DataManager;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Created by George on 27.05.2016.
 */
public class TokenizerUtil {
    private Tokenizer learnableTokenizer;
    private TokenizerModel tokenizerModel;

    public TokenizerUtil(String modelFile){
        initTokenizerModel(modelFile);
        learnableTokenizer = new TokenizerME(tokenizerModel);
    }

    private void initTokenizerModel(String modelFile){
        Objects.nonNull(modelFile);
        InputStream modelInputStream;

        try{
            modelInputStream = new FileInputStream(modelFile);
        }catch (IOException e){
            System.out.println(e.getMessage());
            return;
        }

        try{
            tokenizerModel = new TokenizerModel(modelInputStream);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }finally {
            try {
                modelInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Tokenizer getLearnableTokenizer() {
        return learnableTokenizer;
    }

    public Tokenizer getWhiteSpaceTokenizer() {
        return WhitespaceTokenizer.INSTANCE;
    }

    public String [] tokenizeWithWhiteSpaceTokenizer(String data){
        return getWhiteSpaceTokenizer().tokenize(data);
    }

    public String [] tokenizeWithLearnableTokenizer(String data){
        return learnableTokenizer.tokenize(data);
    }

    public String [] tokenizeFileWithWhiteSpaceTokenizer(String fileName){
        String data = FileUtil.getFileDataAsString(fileName);
        return tokenizeWithWhiteSpaceTokenizer(data);
    }

    public String [] tokenizeFileWithLearnableTokenizer(String fileName){
        String data = FileUtil.getFileDataAsString(fileName);
        return tokenizeWithLearnableTokenizer(data);
    }
}

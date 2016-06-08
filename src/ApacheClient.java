import DataManager.TokenizerUtil;

/**
 * Created by George on 27.05.2016.
 */
public class ApacheClient {
    public static void main(String[] args) {
        String modelFile = "./bin/en-token.bin";
        TokenizerUtil tokenizer = new TokenizerUtil(modelFile);
        String [] tokens = tokenizer.tokenizeFileWithLearnableTokenizer("TestFile");
        for (String token : tokens)
            System.out.println(token);
    }
}

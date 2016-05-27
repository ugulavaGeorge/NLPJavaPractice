import DataManager.TokenizerUtil;

/**
 * Created by George on 27.05.2016.
 */
public class Client {
    public static void main(String[] args) {
        String modelFile = "./bin/en-token.bin";
        String test = "We are living in an Environment, where multiple Hardware Architectures and Multiple platforms presents. So it is very difficult to write, compile and link the same Application, for each platform and each Architecture separately. The Java Programming Language solves all the above problems. The Java programming language platform provides a portable, interpreted, high-performance, simple, object-oriented programming language and supporting run-time environment. You're he's she'd";
        TokenizerUtil tokenizer = new TokenizerUtil(modelFile);
        String [] tokens = tokenizer.tokenizeWithWhiteSpaceTokenizer(test);
        for (String token : tokens)
            System.out.println(token);
    }
}

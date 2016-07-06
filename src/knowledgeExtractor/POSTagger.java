package knowledgeExtractor;

import dataManager.Tokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 04.07.2016.
 */
public class POSTagger {

    private MaxentTagger posTagger;
    private String preparedText;
    private DocumentPreprocessor preprocessor;
    private final String modelFile = "/bin/tagger/english-left3words-distsim.tagger";

    public POSTagger(String preparedText) {
        this.preparedText = preparedText;
        posTagger = new MaxentTagger(modelFile);
    }

    public void performPOSTagging() {
        TokenizerFactory<CoreLabel> ptbTokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "untokenizable=noneKeep");
        BufferedReader reader = new BufferedReader(new StringReader(preparedText));
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(System.out));
        preprocessor = new DocumentPreprocessor(reader);
        preprocessor.setTokenizerFactory(ptbTokenizerFactory);
        for (List<HasWord> sentence : preprocessor) {
            List<TaggedWord> taggedSentence = posTagger.tagSentence(sentence);
            printWriter.println(Sentence.listToString(taggedSentence, false));
        }
    }
}

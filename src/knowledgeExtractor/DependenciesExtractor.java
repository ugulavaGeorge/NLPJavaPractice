package knowledgeExtractor;

import com.chaoticity.dependensee.Main;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.logging.Redwood;

import java.io.StringReader;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by George on 05.07.2016.
 */
public class DependenciesExtractor{
    private String sourceText;
    private Redwood.RedwoodChannels dependencyLog = Redwood.channels(DependenciesExtractor.class);

    public DependenciesExtractor(String sourceText) {
        this.sourceText = sourceText;
    }

    public void call(){
        //String modelPath = DependencyParser.DEFAULT_MODEL;
        //String taggerPath = "bin/tagger/english-left3words-distsim.tagger";
        //
        //MaxentTagger posTagger = new MaxentTagger(taggerPath);
        //DependencyParser parser = DependencyParser.loadFromModelFile(modelPath);
        //DocumentPreprocessor preprocessor = new DocumentPreprocessor(new StringReader(sourceText));
        //
        //int i = 0;
        //for (List<HasWord> sentence : preprocessor){
        //    i++;
        //    List<TaggedWord> taggedWords = posTagger.tagSentence(sentence);
        //    GrammaticalStructure grammaticalStructure = parser.predict(taggedWords);
        //    dependencyLog.prettyLog("Sentence number " + i + " :",grammaticalStructure);
        //}
        //dependencyLog.info();

        TreebankLanguagePack treebankLanguagePack = new PennTreebankLanguagePack();
        GrammaticalStructureFactory grammaticalStructureFactory =
                treebankLanguagePack.grammaticalStructureFactory();
        LexicalizedParser lexicalizedParser =
                LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
        lexicalizedParser.setOptionFlags(new String[]{"-maxLength", "500", "-retainTmpSubcategories"});
        TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        List<CoreLabel> words = tokenizerFactory.getTokenizer(new StringReader(sourceText)).tokenize();
        Tree tree = lexicalizedParser.apply(words);
        GrammaticalStructure grammaticalStructure = grammaticalStructureFactory.newGrammaticalStructure(tree);
        Collection<TypedDependency> dependencies = grammaticalStructure.typedDependenciesCCprocessed(true);
        try {
            Main.writeImage(tree, dependencies, "image2.png",3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

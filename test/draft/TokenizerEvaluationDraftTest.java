package draft;

import opennlp.tools.tokenize.TokSpanEventStream;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by George on 08.06.2016.
 */
public class TokenizerEvaluationDraftTest {
    @org.junit.Test
    public void checkTokenizerAccuracy1() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == tokens.size());
    }

    @org.junit.Test
    public void checkTokenizerAccuracy2() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B", "-", "M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 13);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy3() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S_", "T", "Johnny", "Johno", "Terry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 9);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy4() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B", "-", "M", "attr", "sup", "what", "the", "fuck", "R_S_T",
                        "Johnny", "Johno", "Perry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 8);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy5() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attrsupwhatthefuck", "R_S_T", "Johnny", "JohnoPerry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R_S_T", "Johnny", "Johno", "Perry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 3);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy6() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-MPerry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B", "-", "M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 0);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy7() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("BPerry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 0);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy8() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>();
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B", "-", "M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 0);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy9() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry"));
        ArrayList<String> tokens = new ArrayList<>();
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        assertTrue(right == 0);
    }

    @org.junit.Test
    public void checkTokenizerAccuracy10() throws Exception {
        ArrayList<String> tokeCandidates = new ArrayList<>(
                Arrays.asList("B-M", "attr", "sup", "what", "the", "fuck", "?" , "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Perry", "Berry", "Terry", "Larry", "."));
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("B", "-", "M", "attr", "sup", "what", "the", "fuck", ".", "R", "_",
                        "S", "_", "T", "Johnny", "Johno", "Barry" ));
        int right = TokenizerEvaluationDraft.checkTokenizerAccuracy(tokeCandidates, tokens);
        //System.out.println(right);
        assertTrue(right == 12);
    }
}
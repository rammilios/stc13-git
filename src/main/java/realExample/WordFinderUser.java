package realExample;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class WordFinderUser {
    WordFinder wordFinder;

    public WordFinderUser(WordFinder wordFinder) {
        this.wordFinder = wordFinder;
    }

    public void doWork(String resource, String word) throws MalformedURLException{
        URL url;
        try {
            url = new URL(resource);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw e;
        }
        Set<String> sentences = wordFinder.getSentences(url);
        if (sentences != null) {
            for (String sentence : sentences) {
                if (wordFinder.checkIfWordInSentence(sentence, word)) {
                    wordFinder.writeSentenceToResult(sentence);
                }
            }
        }
    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import realExample.WordFinder;
import realExample.WordFinderUser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WordFinderUserTest {
    public static final String WORD = "";
    private WordFinderUser wordFinderUser;
    private WordFinder wordFinder = Mockito.mock(WordFinder.class);
    static final String FIRST_SENTENCE = "first sentence";
    static final String SECOND_SENTENCE = "second sentence";
    static final String FILE_URL = "file://any";

    @BeforeEach
    void before() {
        wordFinderUser = new WordFinderUser(wordFinder);
    }

    @Test
    void doWorkNullTest() {
        when(wordFinder.getSentences(any())).thenReturn(null);
        assertDoesNotThrow(() -> wordFinderUser.doWork(FILE_URL, WORD));
    }

    @Test
    void doWorkEmptySetTest() {
        when(wordFinder.getSentences(any())).thenReturn(new HashSet<>());
        try {
            wordFinderUser.doWork(FILE_URL, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder, times(0)).checkIfWordInSentence(any(), any());
        verify(wordFinder, times(0)).writeSentenceToResult(any());
    }

    @Test
    void doWorkIfTrueTest() {
        when(wordFinder.getSentences(any())).thenReturn(new HashSet<>(Arrays.asList(FIRST_SENTENCE, SECOND_SENTENCE)));
        when(wordFinder.checkIfWordInSentence(FIRST_SENTENCE, WORD)).thenReturn(true);
        when(wordFinder.checkIfWordInSentence(SECOND_SENTENCE, WORD)).thenReturn(false);
        try {
            wordFinderUser.doWork(FILE_URL, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder, times(1)).writeSentenceToResult(FIRST_SENTENCE);
        verify(wordFinder, times(0)).writeSentenceToResult(SECOND_SENTENCE);
    }

    @Test
    void doWorkWhenURLForming() {
        final ArgumentCaptor<URL> argument = ArgumentCaptor.forClass(URL.class);
        try {
            wordFinderUser.doWork(FILE_URL, WORD);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        verify(wordFinder).getSentences(argument.capture());
        assertEquals(FILE_URL, argument.getValue().toString());
    }

    @Test
    void doWorkWhenBadURL() {
        assertThrows(MalformedURLException.class, () -> wordFinderUser.doWork(" ", " "));
    }
}
import java.util.*;

/**
 * Created by curti_000 on 1/24/2017.
 */

public class HangmanManager {
    private Set<String> dictionary;
    private SortedSet<Character> userGuess;
    private String cPattern;
    private int numGuess;

    // Constructor for HangmanManager with given words, length, and max userGuess.
    // Throws IllegalArgumentException for improper specs.
    public HangmanManager(Collection<String> dictionary, int length, int max) {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        this.dictionary = new TreeSet<>();
        for (String word : dictionary) {
            if (word.length() == length) {
                this.dictionary.add(word);
            }
        }
        numGuess = max;
        userGuess = new TreeSet<>();
        cPattern = "-";
        for (int i = 1; i < length; i++) {
            cPattern += " -";
        }
    }

    // Returns words currently considered
    public Set<String> words() {
        return dictionary;
    }

    // Returns remaining number of userGuess
    public int numGuesses() {
        return numGuess;
    }

    // Returns userGuess already tried by the user
    public SortedSet<Character> guesses() {
        return new TreeSet<>(userGuess);
    }

    // Returns current pattern of letters based on userGuess already made
    // throws an IllegalStateException if manager has not more words to choose from
    public String pattern() {
        if (dictionary.isEmpty()) {
            throw new IllegalStateException();
        }
        return cPattern.trim();
    }

    // Takes in the user's current guess and determines which words should be used next
    // Returns all occurrences of the current letter in the new pattern
    public int record(char guess) {
        if (numGuess < 1 || dictionary.isEmpty()) {
            throw new IllegalStateException();
        }
        if (userGuess.contains(guess) && !dictionary.isEmpty()) {
            throw new IllegalArgumentException();
        }
        userGuess.add(guess);
        Map<String, Set<String>> patternMap = findPatterns(guess);
        setDictionary(patternMap, guess);
        int occurrences = countOccurrences(cPattern, guess);
        if (occurrences == 0) {
            numGuess--;
        }
        return occurrences;
    }

    // Returns word patterns containing the given character
    private Map<String, Set<String>> findPatterns(char guess) {
        Map<String, Set<String>> patternMap = new TreeMap<>();
        for (String word : dictionary) {
            String guessPattern = createPattern(word);
            if (!patternMap.containsKey(guessPattern)) {
                patternMap.put(guessPattern, new TreeSet<>());
            }
            patternMap.get(guessPattern).add(word);
        }
        return patternMap;
    }

    // Returns string of word pattern
    private String createPattern(String word) {
        String guessPattern = "";
        for (int i = 0; i < word.length(); i++) {
            if (userGuess.contains(word.charAt(i))) {
                guessPattern += word.charAt(i) + " ";
            } else {
                guessPattern += "- ";
            }
        }
        return guessPattern;
    }

    // Chooses the set of words to be used by the manager for next round
    private void setDictionary(Map<String, Set<String>> patternMap, char guess) {
        int wordCount = 0;
        for (String pattern : patternMap.keySet()) {
            int count = countOccurrences(pattern, guess);
            if (count < cPattern.length() && patternMap.get(pattern).size() > wordCount) {
                wordCount = patternMap.get(pattern).size();
                dictionary = patternMap.get(pattern);
                cPattern = pattern;
            }
        }
    }

    // Returns # of times the current character appears in the string
    private int countOccurrences(String word, char guess) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == guess) {
                count++;
            }
        }
        return count;
    }
}
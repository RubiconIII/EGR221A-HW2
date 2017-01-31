import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by curti_000 on 1/30/2017.
 */
public class HangmanManagerTest {

        private Set<String> txtGetter() {
            try {
                Scanner get = new Scanner(new File("dictionary2.txt"));
                Set<String> dictionary = new HashSet<>();

                while(get.hasNext())
                    dictionary.add(get.next());

                return dictionary;
            }
            catch(FileNotFoundException e) {
                Assert.fail("Could not retrieve text file");
            }
            return new HashSet<>();
        }


        private HangmanManager testManagerSetup(int ult) {
            Set<String> d = new HashSet<>();
            d.add("000000");
            return new HangmanManager(d, 6, ult);
        }

        @Test
        public void constructorTest() {
            Set<String> d = txtGetter();
            constructorHappyCase(d, 8, 3);
            constructorHappyCase(d, 6, 4000);
            constructorHappyCase(d, 12, 1200);
            constructorHappyCase(d, 1, 0);
            constructorFail(null, 10, 10);
            constructorFail(null, 10, 10);
            constructorFail(d, -10, 4);
            constructorFail(d, 3, -16);
            constructorFail(d, -1, 8);
            constructorFail(d, -8,-10);
            constructorFail(null, 0, 0);
        }


        @Test
        public void wordsTest() {
            Set<String> t = txtGetter();
            HangmanManager h = new HangmanManager(t, 3, 17);

            Assert.assertNotSame(h.words(), t);
            t.add("abc");
            Assert.assertNotSame(h.words(), t);
        }

        @Test
        public void guessesLeftTest() {
            HangmanManager h = testManagerSetup(10);
            Assert.assertEquals(h.numGuesses(), 10);
            h.record('a');
            Assert.assertEquals(h.numGuesses(), 9);
            h.record('b');
            h.record('c');
            Assert.assertEquals(h.numGuesses(), 7);
        }

        @Test
        public void guessesTest() {
            HangmanManager h = testManagerSetup(10);
            h.record('q');
            h.record('w');
            h.record('e');
            h.record('r');
            h.record('t');
            h.record('y');
            h.record('u');
            h.record('i');
            h.record('o');
            h.record('p');
            Set<Character> characterSet = h.guesses();
            Assert.assertNotSame(characterSet, h.guesses());
            if (characterSet.size() != 10 || !characterSet.contains('q') || !characterSet.contains('w')
                    || !characterSet.contains('e') || !characterSet.contains('r') ||
                    !characterSet.contains('t')|| !characterSet.contains('y') || !characterSet.contains('u')||
                    !characterSet.contains('i')|| !characterSet.contains('o')|| !characterSet.contains('p')) {
                Assert.fail();
            }
        }

        @Test
        public void recordTest() {
            HangmanManager hMan = new HangmanManager(txtGetter(), 4, 10);
            hMan.record('b');
            Set<String> hs = new HashSet<>();
            hs.add("ally");
            hs.add("cool");
            hs.add("deal");
            hs.add("else");
            hs.add("flew");
            hs.add("good");
            hs.add("hope");
            Assert.assertEquals(hMan.words(), hs);

        }
        @Test
        public void recordFailTest() {
            HangmanManager h = testManagerSetup(2);
            h.record('q');
            try {
                h.record('q');
                Assert.fail();
            } catch (IllegalArgumentException t) {}
            h.record('w');
            try {
                h.record('e');
                Assert.fail();
            } catch (IllegalStateException t) {}
        }


        @Test
        public void patternTest() {
            HangmanManager h = new HangmanManager(txtGetter(), 4, 7);
            Assert.assertEquals(h.pattern(), "- - - -");
            int a = h.record('b');
            Assert.assertEquals(h.pattern(), "- - - -");
            Assert.assertEquals(0, a);
            Assert.assertEquals("decrease by 1", 6, h.numGuesses());

            int b = h.record('o');
            Assert.assertEquals(h.pattern(), "- - - -");
            Assert.assertEquals(0, b);
            Assert.assertEquals("decrease by 1 again" , 5, h.numGuesses());

            int c = h.record('a');
            Assert.assertEquals(h.pattern(), "- - - -");
            Assert.assertEquals(0, c);
            Assert.assertEquals("decrease by 1 again" , 4, h.numGuesses());

            int d = h.record('e');
            Assert.assertEquals(h.pattern(), "- - e -");
            Assert.assertEquals(0, c);
            Assert.assertEquals("stops decreasing" , 4, h.numGuesses());
        }

        @Test
        public void patternNegativeTest() {
            HangmanManager hmm = new HangmanManager(new LinkedList<>(), 4, 10);
            try{
                hmm.pattern();
                Assert.fail("should throw based on bad pattern");
            }catch(IllegalStateException b){
            }
        }
    private void constructorFail(Set<String> d, int length, int maximum) {
        try {
            new HangmanManager(d, length, maximum);
            Assert.fail("No exception thrown");
        } catch (RuntimeException e) {}
    }

    private void constructorHappyCase(Set<String> dictionary, int length, int max) {
        try {
            new HangmanManager(dictionary, length, max);
        } catch (RuntimeException e) {
            Assert.fail(e.getMessage());
        }
    }
    }



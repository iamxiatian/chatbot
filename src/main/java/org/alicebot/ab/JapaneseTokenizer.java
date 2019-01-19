package org.alicebot.ab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
//import org.atilika.kuromoji.Token;
//import org.atilika.kuromoji.Tokenizer;

/**
 * Tokenize a Japanese language input by inserting spaces between words
 * <p>
 * see http://atilika.org/
 */
public class JapaneseTokenizer {
    private static final Logger log = LoggerFactory
            .getLogger(JapaneseTokenizer.class);
    //static final Tokenizer tokenizer = Tokenizer.builder().build();
    static final Pattern tagPattern = Pattern.compile("(<.*>.*</.*>)|(<.*/>)");
    static Set<Character.UnicodeBlock> japaneseUnicodeBlocks = new HashSet<Character.UnicodeBlock>() {{
        add(Character.UnicodeBlock.HIRAGANA);
        add(Character.UnicodeBlock.KATAKANA);
        add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
    }};


    /*public static ArrayList<String> tokenize(String sentence) {
         ArrayList<String> result = new ArrayList<String>();
         //Tokenizer tokenizer = Tokenizer.builder().build();
         for (Token token : tokenizer.tokenize(sentence)) {
             result.add(token.getSurfaceForm());
         }
         return result;
    }
    public static String buildFragment(String fragment) {
        ArrayList<String> tokens = tokenize(fragment);
        String result = "";
        for (String word : tokens) {
            result += " "+word;
        }
        return result.trim();
    }*/
  /*  public static String morphSentence (String sentence) {

        Matcher matcher = tagPattern.matcher(sentence);
        String result = "";
        while (matcher.find()) {
            int i = matcher.start();
            int j = matcher.end();

            String prefix, tag;
            if (i > 0) prefix = sentence.substring(0, i-1); else prefix = "";
            tag = sentence.substring(i, j);
            result += " "+buildFragment(prefix)+" "+tag;
            if (j < sentence.length()) sentence = sentence.substring(j, sentence.length()); else sentence = "";
            System.out.print("Start index: " + matcher.start());
            System.out.print(" End index: " + matcher.end() + " ");
            log.info(matcher.group());
        }
        result += " "+buildFragment(sentence);
        while (result.contains("$ ")) result = result.replace("$ ", "$");
        while (result.contains("  ")) result = result.replace("  "," ");
        return result.trim();
    }*/        /*
    public static String morphSentence (String sentence) {
        String result = "";
        for (char c : sentence.toCharArray()) {
            if (japaneseUnicodeBlocks.contains(Character.UnicodeBlock.of(c))) {
                //log.info(c + " is a Japanese character");
                result = result+" "+c+" ";
            } else {
                //log.info(c + " is not a Japanese character");
                result = result + c;
            }
        }
        while (result.contains("$ ")) result = result.replace("$ ", "$");
        while (result.contains("  ")) result = result.replace("  "," ");
        return result.trim();
    }
    */

    /**
     * Tokenize a fragment of the input that contains only text
     *
     * @param fragment fragment of input containing only text and no XML tags
     * @return tokenized fragment
     */
    public static String buildFragment(String fragment) {


        return fragment;
    }

    /**
     * Morphological analysis of an input sentence that contains an AIML pattern.
     *
     * @param sentence
     * @return morphed sentence with one space between words, preserving XML markup and AIML $ operation
     */
    public static String morphSentence(String sentence) {
        return sentence;
    }

}

import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

/**
 * Created by Hendrik Jöntgen on 31.05.2017.
 */
public class LanguageChecker {

    /**
     * This method counts the number of spelling errors in a text using the JLanguageTool library.
     *
     * @param sentence
     * @return The number of spelling errors in a text
     * @author: Hendrik Joentgen
     * @update: 2017-05-31
     */
    public static int getSpellingError(String sentence) throws IOException {
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
        for (Rule rule : langTool.getAllRules()) {
            if (!rule.isDictionaryBasedSpellingRule()) {
                langTool.disableRule(rule.getId());
            }
        }
        List<RuleMatch> matches = langTool.check(sentence);
        //System.out.println("Es wurden " + matches.size() + " Fehler gefunden.");
        return matches.size();
    }
}

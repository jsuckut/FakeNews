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


    public static void main(String[] args) throws IOException {

getSpellingError("tost are very tasty. i lik to eat them very musch. does this tool check for proper writing? And if i use the wronged grammars?");
    }

    public static int getSpellingError(String sentence) throws IOException {
        JLanguageTool langTool = new JLanguageTool(new AmericanEnglish());
        for (Rule rule : langTool.getAllRules()) {
            if (!rule.isDictionaryBasedSpellingRule()) {
                langTool.disableRule(rule.getId());
            }
        }
        List<RuleMatch> matches = langTool.check(sentence);
        System.out.println("Es wurden " + matches.size() + " Fehler gefunden.");
        return matches.size();
    }
}

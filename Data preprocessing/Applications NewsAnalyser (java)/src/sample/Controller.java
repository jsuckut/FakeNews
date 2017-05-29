/**
 * This is the controller class for the graphic user interface (gui).
 * Very important is that the .fxml need this controller for the gui and business logic.
 * Please remember that you have to say the .fxml with controller is
 * the right one. You can handle it with the code lines:
 * @code: fx:controller="sample.Controller"
 * personal comment: For this knowledge i need a half day to figured it out. java...
 * @author Benjamin M. Abdel-Karim
 * @since 2017-05-13
 * @version 2017-05-13
 * @currentDevelopmentTime 16 h
 */
// package routing..
package sample;

// imports
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The class constructor
public class Controller {


     // Views here are the definition for buttons, labels and text fields
    // Pleas remember this variables name must be the same from sample.fxml

    @FXML // The primary text field
    private TextField InputNews;

    @FXML // The Label for number of words
    private Label labelWortCount;

    @FXML // The Label for number of UpperCase
    private Label labelUpperCaceCount;

    @FXML // The Label for number of labelExclamationMarkCount
    private Label labelExclamationMarkCount;

    @FXML // The Label for number of labelQuestionMarkCount
    private Label labelQuestionMarkCount;

    @FXML // The Label for number of labelCitationsCount
    private Label labelCitationsCount;

    @FXML // The Label for number of firstPersonsOccurences
    private Label labelFirstPersonCount;

    @FXML // The Label for number of secondPersonsOccurences
    private Label labelSecondPersonCount;

    @FXML // The Label for number of thirdPersonsOccurences
    private Label labelThirdPersonCount;

    @FXML // The Label for number of BiGramm
    private Label labelBiGramm;

    @FXML // The Label for number of TriGramm
    private Label labelTriGramm;


    public static Pattern firstPersonPattern = Pattern.compile("((\\bi\\b)|(\\bme\\b)|(\\bmy\\b)|(\\bmine\\b)|(\\bmyself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern secondPersonPattern = Pattern.compile("((\\byou\\b)|(\\byour\\b)|(\\byour\\b)|(\\byourself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern thirdPersonPattern = Pattern.compile("((\\bhe\\b)|(\\bhim\\b)|(\\bhis\\b)|(\\bhimself\\b)|(\\bshe\\b)|(\\bher\\b)|(\\bhers\\b)|(\\bherself\\b)|(\\bit\\b)|(\\bits\\b)|(\\bhimself\\b)|(\\bitself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern firstPluralPersonPattern = Pattern.compile("((\\bwe\\b)|(\\bus\\b)|(\\bour\\b)|(\\bours\\b)|(\\bourself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern secondPluralPersonPattern = Pattern.compile("((\\byou\\b)|(\\byour\\b)|(\\byour\\b)|(\\byourselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern thirdPluralPersonPattern = Pattern.compile("((\\bthey\\b)|(\\bthem\\b)|(\\btheir\\b)|(\\bthemselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern exclusiveThirdPluralPersonPattern = Pattern.compile("(\byourselves\b)", Pattern.CASE_INSENSITIVE);

    /**
     * This central method import the News text form the user.
     * And now the business logic can use the String for operation.
     * @code   String sNewsText = InputNews.getText(); Import the News
     * The fallowing code lines call the calculations methods
     * @author Benjamin M. Abdel-Karim
     * @since 2017-05-13
     * @version 2017-05-13
     */
    @FXML
    public void InputHandle(){
        String sNewsText = InputNews.getText();


        // Methods to call and typ caste the value. In the first step the give the
        // News from input text field in the specific method to do something.
        // The The result is converted from a number to a string.
        String sCountOfWord = Integer.toString(getCountOfWords(sNewsText));
        String sCountOfUpperCase = Double.toString(getNumberOfUpperCase(sNewsText));
        String sCountOfExclamationMarkCount = Integer.toString(getNumberOfExclamationMark(sNewsText));
        String sCountOFQuestionMarkCount = Integer.toString(getNumberOfQuestionMark(sNewsText));
        String sCountOfCitations = Integer.toString(getNumberOfCitations(sNewsText));
        String sCountOfFirstPersonOccurences = Double.toString(getPersonDistribution(sNewsText,firstPersonPattern)+getPersonDistribution(sNewsText,firstPluralPersonPattern));
        String sCountOfSecondPersonOccurences = Double.toString(getPersonDistribution(sNewsText,secondPersonPattern)+getPersonDistribution(sNewsText,secondPluralPersonPattern));
        String sCountOfThirdPersonOccurences = Double.toString(getPersonDistribution(sNewsText,thirdPersonPattern)+getPersonDistribution(sNewsText,exclusiveThirdPluralPersonPattern));

       String sBiGramm = Double.toString(getNumberofNGrammes(sNewsText, 2));
       String sTriGramm = Double.toString(getNumberofNGrammes(sNewsText, 3));

        // Print all information in the GUI
        labelWortCount.setText(sCountOfWord);
        labelUpperCaceCount.setText(sCountOfUpperCase);
        labelExclamationMarkCount.setText(sCountOfExclamationMarkCount);
        labelQuestionMarkCount.setText(sCountOFQuestionMarkCount);
        labelCitationsCount.setText(sCountOfCitations);
        labelFirstPersonCount.setText(sCountOfFirstPersonOccurences);
        labelSecondPersonCount.setText(sCountOfSecondPersonOccurences);
        labelThirdPersonCount.setText(sCountOfThirdPersonOccurences);
        labelBiGramm.setText(sBiGramm);
        labelTriGramm.setText(sTriGramm);
    }


    /**
     * This method count the number of words in a given string.
     * Need the package @import java.util.StringTokenizer;
     * @param sText
     * @return The number of sStrinkTokenizer.countTokens
     * @author: Benjamin M. Abdel-Karim
     * @update: 2017-07-12
     */
    public static int getCountOfWords(String sText){
        StringTokenizer sStrinkTokenizer= new StringTokenizer(sText);
        return sStrinkTokenizer.countTokens();
    }


    /**
     * This method count the share of uppercases in a given string.
     * @param sText
     * @return The share of uppercase
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static double getNumberOfUpperCase(String sText) {
        int iUpperCase = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (Character.isUpperCase(sText.charAt(i))) {
                iUpperCase++;
            }
        }
        return (double)iUpperCase / (double)sText.length();
    }

     /**
     * This method count the Exclamation marks in a given string.
     * @param sText
     * @return The number of Exclamation marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfExclamationMark(String sText){
        int iExclamationMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '!'){
                iExclamationMark++;
            }
        }
        return iExclamationMark;
    }


    /**
     * This method count the Question marks in a given string.
     * @param sText
     * @return The number of Question marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfQuestionMark(String sText){
        int iQuestionMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '?'){
                iQuestionMark++;
            }
        }
        return iQuestionMark;
    }

    /**
     * This method count the citations of a given NewsArticle.
     * @param sText
     * @return The number of citations
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static int getNumberOfCitations(String sText){
        Pattern citations = Pattern.compile("\"[^\"]+\"");
        int splits = citations.split(sText).length;
        return splits;

    }

    /**
     * This method count the usage of a given person in a given NewsArticle.
     *
     * @return The number of occurences of a person
     * @author: Hendrik Joentgen
     * @update: 2017-05-20
     */
    public static double getPersonDistribution(String sText, Pattern person) {
        Matcher personMatcher = person.matcher(sText);
        int PersonOccurrence = 0;

        while (personMatcher.find())
            PersonOccurrence++;
        return PersonOccurrence / (double) getCountOfWords(sText);
    }


    /**
     * This method count number of N-Grammes of given Degree of - Gramms.
     * Basis function
     * @param sText
     * @param iMaxNGramSize
     * @return Number of N-Gramms
     * @soruce: The source give an idear how to implements the n-Gramms:
     * https://stackoverflow.com/questions/3656762/n-gram-generation-from-a-sentence
     * @source: For Backround Knowloage http://wiki.languagetool.org/java-spell-
     * @author: Benjamin M. Abdel-Karim
     */

    public static int getNumberofNGrammes(String sText, int iMaxNGramSize) {
        List<String> sentence = Arrays.asList(sText.split("[\\W+]"));
        List<String> AListNGramms = new ArrayList<String>();
        int ngramSize = 0;
        StringBuilder SBText = null;
        //Calculate the Gramm from Sentence
        for (ListIterator<String> it = sentence.listIterator(); it.hasNext();) { String word = (String) it.next();
            // Step One: Add the word itself
            SBText = new StringBuilder(word); AListNGramms.add(word); ngramSize=1;
            it.previous();
            // Step Tow: Insert prevs of the word and add those too
            while(it.hasPrevious() && ngramSize<iMaxNGramSize){ SBText.insert(0,' ');
                SBText.insert(0,it.previous()); AListNGramms.add(SBText.toString());
                ngramSize++;
            }
            //Last Step Go back to initial position
            while(ngramSize>0){ ngramSize--;
                it.next();
            }
        }
        return AListNGramms.size();
    }













}

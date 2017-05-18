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
import java.util.StringTokenizer;

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
        String sCountOfUpperCase = Integer.toString(getNumberOfUpperCase(sNewsText));
        String sCountOfExclamationMarkCount = Integer.toString(getNumberOfExclamationMark(sNewsText));
        String sCountOFQuestionMarkCount = Integer.toString(getNumberOfQuestionMark(sNewsText));

        // Print all information in the GUI
        labelWortCount.setText(sCountOfWord);
        labelUpperCaceCount.setText(sCountOfUpperCase);
        labelExclamationMarkCount.setText(sCountOfExclamationMarkCount);
        labelQuestionMarkCount.setText(sCountOFQuestionMarkCount);

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
     * This method count the uppercase in a given string.
     * @param sText
     * @return The number of uppercase
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfUpperCase(String sText) {
        int iUpperCase = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (Character.isUpperCase(sText.charAt(i))) {
                iUpperCase++;
            }
        }
        return iUpperCase;
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


}

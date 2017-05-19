import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.Date;

/**
 * Created by Hendrik Jöntgen on 18.05.2017.
 * In dieser Klasse wird die Datenbank für den Klassifizierer gebaut.
 * Dabei werden sämtliche NewsArticle aus der Ursprungsdatenbank geladen und unserer Parameterfunktionen auf diese ausgeführt
 * Anschließend werden die Ergebnisse unserer Parameterfunktionen in eine neue Datenbank geschrieben.
 *
 */
public class DatabaseGenerator {

    public static void main(String[] args) throws Exception {
//SQL-Connection wird hergestellt
        Connection sqlConnection = NewsArticle.getConnection();
        //Die aktuelle Resultdatenbank wird gelöscht, sodass eine neue erstellt werden kann.
        PreparedStatement DropStatement = sqlConnection.prepareStatement("DROP TABLE IF EXISTS newsResults");
        int result = DropStatement.executeUpdate();
        //Hier wird die neue Datenbank erstellt, das SQL-Statement muss dann bei neuen Sachen immer erweitert werden.
        PreparedStatement CreateStatement = sqlConnection.prepareStatement("CREATE TABLE newsResults (newsId int, isFake boolean, words int, uppercases int, questions int, exclamations int, authors int)");
        result = CreateStatement.executeUpdate();
        //Alle News-Einträge der Datenbank ausgeben lassen
        PreparedStatement getAllIdsStatement = sqlConnection.prepareStatement("SELECT * FROM newsarticles");
        ResultSet resultSet = getAllIdsStatement.executeQuery();
//In dieser Schleife werden alle NewsArticle nacheinander aufgerufen und unsere Parameter für sie berechnet.
        while(resultSet.next()){
            NewsArticle news = new NewsArticle(resultSet.getInt("newsID"));
            boolean isFake = news.isFake;
            int words = getCountOfWords(news);
            int uppercases = getNumberOfUpperCase(news);
            int questions = getNumberOfQuestionMark(news);
            int exclamations = getNumberOfExclamationMark(news);
            //TODO Ich bekomm die getNumberOfAuthors Methode nicht zu laufen. Gibt mir immer ne NullPointerException raus
            int authors = 1;
                    // getNumberOfAuthors(news);
             //Die eben berechnene Parameter werden hier in die neue Tabelle eingefügt. Muss bei weiteren Parametern entsprechend erweitert werden.
            PreparedStatement InsertStatement = sqlConnection.prepareStatement("INSERT INTO newsResults values ("+resultSet.getInt("newsID")+", "+isFake+","+words+", "+uppercases+", "+questions+", "+exclamations+", "+authors+")");
            result = InsertStatement.executeUpdate();
        }

    }




    /**
     * This method count the number of words in a given string.
     * Need the package @import java.util.StringTokenizer;
     * @param news
     * @return The number of sStrinkTokenizer.countTokens
     * @author: Benjamin M. Abdel-Karim
     * @update: 2017-07-12
     */
    public static int getCountOfWords(NewsArticle news){
        String sText = news.getContent();
        StringTokenizer sStringTokenizer= new StringTokenizer(sText);
        return sStringTokenizer.countTokens();
    }


    /**
     * This method count the uppercase in a given string.
     * @param news
     * @return The number of uppercase
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfUpperCase(NewsArticle news) {
        String sText = news.getContent();
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
     * @param news
     * @return The number of Exclamation marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfExclamationMark(NewsArticle news){
        String sText = news.getContent();
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
     * @param news
     * @return The number of Question marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfQuestionMark(NewsArticle news){
        String sText = news.getContent();
        int iQuestionMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '?'){
                iQuestionMark++;
            }
        }
        return iQuestionMark;
    }

    /**
     * This method count the Authors of a given NewsArticle.
     * @param news
     * @return The number of authors
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static int getNumberOfAuthors(NewsArticle news){
        if (news.getAuthor()==null || news.getAuthor().isEmpty()){
            System.out.println("Returned 0");
       return 0;}
        else{
            return news.getAuthor().size();
        }

    }


}

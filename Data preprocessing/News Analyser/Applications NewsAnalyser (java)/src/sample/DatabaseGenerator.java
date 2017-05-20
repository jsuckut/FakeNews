

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hendrik Jöntgen on 18.05.2017.
 * In dieser Klasse wird die Datenbank für den Klassifizierer gebaut.
 * Dabei werden sämtliche NewsArticle aus der Ursprungsdatenbank geladen und unserer Parameterfunktionen auf diese ausgeführt
 * Anschließend werden die Ergebnisse unserer Parameterfunktionen in eine neue Datenbank geschrieben.
 *
 */
public class DatabaseGenerator {

    public static Pattern firstPersonPattern = Pattern.compile("((\\bi\\b)|(\\bme\\b)|(\\bmy\\b)|(\\bmine\\b)|(\\bmyself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern secondPersonPattern = Pattern.compile("((\\byou\\b)|(\\byour\\b)|(\\byour\\b)|(\\byourself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern thirdPersonPattern = Pattern.compile("((\\bhe\\b)|(\\bhim\\b)|(\\bhis\\b)|(\\bhimself\\b)|(\\bshe\\b)|(\\bher\\b)|(\\bhers\\b)|(\\bherself\\b)|(\\bit\\b)|(\\bits\\b)|(\\bhimself\\b)|(\\bitself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern firstPluralPersonPattern = Pattern.compile("((\\bwe\\b)|(\\bus\\b)|(\\bour\\b)|(\\bours\\b)|(\\bourself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern secondPluralPersonPattern = Pattern.compile("((\\byou\\b)|(\\byour\\b)|(\\byour\\b)|(\\byourselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern thirdPluralPersonPattern = Pattern.compile("((\\bthey\\b)|(\\bthem\\b)|(\\btheir\\b)|(\\bthemselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern exclusivethirdPluralPersonPattern = Pattern.compile("(\byourselves\b)", Pattern.CASE_INSENSITIVE);


    public static void main(String[] args) throws Exception {
//SQL-Connection wird hergestellt
        Connection sqlConnection = NewsArticle.getConnection();
        //Die aktuelle Resultdatenbank wird gelöscht, sodass eine neue erstellt werden kann.
        PreparedStatement DropStatement = sqlConnection.prepareStatement("DROP TABLE IF EXISTS newsResults");
        int result = DropStatement.executeUpdate();
        //Hier wird die neue Datenbank erstellt, das SQL-Statement muss dann bei neuen Sachen immer erweitert werden.
        PreparedStatement CreateStatement = sqlConnection.prepareStatement("CREATE TABLE newsResults (newsId int, isFake boolean, words int, uppercases DECIMAL (4,3), questions int, exclamations int, authors int, citations int, firstperson decimal(6,5), secondperson decimal(6,5), thirdperson decimal(6,5))");
        result = CreateStatement.executeUpdate();
        //Alle News-Einträge der Datenbank ausgeben lassen
        PreparedStatement getAllIdsStatement = sqlConnection.prepareStatement("SELECT * FROM newsarticles");
        ResultSet resultSet = getAllIdsStatement.executeQuery();
//In dieser Schleife werden alle NewsArticle nacheinander aufgerufen und unsere Parameter für sie berechnet.
        while (resultSet.next()) {
            NewsArticle news = new NewsArticle(resultSet.getInt("newsID"));
            boolean isFake = news.isFake;
            int words = getCountOfWords(news);
            double uppercases = getNumberOfUpperCase(news);
            int questions = getNumberOfQuestionMark(news);
            int exclamations = getNumberOfExclamationMark(news);
            double firstPersonOccurences = getPersonDistribution(news, firstPersonPattern)+getPersonDistribution(news, firstPluralPersonPattern);
            double secondPersonOccurences = getPersonDistribution(news, secondPersonPattern)+getPersonDistribution(news, secondPluralPersonPattern);
            double thirdPersonOccurences = getPersonDistribution(news, thirdPersonPattern)+getPersonDistribution(news, exclusivethirdPluralPersonPattern);


            //TODO Ich bekomm die getNumberOfAuthors Methode nicht zu laufen. Gibt mir immer ne NullPointerException raus
            int authors = 1;
            // getNumberOfAuthors(news);
            int citations = getNumberOfCitations(news);
            //Die eben berechnene Parameter werden hier in die neue Tabelle eingefügt. Muss bei weiteren Parametern entsprechend erweitert werden.
            PreparedStatement InsertStatement = sqlConnection.prepareStatement("INSERT INTO newsResults values (" + resultSet.getInt("newsID") + ", " + isFake + ", " + words + ", " + uppercases + ", " + questions + ", " + exclamations + ", " + authors + ", " + citations + ", " +firstPersonOccurences+ ", " +secondPersonOccurences+ ", " +thirdPersonOccurences+")");
            result = InsertStatement.executeUpdate();
        }

    }


    /**
     * This method count the number of words in a given string.
     * Need the package @import java.util.StringTokenizer;
     *
     * @param news
     * @return The number of sStrinkTokenizer.countTokens
     * @author: Benjamin M. Abdel-Karim
     * @update: 2017-07-12
     */
    public static int getCountOfWords(NewsArticle news) {
        String sText = news.getContent();
        StringTokenizer sStringTokenizer = new StringTokenizer(sText);
        return sStringTokenizer.countTokens();
    }


    /**
     * This method share of uppercases in a given string.
     *
     * @param news
     * @return The share of uppercases
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static double getNumberOfUpperCase(NewsArticle news) {
        String sText = news.getContent();
        int iUpperCase = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (Character.isUpperCase(sText.charAt(i))) {
                iUpperCase++;
            }
        }
        System.out.println(iUpperCase / sText.length());
        return (double) iUpperCase / (double) sText.length();

    }

    /**
     * This method count the Exclamation marks in a given string.
     *
     * @param news
     * @return The number of Exclamation marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfExclamationMark(NewsArticle news) {
        String sText = news.getContent();
        int iExclamationMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '!') {
                iExclamationMark++;
            }
        }
        return iExclamationMark;
    }


    /**
     * This method count the Question marks in a given string.
     *
     * @param news
     * @return The number of Question marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getNumberOfQuestionMark(NewsArticle news) {
        String sText = news.getContent();
        int iQuestionMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '?') {
                iQuestionMark++;
            }
        }
        return iQuestionMark;
    }

    /**
     * This method count the Authors of a given NewsArticle.
     *
     * @param news
     * @return The number of authors
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static int getNumberOfAuthors(NewsArticle news) {
        if (news.getAuthor() == null || news.getAuthor().isEmpty()) {
            System.out.println("Returned 0");
            return 0;
        } else {
            return news.getAuthor().size();
        }

    }

    /**
     * This method count the citations of a given NewsArticle.
     *
     * @param news
     * @return The number of citations
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static int getNumberOfCitations(NewsArticle news) {
        Pattern citations = Pattern.compile("\"[^\"]+\"");
        int splits = citations.split(news.getContent()).length;
        return splits;
    }


    public static double getPersonDistribution(NewsArticle news, Pattern person) {
        Matcher personMatcher = person.matcher(news.getContent());
        int PersonOccurrence = 0;

        while (personMatcher.find())
            PersonOccurrence++;

        return PersonOccurrence / (double) getCountOfWords(news);

    }
}


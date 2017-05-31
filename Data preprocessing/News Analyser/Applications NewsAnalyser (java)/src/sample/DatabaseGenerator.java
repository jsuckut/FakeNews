

import org.jsoup.Jsoup;

import javax.lang.model.util.Elements;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.sql.DriverManager;
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
        PreparedStatement CreateStatement = sqlConnection.prepareStatement("CREATE TABLE newsResults (newsId int, isFake boolean, words int, uppercases DECIMAL (5,4), questions decimal(5,4), exclamations decimal(5,4), authors int, citations decimal(5,4), firstperson decimal(6,5), secondperson decimal(6,5), thirdperson decimal(6,5), sentencelength decimal(5,3), repetitiveness decimal (5,4), authorHits int, titleUppercase decimal(5,4), errorLevel decimal (5,4))");
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
            double questions = getNumberOfQuestionMark(news);
            double exclamations = getNumberOfExclamationMark(news);
            double firstPersonOccurences = getPersonDistribution(news, firstPersonPattern)+getPersonDistribution(news, firstPluralPersonPattern);
            double secondPersonOccurences = getPersonDistribution(news, secondPersonPattern)+getPersonDistribution(news, secondPluralPersonPattern);
            double thirdPersonOccurences = getPersonDistribution(news, thirdPersonPattern)+getPersonDistribution(news, exclusivethirdPluralPersonPattern);
            double averageSentenceLength = getAverageSentenceLength(news);
            double repetitiveness = getRepetitiveness(news);
            int authorHits = getGoogleHits(news);
            int authors = news.numberOfAuthors;
            double citations = getNumberOfCitations(news);
            double titleUppercase = isTitleUppercase(news);
            double errorLevel = errorLevel(news);


                Connection updateConnection = NewsArticle.getConnection();
                //Die eben berechnene Parameter werden hier in die neue Tabelle eingefügt. Muss bei weiteren Parametern entsprechend erweitert werden.
                PreparedStatement insertStatement = updateConnection.prepareStatement("INSERT INTO newsResults values (" + resultSet.getInt("newsID") + ", " + isFake + ", " + words + ", " + uppercases + ", " + questions + ", " + exclamations + ", " + authors + ", " + citations + ", " + firstPersonOccurences + ", " + secondPersonOccurences + ", " + thirdPersonOccurences + ", " + averageSentenceLength + ", " +repetitiveness+", " +authorHits+ ", "+titleUppercase+", " +errorLevel+ ")");
                insertStatement.executeUpdate();

            insertStatement.close();
            updateConnection.close();
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
    public static double getNumberOfExclamationMark(NewsArticle news) {
        String sText = news.getContent();
        int iExclamationMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '!') {
                iExclamationMark++;
            }
        }
        return (double)iExclamationMark/countSentences(news);
    }


    /**
     * This method count the Question marks in a given string.
     *
     * @param news
     * @return The number of Question marks
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static double getNumberOfQuestionMark(NewsArticle news) {
        String sText = news.getContent();
        int iQuestionMark = 0;
        for (int i = 0; i < sText.length(); i++) {
            if (sText.charAt(i) == '?') {
                iQuestionMark++;
            }
        }
        return (double)iQuestionMark/countSentences(news);
    }


    /**
     * This method count the citations of a given NewsArticle.
     *
     * @param news
     * @return The number of citations
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static double getNumberOfCitations(NewsArticle news) {
        Pattern citations = Pattern.compile("\"[^\"]+\"");
        int splits = citations.split(news.getContent()).length;
        return (double)splits/countSentences(news);
    }

    /**
     * This method count the usage of a given person in a given NewsArticle.
     *
     * @return The number of occurences of a person
     * @author: Hendrik Joentgen
     * @update: 2017-05-20
     */
    public static double getPersonDistribution(NewsArticle news, Pattern person) {
        Matcher personMatcher = person.matcher(news.getContent());
        int PersonOccurrence = 0;

        while (personMatcher.find())
            PersonOccurrence++;
        return PersonOccurrence / (double) getCountOfWords(news);

    }

    public static double countSentences(NewsArticle news){
        double sentenceCount = 0;
        for (int iIndex = 0; iIndex < news.getContent().length(); ++iIndex) {
            char cLetter = news.getContent().charAt(iIndex);
            if (cLetter == 46 || cLetter == 33 || cLetter == 63) {
                sentenceCount++;
            }
        }
        return sentenceCount;
    }


    public static double getAverageSentenceLength(NewsArticle news) {
       return (double) getCountOfWords(news) / countSentences(news);
    }

    public static double getRepetitiveness(NewsArticle news){
        Set<String> uniqueWords = new HashSet<String>();
        String[] words = news.getContent().split(" ");
        for(String wrd:words){
            uniqueWords.add(wrd);
        }
        return (double)uniqueWords.size()/getCountOfWords(news);
    }

    public static int getGoogleHits(NewsArticle news) throws IOException, URISyntaxException {

        String GOOGLE_SEARCH_URL = "https://www.bing.com/search";
        String newsSite = news.getUrl().split("/")[2];

        int hitNumber = 0;
        for (String author : news.author) {

            String searchURL = GOOGLE_SEARCH_URL + "?q=\"" + author.replace(" ", "+") + "\"+" + newsSite;
            org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Chrome/41.0.2228.0").get();
            if (!doc.select(".sb_count").isEmpty()) {
                org.jsoup.nodes.Element hitResult = doc.select(".sb_count").first();

                String hitText = hitResult.text().replace(".", "");

                String hits = null;
                if (hitText.split(" ").length == 3) {
                    hits = hitText.split(" ")[1];
                } else if (hitText.split(" ").length == 2) {
                    hits = hitText.split(" ")[0];
                }

                hitNumber = 0;
                if (!(hits == null)) {
                    hitNumber += Integer.parseInt(hits);
                } else {
                    System.out.println("Autor hat keine Hits");
                }
            } else {

            }
            if (!news.author.isEmpty()) {
                return hitNumber / news.author.size();
            } else {
                return 0;
            }
        }
        return hitNumber;
    }

    public static double isTitleUppercase(NewsArticle news){
        String title = news.getTitle();

        int iUpperCase = 0;
        for (int i = 0; i < title.length(); i++) {
            if (Character.isUpperCase(title.charAt(i))) {
                iUpperCase++;
            }
        }
        return (double) iUpperCase / (double) title.length();
    }

    public static double errorLevel(NewsArticle news) throws IOException {
        return (double)LanguageChecker.getSpellingError(news.getContent())/getCountOfWords(news);
    }


}


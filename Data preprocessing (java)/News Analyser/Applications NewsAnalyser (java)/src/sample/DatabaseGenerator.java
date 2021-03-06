import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import org.jsoup.Jsoup;

import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.*;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.PropertiesUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hendrik Jöntgen on 18.05.2017.
 * last updated by Jörg Suckut on 02.07.2017.
 * In dieser Klasse wird die Datenbank für den Klassifizierer gebaut.
 * Dabei werden sämtliche NewsArticle aus der Ursprungsdatenbank geladen und unserer Parameterfunktionen auf diese ausgeführt
 * Anschließend werden die Ergebnisse unserer Parameterfunktionen in eine neue Datenbank geschrieben.
 *
 */
public class DatabaseGenerator {

    public static Pattern firstPersonPattern = Pattern.compile("((\\bi\\b)|(\\bme\\b)|(\\bmy\\b)|(\\bmine\\b)|(\\bmyself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern secondPersonPattern = Pattern.compile("((\\byou\\b)|(\\byour\\b)|(\\byour\\b)|(\\byourself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern thirdPersonPattern = Pattern.compile("((\\bhe\\b)|(\\bhim\\b)|(\\bhis\\b)|(\\bhimself\\b)|(\\bshe\\b)|(\\bher\\b)|(\\bhers\\b)|(\\bherself\\b)|(\\bit\\b)|(\\bits\\b)|(\\bhimself\\b)|(\\bitself\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern firstPluralPersonPattern = Pattern.compile("((\\bwe\\b)|(\\bus\\b)|(\\bour\\b)|(\\bours\\b)|(\\bourselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern secondPluralPersonPattern = Pattern.compile("((\\byou\\b)|(\\byour\\b)|(\\byour\\b)|(\\byourselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern thirdPluralPersonPattern = Pattern.compile("((\\bthey\\b)|(\\bthem\\b)|(\\btheir\\b)|(\\bthemselves\\b))", Pattern.CASE_INSENSITIVE);
    public static Pattern exclusivethirdPluralPersonPattern = Pattern.compile("(\byourselves\b)", Pattern.CASE_INSENSITIVE);


    public static void main(String[] args){
        try {
            createDatabase();
            //updateDatabase();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method creates the desired database with all the necessary fields by iterating over the original data and using the methods below.
     *
     * @return void
     * @author: Hendrik Jöntgen & Jörg Suckut
     * @update: 2017-06-25
     */
    public static void createDatabase() throws Exception {
        //SQL-Connection wird hergestellt
        Connection sqlConnection = NewsArticle.getConnection();
        //Die aktuelle Resultdatenbank wird gelöscht, sodass eine neue erstellt werden kann.
        PreparedStatement DropStatement = sqlConnection.prepareStatement("DROP TABLE IF EXISTS newsResults");
        int result = DropStatement.executeUpdate();
        //Hier wird die neue Datenbank erstellt, das SQL-Statement muss dann bei neuen Sachen immer erweitert werden.
        PreparedStatement CreateStatement = sqlConnection.prepareStatement("CREATE TABLE newsResults (newsId int, isFake boolean, words int, uppercases DECIMAL (5,4), questions decimal(5,4), exclamations decimal(5,4), authors int, citations decimal(5,4), firstperson decimal(6,5), secondperson decimal(6,5), thirdperson decimal(6,5), sentencelength decimal(5,3), repetitiveness decimal (5,4), authorHits int, titleUppercase decimal(5,4), errorLevel decimal (5,4), sentiment decimal (6,5), informativeness decimal (6,5), superlativesPerWords decimal (6,5), superlativesPerAdjectives decimal (6, 5), usedsourcesPerWords decimal (6,5), internsourcesPerWords decimal(6,5), externsourcesPerWords decimal(6,5), usedimagesPerWords decimal(6,5))");
        result = CreateStatement.executeUpdate();
        //Alle News-Einträge der Datenbank ausgeben lassen
        PreparedStatement getAllIdsStatement = sqlConnection.prepareStatement("SELECT * FROM newsarticles");
        ResultSet resultSet = getAllIdsStatement.executeQuery();
        //In dieser Schleife werden alle NewsArticle nacheinander aufgerufen und unsere Parameter für sie berechnet.
        while (resultSet.next()) {

            NewsArticle news = new NewsArticle(resultSet.getInt("newsID"));
            boolean isFake = news.isFake();
            int words = getCountOfWords(news);
            double uppercases = getNumberOfUpperCase(news);
            double questions = getNumberOfQuestionMark(news);
            double exclamations = getNumberOfExclamationMark(news);
            double firstPersonOccurences = getPersonDistribution(news, firstPersonPattern)+getPersonDistribution(news, firstPluralPersonPattern);
            double secondPersonOccurences = getPersonDistribution(news, secondPersonPattern)+getPersonDistribution(news, secondPluralPersonPattern);
            double thirdPersonOccurences = getPersonDistribution(news, thirdPersonPattern)+getPersonDistribution(news, exclusivethirdPluralPersonPattern);
            double averageSentenceLength = getAverageSentenceLength(news);
            double repetitiveness = getRepetitiveness(news);
            int authorHits = getBingHits(news);
            int authors = news.numberOfAuthors;
            double citations = getNumberOfCitations(news);
            double titleUppercase = isTitleUppercase(news);
            double errorLevel = errorLevel(news);
            double sentiment = getSentiment(news);
            double informativeness = (double) getInformationCount(news) / (double) words;
            HashMap<String, Integer> postags = getPosTags(news);
            double normalAdjectives;
            double comparativeAdjectives;
            double superlativeAdjectives;
            if(postags.get("JJ") == null)
                normalAdjectives = 0.0;
            else
                normalAdjectives = postags.get("JJ");
            if(postags.get("JJR") == null)
                comparativeAdjectives = 0.0;
            else
                comparativeAdjectives = postags.get("JJR");
            if(postags.get("JJS") == null)
                superlativeAdjectives = 0.0;
            else
                superlativeAdjectives = postags.get("JJS");
            double superlativesPerWords = superlativeAdjectives / words;
            double superlativesPerAdjectives = superlativeAdjectives / (normalAdjectives + comparativeAdjectives + superlativeAdjectives);

            double usedsourcesPerWords = (double) news.getUsedSources() / (double) words;
            double internsourcesPerWords = (double) news.getInternSources() / (double) words;
            double externsourcesPerWords = (double) news.getExternSources() / (double) words;
            double usedimgagesPerWords = (double) news.getUsedImages() / (double) words;


            Connection updateConnection = NewsArticle.getConnection();
                //Die eben berechnene Parameter werden hier in die neue Tabelle eingefügt. Muss bei weiteren Parametern entsprechend erweitert werden.
                PreparedStatement insertStatement = updateConnection.prepareStatement("INSERT INTO newsResults values (" + resultSet.getInt("newsID") + ", " + isFake + ", " + words + ", " + uppercases + ", " + questions + ", " + exclamations + ", " + authors + ", " + citations + ", " + firstPersonOccurences + ", " + secondPersonOccurences + ", " + thirdPersonOccurences + ", " + averageSentenceLength + ", " +repetitiveness+", " +authorHits+ ", "+titleUppercase+", " +errorLevel + ", " + sentiment + ", " + informativeness + ", " + superlativesPerWords + ", " + superlativesPerAdjectives + ", " + usedsourcesPerWords + ", " + internsourcesPerWords + ", " + externsourcesPerWords + ", " + usedimgagesPerWords + ")");
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
     * @update: 2017-05-12
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
     * @param news, person
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

    /**
     * This method counts the number of sentences countained in a text by using the StanfordCoreNLP library.
     *
     * @param news
     * @return The number of sentences
     * @author: Jörg Suckut
     * @update: 2017-06-25
     */
    public static double countSentences(NewsArticle news){
        StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize, ssplit, parse",
                "tokenize.language", "en"));

        Annotation document = new Annotation(news.getContent());

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        return (double) sentences.size();
    }

    /**
     * This method calculates the average lenght of a sentence.
     *
     * @param news
     * @return The number of sentences
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static double getAverageSentenceLength(NewsArticle news) {
       return (double) getCountOfWords(news) / countSentences(news);
    }

    /**
     * This method counts the number of unique words in a text.
     *
     * @param news
     * @return The number of unique words
     * @author: Benjamin M. Abdel-Karim
     * @update: 2017-05-13
     */
    public static double getRepetitiveness(NewsArticle news){
        Set<String> uniqueWords = new HashSet<String>();
        String[] words = news.getContent().split(" ");
        for(String wrd:words){
            uniqueWords.add(wrd);
        }
        return (double)uniqueWords.size()/getCountOfWords(news);
    }

    /** This method determines the number of Bing search hits an of a given NewsArticle has.
     *
     * @param news
     * @return The number Bing hits
     * @author: Hendrik Joentgen
     * @update: 2017-05-25
     */
    public static int getBingHits(NewsArticle news) throws IOException, URISyntaxException {

        String GOOGLE_SEARCH_URL = "https://www.bing.com/search";
        String newsSite = news.getUrl().split("/")[2];

        int hitNumber = 0;
        for (String author : news.getAuthor()) {

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
            if (!news.getAuthor().isEmpty()) {
                return hitNumber / news.getAuthor().size();
            } else {
                return 0;
            }
        }
        return hitNumber;
    }

    /**
     * This returns a ratio of the number of uppercase letters in a title divided by the number of characters.
     *
     * @param news
     * @return The portion of uppercase letters in a title
     * @author: Hendrik Joentgen
     * @update: 2017-05-25
     */
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

    /**
     * This method determines the ratio of spelling errors in a text divided by the number of words.
     *
     * @param news
     * @return The portion of spelling errors in a text
     * @author: Hendrik Joentgen
     * @update: 2017-05-31
     */
    public static double errorLevel(NewsArticle news) throws IOException {
        return (double)LanguageChecker.getSpellingError(news.getContent())/getCountOfWords(news);
    }

    /**
     * This method determines the overall sentiment of a NewsArticle object by using StanfordCoreNLP. The overall sentiment is average sentiment score of each sentence.
     *
     * @param news
     * @return The sentiment of the whole NewsArtricle.
     * @author: Joerg U. Suckut
     * @update: 2017-05-31
     */
    public static double getSentiment(NewsArticle news){
        String text = news.getContent();

        StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize, ssplit, parse, sentiment",
                "tokenize.language", "en"));

        Annotation document = new Annotation(text);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        int sentiment = 0;
        int i = 0;

        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(SentimentAnnotatedTree.class);
            sentiment += (RNNCoreAnnotations.getPredictedClass(tree) - 2);
            i++;

            //System.out.println("Sentiment of the " + i + " sentence :" + (RNNCoreAnnotations.getPredictedClass(tree) - 2));
            //System.out.println("Overall sentiment: " + sentiment);
        }
        double averageSentiment = ((double) sentiment / (double) i);

        return averageSentiment;
    }

    /**
     * This method determines the number of information contained in a text by using the OpenIE of the StanfordCoreNLP library.
     *
     * @param news
     * @return The information count of the text.
     * @author: Joerg U. Suckut
     * @update: 2017-06-04
     */
    public static int getInformationCount(NewsArticle news){
        String text = news.getContent();

        // Create the Stanford CoreNLP pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie",
                "tokenize.language", "en"));

        // Annotate an example document.
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);

        // Loop over sentences in the document
        int informationCount = 0;
        for (CoreMap sentence : doc.get(CoreAnnotations.SentencesAnnotation.class)) {
            // Get the OpenIE triples for the sentence
            Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
            // Print the triples
            for (RelationTriple triple : triples) {
                // Count something as information when the confidence is above 0.8
                if(triple.confidence > 0.8)
                {
                    informationCount++;
                }
            }
        }
        return informationCount;
    }

    /**
     * This method returns a HashMap containing the Part-Of-Speech-Tags in a text with their respective frequencies.
     *
     * @param news
     * @return HashMap with POS-Tags as keys and their frequencies as values.
     * @author: Jörg Suckut
     * @update: 2017-06-25
     */
    public static HashMap<String, Integer> getPosTags(NewsArticle news) {
        // Create the Stanford CoreNLP pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(PropertiesUtils.asProperties(
                "annotators", "tokenize,ssplit,pos",
                "tokenize.language", "en"));

        // Annotate an example document.
        Annotation doc = new Annotation(news.getContent());
        pipeline.annotate(doc);

        HashMap<String, Integer> postags = new HashMap<>();

        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);
        for(CoreMap sentence : sentences){
            for(CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)){
                //String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                if(postags.get(pos) == null)
                    postags.put(pos, 1);
                else
                    postags.put(pos, postags.get(pos) + 1);
                //System.out.println(word + "/" + pos);
            }
        }

        return postags;
    }
}


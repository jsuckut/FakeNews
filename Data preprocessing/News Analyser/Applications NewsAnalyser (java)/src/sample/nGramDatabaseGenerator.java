import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Jörg U. Suckut on 18.05.2017.
 * In dieser Klasse werden die Tabellen mit den n-Grammen gebildet.*
 */
public class nGramDatabaseGenerator {

    private HashMap<String, Integer> top1000nGrams = new HashMap<String, Integer>();

    public static void main(String[] args) throws Exception {
        nGramDatabaseGenerator monogram = new nGramDatabaseGenerator(1);
        nGramDatabaseGenerator bigram = new nGramDatabaseGenerator(2);
        nGramDatabaseGenerator trigram = new nGramDatabaseGenerator(3);
    }

    public nGramDatabaseGenerator(int n){
        try {
            createAndFillnGramDatabase(n);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method creates a table to store the top 1000 ngrams for each newsarticle and fills it.
     *
     * @param int n
     * @return void
     * @author: Jörg U. Suckut
     * @update: 2017-06-01
     */
    private void createAndFillnGramDatabase(int n) throws Exception{
        // der Name der erstellen Tabelle ist abhängig von der Wahl von n (z.B. 1GramDatabase für Unigramme)
        String tablename = n + "GramDatabase";

        //SQL-Connection wird hergestellt
        Connection sqlConnection = NewsArticle.getConnection();
        //Die aktuelle Resultdatenbank wird gelöscht, sodass eine neue erstellt werden kann.
        PreparedStatement DropStatement = sqlConnection.prepareStatement("DROP TABLE IF EXISTS " + tablename);
        int result = DropStatement.executeUpdate();
        //Hier wird die neue Datenbank erstellt, das SQL-Statement muss dann bei neuen Sachen immer erweitert werden.

        PreparedStatement getAllIdsStatement = sqlConnection.prepareStatement("SELECT * FROM newsarticles");
        ResultSet resultSet = getAllIdsStatement.executeQuery();

        HashMap<String, Integer> allnGrams = new HashMap<String, Integer>();

        // zunächst wird eine Hashmap erstellt in der die nGrams (Key) mit ihrer Häufigkeit des kompletten Corpus an Newsartikeln gespeichert werden
        while(resultSet.next()){
            NewsArticle news = new NewsArticle(resultSet.getInt("newsID"));
            allnGrams = ngrams(n, news.getContent(), allnGrams);
        }

        // die Tabelle wird zunächst ausschließlich mit der newsId Spalte initialisiert
        PreparedStatement createStatement = sqlConnection.prepareStatement("CREATE TABLE " + tablename + " (newsId int)");
        result = createStatement.executeUpdate();

        // die oben erstellte Hashmap wird nach den 1000 häufigsten nGrammen durchsucht und diese in einer neuen Hashmap gespeichert
        // enthält der Korpus weniger als 1000 verschiedene nGramme, so wird die Hashmap nur kopiert
        if(allnGrams.size() > 1000) {
            for (int i = 0; i < 1000; i++) {
                String currentMaxName = "";
                int currentMaxValue = 0;
                for(String name: allnGrams.keySet()){
                    if(allnGrams.get(name) > currentMaxValue && !name.equals(""))
                    {
                        currentMaxName = name;
                        currentMaxValue = allnGrams.get(name);
                    }
                }
                top1000nGrams.put(currentMaxName, currentMaxValue);
                allnGrams.remove(currentMaxName);
            }
        }
        else
        {
            top1000nGrams = allnGrams;
        }

        // anschließend wird die oben schon erstellte Tabelle durch die Spalten für die häufigsten 1000 nGramme ergänzt
        for( String name: top1000nGrams.keySet() )
        {
            String alterString = "ALTER TABLE " + tablename + " ADD `" + name + "` int DEFAULT 0";
            System.out.println(alterString);
            PreparedStatement alterStatement = sqlConnection.prepareStatement(alterString);
            result = alterStatement.executeUpdate();
        }

        resultSet = getAllIdsStatement.executeQuery();

        // zuletzt wird für jeden Newsartikel einzeln die Häufigkeit der vorkommenden nGramme bestimmt und in die Tabelle eingetragen
        while(resultSet.next()){
            NewsArticle news = new NewsArticle(resultSet.getInt("newsID"));
            HashMap<String, Integer> nGrams = ngrams(n, news.getContent(), new HashMap<String, Integer>());
            String columnames = "`newsId`";
            String values = String.valueOf(resultSet.getInt("newsID"));

            for(String name: top1000nGrams.keySet()){
                if(nGrams.containsKey(name))
                {
                    columnames += ", `" + name + "`";
                    values += ", " + nGrams.get(name);
                }
            }
            String insertString = "INSERT INTO " + tablename + " ( " + columnames + " ) VALUES ( " + values + " ) ";
            System.out.println(insertString);
            PreparedStatement insertStatement = sqlConnection.prepareStatement(insertString);
            result = insertStatement.executeUpdate();
        }
    }

    /**
     * This method returns the nGrams of a text.
     * It receives the length of the nGrams it's supposed to find, the string it should search and a Hashmap in which the nGrams are supposed to be stored in.
     *
     * @param int n, String str, Hashmap<String, Integer> ngrams
     * @return ngrams
     * @author: Jörg U. Suckut
     * @update: 2017-06-01
     */
    private static HashMap<String, Integer> ngrams(int n, String str, HashMap<String, Integer> ngrams) {
        String modStr = str.toLowerCase();
        modStr = modStr.replaceAll("([^a-z0-9 ])", "");
        modStr = modStr.trim().replaceAll(" +", " ");

        //List<String> ngrams = new ArrayList<String>();
        String[] words = modStr.split(" ");

        for (int i = 0; i < words.length - n + 1; i++) {
            //ngrams.add(concat(words, i, i + n));
            String ngram = concat(words, i, i + n);
            if(!ngrams.containsKey(ngram))
                ngrams.put(ngram, 1);
            else {
                int value = ngrams.get(ngram);
                ngrams.put(ngram, value + 1);
            }
        }
        return ngrams;
    }

    /**
     * This method appends concats the strings in an array beginning and ending at a given position.
     *
     * @param String[] words, int start, ints end
     * @return ngrams
     * @author: Jörg U. Suckut
     * @update: 2017-06-01
     */
    private static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }

}


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Hendrik Jöntgen on 18.05.2017.
 * In dieser Klasse wird die Datenbank für den Klassifizierer gebaut.
 * Dabei werden sämtliche NewsArticle aus der Ursprungsdatenbank geladen und unserer Parameterfunktionen auf diese ausgeführt
 * Anschließend werden die Ergebnisse unserer Parameterfunktionen in eine neue Datenbank geschrieben.
 *
 */
public class nGramDatabaseGenerator {

    private HashMap<String, Integer> top1000nGrams = new HashMap<String, Integer>();

    public static void main(String[] args) throws Exception {
        //nGramDatabaseGenerator monogram = new nGramDatabaseGenerator(1);
        nGramDatabaseGenerator bigram = new nGramDatabaseGenerator(2);
        //nGramDatabaseGenerator trigram = new nGramDatabaseGenerator(3);
    }

    public nGramDatabaseGenerator(int n){
        try {
            createAndFillnGramDatabase(n);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createAndFillnGramDatabase(int n) throws Exception{

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

        while(resultSet.next()){
            NewsArticle news = new NewsArticle(resultSet.getInt("newsID"));
            allnGrams = ngrams(n, news.getContent(), allnGrams);
        }

        System.out.println("Size of the resulting allnGramsHashMap: " + allnGrams.size());

        PreparedStatement createStatement = sqlConnection.prepareStatement("CREATE TABLE " + tablename + " (newsId int)");
        result = createStatement.executeUpdate();


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

        System.out.println("Size of the resulting top1000nGramsHashMap: " + top1000nGrams.size());

        for( String name: top1000nGrams.keySet() )
        {
            String alterString = "ALTER TABLE " + tablename + " ADD `" + name + "` int DEFAULT 0";
            System.out.println(alterString);
            PreparedStatement alterStatement = sqlConnection.prepareStatement(alterString);
            try {
                result = alterStatement.executeUpdate();
            }
            catch(SQLException e)
            {
                System.out.println(e.getSQLState());
                System.out.println(e.getErrorCode());
                System.out.println(e.getMessage());
            }
        }

        resultSet = getAllIdsStatement.executeQuery();

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

    public static HashMap<String, Integer> ngrams(int n, String str, HashMap<String, Integer> ngrams) {
        String modStr = str.toLowerCase();
        modStr = modStr.replaceAll("([^a-z0-9 ])", "");
        modStr = modStr.trim().replaceAll(" +", " ");

        //List<String> ngrams = new ArrayList<String>();
        String[] words = modStr.split(" ");

        for (int i = 0; i < words.length - n + 1; i++) {
            //ngrams.add(concat(words, i, i + n));
            String ngram = concat(words, i, i + n);
            Boolean bool = ngrams.get(ngram) == null;
            if(bool)
                ngrams.put(ngram, 1);
            else {
                int value = ngrams.get(ngram);
                ngrams.put(ngram, value + 1);
            }
        }
        return ngrams;
    }

    public static String concat(String[] words, int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < end; i++)
            sb.append((i > start ? " " : "") + words[i]);
        return sb.toString();
    }

}


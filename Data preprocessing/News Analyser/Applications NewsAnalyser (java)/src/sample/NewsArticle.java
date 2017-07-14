import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 */

/**
 * @author Hendrik Jöntgen
 * In dieser Klasse wird das Objekt des NewsArtikel definiert.
 * Durch Zugriff auf die SQL-Datenbank können entweder beliebig viele NewsArtikel und ein einzelner NewsArtikel mit einer beliebigen newsID erstellt werden.
 * Diese NewsArtikel-Objekte haben sämtliche Attribute, die in der SQL-Datenbank vorhanden sind.
 */
public class NewsArticle {


    /**
     * @param args
     * @throws Exception
     */


    public static void main(String[] args) throws Exception {
        getConnection();
    }
    private String title;
    private String content;
    private java.sql.Date date;
    private int usedSources;
    private int internSources;
    private int externSources;
    private int usedImages;
    private String url;
    private ArrayList<String> author = new ArrayList<String>();
    public int numberOfAuthors;
    private boolean isFake;

    /**
     * This method creates a connection to the database where the data is stored.
     *
     * @return null
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://hung3r.lima-db.de:3306/db_295068_1?zeroDateTimeBehavior=convertToNull";
            String username = "USER295068";
            String password = "pai7ar2vi7ieGia";
            Class.forName(driver);

            Connection conn = DriverManager.getConnection(url,username,password);
            return conn;
        } catch(Exception e){System.out.println(e);}


        return null;
    }

    /**
     * This method is a constructor of NewsArticle that uses getConnection() to create the object.
     *
     * @return NewsArticle
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public NewsArticle(int newsID) throws Exception{
        Connection con = getConnection();
        PreparedStatement newsStatement = con.prepareStatement("SELECT * FROM newsarticles WHERE newsID=" + newsID);
        ResultSet result = newsStatement.executeQuery();
        if(result.next()){
            newsID =result.getInt("newsID");
            title = result.getString("title");
            content = result.getString("content");
            date = result.getDate("publicationdate");
            usedSources =  result.getInt("usedsources");
            internSources =  result.getInt("internsources");
            externSources =  result.getInt("externsources");
            usedImages =  result.getInt("usedimages");
            url = result.getString("url");
            isFake = result.getBoolean("isfake");

            //Die Autoren sind hier ein besonderer Fall, da sie in einer separaten Tabelle gespeichert sind und somit eine Art Join notwendig ist.
            //Auf einen klassischen Join wurde hier aber verzichtet und eine While-Schleife erstellt, welche sämtliche Autoren, welche am Artikel beteiligt waren, raussucht.
            PreparedStatement authorsStatement = con.prepareStatement("SELECT * FROM newsauthors WHERE newsID=" + newsID);
            ResultSet authorResult = authorsStatement.executeQuery();
            while(authorResult.next()){
                PreparedStatement getNamesStatement = con.prepareStatement("SELECT * FROM authors WHERE authorID=" + authorResult.getInt("authorID"));
                ResultSet namesResult = getNamesStatement.executeQuery();
                if (namesResult.next()){
                    author.add(namesResult.getString("firstname")+" "+namesResult.getString("lastname"));
                }
            }
            numberOfAuthors = author.size();

//Konsolen-Outputs um die erfolgreiche Objekt-Erstellung zu überprüfen
            System.out.println("NewsArticles-Objekt mit ID=" + result.getString("newsID") + " erstellt.");
            result.close();
            newsStatement.close();
            con.close();
        }

    }

    /**
     * This method creates an array containing all the NewsArticle objects of the database.
     *
     * @return NewsArticle[]
     * @author: Hendrik Joentgen
     * @update: 2017-05-18
     */
    public static NewsArticle[] generateNewsArticles(int amount) throws Exception{
        Connection con = getConnection();
        PreparedStatement statement = con.prepareStatement("SELECT * FROM newsarticles");
        ResultSet result = statement.executeQuery();
        NewsArticle[] newsArticles = new NewsArticle[amount];
        int i = 0;
        while(result.next() && i <amount){
            NewsArticle tobeadded = new NewsArticle(result.getInt("newsID"));
            newsArticles[i] = tobeadded;
            i++;
        }

        return newsArticles;
    }

    /**
     * Several methods for getting the variables.
     */
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public Date getDate() {
        return date;
    }
    public int getUsedSources() {
        return usedSources;
    }
    public int getInternSources() {
        return internSources;
    }
    public int getExternSources() {
        return externSources;
    }
    public int getUsedImages() {
        return usedImages;
    }
    public String getUrl() {
        return url;
    }
    public ArrayList<String> getAuthor() {
        return author;
    }
    public boolean isFake(){
        return isFake;
    }
}

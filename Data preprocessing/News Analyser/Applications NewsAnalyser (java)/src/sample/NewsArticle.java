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
 *
 */
public class NewsArticle {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        getConnection();
        generateNewsArticles(18);

    }
    public String title;
    public String content;
    public java.sql.Date date;
    public int usedSources;
    public int internSources;
    public int externSources;
    public int usedImages;
    public String url;
    public ArrayList<String> author;
    public boolean isFake;

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

    //Constructor, welcher aus der NewsID ein Newsarticle-Objekt erstellt.
    NewsArticle(int newsID) throws Exception{
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

            PreparedStatement authorsStatement = con.prepareStatement("SELECT * FROM newsauthors WHERE newsID=" + newsID);
            ResultSet authorResult = authorsStatement.executeQuery();
            ArrayList<String> author = new ArrayList<String>();
            while(authorResult.next()){
                PreparedStatement getNamesStatement = con.prepareStatement("SELECT * FROM authors WHERE authorID=" + authorResult.getInt("authorID"));
                ResultSet namesResult = getNamesStatement.executeQuery();
                if (namesResult.next()){
                    author.add(namesResult.getString("firstname")+" "+namesResult.getString("lastname"));
                }
            }


            System.out.println("NewsArticles-Objekt mit ID=" + result.getString("newsID") + " erstellt.");
            System.out.println(title);
            System.out.println(author.toString());
        }

    }


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

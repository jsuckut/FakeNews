import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Hendrik Jöntgen on 30.05.2017.
 */
public class ImageAge {

    public static void main(String[] args) throws IOException {

getImageDate("https://www.theguardian.com/sport/blog/2017/may/29/kimi-raikkonen-sebastian-vettel-ferrari-lewis-hamilton-fernando-alonso-monaco-f1");

    }

    public static String getImageDate(String url) throws IOException {

        String newsSite = url;
        Document doc = Jsoup.connect(newsSite).userAgent("Chrome/41.0.2228.0").get();
        //Alle Bilder auf der Webseite finden (durch den HTML-Tag)
        Elements foundImages = doc.select("img");
//Diese Schleife durchläuft alle Bilder der Webseite
        for (Element image: foundImages){
            //URL des Bildes bekommen
String imageURL = image.attr("src");
//Wenn es eine richtige URL ist (keine Werbung oder so) dann wird weitergemacht.
if (imageURL.contains("png")||imageURL.contains("jpg")){
                System.out.println(image.attr("src").toString());
            }




        }


        return null;
    }

}

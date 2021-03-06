import java.util.StringTokenizer;

/**
 * Auswertungsprogramm.
 * Dieses java Programm soll aus einem gegeben Text.
 * @code: String sNews die gewünschten Parameter berechnen.
 * @author Benjamin M. Abdel-Karim, Hendrik Jöntgen und Jörg Suckut
 * @since 2017-05-12
 * @version 2017-05-12
 */
public class CNewsAnalyser {


    /**
     * Die Mainmethode
     * Diese zentrale Methode ruft die einzelnen Methode auf und gibt
     * die Ergebnisse direkt auf der Konsole aus.
     * @return: Ergebnisse in Form der Textdaten.
     * @author: Benjamin M. Abdel-Karim
     * @update: 2017-07-12
     */
    public static void main(String[] args) {
    	
    	String test = "Dies ist ein Teststring, den ich abends um halb 12 erstellt habe, weil meine Freundin gerade Zelda spielt und ich nichts besseres zu tun habe. Brauche ich jetzt noch mehr Satzzeichen??! Ja ich brauche mehr Satzzeichen !!!!";
        System.out.println("Anzahl W�rter : " + getAnzahlDerWorter(test));
        System.out.println("Anzahl Zeichen : " + getAnzahlZeichen(test));
        System.out.println("Anzahl Grossbuchstaben : " + getAnzahlGrossbuchstaben(test));
        System.out.println("Anzahl Ausrufezeichen : " + getAnzahlAusrufezeichen(test));
        System.out.println("Anzahl Fragezeichen : " + getAnzahlFragezeichen(test));
        
    }



    /**
     * Die Methode zaehlt die Woerter in einem gegeben String.
     * Benoetigt das Paket java.util.StringTokenizer;
     * @param sText
     * @return Die Anzahl der Woeter durch sStrinkTokenizer.countTokens
     * @author: Benjamin M. Abdel-Karim
     * @update: 2017-07-12
     */
    public static int getAnzahlDerWorter(String sText){
        StringTokenizer sStrinkTokenizer= new StringTokenizer(sText);
        return sStrinkTokenizer.countTokens();
    }
    
    /**
     * Die Methode zaehlt die Zeichen in einem gegeben String.
     * @param sText
     * @return Die Anzahl der Zeichen
     * @author: Hendrik Joentgen
     * @update: 2017-05-12
     */
    public static int getAnzahlZeichen(String sText){
        return sText.length();
    }
    
    /**
     * Die Methode zaehlt die Grooebuchstaben in einem gegeben String.
     * @param sText
     * @return Die Anzahl der Grooeuchstaben
     * @author: Hendrik J�ntgen
     * @update: 2017-05-12
     */
    public static int getAnzahlGrossbuchstaben(String sText){
    	int upperCase = 0;
    	for (int i = 0; i < sText.length(); i++) {
    		if (Character.isUpperCase(sText.charAt(i))){
    			upperCase++;
    		}
    	}
    	return upperCase;
    }
    
    /**
     * Die Methode zaehlt die Ausrufezeichen in einem gegeben String.
     * @param sText
     * @return Die Anzahl der Ausrufezeichen
     * @author: Hendrik J�ntgen
     * @update: 2017-05-12
     */
    public static int getAnzahlAusrufezeichen(String sText){
    	int ausrufezeichen = 0;
    	for (int i = 0; i < sText.length(); i++) {
    		if (sText.charAt(i) == '!'){
    			ausrufezeichen++;
    		}
    	}
    	return ausrufezeichen;
    }
    
    /**
     * Die Methode zaehlt die Fragezeichen in einem gegeben String.
     * @param sText
     * @return Die Anzahl der Fragezeichen
     * @author: Hendrik J�ntgen
     * @update: 2017-05-12
     */
    public static int getAnzahlFragezeichen(String sText){
    	int fragezeichen = 0;
    	for (int i = 0; i < sText.length(); i++) {
    		if (sText.charAt(i) == '?'){
    			fragezeichen++;
    		}
    	}
    	return fragezeichen;
    }
    
}


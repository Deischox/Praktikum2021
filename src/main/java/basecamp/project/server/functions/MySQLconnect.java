package basecamp.project.server.functions;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import basecamp.project.server.controller.word;


/**
 * Diese Klasse stellt eine Verbindung zu MySQL her und kann Daten in die Datenbank speichern, bestehende Zeilen aktualisieren und Daten auslesen.
 */
public class MySQLconnect{
    private static Connection con = null;

    private static String dbHost;	// Hostname

    private static String dbPort = "3306";		// Port -- Standard: 3306

    
    private static String dbName;	// Datenbankname

    
    private static String dbUser;		// Datenbankuser

    
    private static String dbPass;		// Datenbankpasswort
    //private static String dbTable = "posts";		// Posts Tabelle

    /**
     * Stellt eine Verbindung zur Datenbank her.
     */
    public MySQLconnect() {
        // get credentials
        
        try {
			File file = ResourceUtils.getFile("classpath:credentials.txt");
            System.out.println("Credential File found!");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
			  String data = myReader.nextLine();
              switch(data.split("\\.")[1].split("=")[0])
              {
                case "user":
                    dbUser = data.split("=")[1];
                    break;
                case "password":
                    dbPass = data.split("=")[1];
                    break;
                case "database":
                    dbName = data.split("=")[1];
                    break;
                case "host":
                    dbHost = data.split("=")[1];
                    break;
              }
			}
			myReader.close();
		  }catch(IndexOutOfBoundsException e)
          {
            System.out.println("------------Error------------");
            System.out.println("Your Credential File is wrong! Please check your format! It should contain mysql.user, mysql.password, mysql.database, mysql.host");
            System.out.println("------------Error------------");
        } catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		  }

        try {
            Class.forName("com.mysql.jdbc.Driver");    // Datenbanktreiber für JDBC Schnittstellen laden.

            // Verbindung zur JDBC-Datenbank herstellen.
            con = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useSSL=false", dbUser, dbPass);
            System.out.println("Verbindung erfolgreich!");
        } catch (ClassNotFoundException e) {
            System.out.println("Treiber nicht gefunden");
        } catch (SQLException e) {
            System.out.println("Verbindung nicht moglich");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    /**
     * Schließt die Verbindung zur Datenbank
     */
    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Verbindung konnte nicht geschlossen werden");
        }
    }

    /**
     * Diese Methode gibt alle Hashtags aus der Datenbank in einer ArrayList wieder.
     * @return
     */
    public static ArrayList getHashtags()
    {
        Statement st = null;
        try
        {
            st = con.createStatement();
        }
        catch (SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        String sql = ("SELECT DISTINCT hashtag FROM hashtagsWithWords;");
        ResultSet rs = null;
        ArrayList results = new ArrayList();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                String hashtag = rs.getString("hashtag");
                results.add(hashtag);
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return results;


    }

    /**
     * Diese Methode gibt alle Hashtags aus der Datenbank in einer ArrayList wieder.
     * @return
     */
    public static ArrayList getRandomHashtag()
    {
        Statement st = null;
        try
        {
            st = con.createStatement();
        }
        catch (SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        String sql = ("SELECT DISTINCT hashtag FROM hashtagsWithWords WHERE occurrence > 60000 ORDER BY hashtag ASC LIMIT 100;");
        ResultSet rs = null;
        ArrayList results = new ArrayList();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                String hashtag = rs.getString("hashtag");
                results.add(hashtag);
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return results;


    }
    /**
     * Returnt eine ArrayList mit allen Wörtern und Häufigkeiten, die zusammen mit Hashtag auftauchen.
     * @param hashtag
     * @return
     */
    public static List<word> getWordsAndOccurrence(String hashtag, boolean rt, boolean at, boolean latin)
    {
        Statement st = null;


        try
        {
            st = con.createStatement();
        }
        catch (SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        // für die Tabelle
        System.out.println("SETUP");
        String sql = ("SELECT word, occurrence FROM hashtagsWithWords WHERE hashtag = '" + hashtag + "' ORDER BY occurrence DESC;");
        ResultSet rs = null;

        List<word> re = new ArrayList<word>();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                int occurrenceInt = rs.getInt("occurrence");
                String occurrence = String.valueOf(occurrenceInt);
                String word    = rs.getString("word");
                String[] wordOcc = {word, occurrence};
                
                if(!(word.contains("@") && !at) 
					&&  !((word.equals("RT") || word.equals("rt"))&& !rt)
					&&  (!latin || (word.matches("[a-zA-Z1-9]+")))
					)
                    {
                        re.add(new word(word, Float.parseFloat(occurrence)));
                    }
                
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return re;
    }

    /**
     * Returnt eine ArrayList mit allen Wörtern und Häufigkeiten, die zusammen mit Hashtag auftauchen.
     * @param hashtag
     * @return
     */
    public static List<word> getWordsAndOccurrencebyLang(String hashtag, String lang, boolean rt, boolean at, boolean latin)
    {
        Statement st = null;


        try
        {
            st = con.createStatement();
        }
        catch (SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        // für die Tabelle
        System.out.println("SETUP");
        String sql = ("SELECT word, SUM(occurrence) AS Anzahl FROM hashtagsWordsLanguageAndDate WHERE hashtag ='" + hashtag +"' AND lang ='" + lang + "'GROUP BY word;");
        ResultSet rs = null;

        List<word> re = new ArrayList<word>();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                int occurrenceInt = rs.getInt("occurrence");
                String occurrence = String.valueOf(occurrenceInt);
                String word    = rs.getString("word");
                String[] wordOcc = {word, occurrence};
                
                if(!(word.contains("@") && !at) 
					&&  !((word.equals("RT") || word.equals("rt"))&& !rt)
					&&  (!latin || (word.matches("[a-zA-Z1-9]+")))
					)
                    {
                        re.add(new word(word, Float.parseFloat(occurrence)));
                    }
                
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        System.out.println(re);
        return re;
    }

    /**
     * Returnt eine Liste mit allen Wörtern und Häufigkeiten, die zusammen mit Hashtag in einer
     * bestimmten Sprache auftauchen.
     * @param hashtag, language
     * @return
     */
    public static List<word> getWordsWithOccurrenceByLanguage(String hashtag, String language, boolean rt, boolean at, boolean latin)
    {
        Statement st = null;
        try
        {
            st = con.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        String sql = ("SELECT word, occurrence FROM hashtagsWordsLanguage WHERE hashtag =" + hashtag + "AND lang =" + language + "ORDER BY occurrence DESC;");
        ResultSet rs = null;
        List<word> re = new ArrayList<word>();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                int occurrenceInt = rs.getInt("occurrence");
                String occurrence = String.valueOf(occurrenceInt);
                String word    = rs.getString("word");
                String [] wordOcc = {word, occurrence};
                
                if(!(word.contains("@") && !at) 
					&&  !((word.equals("RT") || word.equals("rt"))&& !rt)
					&&  (!latin || (word.matches("[a-zA-Z1-9]+")))
					)
                    {
                        re.add(new word(word, Float.parseFloat(occurrence)));
                    }
                
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return re;
    }

    /**
     * Returnt eine ArrayList mit allen Wörtern und Häufigkeiten, die zusammen mit dem Hashtag an einem
     * bestimmten Tag auftauchen.
     * @param hashtag, date
     * @return
     */
    public static ArrayList getWordsWithOccurrenceByDate(String hashtag, String date)
    {
        Statement st = null;
        try
        {
            st = con.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        String sql = ("SELECT word, occurrence FROM hashtagsWordsDate WHERE hashtag ='" + hashtag + "' AND dateMDY =" + date + "ORDER BY occurrence DESC;");
        ResultSet rs = null;
        ArrayList results = new ArrayList();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                int occurrenceInt = rs.getInt("occurrence");
                String occurrence = String.valueOf(occurrenceInt);
                String word    = rs.getString("word");
                String [] wordOcc = {word, occurrence};
                results.add(wordOcc);
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return results;
    }

    /**
     * Returnt eine ArrayList mit allen Wörtern und Häufigkeiten, die zusammen mit dem Hashtag an einem
     * bestimmten Tag auftauchen.
     * @param hashtag, date
     * @return
     */
    public static List<word> getTimeline(String hashtag)
    {
        Statement st = null;
        try
        {
            st = con.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        String sql = ("SELECT * FROM hashtagsWithDate WHERE hashtag='"+hashtag+"';");
        ResultSet rs = null;
        List<word> re = new ArrayList<word>();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                int occurrenceInt = rs.getInt("occurrence");
                String occurrence = String.valueOf(occurrenceInt);
                String word = rs.getString("dateMDY");
                re.add(new word(word, Float.parseFloat(occurrence)));
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return re;
    }

    /**
     * Returnt eine Liste mit allen Wörtern und Häufigkeiten, die zusammen mit dem Hashtag an einem
     * bestimmten Tag in einer bestimmten Sprache auftauchen.
     * @param hashtag, language, date
     * @return
     */
    public static ArrayList getWordsWithOccurrenceByLanguageAndDate(String hashtag, String language, String date)
    {
        Statement st = null;
        try
        {
            st = con.createStatement();
        }
        catch(SQLException e)
        {
            System.out.println("Statement konnte nicht erstellt werden");
        }
        String sql = ("SELECT word, occurrence FROM hashtagsWordsLanguageDate WHERE hashtag =" + hashtag + "AND lang =" + language + "AND dateMDY =" + date + "ORDER BY occurrence DESC;");
        ResultSet rs = null;
        ArrayList results = new ArrayList();
        try
        {
            rs = st.executeQuery(sql);
        }
        catch(SQLException e)
        {
            System.out.println("Anfrage konnte nicht ausgeführt werden");
        }
        try {
            while(rs.next()) {
                int occurrenceInt = rs.getInt("occurrence");
                String occurrence = String.valueOf(occurrenceInt);
                String word    = rs.getString("word");
                String [] wordOcc = {word, occurrence};
                results.add(wordOcc);
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return results;
    }

}
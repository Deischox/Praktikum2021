package basecamp.project.server.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import basecamp.project.server.controller.word;


/**
 * Diese Klasse stellt eine Verbindung zu MySQL her und kann Daten in die Datenbank speichern, bestehende Zeilen aktualisieren und Daten auslesen.
 */
public class MySQLconnect{
    private static Connection con = null;
    private static String dbHost;	// Hostname
    private static String dbPort = "3306";		// Port -- Standard: 3306
    private static String dbName;	// Datenbankname
    private static String dbUser;;		// Datenbankuser
    private static String dbPass;		// Datenbankpasswort
    //private static String dbTable = "posts";		// Posts Tabelle

    /**
     * Stellt eine Verbindung zur Datenbank her.
     */
    public MySQLconnect() {
        // get credentials
        
        InputStream is = MySQLconnect.class.getResourceAsStream("/static/json/credentials.txt");
       
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(streamReader);
        try {
            String line = reader.readLine();
            while (line != null) {
                String[] fields = line.split("=");
                if (fields[0].compareTo("mysql.user") == 0) {
                    dbUser = fields[1];
                }
                if (fields[0].compareTo("mysql.password") == 0) {
                    dbPass = fields[1];
                }
                if (fields[0].compareTo("mysql.host") == 0) {
                    dbHost = fields[1];
                }
                if (fields[0].compareTo("mysql.database") == 0) {
                    dbName = fields[1];
                }

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Abfrage hat nicht funktioniert");
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
        ArrayList results = new ArrayList();
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
                
               
                

                results.add(wordOcc);
            }
        } catch (SQLException e) {
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden");
        }
        return re;
    }

    /**
     * Returnt eine Liste mit allen Wörtern und Häufigkeiten, die zusammen mit Hashtag in einer
     * bestimmten Sprache auftauchen.
     * @param hashtag, language
     * @return
     */
    public static ArrayList getWordsWithOccurrenceByLanguage(String hashtag, String language)
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
        String sql = ("SELECT word, occurrence FROM hashtagsWordsDate WHERE hashtag =" + hashtag + "AND dateMDY =" + date + "ORDER BY occurrence DESC;");
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
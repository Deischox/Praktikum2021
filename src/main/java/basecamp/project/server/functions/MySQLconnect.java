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
* The Class is used to build a connection with the Database and get the data
*
* @author  Silas Ueberschaer
* @version 2.0
* @since   2021-03-28
*/
public class MySQLconnect{
    private static Connection con = null;

    // Hostname
    private static String dbHost;	

    // Port -- Standard: 3306
    private static String dbPort = "3306";		

    // Datenbankname
    private static String dbName;	

    // Datenbankuser
    private static String dbUser;		

    // Datenbankpasswort
    private static String dbPass;		

    /**
	 * This Function builds a connection with the Database
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
	 * This Function closes the connection with the database
	 */
    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            System.out.println("Verbindung konnte nicht geschlossen werden");
        }
    }
    /** 
	 * Function gives Back a List with the most used hashtags
	 * @return		A ArrayList with the 100 most used hashtags
	 */
    public static ArrayList getMostHashtags()
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
	 * This Function gives back the Data of a Hashtag
	 * 
	 * @param hashtag 	The Hashtag to analyse
	 * @param at 		If '@' should be included in the Data (exp: @Obama)
	 * @param rt		If 'RT' or 'rt' should be included in the Data
	 * @param latin		If all Data should be in Latin Letters (No emojis, punctuation character, ...)
	 * @return			Returns a List of 'word' with the Data
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
	 * This Function gives back the Data of a Hashtag with only data of hashtags with the given language
	 * 
	 * @param hashtag 	The Hashtag to analyse
	 * @param lang 		The Language of the Tweets
	 * @param at 		If '@' should be included in the Data (exp: @Obama)
	 * @param rt		If 'RT' or 'rt' should be included in the Data
	 * @param latin		If all Data should be in Latin Letters (No emojis, punctuation character, ...)
	 * @return			Returns a List of 'word' with the Data
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
        String sql = ("SELECT word, SUM(occurrence) AS Anzahl FROM hashtagsWordsLanguageAndDate WHERE hashtag ='" + hashtag +"' AND lang ='" + lang + "'GROUP BY word ORDER BY Anzahl DESC LIMIT 500;");
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
                int occurrenceInt = rs.getInt("Anzahl");
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
            System.out.println("Auf ResultSet konnte nicht zugegriffen werden, " + e);
        }
        System.out.println(re);
        return re;
    }

    /**
	 * This Function gives back a List of 'word' with date of usage and count
	 * 
	 * @param hashtag 	The Hashtag to analyse
	 * @return			List of 'word' with date of usage and count
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
	 * This Function gives back a List of 'word' with date of usage and count of a given Hashtag with Language
	 * 
	 * @param hashtag 	The Hashtag to analyse
     * @param len       The Language of the Tweets
	 * @return			List of 'word' with date of usage and count
	 */
    public static List<word> getTimelineLen(String hashtag, String len)
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
        String sql = ("SELECT hashtag, dateMDY, SUM(occurrence) AS Anzahl FROM hashtagsWordsLanguageAndDate WHERE hashtag='"+hashtag+"' AND lang='"+len+"' GROUP BY dateMDY;");
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
                int occurrenceInt = rs.getInt("Anzahl");
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
}

package basecamp.project.server.controller;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import basecamp.project.server.functions.MySQLconnect;

import java.io.File;
import java.util.*; 



/**
* The HashtagController is responsible to return a JSON-String with the data of Hashtag
*
* @author  Silas Ueberschaer
* @version 2.0
* @since   2021-03-27
*/
@RestController
@RequestMapping("/data")
public class HashtagController {

	@Value("${service.key}")
	private String key;

	/**
	 * This Function is responsible for giving back the data of the Hashtag for the Visualisation
	 * 
	 * @param hashtag 	The Hashtag to analyse
	 * @param date 		The Date to use for the Data
	 * @param lang 		The Language of the Tweets
	 * @param at 		If '@' should be included in the Data (exp: @Obama)
	 * @param rt		If 'RT' or 'rt' should be included in the Data
	 * @param latin		If all Data should be in Latin Letters (No emojis, punctuation character, ...)
	 * @return			Returns a JSON String with the Data of the Hashtag
	 */
	@GetMapping
	public String data(String hashtag, String date, String lang, Boolean at, Boolean rt, Boolean latin) {
		
		JSONObject www = new JSONObject();
		JSONArray words = new JSONArray();
		JSONArray time = new JSONArray();
		List<word> values = new ArrayList<word>();
		List<word> timeline = new ArrayList<word>();

		
		MySQLconnect sql = new MySQLconnect();
		System.out.println("SQL: "+sql);

		try{
			if(lang.equals("All"))
			{
				values = sql.getWordsAndOccurrence(hashtag, rt, at, latin);
				timeline = sql.getTimeline(hashtag);
				
			}else{
				switch(lang){
					case "English":
						values = sql.getWordsAndOccurrencebyLang(hashtag, "en", rt, at, latin);
						timeline = sql.getTimelineLen(hashtag,"en");
						break;
					case "German":
						values = sql.getWordsAndOccurrencebyLang(hashtag, "de", rt, at, latin);
						timeline = sql.getTimelineLen(hashtag,"de");
						break;
					case "Spanish":
						values = sql.getWordsAndOccurrencebyLang(hashtag, "es" , rt, at, latin);
						timeline = sql.getTimelineLen(hashtag,"es");
						break;

				}
					

			}
			

		}catch(Exception e)
		{
			System.out.println(e);
		}

		for (word w : values) {
			JSONObject target = new JSONObject();
			target.put("word", w.wo);
			target.put("percentage", w.amount);
			words.add(target);
		}

		for (word w : timeline) {
			JSONObject target = new JSONObject();
			target.put("word", w.wo);
			target.put("percentage", w.amount);
			time.add(target);
		}

		
		www.put("words",words);
		www.put("timeline",time);
		

		return www.toJSONString();
	}

	
	/** 
	 * Function to create a List with words of a JSON-File (old Function, only usefull if Mysql Connection failed)
	 * 
	 * @param path	The Path to the JSON File
	 * @param at 	If '@' should be included in the Data (exp: @Obama)
	 * @param rt	If 'RT' or 'rt' should be included in the Data
	 * @param latin	If all Data should be in Latin Letters (No emojis, punctuation character, ...)
	 * @return		A List of 'word' with the word and the amount
	 */
	public List<word> createWords(String path, Boolean at, Boolean rt, Boolean latin)
	{
		System.out.println("Start creating Words....");

		List<word> words = new ArrayList<word>();
		try {
			File file = ResourceUtils.getFile(path);
			

			System.out.println("Found File: " + file);

			Scanner myReader = new Scanner(file);

			System.out.println("Reader created: " + myReader);


			while (myReader.hasNextLine()) {
			  String data = myReader.nextLine();

			  System.out.println("Line to analyse: " + data);
					if( 
						!(data.split("#")[1].split("\t")[0].contains("@") && !at) 
					&&  !((data.split("#")[1].split("\t")[0].equals("RT") || data.split("#")[1].split("\t")[0].equals("rt")) && !rt)
					&&  (!latin || (data.split("#")[1].split("\t")[0].matches("[a-zA-Z1-9]+")))
					)
					
					{
						System.out.println(data.split("#")[1].split("\t")[0]);
						words.add(new word(data.split("#")[1].split("\t")[0], Float.parseFloat(data.split("\t")[1])));
					}
					
				
			}
			myReader.close();
		  } catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		  }

		Collections.sort(words, (o1, o2) -> (int)(o1.getAmount() - o2.getAmount()));
		Collections.reverse(words);
		return words;
	}


}

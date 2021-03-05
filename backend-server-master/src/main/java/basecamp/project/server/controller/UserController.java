package basecamp.project.server.controller;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import basecamp.project.server.functions.NLP;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.*; // Import the Scanner class to read text files


import org.springframework.util.ResourceUtils;


@RestController
@RequestMapping("/users")
public class UserController {

	@Value("${service.key}")
	private String key;

	@GetMapping
	public String users(String hashtag) {

		System.out.println(hashtag.replace(" ", ""));
		JSONObject www = new JSONObject();
		JSONArray words = new JSONArray();
		List<word> test = new ArrayList<word>();
		switch(hashtag.replace(" ", ""))
		{
			case "china":
				test = createWords("classpath:static/json/als.txt");
				break;
			case "BTS":
				test = createWords("classpath:static/json/bts.txt");
				break;
			

		}



		
		for (word w : test) {
			JSONObject target = new JSONObject();
			target.put("word", w.wo);
			target.put("percentage", w.percentage);
			words.add(target);
		}



		NLP.init();
		float sentiment = 0f;
		float perc = 0f;
		for(word w : test)
		{
			sentiment += (NLP.findSentiment(w.wo))*w.percentage;
			perc += w.percentage;
		}

		sentiment /= perc;

		www.put("words",words);
		www.put("sentiment", sentiment);
		return www.toJSONString();
	}

	@RequestMapping("nrusers")
	String nrUsers() {
		return "2";
	}

	public List<word> createWords(String path)
	{
		List<word> words = new ArrayList<word>();
		try {
			File file = ResourceUtils.getFile(path);
			
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
			  String data = myReader.nextLine();
			  System.out.print(data.split("#")[1].split("\t")[0] + "    ");
			  System.out.println(data.split("#")[1].split("\t")[0].matches("[a-zA-Z1-9]+"));
				if(data.split("#")[1].split("\t")[0].matches("[a-zA-Z1-9]+"))
				{
					words.add(new word(data.split("#")[1].split("\t")[0], Float.parseFloat(data.split("\t")[1])));
				}
			}
			myReader.close();
		  } catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		  }

		System.out.println(words);
		return words;
	}


}

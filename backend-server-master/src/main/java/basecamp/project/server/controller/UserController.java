package basecamp.project.server.controller;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import basecamp.project.server.functions.NLP;


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
		word[] test;
		if(hashtag.replace(" ", "").equals("blm"))
		{
				test = new word[] { new word("Police", 80.5f), new word("Black", 25.6f), new word("Matters", 14.2f),
				new word("dumb", 8.7f), new word("hate", 4.7f), new word("America", 1.7f), new word("Trump", 1.4f),
				new word("Sad", 0.7f), new word("Black", 25.6f), new word("Matters", 14.2f), new word("dumb", 8.7f),
				new word("hate", 4.7f), new word("America", 1.7f), new word("Trump", 1.4f), new word("Sad", 0.7f) };
		}else{
			test = new word[] { new word("dumb",23.5f), new word("love", 95.6f), new word("Sad", 64.2f),
				new word("Happy", 67.7f), new word("Together", 2.7f), new word("kill", 9.7f), new word("hate", 17.4f)};
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

		System.out.println(sentiment/perc);
		sentiment /= perc;

		www.put("words",words);
		www.put("sentiment", sentiment);
		return www.toJSONString();
	}

	@RequestMapping("nrusers")
	String nrUsers() {
		return "2";
	}


}

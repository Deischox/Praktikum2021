package basecamp.project.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import basecamp.project.server.controller.word;
import basecamp.project.server.functions.MySQLconnect;
import basecamp.project.server.functions.analyser;

import java.io.File;
import java.io.InputStream;

@Controller
public class IndexController {

	@Value("${service.key}")
	private String key;

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(Model model) {

		String message = "Our model message. Hello.";
		model.addAttribute("message", message);

		model.addAttribute("key", key);

		return "index";
	}


	@RequestMapping(value = { "/hello" }, method = RequestMethod.GET)
	public String hellotest(Model model, @RequestParam(name="hashtag", defaultValue = "User")  String name) {

		word[] words;
		model.addAttribute("hashtag", name);
		if(name.toLowerCase().equals("blm"))
		{
			words = new word[] {new word("Police", 80.5f), new word("Black", 25.6f), new word("Matters", 14.2f), new word("Love", 8.7f),new word("hate", 4.7f),new word("America", 1.7f),
			new word("Trump", 1.4f),new word("Sad", 0.7f), new word("Black", 25.6f), new word("Matters", 14.2f), new word("Love", 8.7f),new word("hate", 4.7f),new word("America", 1.7f),
			new word("Trump", 1.4f),new word("Sad", 0.7f)};
		}else
		{
			words = new word[] {new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f),new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f)
		,new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f),new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f), new word("w", 31.0f)};
		}

		String s = analyser.getwords();
		model.addAttribute("words",words);

 		//TODO: SQL abfrage um alle Hashtags zu bekommen
		String[] hashs = {"BTS","GOT7","2020Mama", "InWonderWatchParty"};
		
		MySQLconnect sql = new MySQLconnect();



		model.addAttribute("hashs", hashs);

		return "hello";
	}


	@RequestMapping(value = { "/hello2" }, method = RequestMethod.GET)
	public String helloAgain(Model model, @RequestParam(name = "name", defaultValue = "User") String n) {

		model.addAttribute("name", n);

		return "data";
	}

	@RequestMapping(value = { "/info" }, method = RequestMethod.GET)
	public String helloAgain3(Model model, @RequestParam(name = "name", defaultValue = "User") String n) {

		model.addAttribute("name", n);

		return "test";
	}


}

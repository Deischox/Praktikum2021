package basecamp.project.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import basecamp.project.server.functions.MySQLconnect;

/**
* The Index Controller is responsible to return the html files and the Attributes for the page
*
* @author  Silas Ueberschaer
* @version 2.0
* @since   2021-03-27
*/
@Controller
public class IndexController {

	@Value("${service.key}")
	private String key;

	/**
	 * If the index page is called it redirects to /dashboard
	 * 
	 * @param model	The Request of the User
	 * @return		The Redirect to /dashboard
	 */
	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(Model model) {

		return "redirect:/dashboard";
	}


	/**
	 * The Mysql connection gets tested and the initial data gets created
	 * 
	 * @param model	The Request of the User
	 * @return		Returns the html page of /dashboard with the added model Attributes
	 */
	@RequestMapping(value = { "/dashboard" }, method = RequestMethod.GET)
	public String hellotest(Model model) {

		word[] words = new word[16];
		
		for(int i = 0; i < 16; i++)
		{
			words[i] = new word("",0f);
		}

		model.addAttribute("words",words);

		try
		{
			MySQLconnect sql = new MySQLconnect();
			model.addAttribute("hashs", sql.getRandomHashtag());
			model.addAttribute("sql", "Connected Successfully!");
		}catch(Exception e)
		{
			model.addAttribute("sql", "cant connect to mysql!");
			System.out.println(e);
		}
		
		return "hello";
	}
}

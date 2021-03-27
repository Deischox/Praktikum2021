package basecamp.project.server;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ServerApplication extends SpringBootServletInitializer {

	/**
	 * The main function to Start the Spring-Boot website
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}

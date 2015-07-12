package FileCatalog;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired(required = false)
	DatabaseLoader databaseLoader;

	@Autowired
	FileRepository fileRepository;

	@PostConstruct
	void logTheFiles() {
		log.info("\n\n\n\n");
		for (MyFile myFile : fileRepository.findAll()) {
			log.info(myFile.toString());
		}
		log.info("\n\n\n\n");
	}
}

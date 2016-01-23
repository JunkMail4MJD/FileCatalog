package FileCatalog;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
		log.info("\n\n\n\n file repository logging code");
		for (MyFile myFile : fileRepository.findAll()) {
			String myString = myFile.fileAttributes.get("name").toString();
			log.info("Found File : " + myString);
		}
		log.info("\n\n\n\n");
	}
}

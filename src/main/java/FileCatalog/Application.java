package FileCatalog;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@EnableAutoConfiguration
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private static Gson gson = new GsonBuilder().create();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Autowired(required = false)
	FileScanner fileScanner;

	@Autowired
	FileScanRepository fileScanRepository;
	

	@PostConstruct
	void logTheFiles() {
		int fileCount = 0;
		log.debug("---White Space\n\n\n\n file repository logging code");
		for (FileData fileData : fileScanRepository.findAll()) {
			fileCount ++;
//			log.debug("File data: " + gson.toJson( fileData ) );

		}
		log.debug("Files in MongoDB File Scan Collection: " + fileCount );
		log.debug("---White Space\n\n\n\n");
	}
}

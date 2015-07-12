package FileCatalog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.jar.Attributes;

@Service
@Profile("!production")
public class DatabaseLoader {

	private final FileRepository fileRepository;

	@Autowired
	public DatabaseLoader(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}

	@PostConstruct
	private void initDatabase() {

		FileSystem myFileSystem = FileSystems.getDefault();
		Iterable<Path> myPaths = myFileSystem.getRootDirectories();
		ArrayList<MyFile> myFileList = new ArrayList<MyFile>();

		try {
			for ( Path myPath : myPaths ){
				System.out.printf("\nGet Root Directories item : %s", myPath.toString());

				MyFile theFile = new MyFile();

				Map<String, ?> rootDirectoriesAttributes = Files.readAttributes(myPath, "*");

				Map<String, String> pathAttributes = new HashMap<String, String>();
				pathAttributes.put("Name", myPath.getRoot().toString());
				pathAttributes.put("Attributes", rootDirectoriesAttributes.toString());
				//pathAttributes.put("name", myPath.getFileName().toString());
				//pathAttributes.put("parent", myPath.getParent().toString());
				theFile.setFileAttributes(pathAttributes);
				myFileList.add(theFile);
			}

			DirectoryStream<Path> myDirectoryStream = Files.newDirectoryStream(Paths.get("/"), "*.*");
			for (Path p : myDirectoryStream) {

				MyFile theFile = new MyFile();

				Map<String, ?> directoryStreamAttributes = Files.readAttributes(p, "*");
				Map<String, String> pathAttributes = new HashMap<String, String>();
				pathAttributes.put("root", p.getRoot().toString());
				pathAttributes.put("name", p.getFileName().toString());
				pathAttributes.put("parent", p.getParent().toString());
				pathAttributes.put("Attributes", directoryStreamAttributes.toString());
				theFile.setFileAttributes(pathAttributes);
				myFileList.add(theFile);

				if (Files.isDirectory(p) )
					System.out.printf("\nDirectory Stream Directory : %s\n", p.toString());
				else if (Files.isRegularFile(p))
					System.out.printf("\nDirectory Stream Regular File : %s, Size : %d\n", p.toString(), Files.size(p));
				else if (Files.isExecutable(p))
					System.out.printf("\nDirectory Stream Executable File : %s, Size : %d\n", p.toString(), Files.size(p));
			}
		}
		catch ( Exception ex) {
			ex.printStackTrace();
		}

		fileRepository.save(myFileList);

	}
}
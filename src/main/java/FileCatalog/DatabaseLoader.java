package FileCatalog;

import javax.annotation.PostConstruct;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.*;


@Service
@Profile("!production")
public class DatabaseLoader {

	private final FileRepository fileRepository;

	@Autowired
	public DatabaseLoader(FileRepository fileRepository) {
		this.fileRepository = fileRepository;
	}


	private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);

	private static Gson gson = new GsonBuilder().create();

	public class PrintFiles extends SimpleFileVisitor<Path> {

		// Print information about
		// each type of file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws java.io.IOException {

			log.info("Visiting File : " + file.toString()   );
			log.info("  File Attributes:  " + gson.toJson( attr ) );

			if (attr.isSymbolicLink()) {
				System.out.format("Symbolic link: %s ", file);
			} else if (attr.isRegularFile()) {
				System.out.format("Regular file: %s ", file);
			} else {
				System.out.format("Other: %s ", file);
			}
			System.out.println("(" + attr.size() + "bytes)");

			try {
				MyFile theFile = new MyFile();
				Map attributes = Files.readAttributes(file, "posix:*");
				Map pathAttributes = new HashMap();

				pathAttributes.put("root", file.getRoot().toString());
				pathAttributes.put("name", file.getFileName().toString());
				pathAttributes.put("parent", file.getParent().toString());
				pathAttributes.put("crawled", false);
				pathAttributes.put("isHashed", false);

				Iterator it = attributes.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					if (pair.getKey().equals("lastAccessTime")   ||
						pair.getKey().equals("lastModifiedTime") ||
						pair.getKey().equals("creationTime") ) {
						pathAttributes.put(pair.getKey(), pair.getValue().toString());
					}
					else if ( pair.getKey().equals("owner") ||
							  pair.getKey().equals("group")){
						pathAttributes.put(pair.getKey(), pair.getValue().toString());
					}
					else if ( !pair.getKey().equals("fileKey")) {
						pathAttributes.put(pair.getKey(), pair.getValue());
					}
				}
				theFile.fileAttributes = pathAttributes;
				DatabaseLoader.this.fileRepository.save(theFile);
			}
			catch ( Exception ex) {
				ex.printStackTrace();
			}
			return CONTINUE;
		}

		// Print each directory visited.
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
			System.out.format("Directory: %s%n", dir);
			return CONTINUE;
		}

		// If there is some error accessing the file, let the user know.
		// If you don't override this method and an error occurs, an IOException is thrown.
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
			System.err.println(exc);
			return CONTINUE;
		}
	}







	@PostConstruct
	private void initDatabase() {
//		FileSystem myFileSystem = FileSystems.getDefault();
//		Iterable<Path> myPaths = myFileSystem.getRootDirectories();
		ArrayList<MyFile> myFileList = new ArrayList<MyFile>();
//		ArrayList<Map> myFileList = new ArrayList<Map>();
		try {
/*			for ( Path myPath : myPaths ){
				System.out.printf("\nGet Root Directories item : %s", myPath.toString());

//				MyFile theFile = new MyFile();

				Map<String, ?> rootDirectoriesAttributes = Files.readAttributes(myPath, "*");

				Map<String, String> pathAttributes = new HashMap<String, String>();
				pathAttributes.put("Name", myPath.getRoot().toString());
				pathAttributes.put("Attributes", rootDirectoriesAttributes.toString());
				//pathAttributes.put("name", myPath.getFileName().toString());
				//pathAttributes.put("parent", myPath.getParent().toString());
//				theFile.setName(myPath.getRoot().toString());
//				theFile.setAttributes(rootDirectoriesAttributes.toString());
//				myFileList.add(theFile);
			}
*/

			Path startingDir = Paths.get("/WS");
			PrintFiles pf = new PrintFiles();
			Files.walkFileTree(startingDir, pf);


			DirectoryStream<Path> myDirectoryStream = Files.newDirectoryStream(Paths.get("/"), "*.*");
			for (Path p : myDirectoryStream) {

				MyFile theFile = new MyFile();
//				Map theFile = new HashMap();

//				Map attributes = Files.readAttributes(p, "*");
				Map attributes = Files.readAttributes(p, "posix:*");
				Map pathAttributes = new HashMap();

				pathAttributes.put("root", p.getRoot().toString());
				pathAttributes.put("name", p.getFileName().toString());
				pathAttributes.put("parent", p.getParent().toString());
				pathAttributes.put("crawled", false);
				pathAttributes.put("isHashed", false);

				Iterator it = attributes.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					if (pair.getKey().equals("lastAccessTime")   ||
							 pair.getKey().equals("lastModifiedTime") ||
							 pair.getKey().equals("creationTime") ) {
						pathAttributes.put(pair.getKey(), pair.getValue().toString());
						}
					else if ( pair.getKey().equals("owner") ||
							  pair.getKey().equals("group")){
//						System.out.println(pair.getValue().toString());
						pathAttributes.put(pair.getKey(), pair.getValue().toString());

					}
					else if ( !pair.getKey().equals("fileKey")) {
						pathAttributes.put(pair.getKey(), pair.getValue());
					}
//					theFile.fileAttributes.put(pair.getKey(), pair.getValue());
				}
//				pathAttributes.put( "root", p.getRoot().toString());
//				pathAttributes.put("name", p.getFileName().toString());
//				pathAttributes.put("parent", p.getParent().toString());
//				pathAttributes.put("Attributes", directoryStreamAttributes.toString());
//				theFile.setName(p.getFileName().toString());
//				theFile.setAttributes(directoryStreamAttributes.toString());
				theFile.fileAttributes = pathAttributes;
				myFileList.add(theFile);
			}
		}
		catch ( Exception ex) {
			ex.printStackTrace();
		}
		fileRepository.save(myFileList);
	}
}

//*********************************************************************************************************************


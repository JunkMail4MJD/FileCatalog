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
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.file.FileVisitResult.*;


@Service
@Profile("!production")
public class FileScanner {

	private final FileScanRepository fileScanRepository;
	private static final Logger log = LoggerFactory.getLogger(FileScanner.class);
	private static Gson gson = new GsonBuilder().create();
	private static final String volumeID = "1";
	private String scanTime;
	private int fileCount;
	LocalDateTime startTS;
	LocalDateTime endTS;
	ArrayList<FileData> theFileList = new ArrayList<FileData>();

	@Autowired
	public FileScanner(FileScanRepository fileScanRepository) {
		this.fileScanRepository = fileScanRepository;
	}

	public class theFileVisitor extends SimpleFileVisitor<Path> {

		// Print information about
		// each type of file.
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws java.io.IOException {

			fileCount ++;

			try {
				FileData theFile = new FileData();
				theFile.setVolumeId( volumeID );
				theFile.setScanId( volumeID + "_" + scanTime);
				theFile.setScanTime(  scanTime );
				theFile.setRoot( file.getRoot().toString() );
				theFile.setFileName( file.getFileName().toString() );
				theFile.setParent( file.getParent().toString() );

				theFile.setCreationTime(     attr.creationTime().toString() );
				theFile.setLastAccessTime(   attr.lastAccessTime().toString() );
				theFile.setLastModifiedTime( attr.lastModifiedTime().toString() );

				theFile.setSize( attr.size() );

				theFile.setFileKey( attr.fileKey().toString() );
				theFile.setDirectory( attr.isDirectory() );
				theFile.setRegularFile( attr.isRegularFile() );
				theFile.setSymbolicLink( attr.isSymbolicLink() );
				theFile.setHashed( false);

				theFile = FileScanner.this.fileScanRepository.save(theFile);
				theFileList.add( theFile );
				log.debug("  File Attributes:  " + gson.toJson( theFile ) );
			}
			catch ( Exception ex) {
				ex.printStackTrace();
			}
			return CONTINUE;
		}

		// Print each directory visited.
		@Override
		public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
//			System.out.format("Directory: %s%n", dir);
			return CONTINUE;
		}

		// If there is some error accessing the file, let the user know.
		// If you don't override this method and an error occurs, an IOException is thrown.
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException exc) {
//			System.err.println(exc);
			return CONTINUE;
		}
	}

	private void printStartTime() {
		startTS = LocalDateTime.now();
		log.debug( "********* Start Time: " + startTS.toString() );
	}

	private void printEndTimeAndDuration() {
		endTS = LocalDateTime.now();
		Long diffInMilli = java.time.Duration.between(startTS, endTS).toMillis();
		Double timetoScanD = ( diffInMilli.doubleValue() / 1000.0);
		String timeToScan = timetoScanD.toString();
		log.debug( "********* Duration in seconds : " + timeToScan );
	}

	@PostConstruct
	public void scanDirectoryTree( ) {
//	private void scanDirectoryTree( Path theDirectory, FileScanRepository fileScanRepository) {
		ZonedDateTime zdt = ZonedDateTime.now();
		scanTime = zdt.format( DateTimeFormatter.ISO_OFFSET_DATE_TIME );
		fileCount = 0;
		try {

			theFileList.clear();
			//******************************************************
			printStartTime();
			//******************************************************

			Path startingDir = Paths.get("/Volumes/Seagate Backup Plus Drive");
			theFileVisitor visitor = new theFileVisitor();
			Files.walkFileTree(startingDir, visitor);
			//Files.walkFileTree( theDirectory, visitor);

			//******************************************************
			printEndTimeAndDuration();
			//******************************************************

			calculateHashes();



		}
		catch ( Exception ex) {
			ex.printStackTrace();
		}
	}

	public void calculateHashes() {


		HashUtility hashUtility = new HashUtility();

		int fileCounter = 0, totalFiles = theFileList.size();
		long currentSize = 0, totalSize = 0;

		MathContext mc = new MathContext( 6 );
		BigDecimal fileCountTotal = new BigDecimal( totalFiles );
		BigDecimal gigaByte_BD = new BigDecimal( 1024 * 1024 *1024 );
		for ( FileData file: theFileList ) {
			totalSize += file.getSize();
		}
		BigDecimal fileSizeTotal = new BigDecimal( totalSize );
		fileSizeTotal = fileSizeTotal.divide( gigaByte_BD, mc );


		for ( FileData file : theFileList) {

			log.info("************************************************************************************************************* ");
			log.info("File :  " + file.getParent() + "/" + file.getFileName() );

			//******************************************************
			printStartTime();
			//******************************************************
			try {
				file = hashUtility.calculateFileHash("SHA-256", file);
				file = FileScanner.this.fileScanRepository.save( file );
			}
			catch ( Exception ex) {
				ex.printStackTrace();
			}

			//******************************************************
			printEndTimeAndDuration();
			//******************************************************


			// Count Progress
			fileCounter ++;
			BigDecimal fileCountProgress = new BigDecimal( fileCounter );
			fileCountProgress = fileCountProgress.divide( fileCountTotal, mc );
			fileCountProgress = fileCountProgress.multiply( new BigDecimal( 100 ), mc );

			// Size Progress
			currentSize += file.getSize();
			BigDecimal fileSizeProgress = new BigDecimal( currentSize );
			fileSizeProgress = fileSizeProgress.divide( new BigDecimal( totalSize ), mc );
			fileSizeProgress = fileSizeProgress.multiply( new BigDecimal( 100 ), mc );

			BigDecimal currentSize_BD   = new BigDecimal( currentSize );
			currentSize_BD = currentSize_BD.divide( gigaByte_BD , mc );


			log.info("Overall Progress : " + fileCounter + " of " + totalFiles + " Total Files --- "  + fileCountProgress.toString() + " Percent Complete" );
			log.info("Overall Progress : " + currentSize_BD.toString() + " GB of " + fileSizeTotal.toString() + " Total GB --- " + fileSizeProgress.toString() + " Percent Complete");
			log.info("************************************************************************************************************* ");

		}


	log.info(" Finished! ");



	}

}
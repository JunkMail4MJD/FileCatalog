package FileCatalog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Created by xby099 on 1/30/16.
 *
 *
 * HASH Utility for calculating file hash
 */
public class HashUtility {

    static final int KILO_BYTE = 1024;
    static final int MEGA_BYTE = 1024 * KILO_BYTE;
    static final int CHUNK_SIZE = 250 * MEGA_BYTE;

    private static final Logger log = LoggerFactory.getLogger(FileScanner.class);
    private static Gson gson = new GsonBuilder().create();


    public FileData calculateFileHash( String algorithm, FileData theFile ) throws NoSuchAlgorithmException, IOException {


        Long size = theFile.getSize();
        if ( size > 0 ) {
            MessageDigest fileMd = MessageDigest.getInstance(algorithm);
            FileInputStream theFileInputStream = new FileInputStream(theFile.getParent() + "/" + theFile.getFileName());
            byte[] fileChunk = new byte[250 * MEGA_BYTE];
            HashMap<String, String> hashOfChunks = new HashMap<String, String>();
            int bytesRead;
            Integer chunkCount = 0;
            Long totalChunks = (size / CHUNK_SIZE) + 1;
            while ((bytesRead = theFileInputStream.read(fileChunk)) != -1) {
                chunkCount++;
                fileMd.update(fileChunk, 0, bytesRead);
                MessageDigest chunkMd = MessageDigest.getInstance(algorithm);
                chunkMd.update(fileChunk, 0, bytesRead);
                hashOfChunks.put("Chunk_" + chunkCount, DatatypeConverter.printHexBinary(chunkMd.digest()));
                log.debug("File Chunk Hashes:  " + gson.toJson(hashOfChunks));
                log.info("File Chunk Progress : " + chunkCount + " of " + totalChunks);
            }
            theFile.setHashed(true);
            theFile.setHashCalculatedTime(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) );
            theFile.setHashOfChucks(hashOfChunks);
            theFile.setSha256Hash(DatatypeConverter.printHexBinary(fileMd.digest()));
            log.debug("Updated File Info:  " + gson.toJson(theFile));
        }


        return theFile;
    }

}

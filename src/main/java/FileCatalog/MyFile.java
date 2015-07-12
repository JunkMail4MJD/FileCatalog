package FileCatalog;

import org.springframework.data.annotation.Id;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Created by Admin on 7/11/2015.
 */
public class MyFile {

    @Id
    private String id;

    Map<String, ?> fileAttributes;
    Boolean crawled = false;
    Boolean hashed = false;
    String hashCodeHex ="00";

    public Boolean getCrawled() {
        return crawled;
    }

    public void setCrawled(Boolean crawled) {
        this.crawled = crawled;
    }


    public Map<String, ?> getFileAttributes() {
        return fileAttributes;
    }

    public void setFileAttributes(Map<String, ?> fileAttributes) {
        this.fileAttributes = fileAttributes;
    }

    public Boolean getHashed() {
        return hashed;
    }

    public void setHashed(Boolean hashed) {
        this.hashed = hashed;
    }

    public String getHashCodeHex() {
        return hashCodeHex;
    }

    public void setHashCodeHex(String hashCodeHex) {
        this.hashCodeHex = hashCodeHex;
    }
}

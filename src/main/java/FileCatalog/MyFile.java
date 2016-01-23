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

    Map fileAttributes;

    public Map getFileAttributes() {
        return fileAttributes;
    }

    public void setFileAttributes(Map fileAttributes) {
        this.fileAttributes = fileAttributes;
    }
}

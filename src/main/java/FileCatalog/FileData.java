package FileCatalog;

import org.springframework.data.annotation.Id;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 7/11/2015.
 *
 * model object for directory scanner
 */
public class FileData {

    @Id
    private String id;

    private String scanId;

    private String scanTime;

    private String volumeId;

    private String root;
    private String fileName;
    private String parent;

    private String creationTime;
    private String lastAccessTime;
    private String lastModifiedTime;
    private long size;

    private String fileKey;
    private boolean isDirectory;
    private boolean isRegularFile;
    private boolean isSymbolicLink;
    private boolean isHashed;
    private String sha256Hash;
    private String hashCalculatedTime;
    private HashMap hashOfChucks;



    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getScanTime() {
        return scanTime;
    }

    public void setScanTime(String scanTime) {
        this.scanTime = scanTime;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(String lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean isRegularFile() {
        return isRegularFile;
    }

    public void setRegularFile(boolean regularFile) {
        isRegularFile = regularFile;
    }

    public boolean isSymbolicLink() {
        return isSymbolicLink;
    }

    public void setSymbolicLink(boolean symbolicLink) {
        isSymbolicLink = symbolicLink;
    }

    public boolean isHashed() {
        return isHashed;
    }

    public void setHashed(boolean hashed) {
        isHashed = hashed;
    }

    public String getSha256Hash() {
        return sha256Hash;
    }

    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    public String getHashCalculatedTime() {
        return hashCalculatedTime;
    }

    public void setHashCalculatedTime(String hashCalculatedTime) {
        this.hashCalculatedTime = hashCalculatedTime;
    }


    public HashMap getHashOfChucks() {
        return hashOfChucks;
    }

    public void setHashOfChucks(HashMap hashOfChucks) {
        this.hashOfChucks = hashOfChucks;
    }
}

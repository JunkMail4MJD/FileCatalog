package FileCatalog;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileScanRepository extends MongoRepository<FileData, String> {


    List<FileData> findByScanId( String scanId);
    List<FileData> findByVolumeId( String volumeID);
    List<FileData> findByScanTime( String scanTime);


}


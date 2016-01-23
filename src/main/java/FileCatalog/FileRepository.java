package FileCatalog;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Map;

public interface FileRepository extends MongoRepository<MyFile, String> {

}


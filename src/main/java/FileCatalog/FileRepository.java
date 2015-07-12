package FileCatalog;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<MyFile, String> {

}


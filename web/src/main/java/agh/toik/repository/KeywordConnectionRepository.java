package agh.toik.repository;

import agh.toik.model.KeywordConnection;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public interface KeywordConnectionRepository extends MongoRepository<KeywordConnection, String> {
}

package agh.toik.repository;

import agh.toik.model.Keyword;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public interface KeywordRepository extends MongoRepository<Keyword, String> {
    Keyword findByValue(String secondKeyword);
}

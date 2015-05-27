package twitter.rest;

import agh.toik.model.Keyword;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public class KeywordsRepository extends GenericRepository<Keyword> {

    public static final BasicDBObject UPDATE_OCCURRENCES_QUERY = new BasicDBObject("$push", new BasicDBObject("occurrences", new Date()));
    private DBCollection keywordCollection;

    public KeywordsRepository() {
        try {
            this.keywordCollection = keywordsCollection();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    private static DBCollection keywordsCollection() throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("polpc00860", 27017);
        DB db = mongoClient.getDB("keywords");
        return db.getCollection("keyword");
    }

    public List<Keyword> getAll() {
        return this.findByWeb("http://localhost:8080/keywords", map -> new Keyword(map.get("value")));
    }

    public void addKeywordOccurrence(List<String> keywords) {
        keywords.forEach(keyword -> {
            DBObject searchQuery = new BasicDBObject("value", keyword);
            keywordCollection.update(searchQuery, UPDATE_OCCURRENCES_QUERY);
        });
    }
}

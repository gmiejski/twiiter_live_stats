package twitter.rest;

import agh.toik.model.KeywordConnection;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordsConnectionsRepository extends GenericRepository<KeywordConnection> {

    private DBCollection keywordCollection;

    public KeywordsConnectionsRepository() {
        try {
            DB db = new MongoClient("localhost", 27017).getDB("keywords");
            keywordCollection = db.getCollection("keywordConnection");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public List<KeywordConnection> getAll() {
        Function<LinkedHashMap<String, String>, KeywordConnection> mapping = map ->
                new KeywordConnection(map.get("firstKeyword"), map.get("secondKeyword"));
        return this.findByWeb("http://localhost:8080/keywords/connections", mapping);
    }

    public void incrementCount(KeywordConnection keywordConnection) {
        DBObject modifier = new BasicDBObject("totalCount", 1);
        DBObject incQuery = new BasicDBObject("$inc", modifier);

        Map<String, String> searchQueryArguments = new HashMap<>();
        searchQueryArguments.put("firstKeyword", keywordConnection.getFirstKeyword());
        searchQueryArguments.put("secondKeyword", keywordConnection.getSecondKeyword());
        DBObject searchQuery = new BasicDBObject(searchQueryArguments);
        keywordCollection.update(searchQuery, incQuery);
    }
}

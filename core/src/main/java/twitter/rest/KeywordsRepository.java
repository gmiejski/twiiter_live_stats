package twitter.rest;

import agh.toik.model.Keyword;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public class KeywordsRepository extends GenericRepository<Keyword> {

    public KeywordsRepository() {
    }

    public static void main(String[] args) {
        new KeywordsRepository().getAll().forEach(System.out::println);

//        try {
//            DBCollection keyword = dbCollection("keyword");
//            DBCursor dbCursor = keyword.find();
//            List<Keyword> keywords = StreamSupport.stream(dbCursor.spliterator(), false)
//                    .map(KeywordsRepository::getKeyword)
//                    .collect(toList());
//
//            keywords.forEach(System.out::println);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    }

    private static Keyword getKeyword(DBObject dbObject) {
        return new Keyword((String) dbObject.get("value"));
    }

    private static DBCollection dbCollection(String collectionName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("polpc00860", 27017);
        DB db = mongoClient.getDB("keywords");
        return db.getCollection(collectionName);
    }

    public List<Keyword> findByDatabase() {
        List<Keyword> keywords = new ArrayList<>();
        try {
            DBCollection keyword = dbCollection("keyword");

            DBCursor dbCursor = keyword.find();
            for (DBObject dbObject : dbCursor) {
                String value = (String) dbObject.get("value");
                keywords.add(new Keyword(value));
            }

            keywords.forEach(System.out::println);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return keywords;
    }

    public List<Keyword> getAll() {
        return this.findByWeb("http://localhost:8080/keywords", map -> new Keyword(map.get("value")));
    }
}

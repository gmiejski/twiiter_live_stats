package twitter.rest;

import agh.toik.model.Keyword;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public class KeywordsRepository {

    public KeywordsRepository() {
    }

    public static void main(String[] args) {

        try {
            DBCollection keyword = dbCollection("keyword");
            DBCursor dbCursor = keyword.find();
            List<Keyword> keywords = StreamSupport.stream(dbCursor.spliterator(), false)
                    .map(KeywordsRepository::getKeyword)
                    .collect(toList());

            keywords.forEach(System.out::println);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static Keyword getKeyword(DBObject dbObject) {
        return new Keyword((String) dbObject.get("value"));
    }

    private static DBCollection dbCollection(String collectionName) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("polpc00860", 27017);
        DB db = mongoClient.getDB("keywords");
        return db.getCollection(collectionName);
    }

    public List<Keyword> find() {
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
}

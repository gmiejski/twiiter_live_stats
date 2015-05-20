package twitter.rest;

import agh.toik.model.Keyword;
import com.mongodb.*;
import org.springframework.web.client.RestTemplate;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
        RestTemplate restTemplate = new RestTemplate();
        List<LinkedHashMap<String, String>> listResponseEntity = restTemplate.getForObject("http://localhost:8080/keywords", List.class);

        List<Keyword> keywords2 = listResponseEntity.stream().map(x -> new Keyword(x.get("value"))).collect(toList());

        for (Keyword keyword : keywords2) {
            System.out.println(keyword.getValue());
        }

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

    public List<Keyword> findByWeb() {
        RestTemplate restTemplate = new RestTemplate();
        List<LinkedHashMap<String, String>> listResponseEntity = restTemplate.getForObject("http://localhost:8080/keywords", List.class);

        return listResponseEntity.stream().map(x -> new Keyword(x.get("value"))).collect(toList());
    }

}

package twitter.rest;

import agh.toik.model.KeywordConnection;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordsConnectionsRepository extends GenericRepository<KeywordConnection> {

    @Override
    public List<KeywordConnection> getAll() {
        Function<LinkedHashMap<String, String>, KeywordConnection> mapping = map ->
                new KeywordConnection(map.get("firstKeyword"), map.get("secondKeyword"));
        return this.findByWeb("http://localhost:8080/keywords/connections", mapping);
    }
}

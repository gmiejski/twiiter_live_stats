package twitter.storm.bolt.cache.api;

import agh.toik.model.Keyword;

import java.util.List;

/**
 * Created by grzegorz.miejski on 27/05/15.
 */
public interface KeywordCache extends RepositoryCache<Keyword> {
    List<String> retrieveKeywords();
}

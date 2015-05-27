package twitter.storm.bolt.cache.api;

import java.util.List;

/**
 * Created by grzegorz.miejski on 27/05/15.
 */
public interface RepositoryCache<T> {
    List<T> retrieve();
}

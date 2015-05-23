package twitter.storm.bolt.connections.cache;

import agh.toik.model.KeywordConnection;

import java.util.List;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public interface KeywordsConnectionsCache {

    List<KeywordConnection> getKeywordsConnections();

}

package twitter.storm.bolt.connections.cache;

import agh.toik.model.KeywordConnection;
import org.springframework.web.client.RestClientException;
import twitter.rest.KeywordsConnectionsRepository;

import java.util.*;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class AutoUpdateKeywordsConnectionsCache implements KeywordsConnectionsCache {

    private final KeywordsConnectionsRepository keywordsConnectionsRepository;
    private List<KeywordConnection> cachedConnections;

    public AutoUpdateKeywordsConnectionsCache() {
        this.keywordsConnectionsRepository = new KeywordsConnectionsRepository();
    }

    public void initiate() {

        updateCachedConnections();
        scheduleAutoUpdate();
    }

    private void scheduleAutoUpdate() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (keywordsConnectionsRepository) {
                    System.out.println("******************************************************");
                    System.out.println("Updating keywords connections");
                    System.out.println("******************************************************");
                    cachedConnections = keywordsConnectionsRepository.getAll();
                    System.out.println(Arrays.toString(cachedConnections.toArray()));
                    System.out.println("******************************************************");
                }
            }
        }, 5000, 5000);
    }

    private void updateCachedConnections() {
        List<KeywordConnection> retrievedKeywords;
        try {
            synchronized (keywordsConnectionsRepository) {
                retrievedKeywords = keywordsConnectionsRepository.getAll();
            }
        } catch (RestClientException restClientException) {
            System.out.println("ERROR! CANNOT RETRIEVE keywords!");
            throw new IllegalStateException(restClientException.getMessage());
        }
        this.cachedConnections = retrievedKeywords;
    }

    @Override
    public List<KeywordConnection> getKeywordsConnections() {
        synchronized (keywordsConnectionsRepository) {
            return new ArrayList<>(this.cachedConnections);
        }
    }
}

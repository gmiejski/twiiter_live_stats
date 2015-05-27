package twitter.storm.bolt.cache.impl;

import agh.toik.model.Keyword;
import org.springframework.web.client.RestClientException;
import twitter.rest.KeywordsRepository;
import twitter.storm.bolt.cache.api.KeywordCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class AutoUpdateKeywordsCache implements KeywordCache {

    private static final int CACHE_REFRESH_TIME = 5000;
    private final KeywordsRepository keywordsRepository;
    private List<Keyword> cachedKeywords;

    public AutoUpdateKeywordsCache() {
        this.keywordsRepository = new KeywordsRepository();
        initiate();
    }

    public void initiate() {
        updateCachedConnections();
        scheduleAutoUpdate();
    }

    private void scheduleAutoUpdate() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (keywordsRepository) {
//                    System.out.println("******************************************************");
//                    System.out.println("Updating keywords");
//                    System.out.println("******************************************************");
                    cachedKeywords = keywordsRepository.getAll();
//                    System.out.println(Arrays.toString(cachedKeywords.toArray()));
//                    System.out.println("******************************************************");
                }
            }
        }, CACHE_REFRESH_TIME, CACHE_REFRESH_TIME);
    }

    private void updateCachedConnections() {
        List<Keyword> retrievedKeywords;
        try {
            synchronized (keywordsRepository) {
                retrievedKeywords = keywordsRepository.getAll();
            }
        } catch (RestClientException restClientException) {
            System.out.println("ERROR! CANNOT RETRIEVE keywords!");
            throw new IllegalStateException(restClientException.getMessage());
        }
        this.cachedKeywords = retrievedKeywords;
    }

    @Override
    public List<Keyword> retrieve() {
        synchronized (keywordsRepository) {
            return new ArrayList<>(this.cachedKeywords);
        }
    }

    @Override
    public List<String> retrieveKeywords() {
        synchronized (keywordsRepository) {
            return this.cachedKeywords.stream().map(Keyword::getValue).collect(Collectors.toList());
        }
    }
}

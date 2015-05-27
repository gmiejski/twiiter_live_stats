package twitter;

import twitter.storm.bolt.cache.api.KeywordCache;
import twitter.storm.bolt.cache.impl.AutoUpdateKeywordsCache;
import twitter4j.*;

import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by grzmiejski on 4/26/15.
 */
public class Twitter4jReader {
    public static final int KEYWORDS_CACHE_REFRESH_TIME = 5000;
    private final Queue<Status> statuses;
    private List<String> keywords;
    private TwitterStream twitterStream;
    private KeywordCache keywordCache;

    public Twitter4jReader(Queue<Status> statuses) {
        this.statuses = statuses;
        this.keywordCache = new AutoUpdateKeywordsCache();
    }

    public void startStreaming() {
        List<String> keywords = keywordCache.retrieveKeywords();
        synchronized (statuses) {
            startReadingTweets(keywords);
            cacheKeywords(keywords);
            setRepositoryWatcher();
        }
    }

    private void startReadingTweets(List<String> keywords) {
        if (twitterStream != null) {
            twitterStream.shutdown();
        }

        twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(new StatusAdapter() {
            public void onStatus(Status status) {
                statuses.offer(status);
            }
        });

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.language(new String[]{"en"});
        tweetFilterQuery.track(keywords.toArray(new String[keywords.size()]));

        keywords.forEach(System.out::println);
        twitterStream.filter(tweetFilterQuery);
    }

    private void setRepositoryWatcher() {
        new Timer()
                .schedule(new TimerTask() {
                    @Override
                    public void run() {
                        synchronized (statuses) {
                            List<String> newPossibleKeywords = keywordCache.retrieveKeywords();
                            if (shouldReplaceKeywords(newPossibleKeywords, cachedKeywords())) {
                                System.out.println("New Keywords. Updating keywords list: ");
                                startReadingTweets(newPossibleKeywords);
                                cacheKeywords(newPossibleKeywords);
                            }
                        }
                    }
                }, KEYWORDS_CACHE_REFRESH_TIME, KEYWORDS_CACHE_REFRESH_TIME);
    }

    private boolean shouldReplaceKeywords(List<String> newPossibleKeywords, List<String> cachedKeywords) {
        return newPossibleKeywords.size() != cachedKeywords.size() || !newPossibleKeywords.containsAll(cachedKeywords);
    }

    private List<String> cachedKeywords() {
        return this.keywords;
    }

    private void cacheKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void shutdown() {
        if (twitterStream != null) {
            twitterStream.shutdown();
        }
    }
}
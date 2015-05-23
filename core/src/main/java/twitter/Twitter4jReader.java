package twitter;

import agh.toik.model.Keyword;
import twitter.rest.KeywordsRepository;
import twitter4j.*;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * Created by grzmiejski on 4/26/15.
 */
public class Twitter4jReader {
    public static final int KEYWORDS_CACHE_REFRESH_TIME = 5000;
    private final Queue<Status> statuses;
    private final KeywordsRepository keywordsRepository;
    private List<String> keywords;
    private TwitterStream twitterStream;

    public Twitter4jReader(Queue<Status> statuses, List<String> keywords, KeywordsRepository keywordsRepository) {
        this.statuses = statuses;
        this.keywords = keywords;
        this.keywordsRepository = keywordsRepository;
    }

    public void startStreaming() {
        List<String> keywords = getKeywordsFromRepository();

        synchronized (statuses) {
            startReadingTweets(keywords);
            cacheKeywords(keywords);
            setRepositoryWatcher();
        }
    }

    private List<String> getKeywordsFromRepository() {
        return keywordsRepository.findByWeb()
                .stream()
                .map(Keyword::getValue)
                .collect(Collectors.toList());
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
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(KEYWORDS_CACHE_REFRESH_TIME);
                    System.out.println("******************************************************");
                    System.out.println("checking if new keywords appeared");
                    System.out.println("******************************************************");
                    synchronized (statuses) {
                        List<String> newPossibleKeywords = getKeywordsFromRepository();
                        if (shouldReplaceKeywords(newPossibleKeywords, cachedKeywords())) {
                            System.out.println("New Keywords. Updating keywords list: ");
                            startReadingTweets(newPossibleKeywords);
                            cacheKeywords(newPossibleKeywords);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
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
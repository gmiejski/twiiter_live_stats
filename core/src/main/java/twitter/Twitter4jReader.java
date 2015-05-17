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
    private final Queue<Status> statuses;
    private final List<String> keywords;
    private final KeywordsRepository keywordsRepository;
    private TwitterStream twitterStream;

    public Twitter4jReader(Queue<Status> statuses, List<String> keywords, KeywordsRepository keywordsRepository) {
        this.statuses = statuses;
        this.keywords = keywords;
        this.keywordsRepository = keywordsRepository;
    }

    public void startStreaming() {
        twitterStream = new TwitterStreamFactory().getInstance();

        twitterStream.addListener(new StatusListener() {
            public void onStatus(Status status) {
                statuses.offer(status);
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            public void onTrackLimitationNotice(int i) {

            }

            public void onScrubGeo(long l, long l1) {

            }

            public void onStallWarning(StallWarning stallWarning) {

            }

            public void onException(Exception e) {

            }
        });

        FilterQuery tweetFilterQuery = new FilterQuery();
        tweetFilterQuery.language(new String[]{"en"});

        if (keywords != null && !keywords.isEmpty()) {
            tweetFilterQuery.track((String[]) keywords.toArray());
        } else {
            List<String> keywords = keywordsRepository.find()
                    .stream()
                    .map(Keyword::getValue)
                    .collect(Collectors.toList());
            tweetFilterQuery.track(keywords.toArray(new String[keywords.size()]));

            keywords.forEach(System.out::println);
        }
        twitterStream.filter(tweetFilterQuery);
    }

    public void shutdown() {
        twitterStream.shutdown();
    }

}
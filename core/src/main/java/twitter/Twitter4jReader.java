package twitter;

import twitter4j.*;

import java.util.List;
import java.util.Queue;

/**
 * Created by grzmiejski on 4/26/15.
 */
public class Twitter4jReader {
    private final Queue<Status> statuses;
    private final List<String> keywords;
    private TwitterStream twitterStream;

    public Twitter4jReader(Queue<Status> statuses, List<String> keywords) {
        this.statuses = statuses;
        this.keywords = keywords;
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
        }
        twitterStream.filter(tweetFilterQuery);
    }

    public void shutdown() {
        twitterStream.shutdown();
    }

}
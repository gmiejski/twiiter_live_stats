package twitter.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import camel.RouteStarter;
import twitter.storm.bolt.connections.KeywordConnectionsBolt;
import twitter.storm.bolt.keywords.TweetBolt;
import twitter.storm.bolt.keywords.TweetFilterBolt;
import twitter.storm.bolt.keywords.websocket.KeywordsWebSocketBolt;
import twitter.storm.bolt.repository.KeywordsConnectionsDBBolt;
import twitter.storm.bolt.repository.KeywordsDBBolt;
import twitter.storm.spout.TweetSpout;

/**
 * Created by grzmiejski on 4/26/15.
 */
public class TwitterStreamTopology {

    public static final String TWEET_SPOUT = "tweetSpout";
    public static final String TWEET_FILTER = "tweetFilter";

    public static void main(String[] args) {
        RouteStarter.buildRoute();

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(TWEET_SPOUT, new TweetSpout());

        // keywords
        builder.setBolt(TWEET_FILTER, new TweetFilterBolt(), 1).shuffleGrouping(TWEET_SPOUT);

        builder.setBolt("tweet", new TweetBolt(), 1).shuffleGrouping(TWEET_FILTER);
        builder.setBolt("websocket", new KeywordsWebSocketBolt(), 2).shuffleGrouping(TWEET_FILTER);
        builder.setBolt("tweetKeywordsOccurrencesUpdater", new KeywordsDBBolt(), 1).shuffleGrouping(TWEET_FILTER);

        // keywords connections
        builder.setBolt("keywordsConnection", new KeywordConnectionsBolt(), 1).shuffleGrouping(TWEET_SPOUT);
        builder.setBolt("keywordsConnectionUpdater", new KeywordsConnectionsDBBolt(), 1).shuffleGrouping("keywordsConnection");

        Config conf = new Config();
        conf.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());
    }
}

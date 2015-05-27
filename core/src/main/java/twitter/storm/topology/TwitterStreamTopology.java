package twitter.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import camel.RouteStarter;
import twitter.storm.bolt.TweetBolt;
import twitter.storm.bolt.connections.KeywordConnectionsBolt;
import twitter.storm.bolt.repository.KeywordsConnectionsDBBolt;
import twitter.storm.bolt.websocket.KeywordsWebSocketBolt;
import twitter.storm.spout.TweetSpout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by grzmiejski on 4/26/15.
 */
public class TwitterStreamTopology {

    public static final String TWEET_SPOUT = "tweetSpout";

    public static void main(String[] args) {
        List<String> keywords = Arrays.asList(args);

        RouteStarter.buildRoute();

        keywords.stream().forEach(System.out::println);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(TWEET_SPOUT, new TweetSpout(keywords));

        // keywords
        builder.setBolt("tweet", new TweetBolt(), 1).shuffleGrouping(TWEET_SPOUT);
        builder.setBolt("websocket", new KeywordsWebSocketBolt(keywords), 2).shuffleGrouping(TWEET_SPOUT);

        // keywords connections
        builder.setBolt("keywordsConnection", new KeywordConnectionsBolt(), 1).shuffleGrouping(TWEET_SPOUT);
        builder.setBolt("keywordsConnectionUpdater", new KeywordsConnectionsDBBolt(), 1).shuffleGrouping("keywordsConnection");

        Config conf = new Config();
        conf.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());


//        Utils.sleep(20000);
//        cluster.shutdown();
    }


}

package twitter.storm.topology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import camel.RouteStarter;
import twitter.storm.bolt.TweetBolt;
import twitter.storm.bolt.WebSocketBolt;
import twitter.storm.spout.TweetSpout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by grzmiejski on 4/26/15.
 */
public class TwitterStreamTopology {

    public static void main(String[] args) {
        List<String> keywords = Arrays.asList(args);

        RouteStarter.buildRoute();

        keywords.stream().forEach(System.out::println);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("tweetSpout", new TweetSpout(keywords));
        builder.setBolt("tweet", new TweetBolt(), 1).shuffleGrouping("tweetSpout");
        builder.setBolt("websocket", new WebSocketBolt(), 2).shuffleGrouping("tweetSpout");
        Config conf = new Config();
        conf.setDebug(false);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("test", conf, builder.createTopology());


//        Utils.sleep(20000);
//        cluster.shutdown();
    }

}

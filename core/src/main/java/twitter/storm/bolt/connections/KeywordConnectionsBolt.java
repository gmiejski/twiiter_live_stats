package twitter.storm.bolt.connections;

import agh.toik.model.KeywordConnection;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter.storm.bolt.connections.cache.AutoUpdateKeywordsConnectionsCache;
import twitter4j.Status;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordConnectionsBolt extends BaseRichBolt {
    public static final String KEYWORD_CONNECTION_OUTPUT_STRING = "keywordConnection";
    private OutputCollector outputCollector;

    private AutoUpdateKeywordsConnectionsCache keywordsConnectionsCache;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
        this.keywordsConnectionsCache = new AutoUpdateKeywordsConnectionsCache();
        this.keywordsConnectionsCache.initiate();
    }

    @Override
    public void execute(Tuple input) {
        Status tweet = (Status) input.getValueByField("tweet");
        String tweetText = tweet.getText().toLowerCase();
        List<KeywordConnection> foundKeywordsConnections = keywordsConnectionsCache.getKeywordsConnections()
                .stream()
                .filter(keywordConnection -> tweetText.contains(keywordConnection.getFirstKeyword())
                        && tweetText.contains(keywordConnection.getSecondKeyword()))
                .collect(Collectors.toList());

        foundKeywordsConnections.forEach(keywordConnection -> outputCollector.emit(new Values(keywordConnection)));
        System.out.println("Found keywords connections = " + foundKeywordsConnections.size());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields(KEYWORD_CONNECTION_OUTPUT_STRING));
    }
}

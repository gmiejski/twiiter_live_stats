package twitter.storm.bolt.keywords;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import twitter.storm.bolt.cache.api.KeywordCache;
import twitter.storm.bolt.cache.impl.AutoUpdateKeywordsCache;
import twitter4j.Status;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by grzegorz.miejski on 28/05/15.
 */
public class TweetFilterBolt extends BaseRichBolt {

    public static final String KEYWORDS_FOUND = "keywords";
    private OutputCollector outputCollector;
    private KeywordCache keywordCache;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
        this.keywordCache = new AutoUpdateKeywordsCache();
    }

    @Override
    public void execute(Tuple input) {
        Status status = (Status) input.getValueByField("tweet");
        String statusTextLower = status.getText().toLowerCase();

        List<String> matchingKeywords = this.keywordCache.retrieveKeywords()
                .stream()
                .filter(statusTextLower::contains)
                .collect(toList());
        outputCollector.emit(new Values(status, matchingKeywords));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("tweet", KEYWORDS_FOUND));
    }
}

package twitter.storm.bolt.repository;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import twitter.rest.KeywordsRepository;
import twitter.storm.bolt.keywords.TweetFilterBolt;

import java.util.List;
import java.util.Map;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordsDBBolt extends BaseRichBolt {

    KeywordsRepository keywordsRepository;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        keywordsRepository = new KeywordsRepository();
    }

    @Override
    public void execute(Tuple input) {
        List<String> keywords = (List<String>) input.getValueByField(TweetFilterBolt.KEYWORDS_FOUND);
        keywordsRepository.addKeywordOccurrence(keywords);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}

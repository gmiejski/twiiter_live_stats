package twitter.storm.bolt.repository;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import twitter.rest.KeywordsRepository;
import twitter.storm.bolt.keywords.TweetFilterBolt;
import twitter4j.Place;
import twitter4j.Status;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Optional<String> countryCode = getCountryCode(input);
        keywordsRepository.addKeywordOccurrence(keywords, countryCode);
    }

    private Optional<String> getCountryCode(Tuple input) {
        Status status = (Status) input.getValueByField("tweet");
        Place place = status.getPlace();
        return place != null ? Optional.of(place.getCountryCode()) : Optional.empty();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}

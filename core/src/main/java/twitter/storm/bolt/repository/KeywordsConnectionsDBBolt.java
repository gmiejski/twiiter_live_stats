package twitter.storm.bolt.repository;

import agh.toik.model.KeywordConnection;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import twitter.rest.KeywordsConnectionsRepository;
import twitter.storm.bolt.connections.KeywordConnectionsBolt;

import java.util.Map;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordsConnectionsDBBolt extends BaseRichBolt {

    KeywordsConnectionsRepository keywordsConnectionsRepository;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        keywordsConnectionsRepository = new KeywordsConnectionsRepository();
    }

    @Override
    public void execute(Tuple input) {
        KeywordConnection keywordConnection = (KeywordConnection) input.getValueByField(KeywordConnectionsBolt.KEYWORD_CONNECTION_OUTPUT_STRING);

        keywordsConnectionsRepository.incrementCount(keywordConnection);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}

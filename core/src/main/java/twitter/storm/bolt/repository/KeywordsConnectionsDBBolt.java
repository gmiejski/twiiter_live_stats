package twitter.storm.bolt.repository;

import agh.toik.model.KeywordConnection;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import twitter.storm.bolt.connections.KeywordConnectionsBolt;

import java.util.Map;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordsConnectionsDBBolt extends BaseRichBolt {


    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {

    }

    @Override
    public void execute(Tuple input) {
        KeywordConnection keywordConnection = (KeywordConnection) input.getValueByField(KeywordConnectionsBolt.KEYWORD_CONNECTION_OUTPUT_STRING);
        System.out.println(keywordConnection);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}

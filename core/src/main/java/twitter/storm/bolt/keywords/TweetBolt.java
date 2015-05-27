package twitter.storm.bolt.keywords;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import twitter4j.Status;

import java.util.Map;

/**
 * Created by grzmiejski on 4/18/15.
 */
public class TweetBolt extends BaseRichBolt {

    private OutputCollector outputCollector;

    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.outputCollector = outputCollector;
    }

    public void execute(Tuple input) {
        Status s = (Status) input.getValueByField("tweet");
        System.out.println(s.getText());
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

}

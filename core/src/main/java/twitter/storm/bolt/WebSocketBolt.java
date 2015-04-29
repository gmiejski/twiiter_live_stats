package twitter.storm.bolt;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import camel.RouteStarter;
import org.apache.camel.ProducerTemplate;
import twitter4j.Status;

import java.util.Map;

/**
 * Created by grzmiejski on 4/27/15.
 */
public class WebSocketBolt extends BaseBasicBolt {
    private ProducerTemplate producerTemplate;

    @Override
    public void execute(Tuple input, BasicOutputCollector basicOutputCollector) {
        Status s = (Status) input.getValueByField("tweet");
        producerTemplate.sendBody("direct:main", s.getText());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        this.producerTemplate = RouteStarter.getP();
    }
}

package twitter.storm.bolt.keywords.websocket;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import camel.RouteStarter;
import com.google.gson.Gson;
import org.apache.camel.ProducerTemplate;

import java.util.List;
import java.util.Map;

/**
 * Created by grzmiejski on 4/27/15.
 */
public class KeywordsWebSocketBolt extends BaseBasicBolt {

    private ProducerTemplate producerTemplate;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        this.producerTemplate = RouteStarter.getProducerTemplate();
    }

    @Override
    public void execute(Tuple input, BasicOutputCollector basicOutputCollector) {
        List<String> keywords = (List<String>) input.getValueByField("keywords");
        String json = new Gson().toJson(keywords);
        producerTemplate.sendBody("direct:main", json);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
}

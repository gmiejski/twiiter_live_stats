package twitter.storm.bolt.websocket;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import camel.RouteStarter;
import com.google.gson.Gson;
import org.apache.camel.ProducerTemplate;
import twitter.storm.bolt.cache.impl.AutoUpdateKeywordsCache;
import twitter4j.Status;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by grzmiejski on 4/27/15.
 */
public class KeywordsWebSocketBolt extends BaseBasicBolt {

    private ProducerTemplate producerTemplate;
    private AutoUpdateKeywordsCache autoUpdateKeywordsCache;

    @Override
    public void execute(Tuple input, BasicOutputCollector basicOutputCollector) {
        Status s = (Status) input.getValueByField("tweet");
        String statusTextLower = s.getText().toLowerCase();

        List<String> matchingKeywords = autoUpdateKeywordsCache.retrieveKeywords()
                .stream()
                .filter(statusTextLower::contains)
                .collect(toList());
        String json = new Gson().toJson(matchingKeywords);
        producerTemplate.sendBody("direct:main", json);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
        this.producerTemplate = RouteStarter.getProducerTemplate();
        this.autoUpdateKeywordsCache = new AutoUpdateKeywordsCache();
    }
}

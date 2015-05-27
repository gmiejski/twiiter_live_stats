package twitter.storm.spout;

import backtype.storm.Config;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import twitter.Twitter4jReader;
import twitter4j.Status;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by grzmiejski on 4/18/15.
 */
public class TweetSpout extends BaseRichSpout {

    private SpoutOutputCollector spoutOutputCollector;
    private Queue<Status> queue;
    private Twitter4jReader twitter4jReader;

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
        queue = new LinkedBlockingQueue<>(1000);
        this.twitter4jReader = new Twitter4jReader(queue);
        twitter4jReader.startStreaming();
    }

    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("tweet"));
    }

    public void nextTuple() {
        Status status = queue.poll();
        if (status == null) {
            Utils.sleep(50);
        } else {
            spoutOutputCollector.emit(new Values(status));
        }
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        Config ret = new Config();
        ret.setMaxTaskParallelism(1);
        return ret;
    }

    @Override
    public void close() {
        twitter4jReader.shutdown();
    }
}

# twitter_live_stats


## Running
1. Install storm locally following this:
    https://github.com/apache/storm/tree/master/examples/storm-starter#build-and-install-storm-jars-locally

2. Run:
    * change twitter4j.properties.template into filled twitter4j.properties file
    * maven package
    * storm jar TwitterLiveStats-1.0-SNAPSHOT-jar-with-dependencies.jar  twitter.storm.topology.TwitterStreamTopology python ruby java
    
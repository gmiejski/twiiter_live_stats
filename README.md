# twitter_live_stats


## Running

1. Install the necessary software:
    * Install Storm locally following this: https://github.com/apache/storm/tree/master/examples/storm-starter#build-and-install-storm-jars-locally
    * Install MongoDB server and node.js

2. Run the application:
    * Change twitter4j.properties.template into filled twitter4j.properties file
    * mvn package
    * Run agh.toik.DemoApplication
    * storm jar core/target/core-1.0-SNAPSHOT-jar-with-dependencies.jar twitter.storm.topology.TwitterStreamTopology
    
3. Run the web client:
    * cd web/src/main/node
    * npm install connect serve-static
    * node server.js
    * Go to localhost:8081 to see the visualizations!
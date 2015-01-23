package spark.streaming;


import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import twitter4j.Status;

import java.util.Arrays;

/**
 * A simple example of spark streaming counting twitter hashtags for a given filter
 */

public class HashtagsCount {
    public static void main(String[] args) {

        final String consumerKey = "*****your code here *****";
        final String consumerSecret = "*****your code here *****";
        final String accessToken = "*****your code here *****";
        final String accessTokenSecret = "*****your code here *****";

        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName("StreamingHashtagsExample");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(5000));

        System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
        System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
        System.setProperty("twitter4j.oauth.accessToken", accessToken);
        System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

        String[] filters = {"happy"};

        JavaReceiverInputDStream<Status> twitterStream = TwitterUtils.createStream(jssc, filters);

        twitterStream
                .filter(t->t.getGeoLocation()!=null)
                .flatMap(h-> Arrays.asList(h.getHashtagEntities()))
                .map(h->h.getText().toLowerCase())
                .filter(h->!h.equals("happy"))
                .countByValue()
                .print();

        jssc.start();
        jssc.awaitTermination();
    }
}

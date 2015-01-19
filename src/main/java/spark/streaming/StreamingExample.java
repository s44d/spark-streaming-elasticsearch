package spark.streaming;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.SparkConf;
import org.apache.spark.serializer.KryoSerializer;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark;
import twitter4j.Status;

public class StreamingExample {

    public static void main(String[] args) throws Exception {

        // Jackson
        ObjectMapper mapper = new ObjectMapper();

        final String consumerKey = "*****your code here *****";
        final String consumerSecret = "*****your code here *****";
        final String accessToken = "*****your code here *****";
        final String accessTokenSecret = "*****your code here *****";

        SparkConf conf = new SparkConf().setMaster("local[2]")
                .setAppName("StreamingExample")
                .set("spark.serializer", KryoSerializer.class.getName())
                .set("es.nodes", "localhost:9200")
                .set("es.index.auto.create", "true");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(5000));

        System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
        System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
        System.setProperty("twitter4j.oauth.accessToken", accessToken);
        System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);

        String[] filters = { "happy" };

        JavaReceiverInputDStream<Status> twitterStream = TwitterUtils.createStream(jssc,filters);


        twitterStream
                .filter(t -> t.getGeoLocation() != null)
                .map(t -> new Tweet(t.getUser().getName(), t.getText(), t.getCreatedAt(), new Double[]{t.getGeoLocation().getLongitude(), t.getGeoLocation().getLatitude()}))
                .map(t -> mapper.writeValueAsString(t))
                .foreachRDD(tweets -> {
                    JavaEsSpark.saveJsonToEs(tweets, "tweets/happy");
                    return null;
                });

        jssc.start();
    }
}
